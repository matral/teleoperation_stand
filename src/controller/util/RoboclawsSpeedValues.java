package controller.util;

public class RoboclawsSpeedValues {
	private double frontLeftSpeed,frontRightSpeed,rearLeftSpeed,rearRightSpeed;
	
	public RoboclawsSpeedValues(double fLSpeed, double fRSpeed, double rLSpeed, double rRSpeed)
	{
		setRoboclawsSpeedValues(fLSpeed, fRSpeed, rLSpeed, rRSpeed);
	}
	
	public void setRoboclawsSpeedValues(double fLSpeed, double fRSpeed, double rLSpeed, double rRSpeed ) {
		frontLeftSpeed = fLSpeed;
		frontRightSpeed = fRSpeed;
		rearLeftSpeed = rLSpeed;
		rearRightSpeed = rRSpeed;
	}
	
	public double getFrontLeftSpeed() {
		return frontLeftSpeed;
	}

	public void setFrontLeftSpeed(double frontLeftSpeed) {
		this.frontLeftSpeed = frontLeftSpeed;
	}

	public double getFrontRightSpeed() {
		return frontRightSpeed;
	}

	public void setFrontRightSpeed(double frontRightSpeed) {
		this.frontRightSpeed = frontRightSpeed;
	}

	public double getRearLeftSpeed() {
		return rearLeftSpeed;
	}

	public void setRearLeftSpeed(double rearLeftSpeed) {
		this.rearLeftSpeed = rearLeftSpeed;
	}

	public double getRearRightSpeed() {
		return rearRightSpeed;
	}

	public void setRearRightSpeed(double rearRightSpeed) {
		this.rearRightSpeed = rearRightSpeed;
	}
	
	public String toString()
	{
		return
		"frontLeftSpeed " + frontLeftSpeed + "\n" +
		"frontRightSpeed " + frontRightSpeed + "\n"+
		"rearLeftSpeed " + rearLeftSpeed + "\n" +
		"rearRightSpeed " + rearRightSpeed + "\n";
	
	}
	
	
}
