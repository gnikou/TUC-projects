/*Main Class
 * 
*/
package mypackage;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class MainClass {
	public static void main(String[] args) throws IOException {
		float access = 0;
		float time = 0;
		long startTime = 0;
		long endTime = 0;
		Random rand = new Random();
		BinarySearchTree bst = new BinarySearchTree();
		BinarySearchTreeWithNodes nodesBST = new BinarySearchTreeWithNodes();
		SortedArray sortedArray = new SortedArray();
		Scanner scanner = new Scanner(System.in);
		DataInputStream in = new DataInputStream(new FileInputStream("testnumbers_1000000_BE.bin"));
	
		bst.initialize();
		int choice = 1;
		
		while(choice != 0) {
			System.out.println("\n1)Insert numbers\n2)Search number\n3)Inorder traversal\n4)Range search\n5)Insert numbers(node)\n6)Search number(node)\n7)rangeSearch(node)\n8)Sorted Array search\n9)Sorted Array range search\n0)Exit\nChoice:");
			choice = scanner.nextInt();
			access = 0;
			switch(choice) {
				case 0:
					System.exit(0);	//exit
					break;
					
				case 1:				//insert numbers(array)
					int count = 0;
					startTime = System.nanoTime();
					try {
					    while (true) {
					    	int n = in.readInt(); //read int from file
					    	access += bst.insertNumber(n); //insert number in array, retuerns no of accesses
					    	count++;	//count words
					    }
					}catch (EOFException ignored) {
						
					}
					endTime = System.nanoTime();
				    float average = (float)access/count; //avg. accesses
					time = (float)(endTime-startTime)/1000000; //time in ms
				    System.out.printf("Avg. Accesses: %.2f" + "\nTime(ms): %.2f", average, time);
					break;
					
				case 2:			//search number(array)
					startTime = System.nanoTime();
					for(int i =0;i<100;i++) {	//search 100 random numbers
						access += bst.searchNumber(rand.nextInt());
					}
					endTime = System.nanoTime();
					average = (float)access/100;
					time = (float)(endTime-startTime)/1000000; //time in ms
				    System.out.printf("Avg. Accesses: %.2f" + "\nTime(ms): %.2f", average, time);
					break;
				
				case 3:
					bst.inorder(); 	//inorder traversal
					break;
				
				case 4:		//range search(array)
					for(int i =0;i<100;i++) { 
						int num = rand.nextInt();
						access += bst.range(num,num+100); //range search for rand,rand+100
					}
					average = (float)access/100;
				    System.out.printf("\nAvg. Accesses(k=100): %.2f\n" , average);
					for(int i =0;i<100;i++) {
						int num = rand.nextInt();
						access += bst.range(num,num+1000);	//range search for rand,rand+1000
					}
					average = (float)access/100;
				    System.out.printf("\nAvg. Accesses(k=1000): %.2f" , average);
					break;
					
				case 5: 	//insert numbers(nodes)
					count = 0;
					startTime = System.nanoTime();
					try {
					    while (true) {
					    	int n = in.readInt();
					    	access += nodesBST.insert(n);
					    	count++;
					    }
					}catch (EOFException ignored) {
						
					}
					endTime = System.nanoTime();
				    average = (float)access/count;
					time = (float)(endTime-startTime)/1000000; //time in ms
				    System.out.printf("Avg. Accesses: %.2f" + "\nTime(ms): %.2f", average, time);
					break;
					
				case 6:		//search number(nodes)
					startTime = System.nanoTime();
					for(int i =0;i<100;i++) {
						access += nodesBST.search(rand.nextInt());
					}
					endTime = System.nanoTime();
					average = (float)access/100;
					time = (float)(endTime-startTime)/1000000; //time in ms
				    System.out.printf("Avg. Accesses: %.2f" + "\nTime(ms): %.2f", average, time);
					break;
				
				case 7:		//range search(nodes)
					for(int i =0;i<100;i++) {
						int num = rand.nextInt();
						access += nodesBST.range(num,num+100); //range search for rand,rand+100
					}
					average = (float)access/100;
				    System.out.printf("\nAvg. Accesses: %.2f\n" , average);
				    
				    for(int i =0;i<100;i++) {
						int num = rand.nextInt();
						access += nodesBST.range(num,num+1000); //range search for rand,rand+1000
					}
					average = (float)access/100;
				    System.out.printf("\nNode Avg. Accesses: %.2f" , average);
					break;
					
				case 8:		//search number(sorted array)
					access = 0;
					int[] ret = bst.inorder(); //return inorder traversal result to int array
					startTime = System.nanoTime();
					for(int i =0;i<100;i++) {
						access += sortedArray.binarySearch(ret,rand.nextInt());
					}
					endTime = System.nanoTime();
					average = (float)access/100;
					time = (float)(endTime-startTime)/1000000; //time in ms
				    System.out.printf("Avg. Accesses: %.2f" + "\nTime(ms): %.2f", average, time);
					break;	
				
				case 9:		//range saerch(sorted array)
					access = 0;
					ret = bst.inorder(); //return inorder traversal result to int array
					for(int i =0;i<100;i++){
						int num = rand.nextInt();
						access += sortedArray.rangeSearch(ret, num, num+100); //range search for rand,rand+100
					}
					average = (float)access/100;
				    System.out.printf("\nAvg. Accesses(k=100): %.2f\n" , average);
				    
				    for(int i =0;i<100;i++) {
						int num = rand.nextInt();
						access += sortedArray.rangeSearch(ret, num, num+1000); //range search for rand,rand+1000
					}
					average = (float)access/100;
				    System.out.printf("Avg. Accesses(k=1000): %.2f" , average);
					break;
				default:
					System.out.println("Bad command");		
			}
		}
	}
}
