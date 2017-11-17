package string;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class StringPermutationsTest {
	@Test
	public void givenEmptyString_ShouldReturnEmptySet() {
		assertPermute("", "");
	}

	@Test
	public void givenSingleCharString_ShouldPermuteOne() {
		assertPermute("a", "a");
		assertPermute("b", "b");
	}

	@Test
	public void givenStringWithTwoSimilarChars_ShouldPermuteOne() {
		assertPermute("aa", "aa");
	}

	@Test
	public void givenStringWithTwoDiffChars_ShouldPermuteTwo() {
		assertPermute("ab", "ab", "ba");
		assertPermute("ba", "ab", "ba");
	}

	@Test
	public void givenStringWithThreeDiffChars_ShouldPermuteSix() {
		assertPermute("abc", "abc", "acb", "bac", "bca", "cab", "cba");
	}

	@Test
	public void givenStringWithTwoOutOfThreeSimilarChars_ShouldPermuteThree() {
		assertPermute("abb", "abb", "bab", "bba");
	}

	@Test
	public void givenStringWithFourDiffChars_ShouldPermuteThree() {
		assertPermute("abcd", "abcd", "abdc", "acbd", "acdb", "adbc", "adcb", "bacd", "badc", "bcad", "bcda", "bdac",
				"bdca", "cabd", "cadb", "cbad", "cbda", "cdab", "cdba", "dabc", "dacb", "dbac", "dbca", "dcab", "dcba");
	}

	@Test
	public void givenStringWithAllSameChars_ShouldPermuteOne() {
		assertPermute("aaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaa");
	}

	private void assertPermute(String string, String... expectedPermutations) {
		assertThat(permute(string), is(equalTo(Arrays.stream(expectedPermutations).collect(Collectors.toSet()))));
	}

	private Set<String> permute(String string) {
		if (string.length() <= 1)
			return Collections.singleton(string);
		else
			return doPermute(string);
	}

	private Set<String> doPermute(String string) {
		String stringWithoutFirstChar = string.substring(1);
		Set<String> permutationsWithoutFirstChar = permute(stringWithoutFirstChar);
		return generatePermutations(string.charAt(0), stringWithoutFirstChar, permutationsWithoutFirstChar);
	}

	private Set<String> generatePermutations(char firstChar, String stringWithoutFirstChar,
			Set<String> permutationsWithoutFirstChar) {
		Set<String> permutedSet = new HashSet<>();
		for (String permStr : permutationsWithoutFirstChar) {
			for (int position = 0; position <= stringWithoutFirstChar.length(); position++) {
				permutedSet.add(stringWithCharAddedToPosition(permStr, firstChar, position));
			}
		}
		return permutedSet;
	}

	private String stringWithCharAddedToPosition(String perm, char character, int position) {
		return perm.substring(0, position) + character + perm.substring(position);
	}
}
