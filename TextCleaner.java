import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//import PorterStemmer.java ;

/**
 * This class cleans up text with a combination of lowercasing,
 * non-alpha removal, stop word removal and stemming.
 */
public class TextCleaner {
    /**
     * Create a TextCleaner object
     */
    public TextCleaner() {
    }

    /**
     * Clean a word. Return the cleaned word, or "" if it was a stop word
     *
     * @param word The word to clean
     * @return The cleaned word
     */
    public String clean(String word) {
        // This function calls the returnStopWords method to get a usable ArrayList of stopwords
        ArrayList<String> stop_words_list = returnStopWords();
        // We call the String function toLowerCase on the word, and then create a StringBuilder
        String inp_word = word.toLowerCase(); // makes lower case string
        StringBuilder word_builder = new StringBuilder();
        
        // We iterate through the word, and check each Character to see if it is alphabetic
        for(int i = 0; i < inp_word.length(); i ++){
            // When a character is alphabetic, it gets added to the StringBuilder
            if(Character.isAlphabetic(inp_word.codePointAt(i))){
                word_builder.append(inp_word.charAt(i));
            }
        }
        // The StringBuilder is set back to a String
        inp_word = word_builder.toString();
        
        // Honestly I have no idea how this works
        PorterStemmer stemmer = new PorterStemmer();
        String result = stemmer.stem(inp_word);    // result should be "run"
        
        // We see if the stemmed word is listed in our ArrayList of stopwords, and if it is we return an empty String
        if(stop_words_list.contains(result)){
            return "";
        }
        return result;
    }
    
    // We were given a stopword.txt file that has a list of words that would not be applicable in most searches. (The, about pronouns, etc.)
    // This function is to read in that txt file and return it as an ArrayList
    public ArrayList<String> returnStopWords(){
        ArrayList<String> stop_words = new ArrayList<String>();
        
        // Using a Scanner to read a file requires a try catch block
        try {
            // Here we make a new Scanner for the stopwords.txt file
            Scanner scanner = new Scanner(new File("Lib/stopwords.txt"));
            // This iterates through the scanner, reading every line into the ArrayList.
            while(scanner.hasNextLine()){
                stop_words.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }
        // The ArrayList of stopwords is then returned.
        return stop_words;
    }


    /**
     * Clean an array of words by applying clean to each word.
     *
     * @param words An array of words to clean
     * @return A new array of words which are all cleaned.
     */
    
    // This version of the clean method is meant to clean an array of words, rather than a single word.
    public String[] clean(String[] words) {
        // It iterates through the given array, and calls clean on each item before placing back into the array at the same index.
        for(int i = 0; i < words.length; i++){
            words[i] = clean(words[i]);
        }
        return words;
    }
}
