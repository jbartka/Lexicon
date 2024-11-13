import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;

/**
 *
 * A class that is responsible for representing and dealing with the tree
 * (trie) of a Lexicon. This class has methods that allows the access of and
 * deals with the words within the tree, which are specified in the Lexicon
 * interface class. This class provides the methods of addition of the word
 * into a trie (both manually by user and from the file), removal of the
 * words from a trie, calculation of the active number of the words in a
 * trie, checks whether the words and prefixes exist within a trie, iterates
 * through the words in a trie, offers suggestions when trying to find a word
 * in the trie, and matching the words within a trie to the provided regex
 * pattern.
 *
 * @author Justas Bardauskas
 * @version 1.0 Build 2023.04.23
 */
public class LexiconTrie implements Lexicon {
    // A variable that stores the root node of the word in a trie
    private LexiconNode root;
    // A variable that keeps track of how many words are in a trie
    private int wordCount;

    /**
     * LexiconTrie custom constructor. This constructor initializes the root
     * as a node with an empty string as a default value and word counter
     * variable to zero as a default value
     */
    public LexiconTrie() {
        this.root = new LexiconNode(' ');
        this.wordCount = 0;
    }

    /**
     * Adds the specified word to the Lexicon trie. Returns whether
     * the Lexicon trie was modified by adding the word.
     *
     * @param word the lowercase word to add to the Lexicon trie.
     * @return true if the word was added and false if the word was already part
     * of the Lexicon trie.
     */
    @Override
    public boolean addWord(String word) {
        // Converting the word to lowercase first
        word = word.toLowerCase();
        if (word.isEmpty()) {
            return false;
        }
        // We start adding a word to a trie from a root
        LexiconNode currentNode = root;
        for (char letter : word.toCharArray()) {
            LexiconNode currentLetter = new LexiconNode(letter);
            // If a child with a letter does not exist in a trie - add a
            // child with a letter into a trie
            if (currentNode.getChild(currentLetter) == null) {
                currentNode.addChild(currentLetter);
            }
            // We move the pointer to a children with one letter to the right
            currentNode = currentNode.getChild(currentLetter);
        }
        currentNode.setWordEnd(true);
        wordCount++;
        return true;
    }

    /**
     * Reads the words contained in the specified file and adds them to the
     * Lexicon trie
     *
     * @param filename the name of the file to read
     * @return the number of new words added, or -1 if the file could not be
     * read.
     */
    @Override
    public int addWordsFromFile(String filename) {
        Scanner scan;
        int countWords = 0;
        try {
            scan = new Scanner(new File("words/" + filename));
        } catch (Exception e) {
            // Failed to read file - good idea to print an error and exit/return
            System.out.println("Failed to read file.");
            return -1;
        }
        // While there’s more of the file to read
        while (scan.hasNext()) {
            // Read the next line
            String word = scan.nextLine().trim().toLowerCase();
            addWord(word);
            countWords++;
        }
        // Done reading the file, so we close the Scanner
        scan.close();
        return countWords;
    }

    /**
     * Attempts to remove the specified word from the Lexicon trie. If the word
     * appears in the Lexicon trie, it is removed and true is returned. If
     * the word does not appear in the Lexicon trie, the Lexicon trie is
     * unchanged and false is returned
     *
     * @param word the lowercase word to remove from the Lexicon trie
     * @return whether the word was removed
     */
    @Override
    public boolean removeWord(String word) {
        if (word.isEmpty()) {
            return false;
        }
        return removeWord(root, word, 0);
    }

    /**
     * A helper function tha recursively calls itself and goes through the
     * word that has to be removed. After it reaches the end of the word, the
     * pointer recursively goes back and removes the children of the nodes that
     * are not the prefixes of other words.
     *
     * @param lexiconNode a specified LexiconNode that we are standing on
     * @param word a specified word that we are looking for within a trie
     * @param index an index that will be incremented to keep track of the
     * letters of the word we have already checked
     * @return a boolean true/false value whether the word was removed
     */
    private boolean removeWord(LexiconNode lexiconNode, String word,
                               int index) {
        if (index == word.length()) {
            if (lexiconNode.isWordEnd()) {
                // Mark the node as not the end of a word in order to
                // correctly remove the word from the trie and prevent an
                // incorrect word count
                lexiconNode.setWordEnd(false);
                wordCount--;
                return true;
            } else {
                // Return false if we have not reached the end of the word yet
                return false;
            }
        }
        char letter = word.charAt(index);
        LexiconNode currentNode = lexiconNode.getChild(new LexiconNode(letter));
        if (currentNode == null) {
            // Return false if the word doesn't exist in the trie
            return false;
        }
        // Keeps going over the word until it reaches the end and eventually
        // returns true if the word is removed
        return removeWord(currentNode, word, index + 1);
    }

    /**
     * Returns the number of words contained in the Lexicon trie
     *
     * @return the integer value of the active number of word within a
     * Lexicon trie
     */
    @Override
    public int numWords() {
        return wordCount;
    }

    /**
     * Checks whether the given word exists in the Lexicon trie
     *
     * @param word the lowercase word to lookup in the Lexicon trie
     * @return whether the given word exists in the Lexicon trie
     */
    @Override
    public boolean containsWord(String word) {
        if (word.isEmpty()) {
            return true;
        }
        LexiconNode currentNode = root;
        for (char letter : word.toCharArray()) {
            LexiconNode currentLetter = new LexiconNode(letter);
            // If currentNode child does not correspond to a letter of the
            // word, the trie does not contain a word
            if (currentNode.getChild(currentLetter) == null) {
                return false;
            }
            // We move the pointer to a children with one letter to the right
            currentNode = currentNode.getChild(currentLetter);
        }
        return currentNode.isWordEnd();
    }

    /**
     * Checks whether any words in the Lex begin with the specified prefix.
     * A word is defined to be a prefix of itself, and the empty string is
     * defined to be a prefix of everything
     *
     * @param prefix the lowercase prefix to lookup in the Lexicon trie.
     * @return whether the given prefix exists in the Lexicon trie.
     */
    @Override
    public boolean containsPrefix(String prefix) {
        if (prefix.isEmpty()) {
            return true;
        }
        LexiconNode currentNode = root;
        for (char letter : prefix.toCharArray()) {
            LexiconNode currentLetter = new LexiconNode(letter);
            // If currentNode child does not correspond to a letter of the
            // prefix, the trie does not contain a word
            if (currentNode.getChild(currentLetter) == null) {
                return false;
            }
            // We move the pointer to a children with one letter to the right
            currentNode = currentNode.getChild(currentLetter);
        }
        return true;
    }

    /**
     * Returns an iterator over all words contained in the Lexicon trie. The
     * iterator should return words in the Lexicon trie in alphabetical order.
     */
    @Override
    public Iterator<String> iterator() {
        ArrayList<String> wordList = new ArrayList<>();
        return allLexiconWords(root,"", wordList).iterator();
    }

    /**
     * A helper function that recursively calls itself to go through the
     * specified prefix letter by letter, eventually returning the ArrayList
     * of Strings that match the specified prefix
     *
     * @param lexiconNode a type LexiconNode that refers to a node we are
     * currently standing on
     * @param prefix a type String that refers to a passed prefix
     * @param words a type ArrayList that refers to all words that match the
     * specified prefix
     * @return a type ArrayList that refers to all words that match the
     * specified prefix
     */
    private ArrayList<String> allLexiconWords(LexiconNode lexiconNode,
                                              String prefix,
                                              ArrayList<String> words) {

        for (LexiconNode currentChild : lexiconNode) {
            if (currentChild.isWordEnd()) {
                words.add(prefix + currentChild.getLetter());
            }
            allLexiconWords(currentChild,
                    prefix + currentChild.getLetter(), words);
        }
        return words;
    }

    /**
     * Returns a set of words in the Lexicon trie that are suggested
     * corrections for a given (possibly misspelled) word. Suggestions will
     * include all words in the Lexicon trie that are at most maxDistance
     * distance from the target word, where the distance between two words is
     * defined as the number of character positions in which the words differ
     *
     * @param target the target word to be corrected.
     * @param maxDistance the maximum word distance of suggested corrections.
     * @return a set of all suggested corrections within maxDistance of the
     * target word
     */
    @Override
    public Set<String> suggestCorrections(String target, int maxDistance) {
        Set<String> suggestions = new HashSet<>();
        suggestCorrections(target, maxDistance, root, "", suggestions, 0);
        return suggestions;
    }

    /**
     * A helper function that recursively calls itself to suggest the possible
     * corrections for a target word depending on the provided possible
     * maximum distance
     *
     * @param target the target word to be corrected
     * @param maxDistance the maximum word distance of suggested corrections
     * @param currentNode the current node that we are standing on
     * @param word a word that we are populating as the suggestion
     * @param suggestions a set where we add the suggested words
     * @param index an index that will be incremented to keep track of the
     * letters of the word we have already checked
     */
    private void suggestCorrections(String target, int maxDistance,
                         LexiconNode currentNode, String word,
                         Set<String> suggestions, int index) {
        if (index == target.length()) {
            // Add the words to the set if we reach the end and the max
            // distance is more than or equal to zero
            if (maxDistance >= 0 && currentNode.isWordEnd()) {
                suggestions.add(word);
            }
        } else {
            for (LexiconNode childNode: currentNode) {
                int newMaxDistance = maxDistance;
                // We deduct one from the max distance every the letters do
                // not match
                if (childNode.getLetter() != target.charAt(index)) {
                    newMaxDistance -= 1;
                }
                // We recursively add letters to an empty word that match the
                // target distance
                suggestCorrections(target, newMaxDistance, childNode,
                        word + childNode.getLetter(), suggestions,
                        index + 1);
            }
        }
    }

    /**
     * Returns a set of all words in the Lexicon trie that match the given
     * regular expression pattern. The regular expression pattern may contain
     * only letters and wildcard characters '*', '?', and '_'
     *
     * @param pattern the regular expression pattern to match
     * @return a set of all words in the lexicon matching the pattern
     */
    @Override
    public Set<String> matchRegex(String pattern) {
        Set<String> regexSet = new HashSet<>();
        findRegex(pattern, root, "", regexSet);
        return regexSet;
    }

    /**
     * A helper function that uses recursion to help find the words in the
     * trie that match the specified regex pattern
     *
     * @param pattern the regular expression pattern to match
     * @param currentNode the current node that we are standing on
     * @param word a word that we are populating as the suggestion
     * @param regexSet a set where we add the words that match the regex
     */
    private void findRegex(String pattern, LexiconNode currentNode, String word,
                          Set<String> regexSet) {
        if (pattern.isEmpty()) {
            if (currentNode.isWordEnd()) {
                regexSet.add(word);
            }
        } else {
            // If the pointer is a '?' or '*', we first check the case where
            // we do not add anything into an empty word
            if (pattern.charAt(0) == '?' || pattern.charAt(0) == '*') {
                findRegex(pattern.substring(1), currentNode,
                        word, regexSet);
            }
            for (LexiconNode childNode: currentNode) {
                if (childNode.getLetter() == pattern.charAt(0)) {
                    // If the letters match the pattern element exactly, we
                    // add to the word
                    findRegex(pattern.substring(1), childNode,
                            word + childNode.getLetter(),
                            regexSet);
                } else if (pattern.charAt(0) == '_') {
                    // If the letters match the pattern element exactly, we
                    // add to the word
                    findRegex(pattern.substring(1), childNode,
                            word + childNode.getLetter(),
                            regexSet);
                } else if (pattern.charAt(0) == '?') {
                    // If the letters match the pattern element exactly, we
                    // add to the word
                    findRegex(pattern.substring(1), childNode,
                            word + childNode.getLetter(),
                            regexSet);
                } else if (pattern.charAt(0) == '*') {
                    // If the letters match the pattern element exactly, we
                    // either add to the word or do not add anything to the
                    // word (have to check both cases for '*')
                    findRegex(pattern.substring(1), childNode,
                            word + childNode.getLetter(),
                            regexSet);
                    findRegex(pattern, childNode,
                            word + childNode.getLetter(),
                            regexSet);
                }
            }
        }
    }
}
