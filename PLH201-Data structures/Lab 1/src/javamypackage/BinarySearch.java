/**
 * binary search for user-input word through pages
 */
package javamypackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

// TODO: Auto-generated Javadoc
/**
 * The Class BinarySearch.
 */
public class BinarySearch {
	
	/** The fpa. */
	FilePageAccess fpa = new FilePageAccess();
	
	/** The s. */
	Search s = new Search();
	
	/**
	 * Binary search.
	 *
	 * @param totalPages the total pages
	 * @param filename the filename
	 * @param word the word
	 * @param pageElements the page elements
	 * @param PAGE_SIZE 
	 * @param MAX_WORD_SIZE 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int binarySearch(int totalPages, String filename, String word, int pageElements, int PAGE_SIZE, int MAX_WORD_SIZE) throws IOException{
		int mid = totalPages/2;		//middle page from total pages
		int access = 0;
		int ret = 2;
		int firstPage = 0;
		int lastPage = totalPages;
		int prevPage = 0;
		ArrayList<IndexTable> t = new ArrayList<IndexTable>();
		ArrayList<Integer> a = new ArrayList<Integer>();
		ArrayList<Integer> b = new ArrayList<Integer>();
		
		while(ret != 0){		//while loop, ret is 0 when word is found and we don't have to search more or when a word is not found
			t = fpa.readFile(filename+".ndx", mid, totalPages, pageElements, PAGE_SIZE, MAX_WORD_SIZE);		//read middle Page from indexed file
			access++;
			a = s.serialSearch(t, word);		//binary search word in middle page
			b.addAll(a);						//store in arraylist
			IndexTable first = t.get(0);		//get first word
			IndexTable last = t.get(t.size() - 1);		//get last word
			
			if(t.size() < 5){		//for last page if it's not full
				ret = 0;
			}
			else if(first.getWord().compareTo(word) == 0 && last.getWord().compareTo(word) == 0){	//when page is full of word that we search, we have to search both left and right of this page
				int hold = mid;		//keep middle page number in order to later search after it 
				do{					//search before this page
					if(mid == 0){
						break;		//break if first page
					}
					mid = mid - 1;	//previous page
					t = fpa.readFile(filename+".ndx", mid, totalPages, pageElements, PAGE_SIZE, MAX_WORD_SIZE);
					access++;
					a = s.serialSearch(t, word);		//binary search word in middle page
					first = t.get(0);		//get first word
					b.addAll(a);
				}while(first.getWord().compareTo(word) == 0);	//compare first word of page with searched word, exit if not equal
				
				do{					//search after middle page	
					hold = hold + 1;	//next page
					t = fpa.readFile(filename+".ndx", hold, totalPages, pageElements, PAGE_SIZE, MAX_WORD_SIZE);
					access++;
					a = s.serialSearch(t, word);		//binary search word in middle page
					b.addAll(a);
					if(t.size() < 5){	//break if last page and less than 5 values
						break;
					}
					last = t.get(t.size() - 1);
				}while(last.getWord().compareTo(word) == 0);
				ret = 0;		//word is found so we move in this case
			}
			else{
				if(first.getWord().compareTo(word) >= 0 && prevPage != mid - 1 && prevPage != mid){		//prevent from accessing previously visited page
					if(mid == 0){	
						ret = 0;	//ret=0 if first page
					}
					else
					ret = -1;		//get lower half
				}
				else if(last.getWord().compareTo(word) <= 0 && prevPage != mid + 1 && prevPage != mid){		//prevent from accessing previously visited page
					if(mid == totalPages){
						ret = 0;	//ret=0 if last page
					}
					else
					ret = 1;		//get higher half
				}
				else{
					ret = 0;		//word is found and there is no more search
				}
			}

			if(ret == -1){		//lower half
				prevPage =mid;		//previous page so we don't access it again
				lastPage = mid - 1;		//last page is now midle page - 1
				if(b.size()>0){
					mid = mid - 1;		//search previous page if first word of page is equal to searched word
				}
				else mid = (firstPage+lastPage)/2;		//lower half, new middle page
			}
			else if(ret == 1){
				prevPage =mid;		//previous page so we don't access it again
				firstPage = mid + 1;		//first page is now midle page + 1 	
				if(b.size()>0){
					mid = mid + 1;		//search next page if last word of page is equal to searched word
				}
				else mid = (firstPage+lastPage)/2;		//upper half, new middle page
			}
				
		}
		Collections.sort(b);		//sort arraylist of lines
		if(b.size() > 0){
			System.out.println(word + " is on lines " + b);
			System.out.println("Disk accesses: " + access);

		}
		else{
			System.out.println("Not found!");
			System.out.println("Disk accesses: " + access);
		}
		return access;
	}
}
