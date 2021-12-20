import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaveSystem {

	public static class Cave {
		private final List<Cave> connections = new ArrayList<>();
		private int remainingVisits;

		public Cave(String name) {
			this.remainingVisits = Character.isUpperCase(name.charAt(0)) ? -1 : 1;
		}

		public boolean canVisit() {
			return remainingVisits != 0;
		}
	
		public void visit() {
			this.remainingVisits--;
		}

		public void reset() {
			this.remainingVisits++;
		}
	
		public List<Cave> getConnections() {
			return this.connections;
		}
	
		public void addConnection(Cave cave) {
			this.connections.add(cave);
		}

		public void setMultipleTimes(boolean multipleTimes) {
			if (multipleTimes) this.remainingVisits++;
			else this.remainingVisits--;
		}

		public boolean isBigCave() {
			return this.remainingVisits < 0;
		}
	}

	private Cave start;
	private Cave end;
	Map<String, Cave> caveByName;
	Map<Cave, String> nameByCave;

	public CaveSystem() {
		this.caveByName = new HashMap<>();
		this.nameByCave = new HashMap<>();
		this.start      = new Cave("start");
		this.end        = new Cave("end");

		this.caveByName.put("start", this.start);
		this.caveByName.put("end", this.end);

		this.nameByCave.put(this.start, "start");
		this.nameByCave.put(this.end, "end");
	}

	public void parseLine(String line) {
		String[] split = line.split("-");
		this.connect(split[0], split[1]);
	}

	private void connect(String cave1Name, String cave2Name) {
		switch (cave1Name) {
			case "start" -> this.createStartConnection(cave2Name);
			case "end"   -> this.createEndConnection(cave2Name);
			default      -> this.createConnection(cave1Name, cave2Name);
		}
	}

	private void createStartConnection(String caveName) {
		this.start.addConnection(this.getCave(caveName));
	}

	private void createEndConnection(String caveName) {
		this.getCave(caveName).addConnection(this.end);
	}

	private void createConnection(String cave1Name, String cave2Name) {
		switch (cave2Name) {
			case "start" -> this.createStartConnection(cave1Name);
			case "end"   -> this.createEndConnection(cave1Name);
			default      -> {
				Cave cave1 = this.getCave(cave1Name);
				Cave cave2 = this.getCave(cave2Name);

				cave1.addConnection(cave2);
				cave2.addConnection(cave1);
			}
		}
	}

	private Cave getCave(String caveName) {
		if (!this.caveByName.containsKey(caveName)) this.caveByName.put(caveName, new Cave(caveName));

		return this.caveByName.get(caveName);
	}

	public long findAllPaths(boolean multipleTimes) {
		long sum = 0;
		if (multipleTimes) {
			for (Map.Entry<String, Cave> entry : this.caveByName.entrySet()) {
				Cave cave = entry.getValue();
				if (cave.isBigCave() || cave == this.end || cave == this.start) continue;

				Map<Cave, Integer> amountOfVisits = new HashMap<>();
				cave.setMultipleTimes(true);
				sum += this.bfs(this.start, cave, amountOfVisits);
				cave.setMultipleTimes(false);
			}
		}

		sum += this.start.getConnections()
			.stream()
			.mapToLong(this::bfs)
			.sum();

		return sum;
	}

	private long bfs(Cave cave) {
		if (cave == this.end) return 1L;

		cave.visit();
		long paths = cave.getConnections()
			.stream()
			.filter(Cave::canVisit)
			.mapToLong(this::bfs)
			.sum();

		cave.reset();

		return paths;
	}

	private long bfs(Cave cave, Cave multipleTimesCave, Map<Cave, Integer> amountOfVisits) {
		if (cave == this.end) {
			if (amountOfVisits.getOrDefault(multipleTimesCave, 0) < 2) return 0L;
			return 1L;
		}

		amountOfVisits.put(cave, amountOfVisits.getOrDefault(cave, 0) + 1);
		cave.visit();
		long paths = cave.getConnections()
			.stream()
			.filter(Cave::canVisit)
			.mapToLong(c -> this.bfs(c, multipleTimesCave, amountOfVisits))
			.sum();

		cave.reset();

		amountOfVisits.put(cave, amountOfVisits.get(cave) - 1);

		return paths;
	}
}

