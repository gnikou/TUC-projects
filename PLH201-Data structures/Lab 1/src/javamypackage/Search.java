/**
 * this class searches serially through one page at a time 
 */
package javamypackage;

import java.util.ArrayList;


public class Search{
	
	/**
	 * serial search of word.
	 * @param list page that we search
	 * @param word user word that is searched into page
	 * @return arraylist of line numbers of "word"
	 */
	ArrayList<Integer> serialSearch(ArrayList<IndexTable> list, String word){
		int i =0;
		ArrayList<Integer> l = new ArrayList<Integer>();

		for(IndexTable it: list){				//page elements loop
			String s = new String(it.getWord());	//get each word value into string
			if (s.equals(word)){		
				i = it.getLine();		//get line if word is found
				l.add(i);		//add it into arraylist
			}
		}
		return l;
	}
}
