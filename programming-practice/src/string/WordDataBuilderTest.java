package string;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class WordDataBuilderTest {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void emptyString() {
		String inputStr = "";
		Map<String, Integer> wordData = buildWordData(inputStr);
		assertEquals(0, wordData.size());
	}

	@Test
	public void singleWord() throws Exception {
		Map<String, Integer> wordData = buildWordData("word");
		assertEquals(1, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("word"));
	}

	@Test
	public void twoWords() throws Exception {
		Map<String, Integer> wordData = buildWordData("wordone wordtwo");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void multipleOccurrenceOfWords() throws Exception {
		Map<String, Integer> wordData = buildWordData("wordone wordtwo wordone");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(2), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withPeriod() throws Exception {
		Map<String, Integer> wordData = buildWordData("wordone wordtwo.");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withHyphen() throws Exception {
		Map<String, Integer> wordData = buildWordData("wordone hyphen-word");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("hyphen-word"));
	}

	@Test
	public void withComma() throws Exception {
		Map<String, Integer> wordData = buildWordData("wordone, wordtwo");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withApostrophe() throws Exception {
		Map<String, Integer> wordData = buildWordData("shouldn't be");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("shouldn't"));
		assertEquals(Integer.valueOf(1), wordData.get("be"));
	}

	@Test
	public void withEmDash() throws Exception {
		Map<String, Integer> wordData = buildWordData("wordone—wordtwo—");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withEllipses() throws Exception {
		Map<String, Integer> wordData = buildWordData("wordone... wordtwo...");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withMultipleTabsAndSpaces() throws Exception {
		Map<String, Integer> wordData = buildWordData("wordone  \t  \twordtwo\t\t ");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withQuotes() throws Exception {
		Map<String, Integer> wordData = buildWordData("\"wordone \'wordtwo\'\"");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withCapitaliseWords_singleCountCapitaliseWords() throws Exception {
		Map<String, Integer> wordData = buildWordData("Wordone wordtwo Wordthree");
		assertEquals(3, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("Wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
		assertEquals(Integer.valueOf(1), wordData.get("Wordthree"));
	}

	@Test
	public void withCapitaliseWords_mixOfLowerAndCapitaliseWords() throws Exception {
		Map<String, Integer> wordData = buildWordData("Wordone wordone Wordone");
		assertEquals(1, wordData.size());
		assertEquals(Integer.valueOf(3), wordData.get("wordone"));
	}

	@Test
	public void fullSentence() throws Exception {
		Map<String, Integer> wordData = buildWordData(
				"We came, we saw, we conquered...then we ate Bill's (Mille-Feuille) cake.");
		assertEquals(9, wordData.size());
		assertEquals(Integer.valueOf(4), wordData.get("we"));
		assertEquals(Integer.valueOf(1), wordData.get("came"));
		assertEquals(Integer.valueOf(1), wordData.get("saw"));
		assertEquals(Integer.valueOf(1), wordData.get("conquered"));
		assertEquals(Integer.valueOf(1), wordData.get("then"));
		assertEquals(Integer.valueOf(1), wordData.get("ate"));
		assertEquals(Integer.valueOf(1), wordData.get("Bill's"));
		assertEquals(Integer.valueOf(1), wordData.get("Mille-Feuille"));
		assertEquals(Integer.valueOf(1), wordData.get("cake"));
	}

	@Test
	public void oddSentence() throws Exception {
		Map<String, Integer> wordData = buildWordData("New tourists in New York often wait in long lines for cronuts.");
		assertEquals(10, wordData.size());
		assertEquals(Integer.valueOf(2), wordData.get("New"));
		assertEquals(Integer.valueOf(1), wordData.get("tourists"));
		assertEquals(Integer.valueOf(2), wordData.get("in"));
		assertEquals(Integer.valueOf(1), wordData.get("York"));
		assertEquals(Integer.valueOf(1), wordData.get("often"));
		assertEquals(Integer.valueOf(1), wordData.get("wait"));
		assertEquals(Integer.valueOf(1), wordData.get("long"));
		assertEquals(Integer.valueOf(1), wordData.get("lines"));
		assertEquals(Integer.valueOf(1), wordData.get("for"));
		assertEquals(Integer.valueOf(1), wordData.get("cronuts"));
	}

	private Map<String, Integer> buildWordData(String inputStr) {
		List<String> words = splitWords(inputStr);
		return generateWordData(words);
	}

	private List<String> splitWords(String inputStr) {
		List<String> words = new ArrayList<>();
		int currWordStart = 0;
		int currWordLength = 0;
		for (int i = 0; i < inputStr.length(); i++) {
			if (Character.isLetter(inputStr.charAt(i)) || isHyphenSurroundedWithWords(inputStr, i)
					|| isApostropheSurroundedWithWords(inputStr, i)) {
				currWordLength++;
			} else {
				if (currWordLength > 0) {
					words.add(inputStr.substring(currWordStart, currWordStart + currWordLength));
				}
				currWordStart = i + 1;
				currWordLength = 0;
			}
		}

		if (currWordLength > 0) {
			words.add(inputStr.substring(currWordStart, currWordStart + currWordLength));
		}

		return words;
	}

	private boolean isApostropheSurroundedWithWords(String inputStr, int charPos) {
		return inputStr.charAt(charPos) == '\'' && isSurroundedWithWords(inputStr, charPos);
	}

	private boolean isHyphenSurroundedWithWords(String inputStr, int charPos) {
		return inputStr.charAt(charPos) == '-' && isSurroundedWithWords(inputStr, charPos);
	}

	private boolean isSurroundedWithWords(String inputStr, int charPos) {
		return charPos > 0 && charPos < inputStr.length() - 1 && Character.isLetter(inputStr.charAt(charPos - 1))
				&& Character.isLetter(inputStr.charAt(charPos + 1));
	}

	private Map<String, Integer> generateWordData(List<String> words) {
		Map<String, Integer> wordDataMap = new HashMap<>();
		for (String word : words) {
			addWord(word, wordDataMap);
		}
		return wordDataMap;
	}

	private void addWord(String word, Map<String, Integer> wordDataMap) {
		if (wordDataMap.containsKey(word)) {
			wordDataMap.put(word, wordDataMap.get(word) + 1);
		} else if (isLowerCaseButMapHasCapitalise(word, wordDataMap)) {
			int capitaliseCount = wordDataMap.get(capitalise(word));
			wordDataMap.remove(capitalise(word));
			wordDataMap.put(word, capitaliseCount + 1);
		} else if (isCapitaliseButMapHasLowerCase(word, wordDataMap)) {
			wordDataMap.put(word.toLowerCase(), wordDataMap.get(word.toLowerCase()) + 1);
		} else {
			wordDataMap.put(word, 1);
		}
	}

	private boolean isLowerCaseButMapHasCapitalise(String word, Map<String, Integer> wordDataMap) {
		return wordDataMap.containsKey(capitalise(word));
	}

	private boolean isCapitaliseButMapHasLowerCase(String word, Map<String, Integer> wordDataMap) {
		return wordDataMap.containsKey(word.toLowerCase());
	}

	private String capitalise(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}
}
