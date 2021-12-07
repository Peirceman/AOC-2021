/**
 * represents a group of {@link Lanternfish}' wich all share the same day in the cycle
 */
public class Group {
	public boolean change;
	private long num;
	private int cycleDay;

	public Group(long num, int cycleDay) {
		this.num = num;
		this.cycleDay = cycleDay;
	}

	public Group() {
		this(0, Lanternfish.cycleLength + Lanternfish.extraBirthDays);
	}

	public boolean nextDay() {
		this.cycleDay--;

		if (this.cycleDay < 0) {
			this.cycleDay = Lanternfish.cycleLength;
			return true;
		}
		return false;
	}

	public int getCycleDay() {
		return this.cycleDay;
	}

	public long getNum() {
		return this.num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public void incNum(long num) {
		this.num += num;
	}
}
