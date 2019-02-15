package gcs.mission;

public class JumpPoint {
	private int currentNumber;
	private int jumpToNumber;
	private int rep;
	
	public int getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}
	public int getJumpToNumber() {
		return jumpToNumber;
	}
	public void setJumpToNumber(int jumpToNumber) {
		this.jumpToNumber = jumpToNumber;
	}
	public int getRep() {
		return rep;
	}
	public void setRep(int rep) {
		this.rep = rep;
	}
}