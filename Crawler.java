import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.regex.Pattern;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


/**
 * The Crawler implements web crawling by starting from a link
 * and then indexing those pages. It limits itself to a host pattern
 * expression, and only crawls to a specified depth
 */
public class Crawler {
    // This is the inverted index that all the words and PageSets are being placed into.
    InvertedIndex crawledIndex = new InvertedIndex();
    // HASHMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP!!!!!!!!
    // For real though this is to hold all the links and pages that we have already visited.
    HashMap<String, Page> crawledPages = new HashMap<String, Page>();
    // This keeps track of what to name the invertedIndex file when we're done crawling
    String filename;

    /**
     * Create a web crawler that saves the inverted index to the given
     * filename
     *
     * @param filename The file to save the inverted index to
     */

    // Constructor
    public Crawler(String filename) {
        this.filename = filename;
    }

    /**
     * Visit and index the page at the link given. Recursively index the pages
     * given by links on the page up to a given depth from the starting
     * point. Return an inverted index that was created by the indexing.
     *
     * @param link The link to start from.
     * @param hostPattern The java.util.regex.Pattern to limit indexing to
     * @param depth How many levels to follow links
     * @return An inverted index formed by indexing the pages
     */

     // This class gets recursively called to crawl a website given a starting link, depth to crawl to,
     // and a hostPattern to constrain it to a single website.
    public InvertedIndex visit(String link, String hostPattern, int depth) {

        // This if statement check the HASHMAAAAAAAAAP to see if the link currently being visited
        // has been indexed already
        if (crawledPages.containsKey(link)) {
            // If the link has been indexed already it gets the page from the HashMap and
            // immediately returns the invertedIndex
            crawledPages.get(link).increaseRank();
            return this.crawledIndex;
        }

        // The code will only make it to this section if a link has not been visited already

        // A new Page will get created for the link, and the link and Page get added to the 
        // HashMap that keeps track of already visited links
        Page newPage = new Page(link);
        crawledPages.put(link, newPage);

        // This try-catch block is meant to take in every word from a webpage as an array and
        // add them to the Inverted Index
        try {
            // We were given this class and function by the professor
            // I have no idea how it works
            PageDigest digestWords = new PageDigest(link);
            String[] words = digestWords.getWords();

            // Make a new TextCleaner object to read through all the words that we got from the webpage using the
            // PageDigest class and clean them into a new Array
            TextCleaner cleaner = new TextCleaner();
            String[] cleanWords = cleaner.clean(words);

            // ! This was for bugfixing
            /*
            System.out.println("[");
            for (String word : cleanWords) {
                System.out.println(word);
            }
            System.out.println("]");
            */
            // ! This was for bugfixing

            // Call the add function on the invertedIndex object to put all the clean words and new Page in
            crawledIndex.add(cleanWords, newPage);
        }
        catch(IOException e) {System.out.println(e);}

        // Decrement depth to limit how deep we crawl
        depth -= 1;
        
        // Check the depth to see if we should keep crawling
        if (depth >= 0) {
            // This try-catch block is meant to take every link on a webpage into an ArrayList, and recursively
            // call the visit function on each one.
            try {
                // Use PageDigest again to get all the links from a webpage
                // Still have no idea how it works
                PageDigest digestLinks = new PageDigest(link);
                ArrayList<String> links = digestLinks.getLinks();

                // Iterate through all the links given by PageDigest
                for (String newLink : links) {
                    // Check if a link matches the host pattern
                    if (Pattern.matches(hostPattern, newLink)) {
                        // When we find a link matching the hostPattern we get the baseURL from it
                        String baseNewLink = PageDigest.toBaseUrl(newLink);
                        // Then we recursively call the visit function
                        visit(baseNewLink, hostPattern, depth);
                    }
                }   
            }
            catch(IOException e) {System.out.println(e);}
        }
        return this.crawledIndex;
    }

    /**
     * Save the inverted index to disk. The filename to save to was
     * given in the constructor
     */
    public String getFileName(){
        return this.filename;
    }
    public InvertedIndex getInvIndex(){
        return this.crawledIndex;
    }

    public void saveInvertedIndex() throws IOException {
        InvertedIndex invertedIndex = this.getInvIndex();
        String filename = this.getFileName();
        
        // Writes the contents of the inverted index to the file name
        // given in the constructor
        FileOutputStream fs = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fs);
        out.writeObject(invertedIndex);
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("Crawling...");
        Crawler crawler = new Crawler("inverted_index.ser");
        crawler.visit("https://wwu.edu", ".*wwu.edu", 3);
        crawler.saveInvertedIndex();
    }
}
