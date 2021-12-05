import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

class Board {
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

class Location {
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

public class Main {
	private static final File file = new File("input.txt");
	public static final int BOARD_WIDTH  = 5;
	public static final int BOARD_HEIGHT = 5;
	private static Set<Board> wonBoards = new LinkedHashSet<>();

	public static void main(String[] args) throws IOException {
		part1();
		wonBoards = new LinkedHashSet<>();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		int[] draws = Arrays.stream(r.readLine().split(",")).mapToInt(Integer::parseInt).toArray();

		Board[] boards = readBoards(r);

		Board winner = null;
		int i = 0;
		for (; (winner = winner(true)) == null; i++) {
			play(boards, draws[i]);
		}
		System.out.printf("Part1: %d%n", sumOfUnmarked(winner) * draws[i - 1]);
	}

	private static Board[] readBoards(BufferedReader r) throws IOException {
		// skip empty line between the draws and the boards
		r.readLine();

		List<Board> readBoards = new ArrayList<>();

		String line = r.readLine();
		for (int i = 0; line != null; i++, line = r.readLine()) {
			readBoards.add(new Board());

			for (int y = 0; y < Main.BOARD_HEIGHT; line = r.readLine(), y++) {
				readBoards.get(i).getBoard()[y] = Arrays.stream(line.split(" "))
					.filter(s -> s.length() > 0)
					.mapToInt(Integer::parseInt)
					.mapToObj(Location::new)
					.toArray(Location[]::new);
			}
		}

		return readBoards.toArray(new Board[0]);
	}

	private static int sumOfUnmarked(Board board) {
		return Arrays.stream(board.getBoard())
			.mapToInt(line -> Arrays.stream(line)
					.filter(loc -> !loc.isMarked())
					.mapToInt(Location::getValue)
					.sum())
			.sum();
	}

	private static void printMarked(Board[] boards) {
		for (Board board : boards) {
			for (Location[] line : board.getBoard()) {
				Arrays.stream(line)
					.forEach(loc -> System.out.printf("%2s ", (loc.isMarked() ? loc.getValue() : "  ")));
				System.out.println();
			}
			System.out.println("---".repeat(board.getBoard().length));
		}
	}

	private static void play(Board[] boards, int draw) {
		for (Board board : boards) {
			if (!board.hasWon())
				if (board.play(draw)) wonBoards.add(board);
		}
	}

	private static Board winner(boolean first) {
		if (wonBoards.size() < 1) return null;

		if (first) return wonBoards.stream().reduce((a, b) -> a).get();
		return wonBoards.stream().reduce((a, b) -> b).get();
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		int[] draws = Arrays.stream(r.readLine().split(",")).mapToInt(Integer::parseInt).toArray();

		Board[] boards = readBoards(r);

		int draw;
		for (int i = 0;; i++) {
			play(boards, (draw = draws[i]));
			if (wonBoards.size() == boards.length) break;
		}
		
		// printMarked(boards);
		System.out.printf("Part2: %d%n", sumOfUnmarked(winner(false)) * draw);
	}
}
