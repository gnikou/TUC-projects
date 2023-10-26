/** 
 * 
 * this is Main Class
 */
package javamypackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class MainClass {
	
	static final int MIN_WORD_SIZE = 5; /** The min word size. */
	static int MAX_WORD_SIZE = 20; /** The max word size. */
	static int LINE_SIZE = 80;	/** The line size. */
	static int PAGE_SIZE = 128;		/** The page size. */
	static int PAGE_ELEMENTS = PAGE_SIZE/(MAX_WORD_SIZE+4);	/**Number of page elements. */

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		FilePageAccess fpa = new FilePageAccess();
		IndexConverter ic = new IndexConverter();
		Search s = new Search();
		SerialSearch ss = new SerialSearch();
		BinarySearch bs = new BinarySearch();
		ArrayList<IndexTable> tp = new ArrayList<IndexTable>();
		ArrayList<IndexTable> myTable = new ArrayList<IndexTable>();
		Scanner sc = new Scanner(System.in);
		String filename = null;
		String line;
		String lineOut = null;
		BufferedReader br = null;
		int pages = 0;
		int toggle = 0;
		char choice = 0;
		
		
		/**
		 * check for file and ask user to crate a new one if it doesn't exist
		 */
		if(args.length == 0){
			System.out.println("Create new file. Type name: ");
			filename = sc.next();
			PrintWriter writer = new PrintWriter(filename, "UTF-8");
		}
		else{
		    filename = args[0];
		}
		
		/**
		 * open file via bufferReader
		 */
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} 
		
		/**
		 * read all lines(with line size limit)
		 */
		while ((line = br.readLine()) != null) {
			if (line.length() > LINE_SIZE){
				lineOut = line.substring(0,LINE_SIZE);
				dll.FileRead(lineOut);
			}
			else
				dll.FileRead(line);	
		} 
		

		while(true){	
			System.out.println("Give a choice:");		
			choice = sc.next().charAt(0);		//user input
			
			switch(choice){
				case '^': case '$':	case '-': case '+':	//move cursor
					dll.editList(choice);
					System.out.println("OK");
					break;
				
				case 'a':
					dll.addNewLineAfter();		//add new line after current line
					break;
					
				case 't':
					dll.addNewLineBefore();		//add new line before current line
					break;
					
				case 'd':
					dll.deleteLine();			//delete current line
					break;
					
				case 'l':						//print all lines
					dll.printList(toggle);		
					break;	
					
				case 'n':						//toogle line number
					if (toggle == 0){
						toggle = 1;
					}
					else
						toggle = 0;
					break;	
					
				case 'p':
					dll.printLine(toggle);
					break;
					
				case 'w':						//save file
					dll.saveFile(filename);
					break;	
					
				case 'x':						//save file and quit
					dll.saveFile(filename);
					System.exit(0);
					break;	
				
				case '=':						//print current line number
					dll.printCurrentLineNumber();
					break;
					
				case '#':						//print lines and characters
					int size = dll.size();
					int count = dll.stringSize();
					System.out.println( size + " lines, "+ count + " characters");
					break;	
				
				case 'q':						//exit without save
					System.exit(0);
					break;
				
				case 'c':
					File file = new File(filename+".ndx"); 
					tp = new ArrayList<IndexTable>();
			        file.delete(); 
					myTable = dll.fileIndex(MIN_WORD_SIZE, MAX_WORD_SIZE);
					int diskAccess = 0;
					int i = 0;
					for(IndexTable el: myTable){	//loop through indexTable values
						i++;
						tp.add(new IndexTable(el.getWord(),el.getLine()));	//add to a small indexTable to fill a page
						if(i >= PAGE_ELEMENTS){					//fill one page
							byte[] bytes = ic.getPageBytes(tp, MAX_WORD_SIZE, PAGE_SIZE);	//get tuples bytes to write into file
							fpa.writeFile(bytes,filename,diskAccess, PAGE_SIZE);	//write file
							diskAccess++;
							tp= new ArrayList<IndexTable>();
							i=0;
						}			
					}
					byte[] bytes = ic.getPageBytes(tp, MAX_WORD_SIZE, PAGE_SIZE);	//last page
					if(bytes[0] != 0){					//check for empty page
						fpa.writeFile(bytes,filename,diskAccess, PAGE_SIZE);
						diskAccess++;
					}
					System.out.println("OK. Data pages of size "+ PAGE_SIZE +" bytes: " + diskAccess);
					pages = diskAccess-1;
					break;

				case 'v':
					myTable = dll.fileIndex(MIN_WORD_SIZE, MAX_WORD_SIZE);		//get index table in order
					int v =0;
					for(IndexTable el: myTable) {
						System.out.println(el.getWord() + "\t" + el.getLine());		//print indesx table elements
						v++;
					}
					System.out.println("Words:\t" + v);		//total words
				break;
					
				case 's':
					System.out.println("Type word for search:");	//user input
					String word = sc.next();				//word that is searched	
					ss.serialSearch(pages, filename, word, PAGE_ELEMENTS, PAGE_SIZE, MAX_WORD_SIZE);	//serial search
					
//					int numOfTimes = 0;
//					for(int n=0; n<30; n++){
//						String word = RandomString.getAlphaNumericString(n); 
//					ss.serialSearch(pages, filename, word, PAGE_ELEMENTS);
//						numOfTimes++;//						
//					}
//					System.out.println(numOfTimes);
				break;	
					
				case 'b':
					System.out.println("Type word for search:");	//user input
					String wrd = sc.next();		//word that is searched
					bs.binarySearch(pages, filename, wrd, PAGE_ELEMENTS, PAGE_SIZE, MAX_WORD_SIZE);	//binary search
					
//					double ret=0;
//					for(int n=0; n<30; n++){
//						String wrd = RandomString.getAlphaNumericString(n+1);
//						System.out.println(wrd);
//						ret += bs.binarySearch(pages, filename, wrd, PAGE_ELEMENTS);
//					}
//					double accessAverage = ret/30;
//					System.out.println(accessAverage);
					break;
					
				default:
					System.out.println("Bad command");	
			}
		}
	}
}