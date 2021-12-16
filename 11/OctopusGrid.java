import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import java.awt.Point;

public class OctopusGrid {
	private final Octopus[][] octopuses;

	public OctopusGrid(List<int[]> energyLevels) {
		this.octopuses = energyLevels.stream()
			.map(line -> Arrays.stream(line)
					.mapToObj(Octopus::new)
					.toArray(Octopus[]::new))
			.toArray(Octopus[][]::new);
	}

	public long nextStep() {
		// this.printOctopusses();

		long amountOfFlashes = 0L;

		for (int i = 0; i < this.octopuses.length; i++) {
			for (int j = 0; j < this.octopuses[i].length; j++) {
				// System.out.println(i + " " + j);

				amountOfFlashes += this.increasePoint(new Point(i, j), null);
			}
		}

		Arrays.stream(this.octopuses)
			.flatMap(Arrays::stream)
			.forEach(Octopus::reset);


		return amountOfFlashes;
	}

	private long increasePoint(Point point, Point previousCaller) {
		long sum = 0L;

		if (this.octopuses[point.y][point.x].increaseEnergyLevel()) {
			// System.out.println("root: " + point);
			sum += 1 + this.surrounding(point).stream()
				// .peek(System.out::println)
				.mapToLong(p -> this.increasePoint(p, point))
				.sum();
		}

		return sum;
	}

	public void printOctopusses() {
		Arrays.stream(this.octopuses)
			.forEach(line -> {
				Arrays.stream(line)
					.forEach(octopus -> System.out.printf("\u001B[;%dm%-2d",
								octopus.getEnergyLevel() == 0 || octopus.getEnergyLevel() > 9 ? 31 : 0,
								octopus.getEnergyLevel()));
				System.out.println();
			});
		System.out.print("\u001B[;0m");
		System.out.flush();
	}

	private static final List<Point> directions = List.of(
			new Point(-1, -1), new Point(-1, 0), new Point(-1, 1), new Point(0, -1),
			new Point(0, 1),   new Point(1, -1), new Point(1, 0),  new Point(1, 1));

	public List<Point> surrounding(Point p) {
		return directions.stream()
			.map(point -> new Point(point.x + p.x, point.y + p.y))
			.filter(point -> 
					point.y >= 0 &&
					point.y < this.octopuses.length  &&
					point.x >= 0 &&
					point.x < this.octopuses[0].length)
			.collect(Collectors.toList());
	}
}
