package string;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IterativeWordSplitAndCountCapitaliseIfOnlyWordDataBuilder extends WordDataBuilderTemplate {
	@Override
	protected List<String> splitWords(String inputStr) {
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

	@Override
	protected void addWord(String word, Map<String, Integer> wordDataMap) {
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