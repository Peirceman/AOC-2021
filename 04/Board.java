public class Board {
	private final Location[][] board = new Location[Main.BOARD_WIDTH][Main.BOARD_HEIGHT];
	private boolean hasWon;

	public Board(boolean hasWon) {
		this.hasWon = hasWon;
	}

	public Board() {
		this(false);
	}

	public Location[][] getBoard() {
		return this.board;
	}

	public boolean hasWon() {
		return this.hasWon;
	}

	// plays the number and sets if it has won accoringly, returns wether it won or not
	public boolean play(int num) {
		if (this.hasWon) return true;

		int markX = -1, markY = -1;

		for (int y = 0; y < this.board.length; y++) {
			for (int x = 0; x < this.board.length; x++) {
				if (this.board[y][x].getValue() == num) {
					this.board[y][x].setMarked();
					markX = x;
					markY = y;
					break;
				}
			}
		}

		if (markX < 0) return false;

		boolean won = true;
		for (int x = 0; x < this.board.length; x++) {
			won &= this.board[markY][x].isMarked();
		}

		if (won) return this.hasWon = true;

		won = true;
		for (int y = 0; y < this.board.length; y++) {
			won &= this.board[y][markX].isMarked();
		}

		return this.hasWon = won;
	}
}
