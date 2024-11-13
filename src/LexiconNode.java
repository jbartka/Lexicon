import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * A class that is responsible for representing and dealing with a single node
 * within the LexiconTrie. This class has methods that allow the access of the
 * addition of the child, removal of the child, and accessibility of the
 * child for a node. The class also has methods that help to determine
 * whether the node is the end of the word and allows us to iterate over the
 * children of the node
 *
 * @author Justas Bardauskas
 * @version 1.0 Build 2023.04.23
 */
public class LexiconNode implements Iterable<LexiconNode> {
    // A variable that stores the letter of the node
    private char letter;
    // A list of children that stem from the node
    private List<LexiconNode> childrenList;
    // A boolean value meant for checking whether the node marks an end of
    // the word
    private boolean isWordEnd;

    /**
     * LexiconNode custom constructor. This constructor takes the letter
     * character and instantiates a global letter variable. It also
     * instantiates children as an array list and sets the default value for
     * isWordEnd as false
     *
     * @param letter a char type letter of the specific node
     */
    public LexiconNode(char letter) {
        this.letter = letter;
        this.childrenList = new ArrayList<>();
        this.isWordEnd = false;
    }

    /**
     * Adds a child to the node of the tree
     *
     * @param childNode a LexiconNode type that represents the child node to
     * be added
     */
    public void addChild(LexiconNode childNode) {
        int index = 0;
        if (childrenList.isEmpty()) {
            childrenList.add(index, childNode);
        }
        for (LexiconNode child : childrenList) {
            // Sorting letters in alphabetical order using the two if's below
            if (child.letter == childNode.letter) {
                return;
            }
            if (child.letter > childNode.letter) {
                index += 1;
            }
        }
        childrenList.add(index, childNode);
    }

    /**
     * Gets a child from the node of the tree
     *
     * @param childNode a LexiconNode type that represents the child node to
     * fetched
     */
    public LexiconNode getChild(LexiconNode childNode) {
        for (LexiconNode child : childrenList) {
            if (child.letter == childNode.letter) {
                return child;
            }
        }
        return null;
    }

    /**
     * Checks if the node is the end of a word
     *
     * @return true if the current node marks the end of a word
     */
    public boolean isWordEnd() {
        return isWordEnd;
    }

    /**
     * Sets whether the current node marks the end of a word
     *
     * @param changeValue the boolean value to which we want to set isWordEnd
     * variable
     */
    public void setWordEnd(boolean changeValue) {
        isWordEnd = changeValue;
    }

    /**
     * Returns an iterator that goes over the children list of the current node
     *
     * @return a type LexiconNode iterator that traverses over the children
     * of the node
     */
    public Iterator<LexiconNode> iterator() {
        return this.childrenList.listIterator();
    }

    /**
     * Gets the letter associated with the node
     *
     * @return a char type letter associated with the node
     */
    public char getLetter() {
        return letter;
    }
}
