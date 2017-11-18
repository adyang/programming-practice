package string;

import java.util.ArrayList;
import java.util.List;

public class IterativeWordSplitter implements WordSplitter {
	@Override
	public List<String> splitWords(String inputStr) {
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

	boolean isHyphenSurroundedWithWords(String inputStr, int charPos) {
		return inputStr.charAt(charPos) == '-' && isSurroundedWithWords(inputStr, charPos);
	}

	boolean isApostropheSurroundedWithWords(String inputStr, int charPos) {
		return inputStr.charAt(charPos) == '\'' && isSurroundedWithWords(inputStr, charPos);
	}

	boolean isSurroundedWithWords(String inputStr, int charPos) {
		return charPos > 0 && charPos < inputStr.length() - 1 && Character.isLetter(inputStr.charAt(charPos - 1))
				&& Character.isLetter(inputStr.charAt(charPos + 1));
	}
}