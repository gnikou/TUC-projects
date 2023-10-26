package domes_project3;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainClass {

	public static void main(String[] args) throws IOException {
		int initPages = 100, pageSize = 10;
		LinearHashing Hash1 = new LinearHashing(pageSize, initPages, (float) 0.5);
		LinearHashing Hash2 = new LinearHashing(pageSize, initPages, (float) 0.8);
		BinarySearchTree bst = new BinarySearchTree();
		float insertAccess = 0;
		float searchAccess = 0;
		float deleteAccess = 0;
		float insertAccess2 = 0;
		float searchAccess2 = 0;
		float deleteAccess2 = 0;
		float bstInsertAccess = 0;
		float bstSearchAccess = 0;
		float bstDeleteAccess = 0;
		int index = 0;
		
		File file = new File("testnumbers_10000_BE.bin");
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fin);
		DataInputStream din = new DataInputStream(bin);
		int count = (int) (file.length() / 4);
		int[] nums = new int[count];
		for (int r = 0; r < count; r++) {
		    nums[r] = din.readInt();
		}
		din.close();
		
		String titleTemplate = "%-10s %15s %15s %15s %15s %15s %15s %15s %15s %15s%n";
	    String template = "%-10d |%13.2f |%13.2f |%15.2f |%15.2f |%15.2f |%15.2f |%15.2f |%15.2f |%15.2f%n";
	    System.out.printf(titleTemplate, "N", "Insert(0.5)", "Search(0.5)", "Delete(0.5)", "Insert(0.8)", "Search(0.8)", "Delete(0.8)", "Insert(bst)", "Search(bst)", "Delete(bst)");
	    
		for(int n=0;n<100;n++) {
			insertAccess = 0;
			searchAccess = 0;
			deleteAccess = 0;
			insertAccess2 = 0;
			searchAccess2 = 0;
			deleteAccess2 = 0;
			bstInsertAccess = 0;
			bstSearchAccess = 0;
			bstDeleteAccess = 0;
			for(int j=0;j<100;j++) {			
				insertAccess += Hash1.insertKey((nums[100*n+j]));
				insertAccess2 += Hash2.insertKey((nums[100*n+j]));
				bstInsertAccess += bst.insert((nums[100*n+j]));
			}
			
			int size = 100;
	        ArrayList<Integer> list = new ArrayList<Integer>(size);
	        for(int p = 0; p < size; p++) {
	            list.add(p);
	        }
	        
	        
			for(int j=0;j<50;j++) {
		        Random rand = new Random();
		        index = rand.nextInt(list.size());
		        searchAccess+=Hash1.searchKey(nums[100*n+list.get(index)]); 
		        searchAccess2+=Hash2.searchKey(nums[100*n+list.get(index)]); 
		        bstSearchAccess += bst.search(nums[100*n+list.get(index)]); 
		        list.remove(index);
			}

			
			
			
			list = new ArrayList<Integer>(size);
			for(int p = 0; p < size; p++) {
				list.add(p);
			}
			int c=0;
			
			while((Hash1.getKeysNum() != (n+1)*50) && (Hash2.getKeysNum() != (n+1)*50)){
				int del = Hash1.getKeysNum();
				int del1 = Hash2.getKeysNum();
				
		        Random rand = new Random();
		        if(list.size() < 1){
		        	list = new ArrayList<Integer>(size);
					for(int p = 0; p < size; p++) {
						list.add(p);
					}
		        }
				index = rand.nextInt(list.size());

		        if(Hash1.getKeysNum() != (n+1)*50) {deleteAccess+=Hash1.deleteKey(nums[100*n+c]);}
		        if(Hash2.getKeysNum() != (n+1)*50) {deleteAccess2+=Hash2.deleteKey(nums[100*n+list.get(index)]);}
		        if(c<50) {bstDeleteAccess += bst.deleteKey(nums[100*n+list.get(index)]);}
		        c++;

		        if((del - Hash1.getKeysNum() !=0) || (del1 - Hash2.getKeysNum() != 0))
		        	list.remove(index);
		    }
			System.out.printf(template, (n+1)*100, insertAccess/100, searchAccess/50, deleteAccess/50, insertAccess2/100, searchAccess2/50, deleteAccess2/50, bstInsertAccess/100, bstSearchAccess/50, bstDeleteAccess/50);

		}
	}
}
