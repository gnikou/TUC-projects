/**
 * 
 */
package domes_project3;

/**
 * @author home
 *
 */
public class LinearHashing {
	
	private HashBucket[] hashBuckets;	// pointer to the hash buckets

	private float maxThreshold;		// max load factor threshold
	private float minThreshold;		// min load factor threshold

	private int bucketSize;		// max number of keys in each bucket
	private int keysNum;			// number of keys currently stored in the table
	private int keySpace;			// total space the hash table has for keys
	private int p;				// pointer to the next bucket to be split
	private int n;				// current number of buckets
	private int j;				// the n used for the hash function
	private int minBuckets;			// minimum number of buckets this hash table can have
	private int access;
	
	public boolean increaseAccess() {
		access++;
		return true; 
	}

	public int access() {
		return access;
	}
	
	
	private int hashFunction(int key){	// Returns a hash based on the key

		int retval;

		retval = key%this.j; increaseAccess();
		if (increaseAccess() && retval < 0)
			retval *= -1; increaseAccess();
		if (increaseAccess() && retval >= p){
		  //System.out.println( "Retval = " + retval);
		  return retval;
		}
		else {
			 retval = key%(2*this.j); increaseAccess();
			 if (increaseAccess() && retval < 0)
				 retval *= -1; increaseAccess();
			 //System.out.println( "Retval = " + retval);
		         return retval;
		}
	}

	private float loadFactor() {		// Returns the current load factor of the hash table.
		return ((float)this.keysNum)/((float)this.keySpace);
	}

	private void bucketSplit() {		// Splits the bucket pointed by p.

		int i;
		HashBucket[] newHashBuckets;

		newHashBuckets= new HashBucket[n+1]; increaseAccess();
		for (i = 0; i < this.n; i++){
		   newHashBuckets[i] = this.hashBuckets[i];increaseAccess();
		}

		hashBuckets = newHashBuckets; increaseAccess();
		hashBuckets[this.n] = new HashBucket(this.bucketSize); increaseAccess();
		this.keySpace += this.bucketSize; increaseAccess();
		this.hashBuckets[this.p].splitBucket(this, 2*this.j, this.p, hashBuckets[this.n]);
		this.n++;increaseAccess();
		if (increaseAccess() && this.n == 2*this.j) {
		  this.j = 2*this.j;increaseAccess();
		  this.p = 0;increaseAccess();
		}
		else {
		    this.p++;increaseAccess();
		}
	}

	private void bucketMerge() { 		// Merges the last bucket that was split

		int i;

		HashBucket[] newHashBuckets;
		newHashBuckets= new HashBucket[n-1];increaseAccess();
		for (i = 0; i < this.n-1; i++) {
		   newHashBuckets[i] = this.hashBuckets[i];increaseAccess();
		}
		if (increaseAccess() && this.p == 0) {
		  this.j = (this.n)/2;increaseAccess();
		  this.p = this.j-1;increaseAccess();
		}
		else {
		  this.p--;increaseAccess();
		}
		this.n--;increaseAccess();
		this.keySpace -= this.bucketSize; increaseAccess();
		this.hashBuckets[this.p].mergeBucket(this, hashBuckets[this.n]);
		hashBuckets = newHashBuckets; increaseAccess();
	}

	public LinearHashing(int itsBucketSize, int initPages, float max) { 	// Constructor

		int i;

		bucketSize = itsBucketSize;
		keysNum = 0;
		p = 0;
		access = 0;
		n = initPages;
		j = initPages;
		minBuckets = initPages;
		keySpace = n*bucketSize;
		maxThreshold = max;
		minThreshold = (float)0.5;

		if ((bucketSize == 0) || (n == 0)) {
		  System.out.println("error: space for the table cannot be 0");
		  System.exit(1);
		}
		hashBuckets = new HashBucket[n];
		for (i = 0; i < n; i++) {
		   hashBuckets[i] = new HashBucket(bucketSize);
	}
}

	public int getBucketSize() {return bucketSize;}
	public int getKeysNum() {return keysNum;}
	public int getKeySpace() {return keySpace;}
	public void setBucketSize(int size) {bucketSize = size;}
	public void setKeysNum(int num) {keysNum = num;}
	public void setKeySpace(int space) {keySpace = space;}

	public int insertKey(int key) {	// Insert a new key.
		access = 0;
		//System.out.println( "hashBuckets[" + this.hashFunction(key) + "] =  " + key);
		access += this.hashBuckets[this.hashFunction(key)].insertKey(key, this);
		if (increaseAccess() && this.loadFactor() > maxThreshold){
		  //System.out.println("loadFactor = " + this.loadFactor() );
		  this.bucketSplit();
		  //System.out.println("BucketSplit++++++");
		}
		return access();
	}

	public int deleteKey(int key) {	// Delete a key.
		access = 0;
		access += this.hashBuckets[this.hashFunction(key)].deleteKey(key, this);
		if (increaseAccess() && increaseAccess() && (this.loadFactor() < minThreshold) && (this.n > this.minBuckets)){
			 this.bucketMerge();
		}
	    return access();

	}

	public int searchKey(int key) {		// Search for a key.
		access = 0;
		access += this.hashBuckets[this.hashFunction(key)].searchKey(key, this);
		return access();
	}

	public void printHash() {

		int i;

		for (i = 0; i < this.n; i++) {
		   System.out.println("Bucket[" + i + "]");
		   this.hashBuckets[i].printBucket(this.bucketSize);
		}
	}


}
