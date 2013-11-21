package jgrep;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Kmp {
	
	public static void main(String[] args) {
		if (args.length < 2)
			return;
		
		String word = new String();
		for (int i = 0; i < args.length-1; i++) // args[0] through args[length-2] are what we are searching for
			word += args[i];
		
		int[] partialMatchTable = buildPartialMatchTable(word);
		
		try {
			Scanner fileScanner = new Scanner(new File(args[args.length-1])); // file = last arg
			int lineNumber = 1;
			boolean matchFound = false;
			
			while(fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				int matchIndex = KmpSearch(line, word, partialMatchTable);
				
				if (matchIndex++ != -1) {// increment 0 indexed matchIndex to get actual column number
					System.out.println("Match found at line " + lineNumber + " column " + matchIndex);
					matchFound = true;
				}
				
				lineNumber++;
			}
			
			if (!matchFound)
				System.out.println("No match found!");
		} catch (IOException e) {
			System.out.println(e);
		}

	}
	
	public static int KmpSearch(String text, String word, int[] partialMatchTable) {
		
		if (word.length() > text.length())
			return -1;
		
		if (word.length() > partialMatchTable.length)
			return -1;
		
		int m = 0; // index of the start of a possible 'M'atch in the text
		int i = 0; // the current character in word we are comparing
		
		while (m + i < text.length()) {
			if (text.charAt(m+i) == word.charAt(i)) { // a character matches
				if (i == word.length() - 1) // all characters in word match part of text
					return m;
				i++;
			} else {
				m = m + i - partialMatchTable[i]; // partialMatchTable[i] is the amount of backtracking
				if (partialMatchTable[i] > -1)
					i = partialMatchTable[i];
				else 
					i = 0;
			}
		}
				
		return -1;
	}
	
	public static int[] buildPartialMatchTable(String word) {
		int[] table; 
		if (word.length() >= 2)
			table = new int[word.length()];
		else 
			table = new int[2];
		
		// first two values are always the same
		table[0] = -1;
		table[1] = 0;
		
		int pos = 2; // current position of table we are computing
		int cnd = 0; 
		
		while (pos < table.length) {
			if (word.charAt(pos-1) == word.charAt(cnd)) { // proper prefix == proper suffix
				cnd++;
				table[pos] = cnd;
				pos++;
			} else if (cnd > 0) {
				cnd = table[cnd];
			} else {
				table[pos] = 0;
				pos++;
			}
		}
		
		return table;
	}
}