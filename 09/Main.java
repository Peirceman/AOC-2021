import java.util.concurrent.atomic.AtomicInteger;
import java.awt.Point;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

public class Main {
	private static final File file = new File("input.txt");
	private static final int[][] directions = new int[][] {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

	public static void main(String[] args) throws IOException {
		part1();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		StringBuilder s = new StringBuilder();
		for (int c = r.read(); c != -1; c = r.read()) s.append((char) c);

		int[][] heightMap = s.toString().lines()
			.map(line -> Arrays.stream(line.split(""))
						.mapToInt(Integer::parseInt)
						.toArray())
			.toArray(int[][]::new);

		int sum = 0;
		for (int i = 0; i < heightMap.length; i++) {
			for (int j = 0; j < heightMap[i].length; j++) {
				if (surrounding(i, j, heightMap).stream()
						.mapToInt(p -> heightMap[p.y][p.x])
						.reduce(Integer.MAX_VALUE, (a, b) -> a = Math.min(a, b)) > heightMap[i][j]) {
					sum += heightMap[i][j] + 1;
					j++; // add one to j because the next value will definitly have this be less
				}
			}
		}

		System.out.printf("Part1: %d%n", sum);
	}

	private static List<Point> surrounding(int i, int j, int[][] arr) {
		List<Point> surrounding = new ArrayList<>(8);

		for (int k = 0; k < directions.length; k++) {
			int ci = i + directions[k][0];
			int cj = j + directions[k][1];

			if (ci > -1 && ci < arr.length && cj > -1 && cj < arr[ci].length) surrounding.add(new Point(cj, ci));
		}

		return surrounding;
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		StringBuilder s = new StringBuilder();
		for (int c = r.read(); c != -1; c = r.read()) s.append((char) c);

		int[][] heightMap = s.toString().lines()
			.map(line -> Arrays.stream(line.split(""))
						.mapToInt(Integer::parseInt)
						.toArray())
			.toArray(int[][]::new);

		int first = Integer.MIN_VALUE, second = Integer.MIN_VALUE, third = Integer.MIN_VALUE;
		Set<Point> visited = new HashSet<>();
		for (int i = 0; i < heightMap.length; i++) {
			for (int j = 0; j < heightMap[i].length; j++) {
				int current = getBasin(heightMap, i, j, visited);
				if (current == 0) continue;

				if (current > first) {
					third  = second;
					second = first;
					first  = current;
				} else if (current > second) {
					third  = second;
					second = current;
				} else if (current > third) {
					third  = current;
				}
			}
		}

		System.out.printf("Part2: %d%n", first * second * third);
	}

	private static int getBasin(int[][] arr, int i, int j, Set<Point> visited) {
		return getBasin(arr, new Point(j, i), visited);
	}

	private static int getBasin(int[][] arr, Point p, Set<Point> visited) {
		if (!visited.add(p) || arr[p.y][p.x] == 9) return 0;

		return surrounding(p.y, p.x, arr).stream()
			.filter(point -> arr[point.y][point.x] != 9 && !visited.contains(point))
			.mapToInt(point -> getBasin(arr, point, visited))
			.sum() + 1;
	}
}
