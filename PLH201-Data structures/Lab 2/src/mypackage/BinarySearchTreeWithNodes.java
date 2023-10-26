package mypackage;

public class BinarySearchTreeWithNodes {		  
	    /* Class containing left and right child of current node and key value*/
	    class Node { 
	        int key; 
	        Node left, right; 
	  
	        public Node(int item) { 
	            key = item; 
	            left = right = null; 
	        } 
	    } 
	  
	    // Root of BST 
	    Node root; 
	    int access;
	    // Constructor 
	    BinarySearchTreeWithNodes() {  
	        root = null;
	        access = 0;
	    } 
	  
	    public boolean increaseAccess() {
			access++;
			return true;
		}
		public int access() {return access;}

	    // This method mainly calls insertRec() 
	    int insert(int key) { 
	       access = 0;
	       root = insertRec(root, key);
	       return access();
	    } 
	      
	    /* A recursive function to insert a new key in BST */
	    Node insertRec(Node root, int key) { 
	        /* If the tree is empty, return a new node */
	        if (increaseAccess() && root == null) { 
	            root = new Node(key); increaseAccess();
	            return root; 
	        } 
	        /* Otherwise, recur down the tree */
	        if (increaseAccess() && key < root.key) {
	            root.left = insertRec(root.left, key); 
	        }    
	        else if (increaseAccess() && key > root.key) { 
	            root.right = insertRec(root.right, key); 
	        }    
	        /* return the (unchanged) node pointer */
	        return root; 
	    } 
	    
	    public int search(int key) {
	    	access = 0;
	    	searchRec(root,key);
	    	return access();
	    }
	  
	    public Node searchRec(Node root, int key) 
	    { 
	        // Base Cases: root is null or key is present at root 
	        if (increaseAccess() && increaseAccess() && (root==null || root.key==key)) {
	            return root; 
	        }    
	        // val is greater than root's key 
	        if (increaseAccess() && root.key > key) {
	            return searchRec(root.left, key); 
	        }    
	        // val is less than root's key 
	        return searchRec(root.right, key); 
	    } 
	    
	    
	    
	    public int range(int k1, int k2) {
	    	 access = 0;
	    	 rangeSearch(root,k1,k2);
		     return access();
	     }
	    
	    public void rangeSearch(Node node, int k1, int k2) { 
	          
	        /* base case */
	        if (increaseAccess() && node == null) {
	            return; 
	        } 
	  
	        /* Since the desired o/p is sorted, recurse for left subtree first 
	         If root->data is greater than k1, then only we can get o/p keys 
	         in left subtree */
	        if (increaseAccess() && k1 < node.key) {
	        	rangeSearch(node.left, k1, k2); 
	        } 
	  
	        /* if root's data lies in range, then prints root's data */
	        if (increaseAccess() && increaseAccess() && k1 <= node.key && k2 >= node.key) { 
	            System.out.print(node.key + " "); 
	        } 
	  
	        /* If root->data is smaller than k2, then only we can get o/p keys 
	         in right subtree */
	        if (increaseAccess() && k2 > node.key) {
	        	rangeSearch(node.right, k1, k2); 
	        } 
	    } 
}
