package string;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordDataBuilder {
	private WordSplitter splitter;
	private WordDataAdder adder;

	public WordDataBuilder(WordSplitter splitter, WordDataAdder adder) {
		this.splitter = splitter;
		this.adder = adder;
	}

	public Map<String, Integer> build(String inputStr) {
		List<String> words = splitter.splitWords(inputStr);
		return generateWordData(words);
	}

	private Map<String, Integer> generateWordData(List<String> words) {
		Map<String, Integer> wordDataMap = new HashMap<>();
		for (String word : words) {
			adder.addWordToMap(word, wordDataMap);
		}
		return wordDataMap;
	}
}