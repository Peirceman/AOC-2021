import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.BufferedReader;
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
		int increases = 0, prevDepth = -1;
		for(String line = r.readLine(); line != null; line = r.readLine()) {
			int depth = Integer.parseInt(line);
			if (prevDepth < 0) { 
				prevDepth = depth;
				continue;
			}

			if (depth > prevDepth)
				increases++;

			prevDepth = depth;
		}
		System.out.printf("Part 1: %d%n", increases);
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		int increases = 0, prevSum = -1;
		final int[] prev2 = new int[2];

		// initialize the first two lines
		String line = r.readLine();
		prev2[0] = Integer.parseInt(line);
		line = r.readLine();
		prev2[1] = Integer.parseInt(line);

		line = r.readLine();
		for(int i = 0; line != null; line = r.readLine(), i++) {
			int depth = Integer.parseInt(line);
			int sum = prev2[0] + prev2[1] + depth;

			prev2[0] = prev2[1];
			prev2[1] = depth;
			
			if (prevSum < 0) {
				prevSum = sum;
				continue;
			}

			if (sum > prevSum)
				increases++;

			prevSum = sum;
		}

		System.out.printf("Part 2: %d%n", increases);
	}
}
