/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxSum = -1;
        String res = null;
        for (String str1:nouns) {
            int sum = 0;
            for (String str2:nouns) {
                int dis = wordNet.distance(str1, str2);
                sum += dis;
                StdOut.print(str1+" "+str2+":"+dis+"\n");
            }
            if (sum > maxSum) {
                res = str1;
                maxSum = sum;
            }
        }
        return res;
    }
    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));

        }
    }
}
