package string;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class WordDataBuilderTest {
	private WordDataBuilderTemplate wordDataBuilder = new IterativeWordSplitAndCountCapitaliseIfOnlyWordDataBuilder();

	@Before
	public void setUp() throws Exception {
		wordDataBuilder = new IterativeWordSplitAndCountCapitaliseIfOnlyWordDataBuilder();
	}

	@Test
	public void emptyString() {
		String inputStr = "";
		Map<String, Integer> wordData = wordDataBuilder.build(inputStr);
		assertEquals(0, wordData.size());
	}

	@Test
	public void singleWord() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("word");
		assertEquals(1, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("word"));
	}

	@Test
	public void twoWords() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("wordone wordtwo");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void multipleOccurrenceOfWords() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("wordone wordtwo wordone");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(2), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withPeriod() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("wordone wordtwo.");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withHyphen() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("wordone hyphen-word");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("hyphen-word"));
	}

	@Test
	public void withComma() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("wordone, wordtwo");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withApostrophe() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("shouldn't be");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("shouldn't"));
		assertEquals(Integer.valueOf(1), wordData.get("be"));
	}

	@Test
	public void withEmDash() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("wordone—wordtwo—");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withEllipses() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("wordone... wordtwo...");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withMultipleTabsAndSpaces() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("wordone  \t  \twordtwo\t\t ");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withQuotes() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("\"wordone \'wordtwo\'\"");
		assertEquals(2, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
	}

	@Test
	public void withCapitaliseWords_singleCountCapitaliseWords() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("Wordone wordtwo Wordthree");
		assertEquals(3, wordData.size());
		assertEquals(Integer.valueOf(1), wordData.get("Wordone"));
		assertEquals(Integer.valueOf(1), wordData.get("wordtwo"));
		assertEquals(Integer.valueOf(1), wordData.get("Wordthree"));
	}

	@Test
	public void withCapitaliseWords_mixOfLowerAndCapitaliseWords() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder.build("Wordone wordone Wordone");
		assertEquals(1, wordData.size());
		assertEquals(Integer.valueOf(3), wordData.get("wordone"));
	}

	@Test
	public void fullSentence() throws Exception {
		Map<String, Integer> wordData = wordDataBuilder
				.build("We came, we saw, we conquered...then we ate Bill's (Mille-Feuille) cake.");
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
		Map<String, Integer> wordData = wordDataBuilder
				.build("New tourists in New York often wait in long lines for cronuts.");
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
}
