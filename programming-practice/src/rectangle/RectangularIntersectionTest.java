package rectangle;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class RectangularIntersectionTest {
	@Test
	public void widthNotPositive() {
		assertExceptionThrownWhenWidthNotPositive(new Rectangle(0, 0, 0, 1), new Rectangle(0, 0, 1, 1));
		assertExceptionThrownWhenWidthNotPositive(new Rectangle(0, 0, 1, 1), new Rectangle(0, 0, 0, 1));
	}

	private void assertExceptionThrownWhenWidthNotPositive(Rectangle rect1, Rectangle rect2) {
		assertExceptionThrown("Should throw exception when width <= 0.", rect1, rect2,
				"Width of Rectangle should be > 0.");
	}

	@Test
	public void heightNotPositive() {
		assertExceptionThrownWhenHeightNotPositive(new Rectangle(0, 0, 1, 0), new Rectangle(0, 0, 1, 1));
		assertExceptionThrownWhenHeightNotPositive(new Rectangle(0, 0, 1, 1), new Rectangle(0, 0, 1, 0));
	}

	private void assertExceptionThrownWhenHeightNotPositive(Rectangle rect1, Rectangle rect2) {
		assertExceptionThrown("Should throw exception when height <= 0.", rect1, rect2,
				"Height of Rectangle should be > 0.");
	}

	private void assertExceptionThrown(String failureMsg, Rectangle rect1, Rectangle rect2, String exceptionMsg) {
		try {
			rectangularIntersection(rect1, rect2);
			fail(failureMsg);
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is(equalTo(exceptionMsg)));
		}
	}

	@Test
	public void noIntersection() throws Exception {
		assertIntersection(new Rectangle(0, 0, 1, 1), new Rectangle(2, 2, 1, 1), null);
		assertIntersection(new Rectangle(2, 2, 1, 1), new Rectangle(0, 0, 1, 1), null);
		assertIntersection(new Rectangle(0, 0, 1, 1), new Rectangle(1, 1, 1, 1), null);
		assertIntersection(new Rectangle(1, 1, 1, 1), new Rectangle(0, 0, 1, 1), null);
		assertIntersection(new Rectangle(-1, -1, 1, 1), new Rectangle(1, 1, 1, 1), null);
		assertIntersection(new Rectangle(1, 1, 1, 1), new Rectangle(-1, -1, 1, 1), null);
		assertIntersection(new Rectangle(-1, 1, 3, 3), new Rectangle(0, 0, 1, 1), null);
		assertIntersection(new Rectangle(1, -1, 3, 3), new Rectangle(0, 0, 1, 1), null);
	}

	@Test
	public void samePositionAndDimensions() throws Exception {
		assertIntersection(new Rectangle(0, 0, 1, 1), new Rectangle(0, 0, 1, 1), new Rectangle(0, 0, 1, 1));
		assertIntersection(new Rectangle(0, 0, 2, 8), new Rectangle(0, 0, 2, 8), new Rectangle(0, 0, 2, 8));
		assertIntersection(new Rectangle(0, 0, 102, 5), new Rectangle(0, 0, 102, 5), new Rectangle(0, 0, 102, 5));
		assertIntersection(new Rectangle(3, 4, 102, 5), new Rectangle(3, 4, 102, 5), new Rectangle(3, 4, 102, 5));
	}

	@Test
	public void samePositionSameWidth_containedWithin() throws Exception {
		assertIntersection(new Rectangle(0, 0, 1, 2), new Rectangle(0, 0, 1, 1), new Rectangle(0, 0, 1, 1));
		assertIntersection(new Rectangle(0, 0, 1, 1), new Rectangle(0, 0, 1, 2), new Rectangle(0, 0, 1, 1));
		assertIntersection(new Rectangle(2, 7, 3, 8), new Rectangle(2, 7, 3, 2), new Rectangle(2, 7, 3, 2));
		assertIntersection(new Rectangle(2, 7, 3, 2), new Rectangle(2, 7, 3, 8), new Rectangle(2, 7, 3, 2));
	}

	@Test
	public void samePositionSameHeight_containedWithin() throws Exception {
		assertIntersection(new Rectangle(0, 0, 2, 1), new Rectangle(0, 0, 1, 1), new Rectangle(0, 0, 1, 1));
		assertIntersection(new Rectangle(0, 0, 1, 1), new Rectangle(0, 0, 2, 1), new Rectangle(0, 0, 1, 1));
		assertIntersection(new Rectangle(0, 0, 8, 3), new Rectangle(0, 0, 2, 3), new Rectangle(0, 0, 2, 3));
		assertIntersection(new Rectangle(0, 0, 2, 3), new Rectangle(0, 0, 8, 3), new Rectangle(0, 0, 2, 3));
	}

	@Test
	public void samePositionDifferentWidthAndHeight_containedWithin() throws Exception {
		assertIntersection(new Rectangle(0, 0, 3, 8), new Rectangle(0, 0, 2, 7), new Rectangle(0, 0, 2, 7));
		assertIntersection(new Rectangle(0, 0, 2, 7), new Rectangle(0, 0, 3, 8), new Rectangle(0, 0, 2, 7));
		assertIntersection(new Rectangle(0, 0, 3, 8), new Rectangle(0, 0, 2, 8), new Rectangle(0, 0, 2, 8));
	}

	@Test
	public void differentPositionAndWidthAndHeight_containedWithin() throws Exception {
		assertIntersection(new Rectangle(0, 0, 3, 3), new Rectangle(1, 1, 2, 2), new Rectangle(1, 1, 2, 2));
		assertIntersection(new Rectangle(1, 1, 2, 2), new Rectangle(0, 0, 3, 3), new Rectangle(1, 1, 2, 2));
	}

	@Test
	public void differentPosition_overlap() throws Exception {
		assertIntersection(new Rectangle(0, 0, 2, 1), new Rectangle(1, 0, 1, 2), new Rectangle(1, 0, 1, 1));
		assertIntersection(new Rectangle(1, 0, 1, 2), new Rectangle(0, 0, 2, 1), new Rectangle(1, 0, 1, 1));
		assertIntersection(new Rectangle(0, 0, 3, 1), new Rectangle(1, -1, 1, 3), new Rectangle(1, 0, 1, 1));
		assertIntersection(new Rectangle(1, -1, 1, 3), new Rectangle(0, 0, 3, 1), new Rectangle(1, 0, 1, 1));
		assertIntersection(new Rectangle(1, -1, 1, 3), new Rectangle(0, 0, 3, 1), new Rectangle(1, 0, 1, 1));
		assertIntersection(new Rectangle(-2, -2, 3, 3), new Rectangle(-1, -1, 3, 3), new Rectangle(-1, -1, 2, 2));
		assertIntersection(new Rectangle(-2, 0, 3, 1), new Rectangle(-1, -1, 3, 3), new Rectangle(-1, 0, 2, 1));
	}

	private void assertIntersection(Rectangle rect1, Rectangle rect2, Rectangle intersection) {
		assertThat(rectangularIntersection(rect1, rect2), is(equalTo(intersection)));
	}

	private Rectangle rectangularIntersection(Rectangle rect1, Rectangle rect2) {
		validateRectangles(rect1, rect2);
		return calculateRectangularIntersection(rect1, rect2);
	}

	private void validateRectangles(Rectangle rect1, Rectangle rect2) {
		ensureWidthIsPositive(rect1, rect2);
		ensureHeightIsPositive(rect1, rect2);
	}

	private void ensureWidthIsPositive(Rectangle rect1, Rectangle rect2) {
		if (rect1.width <= 0 || rect2.width <= 0) {
			throw new IllegalArgumentException("Width of Rectangle should be > 0.");
		}
	}

	private void ensureHeightIsPositive(Rectangle rect1, Rectangle rect2) {
		if (rect1.height <= 0 || rect2.height <= 0) {
			throw new IllegalArgumentException("Height of Rectangle should be > 0.");
		}
	}

	private Rectangle calculateRectangularIntersection(Rectangle rect1, Rectangle rect2) {
		RangeOverlap xRangeOverlap = findRangeOverlap(rect1.leftX, rect1.width, rect2.leftX, rect2.width);
		RangeOverlap yRangeOverlap = findRangeOverlap(rect1.bottomY, rect1.height, rect2.bottomY, rect2.height);

		if (hasOverlapForBoth(xRangeOverlap, yRangeOverlap)) {
			return new Rectangle(xRangeOverlap.point, yRangeOverlap.point, xRangeOverlap.length, yRangeOverlap.length);
		} else {
			return null;
		}
	}

	private boolean hasOverlapForBoth(RangeOverlap xRangeOverlap, RangeOverlap yRangeOverlap) {
		return xRangeOverlap != null && yRangeOverlap != null;
	}

	private RangeOverlap findRangeOverlap(int point1, int length1, int point2, int length2) {
		int highestStartPoint = Math.max(point1, point2);
		int lowestEndPoint = Math.min(point1 + length1, point2 + length2);

		if (hasOverlap(highestStartPoint, lowestEndPoint)) {
			return new RangeOverlap(highestStartPoint, lowestEndPoint - highestStartPoint);
		} else {
			return null;
		}
	}

	private boolean hasOverlap(int startPoint, int endPoint) {
		return endPoint > startPoint;
	}

	private static class RangeOverlap {
		public int point;
		public int length;

		public RangeOverlap(int point, int length) {
			this.point = point;
			this.length = length;
		}
	}

	private static class Rectangle {
		public int leftX;
		public int bottomY;
		public int width;
		public int height;

		public Rectangle(int leftX, int bottomY, int width, int height) {
			this.leftX = leftX;
			this.bottomY = bottomY;
			this.width = width;
			this.height = height;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + bottomY;
			result = prime * result + height;
			result = prime * result + leftX;
			result = prime * result + width;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Rectangle other = (Rectangle) obj;
			if (bottomY != other.bottomY)
				return false;
			if (height != other.height)
				return false;
			if (leftX != other.leftX)
				return false;
			if (width != other.width)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return String.format("Rectangle [leftX=%s, bottomY=%s, width=%s, height=%s]", leftX, bottomY, width,
					height);
		}
	}
}
