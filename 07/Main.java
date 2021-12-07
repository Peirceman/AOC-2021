import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

public class Main {
	private static final File file = new File("input.txt");

	public static void main(String[] args) throws IOException {
		part1();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		AtomicInteger tmpMin = new AtomicInteger(Integer.MAX_VALUE), tmpMax = new AtomicInteger(Integer.MIN_VALUE);
		int[] positions = Arrays.stream(r.readLine().split(","))
			.mapToInt(Integer::parseInt)
			.peek(x -> {tmpMin.set(Math.min(tmpMin.get(), x)); tmpMax.set(Math.max(tmpMax.get(), x));})
			.toArray();
		int min = tmpMin.get();
		int max = tmpMax.get();

		int minCost = Integer.MAX_VALUE;
		
		for (int i = min; i <= max; i++) {
			int cost = 0;

			for (int position : positions) {
				cost += diff(position, i);
			}
			minCost = Math.min(minCost, cost);
		}

		System.out.printf("Part1: %d%n", minCost);
	}

	private static int diff(int a, int b) {
		return Math.abs(a - b);
	}

	/**
	 * calculates the sum from 1 to {@code a}
	 * @param a the number to calculate the sum to
	 * @return the sum from 1 to {@code a}
	 */
	private static int sum(int a) {
		if (a < 1) return a;

		if (a % 2 == 0)
			return (a + 1) * (a / 2);
		return a * (a >> 1) + a;
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		AtomicInteger tmpMin = new AtomicInteger(Integer.MAX_VALUE), tmpMax = new AtomicInteger(Integer.MIN_VALUE);
		int[] positions = Arrays.stream(r.readLine().split(","))
			.mapToInt(Integer::parseInt)
			.peek(x -> {tmpMin.set(Math.min(tmpMin.get(), x)); tmpMax.set(Math.max(tmpMax.get(), x));})
			.toArray();
		int min = tmpMin.get();
		int max = tmpMax.get();

		int minCost = Integer.MAX_VALUE;
		
		for (int i = min; i <= max; i++) {
			int cost = 0;

			for (int position : positions) {
				cost += sum(diff(position, i));
			}
			minCost = Math.min(minCost, cost);
		}

		System.out.printf("part2: %d%n", minCost);
	}
}
