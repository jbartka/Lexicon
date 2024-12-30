# Lexicon Trie Project

This repository contains a Java-based implementation of a lexicon using a trie structure. It provides functionality to add, remove, search, and suggest words, with support for regex matching and prefix-based lookups.

## Features
- **Lexicon**: An interface defining the structure and methods required for a lexicon implementation.
- **LexiconNode**: Represents a single node in the trie, with methods for managing child nodes and word endings.
- **LexiconTrie**: Implements the `Lexicon` interface using a trie structure for efficient storage and retrieval of words.
- **TestLexicon**: A command-line interface for interacting with the lexicon and testing its functionalities.

## Files Overview
1. **Lexicon.java**:
   - Defines the `Lexicon` interface, which includes methods for:
     - Adding and removing words.
     - Checking for word and prefix existence.
     - Suggesting corrections and matching words using regex.
   - Provides a standard for implementing lexicon functionalities.

2. **LexiconNode.java**:
   - Represents a node in the trie.
   - Manages child nodes, checks word endings, and iterates over children.

3. **LexiconTrie.java**:
   - Implements the `Lexicon` interface using a trie structure.
   - Efficiently handles word addition, removal, and lookup.
   - Supports features like regex matching and word suggestions.

4. **TestLexicon.java**:
   - Provides a command-line interface for testing the `LexiconTrie` implementation.
   - Includes commands for adding/removing words, checking prefixes, and more.

## How to Run
1. **Prerequisites**:
   - Java Development Kit (JDK) installed on your system.
   - A text editor or IDE for editing and running Java files.

2. **Compile**:
   Navigate to the directory containing the files and compile the Java files:
   ```bash
   javac *.java
   ```

3. **Run**:
   Execute the test class:
   ```bash
   java TestLexicon
   ```

4. **Commands**:
   The command-line interface accepts the following commands:
   - **add** `<word>`: Adds a word to the lexicon.
   - **remove** `<word>`: Removes a word from the lexicon.
   - **contains** `<word>`: Checks if a word or prefix exists in the lexicon.
   - **file** `<filename>`: Adds words from a file to the lexicon.
   - **print**: Prints all words in the lexicon.
   - **suggest** `<target>` `<distance>`: Suggests words close to the target within a distance.
   - **match** `<regex>`: Matches words in the lexicon to a regex pattern.
   - **quit**: Exits the program.

## Sample Input Format
For file-based operations, input files should contain one word per line. Example:
```
apple
banana
cherry
date
elderberry
```

## Sample Output
### Adding and Checking Words
```
Enter command: add apple
"apple" added to lexicon.

Enter command: contains app
Prefix "app" IS contained in lexicon.
Word "app" is NOT contained in lexicon.
```

### Suggestions
```
Enter command: suggest apple 1
Words that are within distance 1 of "apple"
--------------------------------------------
[ample, apply]
```

### Regex Matching
```
Enter command: match a*e
Words that match pattern a*e
-----------------------------------
[apple, ample]
```

## Author
This project was created by **Justas Bardauskas**.
