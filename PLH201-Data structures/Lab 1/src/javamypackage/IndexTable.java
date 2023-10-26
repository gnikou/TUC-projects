/**
 * index table of word + line
 */
package javamypackage;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexTable.
 */
public class IndexTable implements Comparable<IndexTable>{
	
	/** The word. */
	private String word;
	
	/** The line. */
	private int line;

	/**
	 * Instantiates a new index table.
	 *
	 * @param word the word
	 * @param line the line
	 */
	public IndexTable(String word, int line) {
		super();
		this.word = word;
		this.line = line;
	}
	
	/**
	 * Gets the word.
	 *
	 * @return the word
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Sets the word.
	 *
	 * @param word the new word
	 */
	public void setWord(String word) {
		this.word = word;
	}
	
	/**
	 * Gets the line.
	 *
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * Sets the line.
	 *
	 * @param line the new line
	 */
	public void setLine(int line) {
		this.line = line;
	}
	
	/**
	 * compare word to sort in alphabetical order.
	 *
	 * @param index the index
	 * @return table
	 */
	public int compareTo(IndexTable index) {
		int word = this.word.compareTo(index.word);
		return word;
	}
}
