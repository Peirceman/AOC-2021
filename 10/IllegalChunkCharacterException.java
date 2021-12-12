@SuppressWarnings("serial")
public class IllegalChunkCharacterException extends RuntimeException {
	public IllegalChunkCharacterException(char illegalChar, boolean opener) {
		super("Illegal chunk " + (opener ? "opener" : "closer") + " character `" + illegalChar + "`");
	}
}
