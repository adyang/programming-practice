package coin;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class CoinChangeTest {
	@Test
	public void zeroAmount() {
		assertNumberOfWaysToMakeCoinChange(0, new int[] { 1 }, 1);
	}

	@Test
	public void emptyDenominations() {
		assertNumberOfWaysToMakeCoinChange(10, new int[] {}, 0);
	}

	@Test
	public void originalDenominationsShouldNotBeAltered() {
		int[] originalDenoms = new int[] { 2 };
		int[] expectedUnchanged = new int[] { 2 };
		numberOfWaysToMakeCoinChange(10, originalDenoms);
		assertThat(originalDenoms, is(equalTo(expectedUnchanged)));
	}

	@Test
	public void singleDenomination_unableToMakeChange() {
		assertNumberOfWaysToMakeCoinChange(1, new int[] { 2 }, 0);
		assertNumberOfWaysToMakeCoinChange(7, new int[] { 2 }, 0);
	}

	@Test
	public void singleDenomination_oneExactMatch() {
		assertNumberOfWaysToMakeCoinChange(1, new int[] { 1 }, 1);
		assertNumberOfWaysToMakeCoinChange(2, new int[] { 2 }, 1);
	}

	@Test
	public void singleDenomination_multipleTimesToMatch() {
		assertNumberOfWaysToMakeCoinChange(2, new int[] { 1 }, 1);
		assertNumberOfWaysToMakeCoinChange(4, new int[] { 1 }, 1);
		assertNumberOfWaysToMakeCoinChange(100, new int[] { 4 }, 1);
	}

	@Test
	public void twoDenominations_unableToMakeChange() {
		assertNumberOfWaysToMakeCoinChange(5, new int[] { 2, 4 }, 0);
		assertNumberOfWaysToMakeCoinChange(5, new int[] { 4, 3 }, 0);
	}

	@Test
	public void twoDenominations_oneExactMatch() {
		assertNumberOfWaysToMakeCoinChange(5, new int[] { 4, 5 }, 1);
	}

	@Test
	public void twoDenominations_multipleTimesToMatchOneWay() {
		assertNumberOfWaysToMakeCoinChange(10, new int[] { 4, 5 }, 1);
	}

	@Test
	public void twoDenominations_multipleTimesToMatchMultipleWays() {
		assertNumberOfWaysToMakeCoinChange(6, new int[] { 2, 3 }, 2);
		assertNumberOfWaysToMakeCoinChange(4, new int[] { 1, 2 }, 3);
	}

	@Test
	public void threeDenominations_multipleTimesToMatchMultipleWays() {
		assertNumberOfWaysToMakeCoinChange(4, new int[] { 1, 2, 3 }, 4);
		assertNumberOfWaysToMakeCoinChange(5, new int[] { 5, 3, 1 }, 3);
	}

	private void assertNumberOfWaysToMakeCoinChange(int amount, int[] denominations, int expected) {
		assertThat(numberOfWaysToMakeCoinChange(amount, denominations), is(equalTo(expected)));
	}

	private int numberOfWaysToMakeCoinChange(int amount, int[] denominations) {
		if (isEmpty(denominations)) {
			return 0;
		} else {
			return numberOfWays(amount, denominations);
		}
	}

	private boolean isEmpty(int[] denominations) {
		return denominations.length == 0;
	}

	private int numberOfWays(int amount, int[] denominations) {
		int[] waysOfDoingAmount = initialiseWaysOfDoingAmount(amount);
		generateWaysOfDoingDifferentAmountsUpTillAmount(amount, denominations, waysOfDoingAmount);
		return waysOfDoingAmount[amount];
	}

	private int[] initialiseWaysOfDoingAmount(int amount) {
		int[] waysOfDoingAmount = new int[amount + 1];
		assignBaseCase(waysOfDoingAmount);
		return waysOfDoingAmount;
	}

	private void assignBaseCase(int[] waysOfDoingAmount) {
		waysOfDoingAmount[0] = 1;
	}

	private void generateWaysOfDoingDifferentAmountsUpTillAmount(int targetAmount, int[] denominations,
			int[] waysOfDoingAmount) {
		for (int denom : denominations) {
			for (int currAmount = denom; currAmount <= targetAmount; currAmount++) {
				int remainingAmount = currAmount - denom;
				waysOfDoingAmount[currAmount] += waysOfDoingAmount[remainingAmount];
			}
		}
	}
}
