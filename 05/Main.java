import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

class Line {
	private Point start;
	private Point end;

	public Line(Point start, Point end) {
		assert(start != null);
		assert(end != null);

		this.start = start;
		this.end   = end;
	}

	public Line(int startX, int startY, int endX, int endY) {
		this(new Point(startX, startY), new Point(endX, endY));
	}

	public Line() {
		this(0, 0, 0, 0);
	}

	public boolean isHorizontal() {
		return this.start.y == this.end.y;
	}

	public boolean isVertical() {
		return this.start.x == this.end.x;
	}

	public boolean isAxisAligned() {
		return this.start.x == this.end.x || this.start.y == this.end.y;
	}

	public boolean isDiagonal() {
		return this.start.x != this.end.x && this.start.y != this.end.y;
	}

	public Point getStart() {
		return this.start;
	}
	
	public Line setStart(Point start) {
		this.start = start;
		return this;
	}

	public Point getEnd() {
		return this.end;
	}
	
	public Line setEnd(Point end) {
		this.end = end;
		return this;
	}

	public String toString() {
		return this.start.x + "," + this.start.y + " -> " + this.end.x + "," + this.end.y;
	}
}

public class Main {
	private static File file = new File("input.txt");
	private static List<Line> lines;

	public static void main(String[] args) throws IOException {
		lines = parseFile(new BufferedReader(new FileReader(file)));

		part1();
		part2();
	}

	private static void part1() {
		System.out.printf("Part1: %d%n", amountOfOverlaps(false));
	}

	private static int amountOfOverlaps(boolean diagonals) {
		Point max = lines.stream()
			.map(line -> new Point(Math.max(line.getStart().x, line.getEnd().x), Math.max(line.getStart().y, line.getEnd().y)))
			.reduce((a, b) -> {a.x = Math.max(a.x, b.x + 1); a.y = Math.max(a.y, b.y + 1); return a;}).get();

		int[][] grid = new int[max.y][max.x];

		for (Line line : lines) {
			if (line.isDiagonal() && !diagonals) continue;

			Point start = line.getStart();
			Point end   = line.getEnd();
			Point p     = new Point(start);
			Point inc   = new Point(start.x < end.x ? 1 : start.x > end.x ? -1 : 0, start.y < end.y ? 1 : start.y > end.y ? -1 : 0);
			
			for (; !p.equals(end); p.x += inc.x, p.y += inc.y) grid[p.y][p.x]++;

			// increment last point
			grid[p.y][p.x]++;

		}
		
		return (int) Arrays.stream(grid)
			.flatMapToInt(Arrays::stream)
			.filter(x -> x > 1)
			.count();
	}

	private static List<Line> parseFile(BufferedReader r) throws IOException {
		List<Line> lines = new ArrayList<>();

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			lines.add(lineFromString(line)); 
		}

		return lines;
	}

	private static Line lineFromString(String s) {
		Point[] points = Arrays.stream(s.split(" -> "))
			.map(point -> {
				int[] coords = Arrays.stream(point.split(","))
					.mapToInt(Integer::parseInt).toArray();
				return new Point(coords[0], coords[1]);})
			.toArray(Point[]::new);

		return new Line(points[0], points[1]);
	}

	private static void part2() {
		System.out.printf("Part2: %d%n", amountOfOverlaps(true));
	}
}
