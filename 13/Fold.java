public record Fold (boolean horizontal, int loc) {
	public Fold {
		if (loc < 0) throw new IllegalArgumentException("loc must be greater than zero");
	}
}

