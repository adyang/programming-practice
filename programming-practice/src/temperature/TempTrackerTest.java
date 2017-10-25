package temperature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

public class TempTrackerTest {
	private TempTracker tracker;

	@Before
	public void setUp() throws Exception {
		tracker = new TempTracker();
	}

	@Test
	public void getMax_noTemp() throws Exception {
		assertExceptionThrownWhenCalling(TempTracker::getMax);
	}

	@Test
	public void getMax_oneTemp() throws Exception {
		assertMax(new int[] { 5 }, 5);
	}

	@Test
	public void getMax_twoTemps() throws Exception {
		assertMax(new int[] { 6, 5 }, 6);
	}

	@Test
	public void getMax_multipleTemps() throws Exception {
		assertMax(new int[] { 6, 1, 0, 7, 5 }, 7);
	}

	@Test
	public void getMin_noTemp() throws Exception {
		assertExceptionThrownWhenCalling(TempTracker::getMin);
	}

	@Test
	public void getMin_oneTemp() throws Exception {
		assertMin(new int[] { 5 }, 5);
	}

	@Test
	public void getMin_twoTemps() throws Exception {
		assertMin(new int[] { 4, 5 }, 4);
	}

	@Test
	public void getMin_multipleTemps() throws Exception {
		assertMin(new int[] { 11, 4, 5, 0, 9, 110 }, 0);
	}

	@Test
	public void getMean_noTemp() throws Exception {
		assertExceptionThrownWhenCalling(TempTracker::getMean);
	}

	@Test
	public void getMean_oneTemp() throws Exception {
		assertMean(new int[] { 5 }, 5);
	}

	@Test
	public void getMean_twoTemps() throws Exception {
		assertMean(new int[] { 5, 6 }, 5.5);
	}

	@Test
	public void getMean_multipleTemps() throws Exception {
		assertMean(new int[] { 5, 0, 6, 2, 20 }, 6.6);
	}

	@Test
	public void getMode_noTemp() throws Exception {
		assertExceptionThrownWhenCalling(TempTracker::getMode);
	}

	@Test
	public void getMode_oneTemp() throws Exception {
		assertMode(new int[] { 5 }, 5);
	}

	@Test
	public void getMode_multipleTempsOneMode() throws Exception {
		assertMode(new int[] { 1, 3, 6, 3, 1, 3 }, 3);
	}

	private void assertExceptionThrownWhenCalling(Consumer<TempTracker> c) {
		try {
			c.accept(tracker);
			fail("Should throw exception when no temp inserted.");
		} catch (IllegalStateException e) {
			assertEquals("No temperature available to compute results.", e.getMessage());
		}
	}

	private void assertMax(int[] temps, int expected) {
		insertAll(temps);
		assertEquals(expected, tracker.getMax());
	}

	private void assertMin(int[] temps, int expected) {
		insertAll(temps);
		assertEquals(expected, tracker.getMin());
	}

	private void assertMean(int[] temps, double expected) {
		insertAll(temps);
		assertEquals(expected, tracker.getMean(), 0.0001);
	}

	private void assertMode(int[] temps, int expected) {
		insertAll(temps);
		assertEquals(expected, tracker.getMode());
	}

	private void insertAll(int[] temps) {
		Arrays.stream(temps).forEach(t -> tracker.insert(t));
	}
}
