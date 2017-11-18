package string;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class WordDataBuilderTemplate {
	public Map<String, Integer> build(String inputStr) {
		List<String> words = splitWords(inputStr);
		return generateWordData(words);
	}

	protected abstract List<String> splitWords(String inputStr);

	private Map<String, Integer> generateWordData(List<String> words) {
		Map<String, Integer> wordDataMap = new HashMap<>();
		for (String word : words) {
			addWord(word, wordDataMap);
		}
		return wordDataMap;
	}

	protected abstract void addWord(String word, Map<String, Integer> wordDataMap);
}