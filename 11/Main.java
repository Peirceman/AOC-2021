import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	private static final File file = new File("input.txt");

	public static void main(String[] args) throws IOException {
		part1();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		List<int[]> energyLevels = new ArrayList<>();

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			energyLevels.add(Arrays.stream(line.split(""))
					.mapToInt(Integer::parseInt)
					.toArray());
		}

		OctopusGrid grid = new OctopusGrid(energyLevels);
		long sum = 0;

		for (int i = 0; i < 100; i++) {
			sum += grid.nextStep();
		}

		System.out.printf("Part1: %d%n", sum);
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		List<int[]> energyLevels = new ArrayList<>();
		int amountOfOctopuses = 0;

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			energyLevels.add(Arrays.stream(line.split(""))
					.mapToInt(Integer::parseInt)
					.toArray());
			amountOfOctopuses += energyLevels.get(energyLevels.size() - 1).length;
		}

		OctopusGrid grid = new OctopusGrid(energyLevels);
		int amountOfSteps = 1;

		for (; grid.nextStep() < amountOfOctopuses; amountOfSteps++){}

		System.out.printf("Part2: %d%n", amountOfSteps);
	}
}
