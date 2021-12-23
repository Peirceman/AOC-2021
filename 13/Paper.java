import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import java.awt.Point;

public class Paper {
	private int width;
	private List<List<PaperPoint>> points;
	private List<Fold> folds;

	public Paper() {
		this.points = new ArrayList<>();
		this.folds  = new ArrayList<>();
		this.width = 0;
	}

	public void parseLine(String line) {
		if (line.length() < 1) return;
		if (line.matches("fold along (x|y)=\\d+"))  parseFold(line);
		else if (Character.isDigit(line.charAt(0))) parsePoint(line);
	}

	private void parseFold(String line) {
		line	       = line.split("fold along ")[1];
		String[] split = line.split("=");
		this.folds.add(new Fold(split[0].charAt(0) == 'y', Integer.parseInt(split[1])));
	}

	private void parsePoint(String line) {
		Point point = new Point();
		String[] split = line.split(",");
		point.x = Integer.parseInt(split[0]);
		point.y = Integer.parseInt(split[1]);

		this.setPoint(point, PaperPoint.POINT);
	}

	private void setPoint(Point point, PaperPoint value) {
		// fill points until point.y with empty lists
		if (this.points.size() <= point.y) this.expandList(this.points, point.y, ArrayList<PaperPoint>::new);

		this.width = Math.max(width, point.x);
		// make sure the x of points[y] is a valid index
		if (this.points.get(point.y).size() <= point.x) this.expandList(this.points.get(point.y), this.width, () -> PaperPoint.NONE);

		this.points.get(point.y).set(point.x, value);
	}

	private PaperPoint getPoint(Point point) {
		if (point.y >= this.points.size() || point.x >= this.points.get(point.y).size()) return PaperPoint.NONE;

		return this.points.get(point.y).get(point.x);
	}

	private <T>void expandList(List<T> list, int newMaxIndex, Supplier<T> supplier) {
		for (int i = list.size(); i <= newMaxIndex; i++) {
			list.add(supplier.get());
		}
	}

	public void print() {
		final int width = this.width;

		AtomicInteger i = new AtomicInteger(0);
		this.points.stream()
			.map(line -> line.stream()
					.map(PaperPoint::toString)
					.collect(Collectors.joining("", "", PaperPoint.NONE.toString().repeat(width - line.size() + 1))))
			.forEach(System.out::println);
	}

	public long numOfDots() {
		return this.points.stream()
			.flatMap(List::stream)
			.filter(point -> point == PaperPoint.POINT)
			.count();
	}

	public void foldFirst() {
		Fold fold = this.folds.get(0);
		if (fold.horizontal()) this.foldHorizontal(fold);
		else	    	       this.foldVertical(fold);
	}

	public void foldAll() {
		for (Fold fold : this.folds) {
			if (fold.horizontal()) this.foldHorizontal(fold);
			else	    	       this.foldVertical(fold);
		}
	}

	private void foldHorizontal(Fold fold) {
		int newHeight = fold.loc();

		List<List<PaperPoint>> newPoints = new ArrayList<>(newHeight);

		for (int y = 1; y <= newHeight; y++) {
			List<PaperPoint> line = new ArrayList<>(this.width);

			for (int x = 0; x <= this.width; x++) {
				PaperPoint point = PaperPoint.NONE;

				if (newHeight - y >= 0)	    	    	 point = point.or(this.getPoint(new Point(x, newHeight - y)));
				if (newHeight + y <= this.points.size()) point = point.or(this.getPoint(new Point(x, newHeight + y)));

				line.add(point);
			}
			newPoints.add(0, line);
		}

		this.points = newPoints;
	}

	private void foldVertical(Fold fold) {
		int newWidth = fold.loc();

		List<List<PaperPoint>> newPoints = new ArrayList<>(this.points.size());

		for (int y = 0; y < this.points.size(); y++) {
			List<PaperPoint> line = new ArrayList<>(newWidth);

			for (int x = 1; x <= newWidth; x++) {
				PaperPoint point = PaperPoint.NONE;

				if (newWidth - x >= 0)	    	point = point.or(this.getPoint(new Point(newWidth - x, y)));
				if (newWidth + x <= this.width) point = point.or(this.getPoint(new Point(newWidth + x, y)));

				line.add(0, point);
			}
			newPoints.add(line);
		}

		this.width = newWidth;
		this.points = newPoints;
	}
}
