import java.util.stream.Stream;
import java.util.stream.Collector;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

public class Main{
	private static File file = new File("input.txt");
	// stores wich value is most frequent in each column.
	// so if mostCommon[0] = 1 then 1 is the most common value in column 1
	// if the value is not 1 or 0, then they are both as likely
	private static int[] mostCommon;

	public static void main(String[] args) throws IOException {
		calcMostCommon();
		part1();
		part2();
	}

	private static void calcMostCommon() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		String line = r.readLine();

		int[][] frequencies = new int[line.length()][2];
		mostCommon = new int[line.length()];

		for(;line != null; line = r.readLine()) {
			char[] chars = line.toCharArray();
			for(int i = 0; i < chars.length; i++) {
				int val = chars[i] - '0';
				frequencies[i][val]++;
			}
		}

		for(int i = 0; i < frequencies.length; i++) {
			int[] column = frequencies[i];
			if (column[0] > column[1])
				mostCommon[i] = 0;
			else 
				mostCommon[i] = 1;
		}
	}

	private static void part1() throws IOException {
		int mask = 0;
		for(int i = 0; i < mostCommon.length; i++) {
			mask = (mask << 1) | 1;
		}

		int gammaRate = 0;
		for(int i = 0; i < mostCommon.length; i++) {
			gammaRate <<= 1;
			gammaRate |= mostCommon[i];
		}

		int epsilonRate = (~gammaRate) & mask;

		System.out.printf("Part1: %d%n", gammaRate * epsilonRate);
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		StringBuilder fileBuilder = new StringBuilder();

		for(String line = r.readLine(); line != null; line = r.readLine())
			fileBuilder.append(line).append("\n");

		String file = fileBuilder.toString();
		Collector<String, List<String>, List<String>> toList = Collector.of(
				ArrayList::new,
				List::add,
				(a, b) -> {a.addAll(b); return a;});
		List<String> remainingLines = file.lines().collect(toList);

		for(int i = 0; remainingLines.size() > 1; i++) {
			int mostUsed = mostUsed(i, remainingLines), finalI = i;
			remainingLines = remainingLines.stream()
				.filter(s -> s.charAt(finalI) == mostUsed || (mostUsed == 0 && s.charAt(finalI) == '1'))
				.collect(toList);
		}
		int o2GenRating = Integer.parseInt(remainingLines.get(0), 2);

		remainingLines = file.lines().collect(toList);
		for(int i = 0; remainingLines.size() > 1; i++) {
			int leastUsed = leastUsed(i, remainingLines), finalI = i;
			remainingLines = remainingLines.stream()
				.filter(s -> s.charAt(finalI) == leastUsed || (leastUsed == 0 && s.charAt(finalI) == '0'))
				.collect(toList);
		}
		int co2ScrubberRating = Integer.parseInt(remainingLines.get(0), 2);

		System.out.printf("Part2: %d%n", o2GenRating * co2ScrubberRating);
	}

	private static int leastUsed(int idx, List<String> strs) {
		int mostUsed = mostUsed(idx, strs);
		return switch(mostUsed) {
			case '1' -> '0'; //:
			case '0' -> '1'; //:
			default -> mostUsed;
		};
	}

	private static int mostUsed(int idx, List<String> strs) {
		int zeros = 0, ones = 0;
		for(int i = 0; i < strs.size(); i++) {
			if (strs.get(i).charAt(idx) == '0') zeros++;
			else ones++;
		}
		return zeros > ones ? '0' : zeros < ones ? '1' : 0;
	}
}
