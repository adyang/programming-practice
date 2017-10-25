package temperature;

public class TempTracker {
	private static final int MAX_TEMP = 110;
	private int max = Integer.MIN_VALUE;
	private int min = Integer.MAX_VALUE;
	private int sumOfTemps = 0;
	private int numTemps = 0;
	private int[] occurrences = new int[MAX_TEMP + 1];
	private int maxOccurrences = 0;
	private int mode;

	public void insert(int temp) {
		calculateForMax(temp);
		calculateForMin(temp);
		calculateForMean(temp);
		calculateForMode(temp);
	}

	private void calculateForMax(int temp) {
		max = Math.max(temp, max);
	}

	private void calculateForMin(int temp) {
		min = Math.min(temp, min);
	}

	private void calculateForMean(int temp) {
		sumOfTemps += temp;
		numTemps++;
	}

	private void calculateForMode(int temp) {
		occurrences[temp]++;
		if (occurrences[temp] > maxOccurrences) {
			mode = temp;
			maxOccurrences = occurrences[temp];
		}
	}

	public int getMax() {
		checkIfTemperatureAvailable();
		return max;
	}

	public int getMin() {
		checkIfTemperatureAvailable();
		return min;
	}

	public double getMean() {
		checkIfTemperatureAvailable();
		return (double) sumOfTemps / numTemps;
	}

	public int getMode() {
		checkIfTemperatureAvailable();
		return mode;
	}

	private void checkIfTemperatureAvailable() {
		if (numTemps == 0) {
			throw new IllegalStateException("No temperature available to compute results.");
		}
	}
}
