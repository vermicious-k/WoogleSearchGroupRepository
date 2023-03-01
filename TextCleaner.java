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
        ArrayList<String> stop_words_list = returnStopWords();
        String inp_word = word.toLowerCase(); // makes lower case string
        StringBuilder word_builder = new StringBuilder();
        for(int i = 0; i < inp_word.length(); i ++){
            if(Character.isAlphabetic(inp_word.codePointAt(i))){
                word_builder.append(inp_word.charAt(i));
            }
        }
        inp_word = word_builder.toString();
        PorterStemmer stemmer = new PorterStemmer();
        String result = stemmer.stem(inp_word);    // result should be "run"
        if(stop_words_list.contains(result)){
            return "";
        }
        return result;
    }
    

    public ArrayList<String> returnStopWords(){
        ArrayList<String> stop_words = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(new File("Lib/stopwords.txt"));
            while(scanner.hasNextLine()){
                stop_words.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }
        return stop_words;
    }


    /**
     * Clean an array of words by applying clean to each word.
     *
     * @param words An array of words to clean
     * @return A new array of words which are all cleaned.
     */
    public String[] clean(String[] words) {
        for(int i = 0; i < words.length; i++){
            words[i] = clean(words[i]);
        }
        return words;
    }
}
