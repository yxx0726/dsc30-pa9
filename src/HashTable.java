/*
 * NAME: Xin Yu
 * PID: A14494949
 */

/**
 * Title: HashTable.java
 * Description: File that can create a hash table.
 */

import javax.xml.soap.Node;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A class that implements a hash table and its associated methods
 */
public class HashTable implements IHashTable {
	private LinkedList<String>[] array;//Array that stores linkedlists
	private int numElem;//Number of element stored in the hash table
	private int expand;//Times that the table is expanded
	private int collision;//Times of collisions occurs
	private String statsFileName;
	private boolean printStats = false;
	private double factor;
	private LinkedList[] table;
	private int longest;
	private int numlongest = 0;
	private PrintStream ps;
	private int max = 2/3;
	
	/**
	 * Constructor for hash table
	 */
	@SuppressWarnings("unchecked")
	public HashTable(int size) {
		//TODO
		this.numElem = 0;
		this.collision = 0;
		this.expand = 0;
		this.statsFileName = " ";
		array = new LinkedList[size];
		for (int i = 0; i < size; i++) {
			array[i] = new LinkedList();
		}
	}
	
	@SuppressWarnings( "unchecked" )
    public HashTable(int size, String fileName){
		//TODO
		this.statsFileName = fileName;
		this.numElem = 0;
		this.collision = 0;
		this.expand = 0;
		array = new LinkedList[size];
		this.printStats = true;
		for (int i = 0; i < array.length; i++) {
			array[i] = new LinkedList<>();
		}
		try {
			ps = new PrintStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert the string value into the hash table
	 * @param value value to insert
	 * @return true if the value was inserted, false if the value was already
	 * present
	 */
    @Override
	public boolean insert(String value) {
		//TODO
		if (value == null){
			throw new NullPointerException();
		}
		if(status()){
			rehash();
			this.printStatistics();
			expand++;
		}

		int hashValue = hashString(value);
		if(array[hashValue].contains(value)){
			return false;
		}
		if(array[hashValue].size()!=0){
			collision++;
		}

		array[hashValue].add(value);
		numElem++;
		return true;
	}

	/**
	 * Delete the given value from the hash table
	 * @param value value to delete
	 * @return true if the value was deleted, false if the value was not found
	 */
	@Override
	public boolean delete(String value) {
		//TODO
		if (value == null) {
			throw new NullPointerException();
		}
		int val = hashString(value);
		if (!array[val].contains(value)) {
			return false;
		}
		LinkedList<String> temp = new LinkedList<>();
		for (int i = 0; i < array[val].size();i++) {
			if (array[val].get((i)).equals(value)) {
				for(int k = 0; k < i; k++){
					temp.add(array[val].get(k));
				}
				for (int j = i; j < array[val].size() - 1; j++) {
					temp.add(array[val].get(j+1));
				}
				array[val] = temp;
				numElem--;
				return true;
			}
		}
		return true;
	}

	/**
	 * Check if the given value is present in the hash table
	 * @param value value to look up
	 * @return true if the value was found, false if the value was not found
	 */
	@Override
	public boolean lookup(String value) {
		if (value == null) {
			throw new NullPointerException();
		}
		int hashValue = hashString(value);
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].size(); j++) {
				if (array[i].get(j).equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Print the contents of the hash table to stdout.
	 */
	@Override
	public void printTable() {
		//TODO
		for (int i = 0; i < array.length; i++){
			String out = Integer.toString(i)+": ";
			for (int j = 0; j < array[i].size() - 1; j++){
				out = out + array[i].get(j) + ",";
			}
			if(array[i].size() > 0) {
				out = out + array[i].get(array[i].size() - 1);
			}
			System.out.println(out);
		}
	}
	
	@Override
	public int getSize() {
		//TODO
		return numElem;
	}


	private int hashString(String value) {
		//TODO
		int hash = 0;
		for (int i = 0; i < value.length(); i++) {
			hash = (27 * hash + value.charAt(i)) % array.length;
		}
		return hash;
	}
	
	/**
	 * Expands the array and rehashes all values.
	 */
	@SuppressWarnings( "unchecked" )
    private void rehash() {
	    //TODO
		LinkedList<String>[] newArray = new LinkedList[array.length * 2];
		for (int m = 0; m < newArray.length; m++) {
			newArray[m] = new LinkedList<>();
		}
		for (int i = 0; i < array.length;i++){
			for (int j = 0; j < array[i].size();j++) {
				int value = 0;
				for (int k = 0; k < array[i].get(j).length();k++){
					int temp = array[i].get(j).charAt(k);
					value = (value * 27 + temp) % newArray.length;
				}
				value = value % newArray.length;
				newArray[value].add(array[i].get(j));
			}
		}
		array = newArray;
	}
	private int findlongest() {
		if (numElem == 0) {
			return 0;
		}
		longest = array[0].size();
		for(int i = 1;i<array.length;i++){
			if(longest<array[i].size()){
				longest = array[i].size();
			}
		}
		for(int i = 1;i<array.length;i++){
			if(longest==array[i].size()){
				numlongest ++;
			}
		}
		return numlongest;
	}
	
	/**
	 * Print statistics to the given file.
	 * @return True if successfully printed statistics, false if the file
	 *         could not be opened/created.
	 */
	//@Override
	public boolean printStatistics() {
		//TODO
		PrintStream ps = null;
		try {
			ps = new PrintStream(statsFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			ps.print(expand + " resizes, load factor " + (double) Math.round
					(factor * 100) / 100 + ", " + collision + " collisions, "
					+ findlongest() + " longest chain" + "\n");
			ps.flush();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	private  boolean status(){
		if((double)numElem / (double) array.length >= factor){
			return true;
		}
		return false;
	}

}
