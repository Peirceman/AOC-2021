import java.io.Reader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Packet {
	private final List<Packet> SUB_PACKETS;
	private final byte[] BITS_BUFFER;
	private int bufferIndex;
	private int endIndex;
	private int bitsRead;

	public final int VERSION;
	public final int TYPE_ID;

	public boolean length15Bits;
	public int length;
	public long value;

	private static int hexToInt(int hex) {
		// System.out.println("c: " + (char) hex + " " + hex);
		hex -= '0';

		if (hex < 10) {
			return hex;
		}

		return hex + '0' - 'A' + 10;
	}

	// closes reader when done
	public Packet(Reader r) throws IOException {
		this(r, new byte[16], 16, 16);

		r.close();
	}

	private Packet(Reader r, byte[] buffer, int bufferIndex, int endIndex) throws IOException {
		// System.out.println(java.util.Arrays.toString(buffer));
		this.SUB_PACKETS = new ArrayList<>();
		this.BITS_BUFFER = buffer;
		this.bufferIndex = bufferIndex;
		this.bitsRead = 0;
		this.endIndex = endIndex;

		this.VERSION = bitsToInt(readBits(3, r));
		this.TYPE_ID = bitsToInt(readBits(3, r));
		// System.out.println("v: " + VERSION);
		// System.out.println("t: " + TYPE_ID);

		this.value = 0;
		this.length15Bits = false;
		this.length = 0;

		// this.print();
		if (TYPE_ID == 4) {
			parseConst(r);
		} else {
			parseOperator(r);
		}

	}

	private void parseConst(Reader r) throws IOException {
		boolean keepReading = true;
		while (keepReading) {
			keepReading = readBits(1, r)[0] == 1;
			this.value = (this.value << 4) | bitsToInt(readBits(4, r));
			// System.out.println(this.value);
		}
	}

	private void parseOperator(Reader r) throws IOException {
		this.length15Bits = readBits(1, r)[0] == 0;

		if (this.length15Bits) {
			this.length = bitsToInt(readBits(15, r));
			int lengthRead = 0;

			while (lengthRead < this.length) {
				Packet subPacket = new Packet(r, this.BITS_BUFFER, this.bufferIndex, this.endIndex);
				this.SUB_PACKETS.add(subPacket);
				this.bufferIndex = subPacket.bufferIndex;
				lengthRead += subPacket.bitsRead;
				// System.out.println(subPacket.value);
				// System.out.println(subPacket.bitsRead);
			}

			this.bitsRead += lengthRead;
			return;
		}

		this.length = bitsToInt(readBits(11, r));
		// System.out.println(this.length);

		for (int i = 0; i < length; i++) {
			Packet subPacket = new Packet(r, this.BITS_BUFFER, this.bufferIndex, this.endIndex);
			this.SUB_PACKETS.add(subPacket);
			this.bufferIndex = subPacket.bufferIndex;
			this.bitsRead += subPacket.bitsRead;
		}
	}

	private static int bitsToInt(byte[] bits) {
		int result = 0;
		for (byte bit : bits) {
			result = (result | bit) << 1;
		}
		return result >>> 1;
	}

	private byte[] readBits(int numOfBits, Reader r) throws IOException {
		byte[] result = new byte[numOfBits];

		for (int i = 0; i < numOfBits; i++) {
			if (bufferIndex >= this.endIndex) {
				fillBuffer(r);
			}
			result[i] = this.BITS_BUFFER[this.bufferIndex++];
			// System.out.println(result[i]);
		}

		this.bitsRead += numOfBits;
		return result;
	}

	private void fillBuffer(Reader r) throws IOException {
		for (int i = 0; i < this.BITS_BUFFER.length; i += 4) {
			int nextChar = r.read();
			if (nextChar == -1) {
				this.bufferIndex = 0;
				this.endIndex = i;
				return;
			}

			int next = hexToInt(nextChar);
			// System.out.println("b: " + Integer.toString(next, 2));
			BITS_BUFFER[i] = (byte) (next >>> 3);
			BITS_BUFFER[i + 1] = (byte) ((next >>> 2) & 1);
			BITS_BUFFER[i + 2] = (byte) ((next >>> 1) & 1);
			BITS_BUFFER[i + 3] = (byte) (next & 1);
		}

		this.bufferIndex = 0;
	}

	public void print() {
		this.print(0);
	}

	private void print(int indentlvl) {
		String indent = "  ".repeat(indentlvl);

		if (this.TYPE_ID == 4) {
			System.out.println(indent + "const:");
			indent += "  ";
			System.out.printf("%sver: %d%n%styp: %d%n%sval: %d%n", indent, this.VERSION, indent,
					this.TYPE_ID, indent, this.value);
			return;
		}

		System.out.println(indent + "opperand:");
		indent += "  ";
		System.out.printf("%sver: %d%n%styp: %d%n%ssubpackets:%n", indent, this.VERSION, indent,
				this.TYPE_ID, indent, this.value);
		for (Packet subPacket : this.SUB_PACKETS) {
			subPacket.print(indentlvl + 2);
		}
	}

	public int sumOfVersions() {
		if (this.TYPE_ID == 4)
			return this.VERSION;

		return this.VERSION + this.SUB_PACKETS.stream().mapToInt(Packet::sumOfVersions).sum();
	}

	public long calc() {
		return switch (this.TYPE_ID) {
			case 0 -> this.SUB_PACKETS.stream().mapToLong(Packet::calc).sum();
			case 1 -> this.SUB_PACKETS.stream().mapToLong(Packet::calc).reduce((a, b) -> a * b).getAsLong();
			case 2 -> this.SUB_PACKETS.stream().mapToLong(Packet::calc).min().getAsLong();
			case 3 -> this.SUB_PACKETS.stream().mapToLong(Packet::calc).max().getAsLong();
			case 4 -> this.value;
			case 5 -> this.SUB_PACKETS.get(0).calc() > this.SUB_PACKETS.get(1).calc() ? 1 : 0;
			case 6 -> this.SUB_PACKETS.get(0).calc() < this.SUB_PACKETS.get(1).calc() ? 1 : 0;
			case 7 -> this.SUB_PACKETS.get(0).calc() == this.SUB_PACKETS.get(1).calc() ? 1 : 0;
			default -> -1; // unreachable
		};
	}
}
