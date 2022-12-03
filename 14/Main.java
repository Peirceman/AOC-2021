import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {
	private static File file = new File("input.txt");

	public static void main(String[] args) throws IOException {
		part1();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		Polymer polymer = new Polymer(r.readLine());
		r.readLine(); // skip empty line between intial state and rules

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			polymer.parseRule(line);
		}
		r.close();

		for (int i = 0; i < 10; i++) {
			polymer.nextStep();
		}

		System.out.printf("min: %d | max: %d | max-min: %d%n", polymer.minCount(), polymer.maxCount(),
				polymer.maxCount() - polymer.minCount());
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		Polymer polymer = new Polymer(r.readLine());
		r.readLine(); // skip empty line between intial state and rules

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			polymer.parseRule(line);
		}
		r.close();

		for (int i = 0; i < 40; i++) {
			polymer.nextStep();
		}

		System.out.printf("min: %d | max: %d | max-min: %d%n", polymer.minCount(), polymer.maxCount(),
				polymer.maxCount() - polymer.minCount());
	}
}
