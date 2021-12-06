import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

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
