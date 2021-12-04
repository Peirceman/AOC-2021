import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

public class Main {
	private static File file = new File("input.txt");
	public static void main(String[] args) throws IOException {
		part1();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		int hPos = 0, depth = 0;

		for(String line = r.readLine(); line != null; line = r.readLine()) {
			// split[0] = command, split[1] = X
			String[] split = line.split(" ");
			assert(split.length == 2);

			switch(split[0]) {
				case "up" -> { //:
					depth -= Integer.parseInt(split[1]);
				}
				case "down" -> { //:
					depth += Integer.parseInt(split[1]);
				}
				case "forward" -> { //:
					hPos += Integer.parseInt(split[1]);
				}
				default -> {
					System.err.println("Unreachable");
					System.exit(1);
				}
			}
		}

		System.out.printf("Part1: %d%n", hPos * depth);
	}

	
	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		int hPos = 0, depth = 0, aim = 0;

		for(String line = r.readLine(); line != null; line = r.readLine()) {
			// split[0] = command, split[1] = X
			String[] split = line.split(" ");
			assert(split.length == 2);

			switch(split[0]) {
				case "up" -> { //:
					aim -= Integer.parseInt(split[1]);
				}
				case "down" -> { //:
					aim += Integer.parseInt(split[1]);
				}
				case "forward" -> { //:
					int x = Integer.parseInt(split[1]);
					hPos += x;
					depth += aim * x;
				}
				default -> {
					System.err.println("Unreachable");
					System.exit(1);
				}
			}
		}

		System.out.printf("Part2: %d%n", hPos * depth);
	}
}
