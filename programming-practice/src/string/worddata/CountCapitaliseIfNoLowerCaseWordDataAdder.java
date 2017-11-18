package string.worddata;

import java.util.Map;

public class CountCapitaliseIfNoLowerCaseWordDataAdder implements WordDataAdder {
	@Override
	public void addWordToMap(String word, Map<String, Integer> wordDataMap) {
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

	boolean isLowerCaseButMapHasCapitalise(String word, Map<String, Integer> wordDataMap) {
		return wordDataMap.containsKey(capitalise(word));
	}

	boolean isCapitaliseButMapHasLowerCase(String word, Map<String, Integer> wordDataMap) {
		return wordDataMap.containsKey(word.toLowerCase());
	}

	String capitalise(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}
}