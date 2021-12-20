import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	private static final File file = new File("input.txt");

	public static void main(String[] args) throws IOException {
		part1();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		CaveSystem caveSystem = new CaveSystem();

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			caveSystem.parseLine(line);
		}

		System.out.printf("Part1: %d%n", caveSystem.findAllPaths(false));
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		CaveSystem caveSystem = new CaveSystem();

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			caveSystem.parseLine(line);
		}

		System.out.printf("Part2: %d%n", caveSystem.findAllPaths(true));
	}
}
