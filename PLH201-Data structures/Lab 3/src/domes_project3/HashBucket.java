package domes_project3;

public class HashBucket {

	private int keysNumber;
	private int[] keys;
	private HashBucket overflowBucket;
	private int access;
	
	public HashBucket(int bucketSize) {		// Constructor: initialize variables

		keysNumber = 0;
		keys = new int[bucketSize];
		overflowBucket = null;
		access = 0;
	} 

	public int numKeys(){return keysNumber;}
	
	public boolean increaseAccess() {
		access++;
		return true;
	}

	public int access() {
		return access;
	}

	public int insertKey(int key, LinearHashing lh) { // inserts a key to the node
		access = 0;
		int i;
		int bucketSize = lh.getBucketSize(); increaseAccess();
		int keysNum = lh.getKeysNum(); increaseAccess();
		int keySpace = lh.getKeySpace(); increaseAccess();

		for (i = 0; increaseAccess() && increaseAccess() && (i < this.keysNumber) && (i < bucketSize); i++){
		   if (increaseAccess() && this.keys[i] == key){	//key already here. Ignore the new one
		     return access();
		   }
		}
		if (increaseAccess() && i < bucketSize){				// bucket not full write the new key
		  keys[i] = key; increaseAccess();
		  this.keysNumber++;
		  keysNum++;
		  lh.setKeysNum(keysNum); 			// update linear hashing class.
		}
		else {
		    //System.out.println("Overflow.............");
		    if (increaseAccess() && this.overflowBucket != null){	// pass key to the overflow
		    	this.overflowBucket.insertKey(key, lh);
		    }
		    else{						// create a new overflow and write the new key
				this.overflowBucket = new HashBucket(bucketSize); increaseAccess();
				keySpace += bucketSize; 
			    lh.setKeySpace(keySpace);		// update linear hashing class.
			  //  overflowBucketSize++;
				this.overflowBucket.insertKey(key, lh);
		    }
		}
		return access();
	}

	public int deleteKey(int key, LinearHashing lh) { // code not correct
		access=0;
		int i;
		int bucketSize = lh.getBucketSize(); increaseAccess();
		int keysNum = lh.getKeysNum(); increaseAccess(); 
		int keySpace = lh.getKeySpace(); increaseAccess();  

		for (i = 0;increaseAccess() && increaseAccess() && (i < this.keysNumber) && (i < bucketSize); i++) {
		   if (increaseAccess() && this.keys[i] == key) {
		     if (increaseAccess() && this.overflowBucket == null) {		// no overflow
				 this.keys[i] = this.keys[this.keysNumber-1];increaseAccess();
				 this.keysNumber--;
				 keysNum--;
				 lh.setKeysNum(keysNum);			// update linear hashing class.
		     }
		     else {	// bucket has an overflow so remove a key from there and bring it here
				 this.keys[i] = this.overflowBucket.removeLastKey(lh); increaseAccess();
				 keysNum--;
				 lh.setKeysNum(keysNum);			// update linear hashing class.
				 if (increaseAccess() && this.overflowBucket.numKeys() == 0) { // overflow empty free it
					 this.overflowBucket = null;increaseAccess();
					 keySpace -= bucketSize;increaseAccess();
				     lh.setKeySpace(keySpace);			// update linear hashing class.
				 }
		     }
		     return access();
		   }
		}
		
		if (increaseAccess() && this.overflowBucket != null) {			// look at the overflow for the key to be deleted if one exists
			this.overflowBucket.deleteKey(key, lh);
			if (increaseAccess() && this.overflowBucket.numKeys() == 0) {	// overflow empty free it
			    this.overflowBucket = null; increaseAccess();
			    keySpace -= bucketSize;
			    lh.setKeySpace(keySpace);				// update linear hashing class.
		    }
	    }
		
	     return access();
	}

	int removeLastKey(LinearHashing lh) {	// remove bucket last key

		int retval;
		int bucketSize = lh.getBucketSize(); increaseAccess();
		int keySpace = lh.getKeySpace(); increaseAccess();

		if (increaseAccess() && this.overflowBucket == null) {
		  if (increaseAccess() && this.keysNumber != 0){
		    this.keysNumber--;
		    return this.keys[this.keysNumber];
		  }
		  return 0;
		}
		else {
		  retval = this.overflowBucket.removeLastKey(lh); increaseAccess();
		  if (increaseAccess() && this.overflowBucket.numKeys() == 0) {	// overflow empty free it
		    this.overflowBucket = null; increaseAccess();
		    keySpace -= bucketSize;
		    lh.setKeySpace(keySpace);			// update linear hashing class.
		  }
		  return retval;
		}
	}


	public int searchKey(int key, LinearHashing lh) {
		access = 0;
		int i;
		int bucketSize = lh.getBucketSize(); increaseAccess();

		for (i = 0;increaseAccess() && increaseAccess() && (i < this.keysNumber) && (i < bucketSize); i++) {
		   if (increaseAccess() && this.keys[i] == key) {	//key found
		     return access();
		   }
		}
		if (increaseAccess() && this.overflowBucket != null) {				//look at the overflow for the key if one exists
		  return this.overflowBucket.searchKey(key,lh);
	      }
	      else {
		  return access();
	      }
	}

	public void splitBucket(LinearHashing lh, int n, int bucketPos, HashBucket newBucket) {	//splits the current bucket
		
		int i;
		int bucketSize = lh.getBucketSize();increaseAccess();
		int keySpace = lh.getKeySpace();increaseAccess();
		int keysNum = lh.getKeysNum();increaseAccess();

		for (i = 0;increaseAccess() && increaseAccess()&& (i < this.keysNumber) && (i < bucketSize);) {
		   if (increaseAccess() && (this.keys[i]%n) != bucketPos){	//key goes to new bucket
		     newBucket.insertKey(this.keys[i], lh);
		     this.keysNumber--;
		     keysNum = lh.getKeysNum();increaseAccess();
		     keysNum--;
		     lh.setKeysNum(keysNum);		// update linear hashing class.
		     //System.out.println("HashBucket.splitBucket.insertKey: KeysNum = " + keysNum );
		     this.keys[i] = this.keys[this.keysNumber]; increaseAccess();
		   }
		   else {				// key stays here
		     i++;
		   }
		}

		if (increaseAccess() && this.overflowBucket != null) {	// split the overflow too if one exists
		  this.overflowBucket.splitBucket(lh, n, bucketPos, newBucket);
		}
		while (increaseAccess() && this.keysNumber != bucketSize) {
		     if (increaseAccess() && this.overflowBucket == null) {
			 return;
		     }
		     if (increaseAccess() && this.overflowBucket.numKeys() != 0) {
		       this.keys[this.keysNumber] = this.overflowBucket.removeLastKey(lh); increaseAccess();
		       if (increaseAccess() && this.overflowBucket.numKeys() == 0) {	// overflow empty free it
					 this.overflowBucket = null; increaseAccess();
					 keySpace -= bucketSize;
					 lh.setKeySpace(keySpace);      // update linear hashing class.
		       }
		       this.keysNumber++;
		     }
		     else {				// overflow empty free it
		    	 this.overflowBucket = null; increaseAccess();
		    	 keySpace -= bucketSize;
		         lh.setKeySpace(keySpace);	// update linear hashing class.
		     }
	 	}
	}

	public void mergeBucket(LinearHashing lh, HashBucket oldBucket) {	//merges the current bucket

		int keysNum = 0;

		while (increaseAccess() && oldBucket.numKeys() != 0) {
		     this.insertKey(oldBucket.removeLastKey(lh), lh);
		}
	}

      public void printBucket(int bucketSize) {

		int i;

		System.out.println("keysNum is: " + this.keysNumber);
		for (i = 0; (i < this.keysNumber) && (i < bucketSize); i++) {
		   System.out.println("key at: " + i + " is: " + this.keys[i]);
		}
		if (this.overflowBucket != null) {
		  System.out.println("printing overflow---");
		  this.overflowBucket.printBucket(bucketSize);
		}
	}

}
