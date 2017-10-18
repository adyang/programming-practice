package meetings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class MergeRangesTest {
	@Test
	public void emptyArray() {
		assertMergedMeetings(new Meeting[] {}, new Meeting[] {});
	}

	@Test
	public void originalMeetingShouldNotChange() {
		Meeting[] original = arrayOf(meeting(3, 7), meeting(1, 4));
		Meeting[] expectedUnchanged = arrayOf(meeting(3, 7), meeting(1, 4));
		mergeRanges(original);
		assertThat(original, is(equalTo(expectedUnchanged)));
	}

	@Test
	public void singleMeeting() {
		assertMergedMeetings(arrayOf(meeting(1, 2)), arrayOf(meeting(1, 2)));
		assertMergedMeetings(arrayOf(meeting(3, 4)), arrayOf(meeting(3, 4)));
	}

	@Test
	public void twoMeetings_nonOverlap() {
		assertMergedMeetings(arrayOf(meeting(1, 2), meeting(3, 4)), arrayOf(meeting(1, 2), meeting(3, 4)));
	}

	@Test
	public void twoMeetings_continuous() {
		assertMergedMeetings(arrayOf(meeting(1, 2), meeting(2, 4)), arrayOf(meeting(1, 4)));
	}

	@Test
	public void twoMeetings_oneContainedWithin() {
		assertMergedMeetings(arrayOf(meeting(1, 6), meeting(2, 4)), arrayOf(meeting(1, 6)));
		assertMergedMeetings(arrayOf(meeting(2, 4), meeting(1, 6)), arrayOf(meeting(1, 6)));
	}

	@Test
	public void twoMeetings_overlap() {
		assertMergedMeetings(arrayOf(meeting(1, 3), meeting(2, 4)), arrayOf(meeting(1, 4)));
		assertMergedMeetings(arrayOf(meeting(2, 4), meeting(1, 3)), arrayOf(meeting(1, 4)));
	}

	@Test
	public void threeMeetings_continuous() {
		assertMergedMeetings(arrayOf(meeting(1, 2), meeting(2, 4), meeting(4, 8)), arrayOf(meeting(1, 8)));
	}

	@Test
	public void threeMeetings_containedWithin() {
		assertMergedMeetings(arrayOf(meeting(1, 2), meeting(5, 7), meeting(4, 8)),
				arrayOf(meeting(1, 2), meeting(4, 8)));
	}

	@Test
	public void threeMeetings_overlap() {
		assertMergedMeetings(arrayOf(meeting(1, 2), meeting(3, 7), meeting(4, 8)),
				arrayOf(meeting(1, 2), meeting(3, 8)));
		assertMergedMeetings(arrayOf(meeting(1, 5), meeting(3, 7), meeting(4, 8)), arrayOf(meeting(1, 8)));
	}

	@Test
	public void fiveMeetings_overlap() {
		assertMergedMeetings(arrayOf(meeting(0, 1), meeting(3, 5), meeting(4, 8), meeting(10, 12), meeting(9, 10)),
				arrayOf(meeting(0, 1), meeting(3, 8), meeting(9, 12)));
	}

	private Meeting[] arrayOf(Meeting... meetings) {
		return meetings;
	}

	private Meeting meeting(int startTime, int endTime) {
		return new Meeting(startTime, endTime);
	}

	private void assertMergedMeetings(Meeting[] input, Meeting[] expected) {
		assertThat(mergeRanges(input), is(equalTo(expected)));
	}

	private Meeting[] mergeRanges(Meeting[] meetings) {
		if (isEmpty(meetings)) {
			return emptyMeetings();
		} else {
			return doMergeRanges(meetings);
		}
	}

	private boolean isEmpty(Meeting[] meetings) {
		return meetings.length == 0;
	}

	private Meeting[] emptyMeetings() {
		return new Meeting[0];
	}

	private Meeting[] doMergeRanges(Meeting[] meetings) {
		List<Meeting> sortedMeetings = generateSorted(meetings);
		return mergeRangesFor(sortedMeetings);
	}

	private List<Meeting> generateSorted(Meeting[] meetings) {
		return Arrays.stream(meetings)
				.map(m -> new Meeting(m))
				.sorted((m1, m2) -> m1.startTime - m2.startTime)
				.collect(Collectors.toList());
	}

	private Meeting[] mergeRangesFor(List<Meeting> sortedMeetings) {
		List<Meeting> mergedMeetings = initialiseWithFirstOf(sortedMeetings);
		mergeAllMeetings(sortedMeetings, mergedMeetings);
		return convertToArray(mergedMeetings);
	}

	private List<Meeting> initialiseWithFirstOf(List<Meeting> sortedMeetings) {
		List<Meeting> mergedMeetings = new ArrayList<>();
		mergedMeetings.add(firstOf(sortedMeetings));
		return mergedMeetings;
	}

	private Meeting firstOf(List<Meeting> meetings) {
		return meetings.get(0);
	}

	private void mergeAllMeetings(List<Meeting> sortedMeetings, List<Meeting> mergedMeetings) {
		for (Meeting m : sortedMeetings) {
			mergeMeeting(m, mergedMeetings);
		}
	}

	private void mergeMeeting(Meeting current, List<Meeting> mergedMeetings) {
		if (isMergeable(current, mergedMeetings)) {
			merge(current, mergedMeetings);
		} else {
			add(current, mergedMeetings);
		}
	}

	private boolean isMergeable(Meeting current, List<Meeting> mergedMeetings) {
		return current.startTime <= lastOf(mergedMeetings).endTime;
	}

	private Meeting lastOf(List<Meeting> meetings) {
		return meetings.get(meetings.size() - 1);
	}

	private void merge(Meeting current, List<Meeting> mergedMeetings) {
		lastOf(mergedMeetings).endTime = Math.max(current.endTime, lastOf(mergedMeetings).endTime);
	}

	private void add(Meeting current, List<Meeting> mergedMeetings) {
		mergedMeetings.add(current);
	}

	private Meeting[] convertToArray(List<Meeting> mergedMeetings) {
		return mergedMeetings.toArray(new Meeting[0]);
	}

	private static class Meeting {
		private int startTime;
		private int endTime;

		public Meeting(int startTime, int endTime) {
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public Meeting(Meeting meeting) {
			this.startTime = meeting.startTime;
			this.endTime = meeting.endTime;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + endTime;
			result = prime * result + startTime;
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
			Meeting other = (Meeting) obj;
			if (endTime != other.endTime)
				return false;
			if (startTime != other.startTime)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return String.format("Meeting(%s, %s)", startTime, endTime);
		}
	}
}
