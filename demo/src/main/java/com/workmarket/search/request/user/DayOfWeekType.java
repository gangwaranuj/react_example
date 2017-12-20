package com.workmarket.search.request.user;

public enum DayOfWeekType {
	SUNDAY(0),
	MONDAY(1),
	TUESDAY(2),
	WEDNESDAY(3),
	THURSDAY(4),
	FRIDAY(5),
	SATURDAY(6);

	private final int value;

	private DayOfWeekType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static DayOfWeekType findByValue(int value) {
		switch (value) {
			case 0:
				return SUNDAY;
			case 1:
				return MONDAY;
			case 2:
				return TUESDAY;
			case 3:
				return WEDNESDAY;
			case 4:
				return THURSDAY;
			case 5:
				return FRIDAY;
			case 6:
				return SATURDAY;
			default:
				return null;
		}
	}
}
