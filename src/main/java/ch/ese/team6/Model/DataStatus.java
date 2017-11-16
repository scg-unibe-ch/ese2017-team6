package ch.ese.team6.Model;



public enum DataStatus {
	ACTIVE(0, "active"), INACTIVE(1, "inactive");
		
	private int value;
	private String title;
	public static final DataStatus[] ALL = { ACTIVE, INACTIVE };
		
	private DataStatus (int value, String title) {
		this.value = value;
		this.title = title;
	}
		
	public int getValue() {
		return value;
	}
		
	public String getTitle() {
		return title;
	}
		
	public void setValue(int value) {
		this.value = value;
	}
					
	public static String getByValue(int value) {
		for(DataStatus s : DataStatus.values()) {
			if (value == s.getValue())
				return "AVAILABLE";
		}
		return "UNAVAILABLE";
	}
}
