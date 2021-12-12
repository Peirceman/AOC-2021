import java.util.List;
import java.util.Set;
import java.util.HashSet;

public enum Chunk {
	C1('(', ')', 3, 1),
	C2('[', ']', 57, 2),
	C3('{', '}', 1197, 3),
	C4('<', '>', 25137, 4);

	private static Set<Character> openers = new HashSet<>(List.of(C1.opener, C2.opener, C3.opener, C4.opener));
	private static Set<Character> closers = new HashSet<>(List.of(C1.closer, C2.closer, C3.closer, C4.closer));

	public char opener;
	public char closer;
	public int illegalScore;
	public int incompleteScore;

	Chunk(char opener, char closer, int illegalScore, int incompleteScore) {
		this.opener = opener;
		this.closer = closer;
		this.illegalScore = illegalScore;
		this.incompleteScore = incompleteScore;
	}

	public static boolean isOpener(char ch) {
		return openers.contains(ch);
	}

	public static boolean isCloser(char ch) {
		return closers.contains(ch);
	}


	public static Chunk getByOpener(char ch) {
		return switch (ch) {
			case '(' -> C1; //:
			case '[' -> C2; //:
			case '{' -> C3; //:
			case '<' -> C4; //:
			default  -> throw new IllegalChunkCharacterException(ch, true);
		};
	}

	public static int getIllegalScore(char ch) {
		return switch(ch) {
			case ')' -> C1.illegalScore; //:
			case ']' -> C2.illegalScore; //:
			case '}' -> C3.illegalScore; //:
			case '>' -> C4.illegalScore; //:
			default  -> throw new IllegalChunkCharacterException(ch, false);
		};
	}
}
