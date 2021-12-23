public enum PaperPoint {
	NONE,POINT;

	public PaperPoint or(PaperPoint point) {
		if (this == NONE && point == NONE)
			return NONE;
		return POINT;
	}

	@Override
	public String toString() {
		return this == NONE ? "." : "#";
	}
}
