public class Location {
	private final int value;
	private boolean marked;

	public Location(int value, boolean marked) {
		this.value = value;
		this.marked = marked;
	}

	public Location(int value) {
		this(value, false);
	}

	public int getValue() {
		return this.value;
	}

	public boolean isMarked() {
		return this.marked;
	}

	public void setMarked() {
		this.marked = true;
	}
}
