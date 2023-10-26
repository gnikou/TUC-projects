/**
 * this class has two methods: the first method converts a page of indexTable values into bytes
 * the second method converts a page of bytes into IndexTable values
 */
package javamypackage;

import java.util.ArrayList;
import java.util.Arrays;

public class IndexConverter {
	
	byte[] byteArray;
	String s;
	
	/**
	 * convert indexTable to bytes to fill a page that is written in .ndx file
	 * @param array		page of IndexTable values
	 * @param MAX_WORD_SIZE 
	 * @param PAGE_SIZE 
	 * @return	bytes
	 */
	public byte[] getPageBytes(ArrayList<IndexTable> array, int MAX_WORD_SIZE, int PAGE_SIZE) {
		byte[] tmp1 = new byte[MAX_WORD_SIZE];
		byte[] tmp = new byte[MAX_WORD_SIZE];
		java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(PAGE_SIZE);		//allocate PAGE_SIZE size of bytebuffer 
		for(IndexTable el: array) {		//access page values
			tmp1 = new byte[MAX_WORD_SIZE];
			if(el.getWord().getBytes(java.nio.charset.StandardCharsets.US_ASCII).length < MAX_WORD_SIZE) {		//handle words less than 20 chars
				int i = el.getWord().getBytes(java.nio.charset.StandardCharsets.US_ASCII).length;		//get word length
				tmp = el.getWord().getBytes();		//get word bytes in tmp
				System.arraycopy(tmp,0,tmp1,0,i);	//arraycopy tmp to tmp1, tmp1 is originally 20 zeros so we copy tmp values in the start of tmp1
			//	System.out.println(Arrays.toString(tmp));									//and the remaining bytes that are not from word remain zeros
				//System.out.println(Arrays.toString(tmp1));									//and the remaining bytes that are not from word remain zeros
			}
			else{
				tmp1 = el.getWord().getBytes();		//get bytes from 20 char words
			}
			bb.put(tmp1);
			int line = el.getLine();
			bb.putInt(line);
			if(bb.remaining() < MAX_WORD_SIZE+4) {	//check if page can get more inputs
				return byteArray;		
			}
			byteArray = bb.array();
		}
		byteArray = bb.array(); 
		
		return byteArray;
	}
	
	/**
	 * convert bytes from page that was read to IndexTable.
	 *
	 * @param pageBytes the page bytes
	 * @param pageElements the page elements
	 * @param MAX_WORD_SIZE 
	 * @return arraylist of IndexTable
	 */
	public ArrayList<IndexTable> toIndex(byte[] pageBytes, int pageElements, int MAX_WORD_SIZE) {
		ArrayList<IndexTable> myTable = new ArrayList<IndexTable>();
		for(int i=0;i<pageElements;i++){		//for loop into page content
			java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(4);	//allocate 4 bytes for java integer
			int r = i*(MAX_WORD_SIZE+4);		//get i-th word position in page
			byte[] arr = Arrays.copyOfRange(pageBytes, r, r+MAX_WORD_SIZE+4);	//read MAW_WORD_SIZE+4 bytes from position r
			byte [] str =  Arrays.copyOfRange(arr, 0, MAX_WORD_SIZE);		//get word bytes 0-MAX_WORD_SIZE
			byte [] valu =  Arrays.copyOfRange(arr, MAX_WORD_SIZE, MAX_WORD_SIZE+4);	//get line bytes
			bb.put(valu);
			int value = bb.getInt(0);
			if (str[0] != 0){		//check if word's first byte is zero, we check if there are more words in page
				s = new String(str).split("\0")[0];		//get rid of zero bytes
				myTable.add(new IndexTable(s,value));	//put word+line into indexTable
			}
		}
		return myTable;
	}
}
