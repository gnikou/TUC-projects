package domes_project3;

public class BinarySearchTree {
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
    BinarySearchTree() {  
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
            root.left = insertRec(root.left, key); increaseAccess();
        }    
        else if (increaseAccess() && key > root.key) { 
            root.right = insertRec(root.right, key); increaseAccess();
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
    
    // This method mainly calls deleteRec() 
    int deleteKey(int key) 
    { 	access = 0;
        root = deleteRec(root, key); 
        return access();
    } 
  
    /* A recursive function to insert a new key in BST */
    Node deleteRec(Node root, int key) 
    { 
        /* Base Case: If the tree is empty */
        if (increaseAccess() && root == null)  return root; 
  
        /* Otherwise, recur down the tree */
        if (increaseAccess() && key < root.key) { 
            root.left = deleteRec(root.left, key); increaseAccess();
        }    
        else if (increaseAccess() && key > root.key) { 
            root.right = deleteRec(root.right, key); increaseAccess();
        }    
        // if key is same as root's key, then This is the node 
        // to be deleted 
        else
        { 
            // node with only one child or no child 
            if (increaseAccess() && root.left == null) 
                return root.right; 
            else if (increaseAccess() && root.right == null) 
                return root.left; 
  
            // node with two children: Get the inorder successor (smallest 
            // in the right subtree) 
            root.key = minValue(root.right); increaseAccess();
  
            // Delete the inorder successor 
            root.right = deleteRec(root.right, root.key); increaseAccess();
        } 
  
        return root; 
    } 
    
    int minValue(Node root) 
    { 
        int minv = root.key; increaseAccess();
        while (increaseAccess() && root.left != null) 
        { 
            minv = root.left.key; increaseAccess();
            root = root.left; increaseAccess();
        } 
        return minv; 
    } 
}
