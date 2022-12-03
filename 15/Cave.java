import java.util.Arrays;

public class Cave {
	private class Node {
		/*
		 * 0
		 * 1 n 2
		 * 3
		 */
		public final Node[] surrounding = new Node[] { null, null, null, null };
		public final int riskLevel;
		public int leastRisk = -1; // can't be negative value, so not initialized

		public Node(int riskLevel) {
			this.riskLevel = riskLevel;
		}
	}

	private Node start = null;
	private Node end = null;
	private int lineNum = 0;
	private int width = 0;

	private void parseFirstLine(String line) {
		this.width = line.length();
		this.start = new Node(line.charAt(0) - '0');
		boolean isFirstChar = true;
		Node left = this.start;

		for (char c : line.toCharArray()) {
			if (isFirstChar) {
				isFirstChar = false;
				continue;
			}

			Node current = new Node(c - '0');
			left.surrounding[2] = current;
			current.surrounding[1] = left;

			left = current;
		}

		this.end = left;
		this.lineNum = 1;
	}

	public void parseLine(String line) {
		if (lineNum == 0) {
			parseFirstLine(line);
			return;
		}

		Node left = null;
		Node above = this.start;

		for (int i = 1; i < lineNum; i++) {
			above = above.surrounding[3];
		}

		for (char c : line.toCharArray()) {
			Node cur = new Node(c - '0');

			cur.surrounding[0] = above;
			cur.surrounding[1] = left;

			if (left != null)
				left.surrounding[2] = cur;

			above.surrounding[3] = cur;

			left = cur;
			above = above.surrounding[2];
		}

		this.end = left;
		this.lineNum++;
	}

	public int leastRiskPath() {
		if (start.leastRisk < 0) {
			this.calculateLeastRiskPath();
		}

		return start.leastRisk;
	}

	private boolean wentBack;

	private int leastNextRisk(Node node, boolean check4) {
		Node up = node.surrounding[0];
		Node left = node.surrounding[1];
		Node right = node.surrounding[2];
		Node down = node.surrounding[3];
		boolean rNull = right == null;
		boolean dNull = down == null;
		int uVal = up == null ? Integer.MAX_VALUE : (up.leastRisk + up.riskLevel);
		int lVal = left == null ? Integer.MAX_VALUE : (left.leastRisk + left.riskLevel);
		int rVal = rNull ? Integer.MAX_VALUE : (right.leastRisk + right.riskLevel);
		int dVal = dNull ? Integer.MAX_VALUE : (down.leastRisk + down.riskLevel);

		if (rNull && dNull) {
			return 0;
		}

		if (check4) {
			int backVal = Math.min(uVal, lVal);
			int frontVal = Math.min(rVal, dVal);

			if (backVal <= frontVal) {
				wentBack = true;
				return backVal;
			}

			wentBack = false;
			return frontVal;
		}

		return Math.min(rVal, dVal);
	}

	// returns the new value for addAbove
	private boolean initNextLayer(int i, boolean addAbove, Node current, Node[] nextLayer) {
		if (i == 0) {
			if (addAbove && current.surrounding[0] != null) {
				nextLayer[0] = current.surrounding[0];
				nextLayer[1] = current.surrounding[1];
				return true;
			}

			nextLayer[0] = current.surrounding[1];
			return false;
		}

		if (addAbove) {
			nextLayer[i + 1] = current.surrounding[1];
			return true;
		}

		nextLayer[i] = current.surrounding[1];
		return false;
	}

	// returns true if start has been reached
	private boolean calculateCurrentLayer(Node[] currentLayer, Node[] nextLayer) {
		boolean addAbove = true;

		// check current layer on 2 sides
		for (int i = 0; i < currentLayer.length; i++) {
			Node current = currentLayer[i];
			if (current == null) {
				break;
			}

			current.leastRisk = this.leastNextRisk(current, false);

			if (current == this.start) {
				return true;
			}

			addAbove = initNextLayer(i, addAbove, current, nextLayer);
		}

		return false;
	}

	// !!NOTE: CHANGES VALUE OF prevLayer!!
	private void calculatePreviousLayer(Node[] prevLayer) {
		Node[] nextPrevLayer = new Node[Math.min(this.width, this.lineNum)];
		Node[] tmp;
		int nextPrevIndex;
		boolean prevAdded = false;

		do {
			nextPrevIndex = 0;

			for (Node current : prevLayer) {
				if (current == null) {
					break;
				}

				current.leastRisk = leastNextRisk(current, true);
				if (!wentBack) {
					prevAdded = false;
					continue;
				}

				if (!prevAdded) {
					nextPrevLayer[nextPrevIndex++] = current.surrounding[2];
				}

				nextPrevLayer[nextPrevIndex++] = current.surrounding[3];

				prevAdded = true;
			}

			tmp = prevLayer;
			prevLayer = nextPrevLayer;
			nextPrevLayer = tmp;
			Arrays.fill(nextPrevLayer, null);
		} while (nextPrevIndex != 0); // if something has been added
	}

	private void calculateLeastRiskPath() {
		Node[] currentLayer = new Node[Math.min(this.width, this.lineNum)];
		Node[] nextLayer = new Node[Math.min(this.width, this.lineNum)];
		Node[] prevLayer = new Node[Math.min(this.width, this.lineNum)];
		Node[] tmp;
		currentLayer[0] = this.end;

		while (true) {
			if (calculateCurrentLayer(currentLayer, nextLayer)) {
				return;
			}

			// check previous layer on 4 sides
			if (prevLayer[0] != null) {
				calculatePreviousLayer(prevLayer);
				// prevLayer isn't used
				calculateCurrentLayer(currentLayer, prevLayer);
			}

			tmp = prevLayer;
			prevLayer = currentLayer;
			currentLayer = nextLayer;
			nextLayer = tmp;
			Arrays.fill(nextLayer, null);
		}
	}

	public void print() {
		for (Node rowStart = this.start; rowStart != null; rowStart = rowStart.surrounding[3]) {
			for (Node node = rowStart; node != null; node = node.surrounding[2]) {
				System.out.printf("[%d %03d] ", node.riskLevel, node.leastRisk);
			}

			System.out.println();
		}
	}
}
