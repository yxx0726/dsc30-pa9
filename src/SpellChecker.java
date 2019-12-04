/**
 * NAME:
 * PID:
 */

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Reads and stores a dictionary from a file, given as command-line argument,
 * in a HashTable, then reads a list of words from another file, given as 
 * command-line argument, and uses the dictionary to determine which words
 * from the list are wrong/misspelled (not contained in the dictionary),
 * providing all possible corrections to the incorrect words (based on the
 * dictionary).
 *
 * @version 1.0
 * @author Thiago Goncalves Marback.
 * @since 2016-03-05
 */
public class SpellChecker implements ISpellChecker {

    private HashTable dictionary;
    private int tableSize = 6;
    private int aIndex = 97;
    private int zIndex = 122;

    /**
     * Reads the dictionary from a Reader input, and stores it in the
     * dictionary HashTable.
     *
     * @param reader The input where the dictionary should be read from.
     */
    @Override
    public void readDictionary( Reader reader ) {
        //TODO

        Scanner scan = new Scanner(new BufferedReader(reader));
        dictionary = new HashTable(tableSize);
        while (scan.hasNext()) {
            String element = scan.next();
            dictionary.insert(element);
        }
    }
    
    /**
     * Checks the possible corrections for a word that can be made by changing
     * a single character in the String.
     *
     * @param word Word to be corrected.
     * @return List containing all possible corrections made by changing one
     *          character.
     */
    private LinkedList<String> checkWrongLetter( String word ) {
        //TODO
        LinkedList<String> result = new LinkedList<>();
        for ( int i = 0; i < word.length(); i++){
            for(int j = aIndex; j <= zIndex; j++){
                String temp = word.substring(0,i)+(char)j+word.substring(i+1);
                if (dictionary.lookup(temp)){
                    result.add(temp);
                }
            }
        }
        return result;
    }
    
    /**
     * Checks the possible corrections for a word that can be made by deleting
     * one of the chars of the String.
     *
     * @param word Word to be corrected.
     * @return List containing all possible corrections made by deleting one
     *          character.
     */
    private LinkedList<String> checkDeletedLetter( String word ) {
        //TODO
        LinkedList<String> result = new LinkedList<>();
        String first = word.substring(1);
        if(dictionary.lookup(first)){
            result.add(first);
        }
        for ( int i = 1; i < word.length(); i++){
            String temp = word.substring(0,i)+word.substring(i+1);
            if (dictionary.lookup(temp)){
                result.add(temp);
            }
        }
        return result;
    }
    
    /**
     * Checks the possible corrections for a word that can be made by inserting
     * one character in the String.
     *
     * @param word Word to be corrected.
     * @return List containing all possible corrections made by inserting one
     *          character.
     */
    private LinkedList<String> checkInsertedLetter( String word ) {
        //TODO
        LinkedList<String> result = new LinkedList<>();
        for ( int i = 0; i <= word.length(); i++){
            for(int j = aIndex; j <= zIndex; j++){

                String temp = word.substring(0,i)+(char)j+word.substring(i);

                if (dictionary.lookup(temp)){
                    result.add(temp);
                }
            }
        }
        return result;
    }
    
    /**
     * Checks the possible corrections for a word that can be made by
     * transposing two adjacent characters in the String.
     *
     * @param word Word to be corrected.
     * @return List containing all possible corrections made by transposing
     *          two characters.
     */
    private LinkedList<String> checkTransposedLetter( String word ) {
        if(word.length() < 2){
            return null;
        }
        LinkedList<String> result = new LinkedList<>();
        if(word.length() == 2){

            String comb = word.substring(1) + word.substring(0, 1);
            if(dictionary.lookup(comb)){
                result.add(comb);
            }
        }
        else {
            String s1 = word.substring(1,2)+word.substring(0,1)+word.substring(2);
            if (dictionary.lookup(s1)) {
                result.add(s1);
            }
            for (int i = 0; i <= word.length() - 3; i++) {
                String s2 = word.substring(0, i+1) + word.charAt(i + 2) + word.charAt(i + 1) +
                        word.substring(i + 3);
                if (dictionary.lookup(s2)) {
                    result.add(s2);
                }
            }
        }
        return result;
    }
    
    /**
     * Checks the possibility of creating two correct words by inserting a 
     * space at some point in the incorrect word.
     * 
     * @param word Word to be corrected.
     * @return List containing all possible corrections made by splitting
     *         into two words.
     */
    private LinkedList<String> checkInsertSpace( String word ) {
        //TODO
        LinkedList<String> result = new LinkedList<>();
        for (int i = 0; i < word.length() - 1; i++){
            String part1 = word.substring(0, i + 1);
            String part2 = word.substring(i + 1);
            if (dictionary.lookup(part1) && dictionary.lookup(part2)) {
                result.add(part1 + " " + part2);
            }
        }
        return result;
    }

    /**
     * Checks if a word is correct (exists in the dictionary), and if it
     * isn't, evaluates possible corrections for the word.
     * Possible errors: one wrong letter; one inserted letter; one deleted
     * letter; two transposed adjacent letters.
     * Possible corrections: changing a single character in the word; deleting
     * a letter in the word; inserting a letter in the word; transposing two
     * adjacent letters.
     *
     * @param word Word to be checked.
     * @return null if the word is correct (is in the dictionary). If it is
     *          wrong but no possible corrections were found, returns an
     *          empty array. Else, returns an array with all possible
     *          corrections found.
     */
    @Override
    public String[] checkWord( String word ) {
        LinkedList<String> all = new LinkedList<>();
        LinkedList<String> temp;
        if(dictionary.lookup(word)) {
            return null;
        }
        else{
            temp = checkWrongLetter(word);
            for(int i = 0; i < temp.size(); i++){
                all.add(temp.get(i));
            }

            temp = checkInsertedLetter(word);
            for(int i = 0; i < temp.size(); i++){
                if(!all.contains(temp.get(i))) {
                    all.add(temp.get(i));
                }
            }

            temp = checkDeletedLetter(word);
            for(int i = 0; i < temp.size();i++){

                if(!all.contains(temp.get(i))) {
                    all.add(temp.get(i));
                }
            }

            temp = checkTransposedLetter(word);
            for(int i = 0; i < temp.size();i++){

                if(!all.contains(temp.get(i))) {
                    all.add(temp.get(i));
                }
            }

            temp = checkInsertSpace(word);
            for(int i = 0; i < temp.size();i++){
                if(!all.contains(temp.get(i))) {
                    all.add(temp.get(i));
                }
            }

            String[] result =  new String[all.size()];
            for (int i = 0; i < result.length;i++){
                result[i] = all.get(i);
            }
            return result;
        }
    }

    /**
     * Reads a dictionary from a given text file, storing it into a HashTable.
     * Then, reads a list of words from another given text file, checking if
     * each one is correct (exists in the dictionary). For each word that
     * isn't correct (does not exist in the dictionary), prints the word,
     * followed by all the possible corrections found (if any).
     *
     * @param args Arguments received on startup. Should only contain two:
     * the file where the dictionary is contained, and the file where the 
     * list of words to be checked is contained.
     */
    public static void main( String[] args ) {
        SpellChecker checker = new SpellChecker();
        File dictionary = new File( args[ 0 ] );
        try {
            Reader reader = new FileReader( dictionary );

            //TODO - call readDictionary with the given reader.
            checker.readDictionary(reader);

        } catch ( FileNotFoundException e ) {
            System.err.println( "Failed to open " + dictionary );
            System.exit( 1 );
        }

        File inputFile = new File( args[ 1 ] );
        try {
            Scanner input = new Scanner( inputFile ); // Reads list of words

            //TODO - go through each word from Scanner
            while (input.hasNext()) {

                String word = input.next().toLowerCase();

                if(checker.dictionary.lookup(word)){
                    System.out.println(word+": ok");
                }

                else if(checker.checkWord(word).length == 0){
                    System.out.println(word+": not found");
                }
                else{
                    String result = "";

                    String[] temp = checker.checkWord(word);
                    for (int i = 0; i < temp.length - 1;i++){
                        result += temp[i]+", ";
                    }
                    result += temp[temp.length-1];
                    System.out.println(word+": " + result);
                }
            }

        } catch ( FileNotFoundException e ) {
            System.err.println( "Failed to open " + inputFile );
            System.exit( 1 );
        }
    }

}
