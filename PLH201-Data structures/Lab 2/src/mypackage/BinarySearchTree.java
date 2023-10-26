/*Binary Search Tree made of array
 * 
*/
package mypackage;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree {

	private int AVAIL;
	private int root;
	private int N;
	private int access;
	
	// Constructor
	BinarySearchTree() {
		AVAIL = 0;
		root = -1;
		access = 0;
		N = (int) 10e6;
	}

	int tree [][]; 	//bst array
	List<Integer> arrayList = new ArrayList<Integer>();
	int i =0;
	
	
	/**
	 * increase access method
	 */
	public boolean increaseAccess() {
		access++;
		return true;
	}
	
	/**
	 * @return numOfAccess
	 */
	public int access() {return access;}
	
	
	
	/**
	 * initialize array filling N positions with -1(null)
	 */
	void initialize() {
		tree = new int[3][N];
		for (int i = 0; i < N; i++) {
			tree[0][i] = -1;
			tree[1][i] = -1;
			tree[2][i] = i + 1;
		}
		tree[2][N-1] = -1;
		AVAIL = 0;
	}
	
	 public int insertNumber(int info) { 
		   access = 0; 	//set access to zero
	       root = insert(root, info);
	       return access();
	    } 

	 
	 /**
	 * @param root first node
	 * @param info new number to insert
	 * @return root node
	 */
	public int insert(int root, int info) { 
	        if (increaseAccess() && AVAIL == 0) { //empty tree
	            tree[0][0] = info; increaseAccess();
	            tree[1][0] = -1; increaseAccess();
	            tree[2][0] = -1; increaseAccess();
	            AVAIL = 1; increaseAccess();
	            root = 0; increaseAccess();
	            return root;
	        }
		    if (increaseAccess()&&info < tree[0][root]) {	//search left tree
		    	if(increaseAccess() && tree[1][root] != -1) { //if left child not empty call insert again
		    		insert(tree[1][root], info);
		    	}    
		    	else {
		    		tree[1][root] = AVAIL; increaseAccess(); //previous node point to new	
		    	 	tree[0][AVAIL] = info; increaseAccess(); //insert number in array
				    tree[1][AVAIL] = -1; increaseAccess();	//set left,right child to -1
		            tree[2][AVAIL] = -1; increaseAccess();
		            AVAIL = AVAIL + 1; increaseAccess();	//new avail      		
		    	}
		    }	
	        else if (increaseAccess() && info > tree[0][root]) { //search right tree
	        	if(increaseAccess()&&tree[2][root] != -1) {
	        		insert(tree[2][root], info);
	        	}    
	        	else {
		    		tree[2][root] = AVAIL; increaseAccess();
				    tree[0][AVAIL] = info; increaseAccess();
				    tree[1][AVAIL] = -1; increaseAccess();
		            tree[2][AVAIL] = -1; increaseAccess();
		            AVAIL = AVAIL + 1; increaseAccess();
	        	}    
	        }
	        return root;    
	    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////	 
	 public int searchNumber(int info) { 
		  access = 0;
	      int res = search(root,info);
	      if (res == -1)
	    		  System.out.println("Not found!");
	      else
	    	  System.out.println("Position found: " + res);
	      return access();
   	 } 
	 
	 public int search(int root, int key) {	
		 if(increaseAccess() && increaseAccess() && (root == -1 || tree[0][root] == key)) {	//return if empty tree or key found
			 return root;
		 }
		 if(increaseAccess() && tree[0][root]> key) {	//search left tree
			 return search(tree[1][root],key);
		 }
		 return search(tree[2][root],key);	//search right tree
	 }
	 
//////////////////////////////////////////////////////////////////////////////////////////////////////////	 
	 
	 public int[] inorder()  { 
		 inorderTraversal(root); 
		 int[] ret = new int[arrayList.size()];
		 for (int i=0; i < ret.length; i++){
			 ret[i] = arrayList.get(i).intValue();	//copy arrayList to int array
		 }
		 return ret;
	 } 
	   
     // A utility function to do inorder traversal of BST 
     public void inorderTraversal(int root) { 
         if (root != -1) { 
        	 inorderTraversal(tree[1][root]);	//traverse left tree
             arrayList.add(tree[0][root]);		//store values to arrayList
           //  System.out.println(tree[0][root]); //print values
             inorderTraversal(tree[2][root]); 	//traverse right tree

         }
     }
     
//////////////////////////////////////////////////////////////////////////////////////////////////////////     
     public int range(int k1, int k2) {
    	 access = 0;
    	 rangeSearch(root,k1,k2);
	     return access();
     }
     
     public void rangeSearch(int root, int k1, int k2) {
         if (increaseAccess() && root == -1) { 
             return; 
         } 
         /* search left tree until treeNum > k1 */
         if (increaseAccess() && k1 < tree[0][root]) { 
        	 rangeSearch(tree[1][root], k1, k2); 
         } 
   
         /* if root's data lies in range, then prints root's data */
         if (increaseAccess() && increaseAccess() && k1 <= tree[0][root] && k2 >= tree[0][root]) { 
        	 System.out.print(tree[0][root] + " "); 
         } 
   
         /* search right tree until treeNum < k2 */
         if (increaseAccess() && k2 > tree[0][root]) { 
        	 rangeSearch(tree[2][root], k1, k2); 
         } 
     } 
}