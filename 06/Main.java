import java.math.BigInteger;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;
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

	// groups are more efficient, but I'm not changing this to groups bc
	// this is how I did it first and I want to preserve it
	public static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		List<Lanternfish> fishes = Arrays.stream(r.readLine().split(","))
			.map(x -> new Lanternfish(Integer.parseInt(x)))
			.collect(Collectors.toList());

		for (int i = 0; i < 80; i++) {
			int startDayLength = fishes.size();
			for (int j = 0; j < startDayLength; j++) {
				if (fishes.get(j).nextDay()) fishes.add(new Lanternfish());
			}
		}

		System.out.printf("Part1: %d%n", fishes.size());
	}

	public static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		List<Group> groups = new ArrayList<>(Lanternfish.cycleLength + 1);
		for (int i = 0; i < Lanternfish.cycleLength + 1; i++) {
			groups.add(new Group(0, i));
		}

		int[] nums = Arrays.stream(r.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
		for (int num : nums) groups.get(num).incNum(1);

		for (int i = 0; i < 256; i++) {
			int size = groups.size();
			for (int j = 0; j < size; j++) {
				if (groups.get(j).nextDay()) groups.add(new Group(groups.get(j).getNum(), Lanternfish.cycleLength + Lanternfish.extraBirthDays));
			}

			// compress all groups of day 7 to one
			long day7s = groups.stream().filter(g -> g.getCycleDay() == 6).mapToLong(Group::getNum).sum();
			groups = groups.stream().filter(g -> g.getCycleDay() != 6).collect(Collectors.toList());
			groups.add(new Group(day7s, 6));
		}


		BigInteger sum = BigInteger.ZERO;
		for (Group group : groups) {
			sum = sum.add(new BigInteger(Long.toUnsignedString(group.getNum())));
		}
		System.out.printf("Part2: %s%n", sum);
	}
}
