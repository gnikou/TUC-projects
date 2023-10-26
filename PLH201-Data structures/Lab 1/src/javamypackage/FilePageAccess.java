/**
 * access file to read or write a page from/to it
 */
package javamypackage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


/**
 * The Class FilePageAccess.
 */
public class FilePageAccess{
	
	/**
	 * write page in index file.
	 *
	 * @param bytes 	page bytes to be written
	 * @param filename the filename
	 * @param pageNo number of page
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeFile(byte[] bytes,String filename, int pageNo, int PAGE_SIZE) throws IOException {
		RandomAccessFile f = new RandomAccessFile(new File(filename+".ndx"), "rw");		//open .ndx file with RandomAccessFile
	    int aPositionWhereIWantToGo = pageNo*PAGE_SIZE;		//page number * page size
	    f.seek(aPositionWhereIWantToGo);		//seek position
	    f.write(bytes);		//write bytes starting from position aPositionWhereIWantToGo
	    f.close();
	}
	
	/**
	 * read page from index file.
	 *
	 * @param filename the filename
	 * @param pageNo page number read
	 * @param totalPages the total pages
	 * @param pageElements the page elements
	 * @return arraylist of indexTable(word + line)
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ArrayList<IndexTable> readFile(String filename, int pageNo, int totalPages, int pageElements, int PAGE_SIZE, int MAX_WORD_SIZE) throws IOException{
		RandomAccessFile r = new RandomAccessFile(filename, "r");
	    int aPositionWhereIWantToGo = pageNo*PAGE_SIZE;		//page number * page size
	    r.seek(aPositionWhereIWantToGo);		//seek position
	    byte[] b = new byte[PAGE_SIZE];
	    r.read(b, 0, PAGE_SIZE);   //read PAGE_SIZE bytes starting from position aPositionWhereIWantToGo
	    IndexConverter ic = new IndexConverter();
	    ArrayList<IndexTable> s = ic.toIndex(b, pageElements, MAX_WORD_SIZE);	//convert bytes to indexTable values
	    r.close();
	    return s;
	}
}
