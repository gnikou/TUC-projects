package mypackage;

public class SortedArray {

	int access = 0;

	public boolean increaseAccess() {
		access++;
		return true;
	}

	public int access() {
		return access;
	}

	public int binarySearch(int arr[], int x) {
		access = 0; 
		int min = 0; increaseAccess();	//set min to zero
	    int max = arr.length - 1; increaseAccess();	//set max to array length
		while (increaseAccess() && min <= max) {
			int mid = min + (max - min) / 2; increaseAccess(); //set middle number
			
			// Check if x is present at mid
			if (increaseAccess() && arr[mid] == x) {	//number found
				return mid;
			}	
			// If x greater, ignore left half
			if (increaseAccess() && arr[mid] < x) {		
				min = mid + 1; increaseAccess();
			}
			// If x is smaller, ignore right half
			else {
				max = mid - 1; increaseAccess(); 
			}	
		}
		// if we reach here, then element was
		// not present
		return access();	//return access number
	}

	

	public int rangeSearch(int arr[], int k1, int k2) {

		access = 0;
		int min = 0, max = arr.length - 1; increaseAccess(); //set min,max
		int mid = min + (max - min) / 2; increaseAccess(); //set mid

		while (increaseAccess() && min<max) {
			mid = min + (max - min) / 2; increaseAccess();
			if (increaseAccess() && k1 > arr[mid]) { //check if k1>mid
				min = mid + 1; increaseAccess(); //search right half
			}	
			else if (increaseAccess() && k2 < arr[mid]) { //check if k2<mid
				max = mid - 1; increaseAccess(); //search left half
			}	
			else{		//found number(s) between k1 and k2
				int first = min; increaseAccess();
				int m = mid; increaseAccess();
				int last = max; increaseAccess();
				max=mid; increaseAccess();
				mid = min + (max - min) / 2; increaseAccess();
				while (increaseAccess() && (k1 > arr[mid] || k1 < arr[mid-1])) { //look for lower bound
					mid = min + (max - min) / 2;		increaseAccess();
					if (increaseAccess() && k1 > arr[mid]) { //search binary until we find numbers greater than k1
						min = mid + 1; increaseAccess();
					}
					else if (increaseAccess() && k1 < arr[mid]) {
						max = mid - 1; increaseAccess();
					}
					else {
						while (increaseAccess() && k1 < arr[mid]) {	
							mid=mid-1; increaseAccess(); //go left until it finds first number less than k1
						}	
						first = mid +1; increaseAccess();	//first=first number above k1
					}
				}
				first = mid +1; increaseAccess();
				min = m; increaseAccess();
				max = last; increaseAccess();
				mid = min + (max - min) / 2; increaseAccess();	
				while (increaseAccess() && (k2 < arr[mid] || k2 > arr[mid+1])) { //look for upper bound
					mid = min + (max - min) / 2; increaseAccess();
					if (increaseAccess() && k2 > arr[mid]) {	 //search binary until we find numbers less than k2
						min = mid + 1;	increaseAccess();
					}	
					else if (increaseAccess() && k2 < arr[mid]) {
						max = mid - 1; increaseAccess();
					}	
					else {
						while (increaseAccess() && k2 < arr[mid]) {
							mid=mid+1; increaseAccess(); //go right until it finds first number greater than k2
						}	
						last = mid-1; increaseAccess();
					}
				}
				last = mid-1; increaseAccess(); //last=last number above k2
				for(int i =first; i<=last;i++){
					increaseAccess();
					System.out.println(arr[i]);	//print numbers between first and last positions of array
				}
				min = max;	
			}
		}
		return access();	//return access number
	}
}
