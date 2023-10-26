/**
 * Node class for doubly linked list
 */
package javamypackage;

// TODO: Auto-generated Javadoc
/**
 * The Class Node.
 *
 * @param <E> the element type
 */
public class Node<E> {
	
	/** The element. */
	E element;
	
	/** The next. */
	Node next;
	
	/** The prev. */
	Node prev;

	/**
	 * Instantiates a new node.
	 *
	 * @param element the element
	 * @param next the next
	 * @param prev the prev
	 */
	public Node(E element, Node next, Node prev) {
		this.element = element;
		this.next = next;
		this.prev = prev;
	}
}
