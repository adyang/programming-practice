package knapsack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ZeroOneCakeSackTest {
	@Test
	public void emptyCakeTypes() {
		assertMaxSackValue(new CakeType[] {}, 2, 0L);
	}

	@Test
	public void zeroWeightSackAndNonZeroWeightCake() {
		assertMaxSackValue(new CakeType[] { new CakeType(2, 2) }, 0, 0L);
	}

	@Test
	public void zeroWeightSackAndZeroWeightCakesWithValue() {
		assertMaxSackValue(new CakeType[] { new CakeType(0, 2) }, 0, 2L);
		assertMaxSackValue(new CakeType[] { new CakeType(0, 2), new CakeType(0, 6) }, 0, 8L);
	}

	@Test
	public void oneWeightSack() {
		assertMaxSackValue(new CakeType[] { new CakeType(2, 2) }, 1, 0L);
		assertMaxSackValue(new CakeType[] { new CakeType(1, 2) }, 1, 2L);
		assertMaxSackValue(new CakeType[] { new CakeType(1, 2), new CakeType(0, 3) }, 1, 5L);
		assertMaxSackValue(new CakeType[] { new CakeType(1, 2), new CakeType(1, 3) }, 1, 3L);
	}

	@Test
	public void twoWeightSack() {
		assertMaxSackValue(new CakeType[] { new CakeType(3, 2) }, 2, 0L);
		assertMaxSackValue(new CakeType[] { new CakeType(2, 2) }, 2, 2L);
		assertMaxSackValue(new CakeType[] { new CakeType(2, 2), new CakeType(2, 3) }, 2, 3L);
		assertMaxSackValue(new CakeType[] { new CakeType(1, 2), new CakeType(1, 3) }, 2, 5L);
		assertMaxSackValue(new CakeType[] { new CakeType(2, 2), new CakeType(1, 3) }, 2, 3L);
	}

	@Test
	public void multipleWeightSack() {
		assertMaxSackValue(new CakeType[] { new CakeType(6, 10), new CakeType(1, 5), new CakeType(2, 7),
				new CakeType(5, 12), new CakeType(4, 8), new CakeType(3, 6) }, 10, 26L);
		assertMaxSackValue(new CakeType[] { new CakeType(10, 60), new CakeType(20, 100), new CakeType(30, 120) }, 50,
				220L);
	}

	private void assertMaxSackValue(CakeType[] cakeTypes, int capacity, long expectedMaxValue) {
		assertEquals(expectedMaxValue, maxSackValue(cakeTypes, capacity));
	}

	private long maxSackValue(CakeType[] cakeTypes, int capacity) {
		long[][] maxValues = new long[cakeTypes.length + 1][capacity + 1];

		for (int i = 0; i < cakeTypes.length; i++) {
			for (int currCap = 0; currCap <= capacity; currCap++) {
				int j = i + 1;
				if (cakeTypes[i].weight > currCap) {
					maxValues[j][currCap] = maxValues[j - 1][currCap];
				} else {
					maxValues[j][currCap] = Math.max(maxValues[j - 1][currCap],
							maxValues[j - 1][currCap - cakeTypes[i].weight] + cakeTypes[i].value);
				}
			}
		}

		return maxValues[cakeTypes.length][capacity];
	}

	private static class CakeType {
		int weight;
		int value;

		public CakeType(int weight, int value) {
			this.weight = weight;
			this.value = value;
		}
	}
}
