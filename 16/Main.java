import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {
	private static File file = new File("input.txt");

	public static void main(String[] args) throws IOException {
		// part1();
		part2();
	}

	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		Packet p = new Packet(r);
		// System.out.printf("v: %d, t: %d, val: %d%n", p.VERSION, p.TYPE_ID, p.value);
		// p.print();
		System.out.println(p.sumOfVersions());
	}

	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		Packet p = new Packet(r);
		// System.out.printf("v: %d, t: %d, val: %d%n", p.VERSION, p.TYPE_ID, p.value);
		// p.print();
		System.out.println(p.calc());
	}
}
