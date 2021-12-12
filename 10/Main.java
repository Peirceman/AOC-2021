import java.util.stream.IntStream;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.util.Stack;

public class Main {
	private static final File file = new File("input.txt");

	public static void main(String[] args) throws IOException {
		part1();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		long sum = 0;
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			Stack<Chunk> chunks = new Stack<>();
			char illegal = '\u0000';

			for (char c : line.toCharArray()) {
				if (Chunk.isOpener(c)) chunks.push(Chunk.getByOpener(c));
				else if (c == chunks.peek().closer) chunks.pop();
				else {
					illegal = c;
					break;
				}
			}

			if (illegal != '\u0000') sum += Chunk.getIllegalScore(illegal);
		}

		System.out.printf("Part1: %d%n", sum);
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));

		List<Long> scores = new ArrayList<>();

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			Stack<Chunk> chunks = new Stack<>();
			boolean corrupted = false;

			for (char c : line.toCharArray()) {
				if (Chunk.isOpener(c)) chunks.push(Chunk.getByOpener(c));
				else if (chunks.peek().closer == c) chunks.pop();
				else {
					corrupted = true;
					break;
				}
			}

			if (!corrupted && chunks.size() > 0) {
				chunks = reverse(chunks);
				scores.add(chunks.stream()
						.mapToLong(chunk -> chunk.incompleteScore)
						.reduce(0L, (a, b) -> a * 5 + b));
			}
		}

		scores = scores.stream().sorted().toList();
		System.out.printf("Part2: %d%n", scores.get(scores.size() / 2));
	}

	private static <T> Stack<T> reverse(Stack<T> stack) {
		return IntStream.range(0, stack.size())
			.mapToObj(i -> stack.get(stack.size() - i - 1))
			.collect(Stack<T>::new,
					Stack::add,
					Stack::addAll);
	}
}
