import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implements an inverted index. This class maps a words to a PageSet.
 */
public class InvertedIndex implements Serializable {
    /**
     * Create an inverted index
     */
    protected LinkedList<Node> indexList;

    public InvertedIndex() {
        this.indexList = new LinkedList<Node>();
    }

    /**
     * Lookup a word in the index. This operation should be fast - O(1).
     *
     * @param word The word to look up
     * @return The PageSet (set of pages) that the word occurs in
     */
    public PageSet lookup(String word) {
        //iterate thru indexlist, search for a matching word node
        for(int i=0; i < indexList.size(); i++){
            if(this.indexList.get(i).getWord() == word){
                return this.indexList.get(i).getPageSet();
            }
        }
        return null;
    }

    /**
     * Add to the inverted index. The array of words are associated to the
     * given Page. The effect is that the Page is added to the PageSet for
     * each of the words.
     *
     * @param words The array of words that belong on the given Page
     * @param page The page that the words are on.
     */
    public void add(String[] words, Page page) {
        //step 1: iterate thru indexList and add any missing words
        for(int i=0; i < words.length; i++){
            //iterate thru every word in words[]
            //assume each time that the word is not contained
            boolean contains = false;
            int j=0;
            while(j < indexList.size() && !contains){
                //iterate thru every node in indexList
                if(this.indexList.get(j).getWord() == words[i]){
                    //if the word is found, set contains to true to break the while loop
                    contains = true;
                    //add the page to the pageSet of the word
                    this.indexList.get(j).addPage(page);
                }
                j++;
            }
            if (!contains) {
                //if the word is not contained within indexList, add it and the page assignment
                this.indexList.add(new Node(words[i], new PageSet()));
                this.indexList.get(i).addPage(page);
            }
        }
    }

    /**
     * The toString function lets you print out an InvertedIndex in a
     * friendly way. This will help you debug.
     */
    public String toString() {
        for(int i=0; i < indexList.size(); i++){
            //print out the word and get the list of associated pages

            System.out.print("[" + indexList.get(i).getWord() + "] - {");
            PageSet tmp = indexList.get(i).getPageSet();

            for (int j=0; j < indexList.get(i).getPageSet().size(); j++){
                //print the page link

                System.out.print(tmp.getPage(j).getLink());
                System.out.print(", ");
            }
        }
        System.out.println();
        return "";
    }

    public static void main(String[] args){
        InvertedIndex a = new InvertedIndex();

    }
}

class Node{
    String word;
    PageSet pageSet;
    Node next;

    Node(String word, PageSet set){
        this.word = word;
        this.pageSet = set;
    }

    Node(String word, PageSet set, Node next){
        this.word = word;
        this.pageSet = set;
        this.next = next;
    }

    public void setNext(Node node){
        this.next = node;
    }
    public String getWord(){
        return this.word;
    }
    public PageSet getPageSet() {
        return this.pageSet;
    }

    public void addPage(Page page){
        this.pageSet.add(page);
    }
}

/*
TODO
- Construct an inverted index as a LinkedList of nodes, each node containing a pageset which can be added to and a keyword
- "add" function:
  1. iterate thru the LinkedList to make sure the words exist, if not, add them
  2. iterate thru the LinkedList and add the given page to the pageset in each node
  3. return void
- "lookup" function:
  1. Iterate thru the LinkedList to find the given word
  2. if the word exists, return the associated pageset, if the word does not exist, return null
 */