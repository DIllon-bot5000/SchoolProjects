/*
Author: Dillon Barr
Assigment: PA 12 - Anagrams
Date: Fall 2019
Course: CSC 210, Software Development
Purpose: This program reads in a text file containing a dictionary of words, a word to search 
for anagrams of and then potentially a length limit. The contents of the dictionary is cut down
to only contain words that can actually be anagrams of the input word. Using recursive backtracking
the program then finds all possible stacks of anagrams that can be generated from the input word.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeSet;

public class PA12Main {
    public static void main(String[] args) {
        LetterInventory bank = new LetterInventory(args[1]);
        TreeSet<String> dict = readInDict(args[0], bank);
        printAnagrams(dict, args[1], Integer.valueOf(args[2]), bank);
    }

    public static TreeSet<String> readInDict(String file,
            LetterInventory bank) {
        // This method reads in the dictionary text file and checks to see if
        // the
        // words being read in are possible anagrams for the input word,
        // represented
        // as the bank object. If the word is a potential match it is stored in
        // a TreeSet
        // so it is still alphabetical.
        TreeSet<String> dict = new TreeSet<String>();
        Scanner fileInput = null;

        try {
            fileInput = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (fileInput.hasNext()) {
            String word = fileInput.nextLine().toLowerCase();
            if (bank.contains(word)) {
                dict.add(word);
            }
        }
        fileInput.close();
        return dict;
    }

    public static void printAnagrams(TreeSet<String> dict, String word, int num,
            LetterInventory bank) {
        // This method exists solely to create a stack to hold the anagrams
        // found in the
        // recursive helper method and print out the required text for the
        // output.
        System.out.println("Phrase to scramble: " + word + "\n");
        Stack<String> list = new Stack<String>();
        System.out.println("All words found in " + word + ":");
        System.out.println(dict + "\n");
        System.out.println("Anagrams for " + word + ":");
        anagramHelper(bank, dict, num, list);
    }

    public static void anagramHelper(LetterInventory letters,
            TreeSet<String> dict, int num, Stack<String> list) {
        // This method uses recursive backtracking to search through the
        // possible
        // matches in the dictionary. If the word is a match it is added to the
        // stack and the
        // recursion continues. If a dead end is reached, or the limit reached,
        // the words are removed
        // from the stack and the recursion continues to explore other paths.
        for (String words : dict) {
            if (letters.isEmpty()) {
                System.out.println(list);
                return;
            } else {
                if (letters.contains(words) && num == 0 && !list.contains(words)) {
                    list.add(words);
                    letters.subtract(words);
                    anagramHelper(letters, dict, num, list);
                    list.remove(words);
                    letters.add(words);
                }
                if (letters.contains(words)) {
                    list.add(words);
                    letters.subtract(words);
                    if (list.size() <= num) {
                        anagramHelper(letters, dict, num, list);
                    }
                    list.remove(words);
                    letters.add(words);
                }

            }
        }
    }
}
