package product;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ProductOfAllOtherIntegerTest {
	@Test
	public void lessThanTwoInts() {
		assertExceptionThrown(new int[] {});
		assertExceptionThrown(new int[] { 2 });
	}

	private void assertExceptionThrown(int[] ints) {
		try {
			getProductOfAllIntsExceptAtIndex(ints);
			fail("Should throw exception when there are less than 2 ints.");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is(equalTo("Array should have at least 2 ints.")));
		}
	}

	@Test
	public void twoInts() {
		assertProductsExceptAtIndex(new int[] { 1, 1 }, new int[] { 1, 1 });
		assertProductsExceptAtIndex(new int[] { 1, 2 }, new int[] { 2, 1 });
		assertProductsExceptAtIndex(new int[] { 4, 5 }, new int[] { 5, 4 });
		assertProductsExceptAtIndex(new int[] { 1, 0 }, new int[] { 0, 1 });
	}

	@Test
	public void threeInts() {
		assertProductsExceptAtIndex(new int[] { 1, 2, 3 }, new int[] { 6, 3, 2 });
		assertProductsExceptAtIndex(new int[] { 1, 0, 3 }, new int[] { 0, 3, 0 });
	}

	@Test
	public void fourInts() {
		assertProductsExceptAtIndex(new int[] { 1, 7, 3, 4 }, new int[] { 84, 12, 28, 21 });
		assertProductsExceptAtIndex(new int[] { 1, 0, 3, 4 }, new int[] { 0, 12, 0, 0 });
		assertProductsExceptAtIndex(new int[] { 1, 0, 3, 0 }, new int[] { 0, 0, 0, 0 });
	}

	private void assertProductsExceptAtIndex(int[] input, int[] expected) {
		assertThat(getProductOfAllIntsExceptAtIndex(input), is(equalTo(expected)));
	}

	private int[] getProductOfAllIntsExceptAtIndex(int[] ints) {
		ensureAtLeastTwo(ints);
		return generateProductOfAllIntsExceptAtIndex(ints);
	}

	private void ensureAtLeastTwo(int[] ints) {
		if (ints.length < 2) {
			throw new IllegalArgumentException("Array should have at least 2 ints.");
		}
	}

	private int[] generateProductOfAllIntsExceptAtIndex(int[] ints) {
		int[] products = new int[ints.length];
		fillProductsOfAllIntsBeforeEachIndex(ints, products);
		fillProductsOfAllIntsAfterEachIndex(ints, products);
		return products;
	}

	private void fillProductsOfAllIntsAfterEachIndex(int[] ints, int[] products) {
		int partialProduct = 1;
		for (int j = ints.length - 1; j >= 0; j--) {
			products[j] *= partialProduct;
			partialProduct *= ints[j];
		}
	}

	private void fillProductsOfAllIntsBeforeEachIndex(int[] ints, int[] products) {
		int partialProduct = 1;
		for (int i = 0; i < ints.length; i++) {
			products[i] = partialProduct;
			partialProduct *= ints[i];
		}
	}
}
