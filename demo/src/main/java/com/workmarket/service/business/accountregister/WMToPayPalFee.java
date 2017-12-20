package com.workmarket.service.business.accountregister;

import com.workmarket.domains.model.account.RegisterTransactionType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class WMToPayPalFee extends SecretAccountingTransaction {
	public WMToPayPalFee() {
		super();
	}

	@Override
	public RegisterTransactionType getRegisterTransactionType() {
		return new RegisterTransactionType(RegisterTransactionType.WM_PAY_PAL_FEE_USA);
	}
}
