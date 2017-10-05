package stocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MaxProfitStocksTest {
	@Test
	public void lessThanTwoStockPrices() {
		assertExceptionThrown(new int[] {});
		assertExceptionThrown(new int[] { 3 });
	}

	@Test
	public void twoStockPrices() {
		assertStockPricesMaxProfit(new int[] { 4, 5 }, 1);
		assertStockPricesMaxProfit(new int[] { 4, 9 }, 5);
		assertStockPricesMaxProfit(new int[] { 4, 3 }, -1);
	}

	@Test
	public void threeStockPrices() {
		assertStockPricesMaxProfit(new int[] { 4, 5, 9 }, 5);
		assertStockPricesMaxProfit(new int[] { 4, 9, 5 }, 5);
		assertStockPricesMaxProfit(new int[] { 5, 9, 4 }, 4);
		assertStockPricesMaxProfit(new int[] { 9, 4, 5 }, 1);
		assertStockPricesMaxProfit(new int[] { 9, 5, 4 }, -1);
	}

	@Test
	public void fourStockPrices() {
		assertStockPricesMaxProfit(new int[] { 4, 5, 9, 11 }, 7);
		assertStockPricesMaxProfit(new int[] { 4, 5, 9, 7 }, 5);
		assertStockPricesMaxProfit(new int[] { 9, 4, 5, 7 }, 3);
		assertStockPricesMaxProfit(new int[] { 4, 9, 5, 7 }, 5);
		assertStockPricesMaxProfit(new int[] { 5, 4, 9, 7 }, 5);
		assertStockPricesMaxProfit(new int[] { 5, 7, 4, 9 }, 5);
		assertStockPricesMaxProfit(new int[] { 9, 7, 5, 4 }, -1);
	}

	@Test
	public void sixStockPrices() {
		assertStockPricesMaxProfit(new int[] { 10, 7, 5, 8, 11, 9 }, 6);
	}

	private void assertExceptionThrown(int[] prices) {
		try {
			getMaxProfit(prices);
			fail("Should throw exception when there is less than 2 stock prices.");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is(equalTo("Getting a profit requires at least 2 prices.")));
		}
	}

	private void assertStockPricesMaxProfit(int[] stockPricesYesterday, int expected) {
		assertThat(getMaxProfit(stockPricesYesterday), is(expected));
	}

	private int getMaxProfit(int[] prices) {
		ensureAtLeastTwo(prices);
		return calculateMaxProfit(prices);
	}

	private void ensureAtLeastTwo(int[] prices) {
		if (prices.length < 2) {
			throw new IllegalArgumentException("Getting a profit requires at least 2 prices.");
		}
	}

	private int calculateMaxProfit(int[] prices) {
		int minPrice = prices[0];
		int maxProfit = prices[1] - prices[0];
		for (int i = 1; i < prices.length; i++) {
			maxProfit = Math.max(maxProfit, prices[i] - minPrice);
			minPrice = Math.min(minPrice, prices[i]);
		}
		return maxProfit;
	}
}
