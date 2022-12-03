import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {
	private static File file = new File("input.txt");

	public static void main(String[] args) throws java.io.IOException {
		part1();
		// part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		Cave cave = new Cave();

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			cave.parseLine(line);
		}
		r.close();
		// cave.print();

		System.out.println(cave.leastRiskPath());
		// cave.print();
	}
}
