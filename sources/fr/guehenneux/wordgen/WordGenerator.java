package fr.guehenneux.wordgen;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Based on sciencetonnante video
 * https://www.youtube.com/watch?v=YsR7r2378j0
 * 
 * @author Jonathan Guéhenneux
 */
public class WordGenerator {

	private static final char NULL_CHARACTER = 0;
	private static final Random RANDOM = new Random();

	/**
	 * @param arguments
	 * @throws IOException
	 */
	public static void main(String[] arguments) throws IOException {

		String dictionaryPath = arguments[0];
		int windowSize = Integer.parseInt(arguments[1]);
		int wordLength = Integer.parseInt(arguments[2]);
		int wordCount = Integer.parseInt(arguments[3]);

		WordGenerator wordGenerator = new WordGenerator(dictionaryPath, windowSize);

		int generatedWordCount = 0;

		while (generatedWordCount < wordCount) {

			System.out.println(wordGenerator.generateRandomWord(wordLength));
			generatedWordCount++;
		}
	}

	private int windowSize;

	/*-
	 * keys: character sequences
	 * values: characters found after each distinct sequence
	 */
	private Map<String, List<Character>> followingCharacters;

	/**
	 * @param dictionaryPath
	 *            the path to a dictionary
	 * @param windowSize
	 *            the window size
	 * @throws IOException
	 *             exception while reading the dictionary
	 */
	public WordGenerator(String dictionaryPath, int windowSize) throws IOException {

		this.windowSize = windowSize;

		followingCharacters = new HashMap<>();

		try (Stream<String> words = Files.lines(Paths.get(dictionaryPath), Charset.defaultCharset())) {
			words.forEach(word -> computeOccurrences(word));
		}
	}

	/**
	 * @return an initial sequence
	 */
	private StringBuilder buildInitialSequence() {

		StringBuilder initialSequence = new StringBuilder();

		while (initialSequence.length() < windowSize) {
			initialSequence.append(NULL_CHARACTER);
		}

		return initialSequence;
	}

	/**
	 * @param word
	 *            the word to add
	 */
	private void computeOccurrences(String word) {

		StringBuilder sequence = buildInitialSequence();

		word.chars().forEach(character -> {

			addOccurence(sequence.toString(), (char) character);
			sequence.deleteCharAt(0).append((char) character);
		});

		addOccurence(sequence.toString(), NULL_CHARACTER);
	}

	/**
	 * Add the character occurrence.
	 * 
	 * @param sequence
	 *            a sequence
	 * @param character
	 *            a character following the specified sequence
	 */
	private void addOccurence(String sequence, char character) {

		List<Character> sequenceFollowingCharacters = followingCharacters.get(sequence);

		if (sequenceFollowingCharacters == null) {

			sequenceFollowingCharacters = new ArrayList<>();
			followingCharacters.put(sequence, sequenceFollowingCharacters);
		}

		sequenceFollowingCharacters.add(character);
	}

	/**
	 * @param sequence
	 *            a character sequence
	 * @return a random character that follows the specified sequence
	 */
	public char getRandomCharacter(String sequence) {

		List<Character> sequenceFollowingCharacters = followingCharacters.get(sequence);

		char followingCharacter;

		if (sequenceFollowingCharacters.isEmpty()) {

			followingCharacter = NULL_CHARACTER;

		} else {

			int followingCharacterIndex = RANDOM.nextInt(sequenceFollowingCharacters.size());
			followingCharacter = sequenceFollowingCharacters.get(followingCharacterIndex);
		}

		return followingCharacter;
	}

	/**
	 * @return a random word
	 */
	public String generateRandomWord() {

		StringBuilder sequence = buildInitialSequence();

		char character;

		StringBuilder randomWordBuilder = new StringBuilder();

		while ((character = getRandomCharacter(sequence.toString())) != NULL_CHARACTER) {

			randomWordBuilder.append(character);
			sequence.deleteCharAt(0).append(character);
		}

		return randomWordBuilder.toString();
	}

	/**
	 * @param length
	 *            the length of the word to generate
	 * @return a random word with the specified length
	 */
	public String generateRandomWord(int length) {

		String randomWord;

		do {

			randomWord = generateRandomWord();

		} while (randomWord.length() != length);

		return randomWord;
	}
}