/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordNet {
    private class Synset {
        public int id;
        public List<String> words;

        public Synset(int idPara, Iterable<String> wordsPara) {
            id = idPara;
            words = new ArrayList<String>();
            for (String s:wordsPara) words.add(s);
        }
    }

    List<Synset> synsetList;
    List<String> wordsList;
    List<Integer> synsetId;
    Digraph graph;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        // NULL argument
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Synsets file name or hypernyms file name is NULL!");
        }

        synsetList = new ArrayList<>();
        wordsList = new ArrayList<>();
        synsetId = new ArrayList<>();

        // Read file
        In synsetsFileIn = new In(new File(synsets));
        String line;
        while ((line = synsetsFileIn.readLine()) != null) {
            String [] splitted = line.split(",");
            int id = Integer.parseInt(splitted[0]);
            String [] synWords = splitted[1].split(" ");
            for (String s:synWords) {
                wordsList.add(s);
                synsetId.add(id);
            }
            synsetList.add(new Synset(id, Arrays.asList(synWords)));
        }

        // judge rooted DAG
        In hypernymsFileIn = new In(new File(hypernyms));
        graph = new Digraph(synsetList.size());
        while ((line = hypernymsFileIn.readLine()) != null) {
            String [] ids = line.split(",");
            int id = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++) {
                graph.addEdge(id, Integer.parseInt(ids[i]));
            }
        }

        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle()) throw new IllegalArgumentException("Cyclic graph");
        int rootNum = 0;
        for (int i = 0; i < graph.V(); i++)
            if (graph.outdegree(i) == 0) rootNum += 1;
        if (rootNum != 1) throw new IllegalArgumentException("Not only 1 root");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordsList;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordsList.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        // not a WordNet noun
        if (!wordsList.contains(nounA)) throw new IllegalArgumentException("nounA is no in List");
        if (!wordsList.contains(nounB)) throw new IllegalArgumentException("nounB is no in List");
        int groupA = synsetId.get(wordsList.indexOf(nounA));
        int groupB = synsetId.get(wordsList.indexOf(nounB));
        StdOut.print(groupA + " " + groupB+ " ");
        SAP sap = new SAP(graph);
        return sap.length(groupA, groupB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        // not a WordNet noun
        if (!wordsList.contains(nounA)) throw new IllegalArgumentException("nounA is no in List");
        if (!wordsList.contains(nounB)) throw new IllegalArgumentException("nounB is no in List");

        int groupA = synsetId.get(wordsList.indexOf(nounA));
        int groupB = synsetId.get(wordsList.indexOf(nounB));
        SAP sap = new SAP(graph);
        return String.join(" ", synsetList.get(sap.ancestor(groupA, groupB)).words);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets6.txt", "hypernyms6TwoAncestors.txt");
        StdOut.print(wordNet.distance("a", "c"));
    }
}
