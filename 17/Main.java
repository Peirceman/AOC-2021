import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {
	private static File file = new File("input.txt");

	public static void main(String[] args) throws IOException {
		part1();
		part2();
	}

	/*
	 * The path of the probe is porablola, which is symetric.
	 * if the start y velocity is positive, you will always
	 * end up with the negative of that velocity at height 0
	 * so the question becomes what is the lowest y velocity
	 * that ends up in the target area. Wich is just reacing
	 * the lowest point in the target area in 1 step.
	 *
	 * the max height for a certain y velocity (y) is:
	 * y+y-1+y-2+..+y-y
	 * = y*y - sum(1, y)
	 */
	private static void part1() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		String[] line = r.readLine().split("(=|,)");
		r.close();
		String[] yRange = line[3].split("\\.+");
		int lowestY = Integer.parseInt(yRange[0]);
		int yVelocityAtStart = Math.abs(lowestY);

		System.out.printf("part1: %d%n", yVelocityAtStart * yVelocityAtStart - sum(yVelocityAtStart));
	}

	// sum from 1 to `val`
	private static int sum(int val) {
		if (val % 2 == 0) {
			return (val + 1) * val / 2;
		}

		return val * (val / 2) + val;
	}

	// the same as amount of x velocities that land in the area * the amount y
	// velocites that land in the area
	// in that amout of steps or more (if xVelocity = 0)
	private static void part2() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		String[] line = r.readLine().split("(=|,)");
		r.close();
		String[] xRange = line[1].split("\\.+");
		String[] yRange = line[3].split("\\.+");
		int lowestX = Integer.parseInt(xRange[0]);
		int highestX = Integer.parseInt(xRange[1]);
		int lowestY = Integer.parseInt(yRange[0]);
		int highestY = Integer.parseInt(yRange[1]);

		int amountOfVectors = 0;

		int[][] xStepRanges = new int[highestX][];
		// everything that gets there in more steps
		for (int xVelocity = 1; xVelocity <= highestX; xVelocity++) {
			xStepRanges[xVelocity - 1] = xStepsToReach(xVelocity, lowestX, highestX);
		}

		for (int yVelocity = lowestY; yVelocity < 0; yVelocity++) {
			int[] yStepRange = yStepsToReach(yVelocity, lowestY, highestY);

			if (yStepRange == null) {
				continue;
			}

			for (int i = 0; i < xStepRanges.length; i++) {
				int[] xStepRange = xStepRanges[i];

				if (xStepRange == null) {
					continue;
				}

				int positiveYVelocity = Math.abs(yVelocity + 1);

				if (xStepRange[1] == Integer.MAX_VALUE) {
					if (yStepRange[1] >= xStepRange[0]) {
						amountOfVectors++;
					}

					if (yStepRange[1] + 2 * positiveYVelocity + 1 >= xStepRange[0]) {
						amountOfVectors++;
					}

					continue;
				}

				if ((xStepRange[1] >= yStepRange[0] && xStepRange[0] <= yStepRange[1])
						|| (yStepRange[1] >= xStepRange[0] && yStepRange[0] <= xStepRange[1])) {
					amountOfVectors++;
				}

				yStepRange[0] += 2 * positiveYVelocity + 1;
				yStepRange[1] += 2 * positiveYVelocity + 1;

				if ((xStepRange[1] >= yStepRange[0] && xStepRange[0] <= yStepRange[1])
						|| (yStepRange[1] >= xStepRange[0] && yStepRange[0] <= xStepRange[1])) {
					amountOfVectors++;
				}

				yStepRange[0] -= 2 * positiveYVelocity + 1;
				yStepRange[1] -= 2 * positiveYVelocity + 1;

			}
		}

		System.out.printf("part2: %d%n", amountOfVectors);
	}

	private static int[] yStepsToReach(int velocity, int lowest, int highest) {
		return yStepsToReach(velocity, lowest, highest, 0);
	}

	private static int[] yStepsToReach(int velocity, int lowest, int highest, int steps) {
		// next step is out of target regon
		if (velocity < lowest) {
			// this step is out
			if (lowest > 0 || highest < 0) {
				return null;
			}

			return new int[] { steps, steps };
		}

		// this step is in target regon
		if (highest >= 0 && lowest <= 0) {
			int[] result = new int[2];
			int[] nextStep = yStepsToReach(velocity - 1, lowest - velocity, highest - velocity, steps + 1);
			if (nextStep == null) {
				return null;
			}

			result[0] = steps;
			result[1] = nextStep[1];
			return result;
		}

		return yStepsToReach(velocity - 1, lowest - velocity, highest - velocity, steps + 1);
	}

	// an array containing the lowest amount of steps needed to reach the target and
	// the higest amount. if the higest amount is Integer.MAX_VALUE you can have
	// infite steps
	private static int[] xStepsToReach(int velocity, int lowest, int highest) {
		int maxDistanceTravelled = sum(velocity);
		if (maxDistanceTravelled < lowest) {
			// System.out.println("0: " + velocity);
			return null;
		}

		int minSteps = Integer.MAX_VALUE;
		int steps = 1;
		int distanceTravelled = velocity;
		while (distanceTravelled <= highest && steps <= velocity) {
			if (minSteps == Integer.MAX_VALUE && distanceTravelled >= lowest) {
				minSteps = steps;
			}

			distanceTravelled += velocity - steps++;
		}

		if (distanceTravelled > highest) {
			// skipped over target area
			if (minSteps == Integer.MAX_VALUE) {
				// System.out.println("1: " + velocity);
				return null;
			}

			// landed in target area 'default' result
			// System.out.println("2: " + velocity);
			return new int[] { minSteps, steps - 1 };
		}

		// maxDistance isfurthest distance

		// only max steps works
		if (minSteps == Integer.MAX_VALUE && maxDistanceTravelled >= lowest) {
			// System.out.println("3: " + velocity);
			return new int[] { steps, Integer.MAX_VALUE };
		}

		// everything until max steps
		if (maxDistanceTravelled <= highest) {
			// System.out.println("5: " + velocity);
			return new int[] { minSteps, Integer.MAX_VALUE };
		}

		// everything until max steps - 1
		// System.out.println("6: " + velocity);
		return new int[] { minSteps, steps - 1 };
		/*
		 * int steps = highest / velocity;
		 * while (true) {
		 * int distanceWithSteps = velocity * steps - sum(steps);
		 * if (distanceWithSteps >= lowest && distanceWithSteps <= highest) {
		 * break;
		 * }
		 * 
		 * if (distanceWithSteps < lowest) {
		 * steps = steps * 3 / 2;
		 * continue;
		 * }
		 * 
		 * steps = steps / 2;
		 * }
		 * 
		 * int lowestSteps = steps - 1;
		 * while (true) {
		 * int distanceWithSteps = velocity * lowestSteps - sum(lowestSteps);
		 * if (distanceWithSteps >= lowest) {
		 * lowestSteps--;
		 * continue;
		 * }
		 * 
		 * lowestSteps++;
		 * break;
		 * }
		 * 
		 * int higestSteps = steps + 1;
		 * while (true) {
		 * int distanceWithSteps = velocity * higestSteps - sum(higestSteps);
		 * if (distanceWithSteps <= highest) {
		 * higestSteps++;
		 * continue;
		 * }
		 * 
		 * higestSteps--;
		 * break;
		 * }
		 * 
		 * return new int[] { lowestSteps, higestSteps };
		 */
	}
}
