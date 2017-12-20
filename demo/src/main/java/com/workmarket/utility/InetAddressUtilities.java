package com.workmarket.utility;

import com.google.common.net.InetAddresses;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for extracting IP addresses.
 *
 * @see http://rod.vagg.org/2011/07/handling-x-forwarded-for-in-java-and-tomcat/
 */
public class InetAddressUtilities {
	private static final String IP_ADDRESS_REGEX = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";
	private static final String PRIVATE_IP_ADDRESS_REGEX = "(^127\\.0\\.0\\.1)|(^10\\.)|(^172\\.1[6-9]\\.)|(^172\\.2[0-9]\\.)|(^172\\.3[0-1]\\.)|(^192\\.168\\.)";
	private static Pattern IP_ADDRESS_PATTERN = null;
	private static Pattern PRIVATE_IP_ADDRESS_PATTERN = null;

	private static String findNonPrivateIpAddress(String s) {
		if (IP_ADDRESS_PATTERN == null) {
			IP_ADDRESS_PATTERN = Pattern.compile(IP_ADDRESS_REGEX);
			PRIVATE_IP_ADDRESS_PATTERN = Pattern.compile(PRIVATE_IP_ADDRESS_REGEX);
		}
		Matcher matcher = IP_ADDRESS_PATTERN.matcher(s);
		while (matcher.find()) {
			if (!PRIVATE_IP_ADDRESS_PATTERN.matcher(matcher.group(0)).find())
				return matcher.group(0);
			matcher.region(matcher.end(), s.length());
		}
		return null;
	}

	public static String getAddressFromRequest(HttpServletRequest request) {
		String forwardedFor = request.getHeader("X-Forwarded-For");
		if (forwardedFor != null && (forwardedFor = findNonPrivateIpAddress(forwardedFor)) != null)
			return forwardedFor;
		return request.getRemoteAddr();
	}

	public static String parseInetAddress(String s) {
		if (InetAddresses.isInetAddress(s))
			return InetAddresses.forString(s).getHostAddress();
		return null;
	}
}
