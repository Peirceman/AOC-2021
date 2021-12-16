import java.util.Set;

public class Octopus {
	private int energyLevel;
	private boolean flashed;

	public Octopus(int energyLevel, boolean flashed) {
		this.energyLevel = energyLevel;
		this.flashed = flashed;
	}

	public Octopus(int energyLevel) {
		this(energyLevel, false);
	}

	public int getEnergyLevel() {
		return this.energyLevel;
	}

	public boolean hasFlashed() {
		return this.flashed;
	}

	public void reset() {
		this.flashed = false;
	}

	public boolean increaseEnergyLevel() {
		if (this.flashed) return false;

		if (++this.energyLevel > 9) {
			this.energyLevel = 0;
			this.flashed = true;
			return true;
		} 

		return false;
	}

	@Override
	public String toString() {
		return "Octopus[" +
			"energyLevel=" + getEnergyLevel() +
			",flashed=" + flashed +
			"]";
	}

}
