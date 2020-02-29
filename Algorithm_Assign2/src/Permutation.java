import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args)
    {
        String in = args[0];
        int k = Integer.valueOf(in);
        RandomizedQueue<String> myRandQueue = new RandomizedQueue<String>();

        // int count = 0;
        while (!StdIn.isEmpty())
        {
            String token = StdIn.readString();
            myRandQueue.enqueue(token);
            /*
            if (k==0){
                continue;
            }
            if (myRandQueue.size()==k)
            {
                if (StdRandom.uniform(count)>0) // whether enqueue or not (not enqueue prob p = 1/count)
                {
                    myRandQueue.dequeue();
                    myRandQueue.enqueue(token);
                }
            }
            else {
                myRandQueue.enqueue(token);
            }
            count++;
            */
        }

        int cnt = 0;
        for (String s: myRandQueue)
        {
            // StdOut.println(s);

            if (cnt>=k)
            {
                break;
            }
            else {
                StdOut.println(s);
                cnt++;
            }

        }
    }
}
