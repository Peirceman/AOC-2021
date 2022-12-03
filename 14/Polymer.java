import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

public class Polymer {
	public final char lastChar;
	private Map<String, Long> pairs = new HashMap<>();
	private final Map<String, Character> rules = new HashMap<>();

	public Polymer(String state) {
		this.lastChar = state.charAt(state.length() - 1);
		for (int i = 1; i < state.length(); i++) {
			addPair(this.pairs, state.substring(i - 1, i + 1), 1L);
		}
	}

	private <T> void addPair(Map<T, Long> map, T pair, long count) {
		map.put(pair, map.getOrDefault(pair, 0L) + count);
	}

	public void parseRule(String line) {
		String[] parts = line.split(" -> ");
		rules.put(parts[0], parts[1].charAt(0));
	}

	public void nextStep() {
		Map<String, Long> newPairs = new HashMap<>();

		for (Map.Entry<String, Long> pair : this.pairs.entrySet()) {
			char newChar = this.rules.get(pair.getKey());

			addPair(newPairs, new String(new char[] { pair.getKey().charAt(0), newChar }), pair.getValue());
			addPair(newPairs, new String(new char[] { newChar, pair.getKey().charAt(1) }), pair.getValue());
		}

		this.pairs = newPairs;
	}

	public long maxCount() {
		Map<Character, Long> counts = new HashMap<>();
		long maxCount = Long.MIN_VALUE;

		counts.put(this.lastChar, 1L);

		for (Map.Entry<String, Long> entry : this.pairs.entrySet()) {
			char curChar = entry.getKey().charAt(0);
			long curCount = entry.getValue() + counts.getOrDefault(curChar, 0L);
			counts.put(curChar, curCount);

			maxCount = Math.max(curCount, maxCount);
		}

		return maxCount;
	}

	public long minCount() {
		Map<Character, Long> counts = new HashMap<>();
		long minCount = Long.MAX_VALUE;

		counts.put(this.lastChar, 1L);

		for (Map.Entry<String, Long> entry : this.pairs.entrySet()) {
			addPair(counts, entry.getKey().charAt(0), entry.getValue());
		}

		for (Map.Entry<Character, Long> entry : counts.entrySet()) {
			minCount = Math.min(minCount, entry.getValue());
		}

		return minCount;
	}
}
