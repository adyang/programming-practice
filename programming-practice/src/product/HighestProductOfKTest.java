package product;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.stream.IntStream;

import org.junit.Test;

public class HighestProductOfKTest {
	@Test
	public void nonPositiveK() {
		assertNonPositiveKExceptionThrown(new int[] { 1, 2, 3 }, 0);
	}

	private void assertNonPositiveKExceptionThrown(int[] arrayOfInts, int k) {
		try {
			calculateHighestProductOfK(arrayOfInts, k);
			fail("Should throw exception when k <= 0.");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is(equalTo("k must be > 0.")));
		}
	}

	@Test
	public void lessThanKInts() {
		assertLessThanKIntsExceptionThrown(new int[] { 1, 2 }, 3);
		assertLessThanKIntsExceptionThrown(new int[] { 1 }, 3);
		assertLessThanKIntsExceptionThrown(new int[] {}, 3);
		assertLessThanKIntsExceptionThrown(new int[] { 1, 2, 3 }, 4);
		assertLessThanKIntsExceptionThrown(new int[] { 1, 2, 3, 4 }, 5);
	}

	private void assertLessThanKIntsExceptionThrown(int[] arrayOfInts, int k) {
		try {
			calculateHighestProductOfK(arrayOfInts, k);
			fail("Should throw exception when there are less than " + k + " ints.");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is(equalTo("Array should have at least " + k + " ints.")));
		}
	}

	@Test
	public void highestOfThree_threeInts() {
		assertHighestProductOfThree(new int[] { 1, 2, 3 }, 6);
		assertHighestProductOfThree(new int[] { 1, 2, 2 }, 4);
	}

	@Test
	public void highestOfThree_fourInts() {
		assertHighestProductOfThree(new int[] { 1, 2, 3, 4 }, 24);
		assertHighestProductOfThree(new int[] { 4, 3, 2, 1 }, 24);
		assertHighestProductOfThree(new int[] { 1, 4, 2, 2 }, 16);
	}

	@Test
	public void highestOfThree_fourIntsWithNegative() {
		assertHighestProductOfThree(new int[] { 1, 4, -2, -2 }, 16);
		assertHighestProductOfThree(new int[] { -1, -10, -1, 2 }, 20);
	}

	@Test
	public void highestOfThree_fiveIntsWithNegative() {
		assertHighestProductOfThree(new int[] { 1, 10, -5, 1, -100 }, 5000);
		assertHighestProductOfThree(new int[] { -10, -10, 1, 3, 2 }, 300);
	}

	@Test
	public void highestOfFour_fourInts() {
		assertHighestProductOfFour(new int[] { 1, 2, 3, 4 }, 24);
		assertHighestProductOfFour(new int[] { 2, 2, 3, 4 }, 48);
		assertHighestProductOfFour(new int[] { 4, 2, 3, 3 }, 72);
	}

	@Test
	public void highestOfFour_fiveInts() {
		assertHighestProductOfFour(new int[] { 1, 2, 3, 4, 5 }, 120);
		assertHighestProductOfFour(new int[] { 5, 4, 3, 2, 1 }, 120);
		assertHighestProductOfFour(new int[] { 5, 4, 3, 2, 3 }, 180);
	}

	@Test
	public void highestOfFour_fiveIntsWithNegative() {
		assertHighestProductOfFour(new int[] { 2, 2, 3, -4, -5 }, 120);
		assertHighestProductOfFour(new int[] { -1, -10, 3, 4, -5 }, 600);
	}

	@Test
	public void highestOfFive_fiveInts() {
		assertHighestProductOfFive(new int[] { 2, 2, 3, 4, 5 }, 240);
	}

	@Test
	public void highestOfFive_sixInts() {
		assertHighestProductOfFive(new int[] { 2, 2, 3, 4, 5, 6 }, 720);
		assertHighestProductOfFive(new int[] { 6, 2, 5, 3, 4, 2 }, 720);
	}

	@Test
	public void highestOfFive_sixIntsWithNegative() {
		assertHighestProductOfFive(new int[] { 2, 2, 3, 4, -5, -6 }, 720);
		assertHighestProductOfFive(new int[] { 2, 2, -3, 4, -5, -6 }, 480);
	}

	private void assertHighestProductOfThree(int[] arrayOfInts, int expected) {
		assertThat(calculateHighestProductOfK(arrayOfInts, 3), is(equalTo(expected)));
	}

	private void assertHighestProductOfFour(int[] arrayOfInts, int expected) {
		assertThat(calculateHighestProductOfK(arrayOfInts, 4), is(equalTo(expected)));
	}

	private void assertHighestProductOfFive(int[] arrayOfInts, int expected) {
		assertThat(calculateHighestProductOfK(arrayOfInts, 5), is(equalTo(expected)));
	}

	private int calculateHighestProductOfK(int[] arrayOfInts, int k) {
		validateInputs(arrayOfInts, k);
		return highestProductOfK(arrayOfInts, k);
	}

	private void validateInputs(int[] arrayOfInts, int k) {
		ensurePositive(k);
		ensureAtLeastKInts(arrayOfInts, k);
	}

	private void ensurePositive(int k) {
		if (k <= 0) {
			throw new IllegalArgumentException("k must be > 0.");
		}
	}

	private void ensureAtLeastKInts(int[] arrayOfInts, int k) {
		if (arrayOfInts.length < k) {
			throw new IllegalArgumentException("Array should have at least " + k + " ints.");
		}
	}

	private int highestProductOfK(int[] arrayOfInts, int k) {
		int[] highestProducts = initialiseProducts(arrayOfInts, k);
		int[] lowestProducts = initialiseProducts(arrayOfInts, k);
		calculateHighestAndLowestProducts(arrayOfInts, k, highestProducts, lowestProducts);
		return highestProducts[k - 1];
	}

	private int[] initialiseProducts(int[] arrayOfInts, int k) {
		int[] products = new int[k];
		products[0] = arrayOfInts[0];
		for (int i = 1; i < products.length; i++) {
			products[i] = products[i - 1] * arrayOfInts[i];
		}
		return products;
	}

	private void calculateHighestAndLowestProducts(int[] arrayOfInts, int k, int[] highestProducts,
			int[] lowestProducts) {
		for (int i = 1; i < arrayOfInts.length; i++) {
			if (isFirstKMinusOneElement(k, i)) {
				calculateProductsSoFarFromCountAndDown(k - 2, arrayOfInts[i], highestProducts, lowestProducts);
			} else {
				calculateProductsSoFarFromCountAndDown(k, arrayOfInts[i], highestProducts, lowestProducts);
			}
			calculateHighestAndLowestIntSoFar(arrayOfInts[i], highestProducts, lowestProducts);
		}
	}

	private boolean isFirstKMinusOneElement(int k, int i) {
		return i < k - 1;
	}

	private void calculateProductsSoFarFromCountAndDown(int pdtCount, int currInt, int[] highestProducts,
			int[] lowestProducts) {
		for (int j = pdtCount - 1; j >= 1; j--) {
			highestProducts[j] = maxOf(highestProducts[j - 1] * currInt, lowestProducts[j - 1] * currInt,
					highestProducts[j]);
			lowestProducts[j] = minOf(highestProducts[j - 1] * currInt, lowestProducts[j - 1] * currInt,
					lowestProducts[j]);
		}
	}

	private void calculateHighestAndLowestIntSoFar(int currInt, int[] highestProducts, int[] lowestProducts) {
		highestProducts[0] = Math.max(currInt, highestProducts[0]);
		lowestProducts[0] = Math.min(currInt, lowestProducts[0]);
	}

	private int maxOf(int... ints) {
		return IntStream.of(ints).max().getAsInt();
	}

	private int minOf(int... ints) {
		return IntStream.of(ints).min().getAsInt();
	}
}
