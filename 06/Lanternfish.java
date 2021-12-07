public class Lanternfish {
	// 7 days, but 0 is a valid state
	public static final int cycleLength = 6;
	public static final int extraBirthDays = 2;
	private int cycleDays;

	public Lanternfish(int cycleDays) {
		this.cycleDays = cycleDays;
	}

	public Lanternfish() {
		this(cycleLength + extraBirthDays);
	}

	/** 
	 * reduces the length of this {@code lanterfish}'s cycle by one
	 * and returns if another {@code lanterfish} should be birthed.
	 * @return if another {@code lanterfish} should be birthed.
	 */
	public boolean nextDay() {
		this.cycleDays--;

		if (this.cycleDays < 0) {
			this.cycleDays = Lanternfish.cycleLength;
			return true;
		}

		return false;
	}

	public int getCycleDays() {
		return this.cycleDays;
	}

	@Override
	public String toString() {
		return "Lanternfish[cycleDays=" + this.cycleDays + "]";
	}
}
