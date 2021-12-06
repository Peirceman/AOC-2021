import java.awt.Point;

public class Line {
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
