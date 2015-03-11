package Roboclaw;

public enum HandInitialValues {
	YAW(50), PITCH(50), ROLL(50),LEFT_RIGHT(50);
	private int value;

	private HandInitialValues( int valueP) {
		value = valueP;
	}


	public int getValue() {
		return value;
		
	}


	
}
