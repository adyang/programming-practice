package product;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class HighestProductOfThreeTest {
	@Test
	public void lessThanThreeInts() {
		assertExceptionThrown(new int[] { 1, 2 });
		assertExceptionThrown(new int[] { 1 });
		assertExceptionThrown(new int[] {});
	}

	private void assertExceptionThrown(int[] arrayOfInts) {
		try {
			calculateHighestProductOfThree(arrayOfInts);
			fail("Should throw exception when there are less than 3 ints.");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is(equalTo("Array should have at least 3 ints.")));
		}
	}

	@Test
	public void threeInts() {
		assertHighestProductOfThree(new int[] { 1, 2, 3 }, 6);
		assertHighestProductOfThree(new int[] { 1, 2, 2 }, 4);
	}

	@Test
	public void fourInts() {
		assertHighestProductOfThree(new int[] { 1, 2, 3, 4 }, 24);
		assertHighestProductOfThree(new int[] { 4, 3, 2, 1 }, 24);
		assertHighestProductOfThree(new int[] { 1, 4, 2, 2 }, 16);
	}

	@Test
	public void fourIntsWithNegative() {
		assertHighestProductOfThree(new int[] { 1, 4, -2, -2 }, 16);
		assertHighestProductOfThree(new int[] { -1, -10, -1, 2 }, 20);
	}

	@Test
	public void fiveIntsWithNegative() {
		assertHighestProductOfThree(new int[] { 1, 10, -5, 1, -100 }, 5000);
		assertHighestProductOfThree(new int[] { -10, -10, 1, 3, 2 }, 300);
	}

	private void assertHighestProductOfThree(int[] arrayOfInts, int expected) {
		assertThat(calculateHighestProductOfThree(arrayOfInts), is(equalTo(expected)));
	}

	private int calculateHighestProductOfThree(int[] arrayOfInts) {
		ensureAtLeastThreeIntsIn(arrayOfInts);
		return highestProductOfThree(arrayOfInts);
	}

	private void ensureAtLeastThreeIntsIn(int[] arrayOfInts) {
		if (arrayOfInts.length < 3) {
			throw new IllegalArgumentException("Array should have at least 3 ints.");
		}
	}

	private int highestProductOfThree(int[] arrayOfInts) {
		int highest = Math.max(arrayOfInts[0], arrayOfInts[1]);
		int lowest = Math.min(arrayOfInts[0], arrayOfInts[1]);
		int highestProductOfTwo = arrayOfInts[0] * arrayOfInts[1];
		int lowestProductOfTwo = arrayOfInts[0] * arrayOfInts[1];
		int highestProductOfThree = arrayOfInts[0] * arrayOfInts[1] * arrayOfInts[2];

		for (int i = 2; i < arrayOfInts.length; i++) {
			int currInt = arrayOfInts[i];
			highestProductOfThree = Math.max(highestProductOfTwo * currInt,
					Math.max(lowestProductOfTwo * currInt, highestProductOfThree));
			highestProductOfTwo = Math.max(highest * currInt, Math.max(lowest * currInt, highestProductOfTwo));
			lowestProductOfTwo = Math.min(highest * currInt, Math.min(lowest * currInt, lowestProductOfTwo));
			highest = Math.max(currInt, highest);
			lowest = Math.min(currInt, lowest);
		}

		return highestProductOfThree;
	}
}
