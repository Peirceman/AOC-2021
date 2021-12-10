import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
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

		List<String> outputs = new ArrayList<>();

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			String[] split = line.split(" \\| ");

			Arrays.stream(split[1].split(" "))
				.forEach(s -> outputs.add(s));
		}

		long count = outputs.stream().filter(s -> s.length() < 5 || s.length() > 6).count();
		System.out.printf("Part1: %d%n", count);
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		long sum = 0L;

		int y = 0;
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			String[] split = line.split(" \\| ");
			
			Map<Integer, Integer> numberToParsed = new HashMap<>();
			Map<Integer, Integer> parsedToNumber = new HashMap<>();

			Arrays.stream(split[0].split("\\s+"))
				.peek(s -> {
					switch(s.length()) {
						case 2 -> numberToParsed.put(1, parse(s)); //:
						case 3 -> numberToParsed.put(7, parse(s)); //:
						case 4 -> numberToParsed.put(4, parse(s)); //:
						case 7 -> numberToParsed.put(8, parse(s)); //:
					}
				})
				.filter(s -> s.length() > 4 && s.length() < 7) // now we are only left with the values wich aren't unique in length
				.toList()
				.stream()
				.forEach(s -> {
					int parsed = parse(s);
					if (s.length() == 6) {
						if      (containsSegments(parsed, numberToParsed.get(4))) numberToParsed.put(9, parsed);
						else if (containsSegments(parsed, numberToParsed.get(1))) numberToParsed.put(0, parsed);
						else                                                      numberToParsed.put(6, parsed);
					} else { // 5
						if      (containsSegments(parsed, numberToParsed.get(1))) numberToParsed.put(3, parsed);
						else {
							int count = 0;
							int four  = numberToParsed.get(4);
							for (int i = 1; i <= parsed; i <<= 1) {
								if ((four & (parsed & i)) != 0) count++;
							}

							if (count == 3) numberToParsed.put(5, parsed);
							else            numberToParsed.put(2, parsed);
						}
					}
				});

			numberToParsed.entrySet().stream().forEach(e -> parsedToNumber.put(e.getValue(), e.getKey()));

			sum += Arrays.stream(split[1].split("\\s+"))
				.mapToLong(s -> (long) parsedToNumber.get(parse(s)))
				.reduce(0L, (a, b) -> a * 10 + b);
		}

		System.out.printf("Part2: %d%n", sum);
	}

	private static int parse(String str) {
		int ret = 0;

		for (char c : str.toCharArray()) {
			ret |= 1 << (c - 'a');
		}

		return ret;
	}

	private static String deparse(int b) {
		StringBuilder ret = new StringBuilder();

		for (int i = 0; i < 8; i++) {
			if ((b & (1 << i)) != 0) {
				ret.append((char) ('a' + i));
			}
		}

		return ret.toString();
	}

	private static boolean containsSegments(int a, int b) {
		return (a & b) == b;
	}
}
