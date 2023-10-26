package javamypackage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class DoublyLinkedList<E> {
	
	Scanner sc = new Scanner(System.in);
	Node head;
	private int size;
	private int stringSize=0;
	private int position=0;
	private Node temp = head;
	private Node tail;
	ArrayList<IndexTable> myTable = new ArrayList<IndexTable>();

	public DoublyLinkedList() {
		size = 0;
	}
	
	public int size() { return size; }		
	
	public int stringSize(){ return stringSize; }
	
	
	/**
	 * character count
	 * 
	 */
	public int charCount(){
		Node tmp = head;
		String sizes = (String) tmp.element;
		int num =0;
		while(tmp.next != null){
			int count= sizes.length();
			tmp = tmp.next;
			num = count;
		}
		return num;
	}
	
	/**
	 * gets lines of opened file and inserts them line by line in linked list
	 * 
	 */
	public void FileRead(String line) {
		Node tmp = new Node(line, null, null);
		String temp = line;
		stringSize += temp.length();
		if(head == null){
			head = tmp;
			size++;
		}
		else{
			if (head.next == null){
				head.next = tmp;
				tmp.prev = head;
				tail = tmp;
				size++;

			}
			else{
				tail.next=tmp;
				tmp.prev = tail;
				tail = tmp;
				size++;
			}

		}

	}
	
	/**
	 * toggle line number
	 * 
	 */
	public void printList(int toggle){
		Node tmp = head;
		int line = 1;
		if (toggle == 0){
			while(tmp != null){
				System.out.println(tmp.element);
				tmp = tmp.next;
			}
		}
		else{
			while(tmp != null){
				System.out.println(line+ ")" + tmp.element);
				tmp = tmp.next;
				line++;
			}
		}
	}
	
	/**
	 * saves changes of linked list into file
	 * @param filename(save in same file)
	 * @throws IOException
	 */
	public void saveFile(String filename) throws IOException{
		Node n = head;
		FileWriter fileWriter = new FileWriter(filename, false);
		while(n.next!=null){
			fileWriter.write(n.element + "\n");
			n = n.next;
		}
		fileWriter.write(n.element + "\n");
		fileWriter.close();
	}
	
	
	/**
	 * create and sort index list
	 * @param MIN_WORD_SIZE 
	 * @param MAX_WORD_SIZE 
	 * @return
	 */
	public ArrayList<IndexTable> fileIndex(int MIN_WORD_SIZE, int MAX_WORD_SIZE){
		myTable = new ArrayList<IndexTable>();
		Node tp =head;
		int line = 1;
		while (tp != null){
			String str = (String) tp.element;
			String delims = "\\P{Alpha}+"; 		//string delims to get words
			String s[] = str.split(delims);		//split line
			for(int i=0;i<s.length;i++){		
				if(s[i].length()>=MIN_WORD_SIZE){	//remove small words
					if(s[i].length()>MAX_WORD_SIZE){
						myTable.add(new IndexTable(s[i].substring(0,MAX_WORD_SIZE),line));		//insert big words limited to 20 chars
					}
					else {
						myTable.add(new IndexTable(s[i],line));		//insert word + line into index table
					}
				}
			}
			line++;
			tp = tp.next;
		}
		
		Collections.sort(myTable);		//sort index table
		return myTable;
	}
	/**
	 * add line after current line
	 */
	public void addNewLineAfter(){
		if (temp == null){
			temp = head;
		}
		System.out.println("Type new line: ");
		String line = sc.nextLine();
		stringSize += line.length();
		Node afterNode = new Node(line,null,null);
		if(temp.next == null){
			temp.next = afterNode;
			afterNode.prev = temp;
		}
		else{
			Node temp1 =temp.next;
			temp.next = afterNode;
			afterNode.prev = temp;
			temp1.prev = afterNode;
			afterNode.next = temp1;
		}
		temp = afterNode;
		size++;
	}
	
	/**
	 * add line before current line
	 */
	public void addNewLineBefore(){
		if (temp == null){
			temp = head;
		}
		System.out.println("Type new line: ");
		String prevline = sc.nextLine();
		stringSize += prevline.length();
		Node prevNode = new Node(prevline,null,null);
		if(temp.prev == null){
			temp.prev = prevNode;
			prevNode.next = temp;
			head = prevNode;
		}
		else{
			Node temp1 =temp.prev;
			temp.prev = prevNode;
			prevNode.next = temp;
			temp1.next = prevNode;
			prevNode.prev = temp1;
		}
		temp = prevNode;
		size++;
	}
	
	/**
	 * delete current line
	 */
	public void deleteLine(){
		if (temp == null){
			temp = head;
		}
		String t =(String)temp.element;
		stringSize -= t.length();
		if (temp == head){
			head = temp.next;
			temp.next.prev =null;
			temp = temp.next;
		}
		else if(temp.next == null){
			temp.prev.next = null;
			temp=temp.prev;
		}
		else{
			temp.prev.next = temp.next;
			temp.next.prev = temp.prev;
			temp=temp.prev;
		}
		size--;
	}
	
	public void printLine(int toggle){
		if (temp == null){
			temp = head;
		}
		if (toggle == 0){
			System.out.println(temp.element);
		}
		else{
			Node tmp1 = head;
			int count = 1;
			while (tmp1!=temp){
				tmp1 = tmp1.next;
				count++;
			}
			System.out.println(count+ ")" + temp.element);
		}
	}
	
	public void printCurrentLineNumber(){
		if (temp == null){
			temp = head;
		}
		Node tmp1 = head;
		int count = 1;
		while (tmp1!=temp){
			tmp1 = tmp1.next;
			count++;
		}
		System.out.println("Current line number:" +count);
	}
	
	/**
	 * move position of our pointer in text
	 * 
	 */
	public void editList(char action){
		if (temp == null){
			temp = head;
		}
		switch(action){
			case '^':
				temp = head;
				break;
				
			case '$':
				while(temp.next != null){
					temp = temp.next;
				}
				break;
				
			case '-':
				if(temp.prev != null)
					temp = temp.prev;
				break;
				
			case '+':
				if(temp.next != null)
					temp = temp.next;
				break;	
		}
	}
}	