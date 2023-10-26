/**
 * serial search for user-input word through pages
 */
package javamypackage;

import java.io.IOException;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class SerialSearch.
 */
public class SerialSearch {
	
	FilePageAccess fpa = new FilePageAccess();
	
	Search s = new Search();

	/**
	 * Serial search.
	 *
	 * @param totalPages total pages to be read of .ndx file
	 * @param filename the filename
	 * @param word word that is searched
	 * @param pageElements how many words per page
	 * @param PAGE_SIZE 
	 * @param MAX_WORD_SIZE 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void serialSearch(int totalPages, String filename, String word, int pageElements, int PAGE_SIZE, int MAX_WORD_SIZE) throws IOException{
		int pageNo=0;
		int dskAccess = 0;
		ArrayList<Integer> result = null;		//arraylist of integers
		ArrayList<Integer> res = null;

		while(pageNo < totalPages){					
			ArrayList<IndexTable> t = fpa.readFile(filename+".ndx", pageNo, totalPages, pageElements, PAGE_SIZE, MAX_WORD_SIZE);		//read pageNo Page from indexed file
			result = s.serialSearch(t, word);		//search word in current page
			if(result.size()>0){		
				if (res == null){		//first finding
					res = result;
				}
				else{
					res.addAll(result);		//store into arraylist
				}
			}
			pageNo++;
			dskAccess = pageNo + 1;
//			if (result.size() == 0 && res!=null){	//EXIT SEARCH WHEN WE HAVE NO MORE RESULTS	
//				pageNo = totalPages;
//			}
		}
		
		if(res != null){
			System.out.println(word + " is on lines " + res);
			System.out.println("Disk accesses: " + dskAccess);
		}
		else{
			System.out.println("Not found! Disk accesses: " + dskAccess);
		}
	}
}
