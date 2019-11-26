package Scooter;


/*
 * TODO 
 * - ������� run() �Լ� ����
 *
 */

public class Scooter {
	private String 	ID;
	private double 	totalBattery;
	private double 	leftBatery;
	private boolean nowUse;
	private int[] 	location;
	
	public double getLeftBatery() {
		return leftBatery;
	}

	public void setLeftBatery(double leftBatery) {
		this.leftBatery = leftBatery;
	}

	public boolean isNowUse() {
		return nowUse;
	}

	public void setNowUse(boolean nowUse) {
		this.nowUse = nowUse;
	}

	public int[] getLocation() {
		return location;
	}

	public void setLocation(int[] location) {
		this.location = location;
	}

	public String getID() {
		return ID;
	}

	public double getTotalBattery() {
		return totalBattery;
	}



	public Scooter(String id) {
		this.ID = id;
		totalBattery = 1000.0;
	}
	
	public int checkTime() {
		long start = System.currentTimeMillis();
		while (isNowUse()) {
			run();
		}
		long end = System.currentTimeMillis();
		return (int)(end-start)/10000; // �� �� ������ ��ȯ....
	}
	
	public void run() {
		while(isNowUse()) { // �̿����϶��� �޸�
			System.out.println("�޸���! �޷�!");
		}
	}
}