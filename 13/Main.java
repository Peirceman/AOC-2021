import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	private static File file = new File("input.txt");

	public static void main(String[] args) throws IOException {
		part1();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		Paper paper = new Paper();
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			paper.parseLine(line);
		}

		paper.foldFirst();

		System.out.printf("Part1: %d%n", paper.numOfDots());
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		Paper paper = new Paper();
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			paper.parseLine(line);
		}

		paper.foldAll();

		System.out.printf("Part2: %n");
		paper.print();
	}
}

