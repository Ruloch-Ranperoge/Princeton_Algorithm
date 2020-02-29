import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
public class RandomizedQueue<Item> implements Iterable<Item> {
    private int num;
    private Item [] arr;
    private int len;
    public RandomizedQueue()                 // construct an empty randomized queue
    {
        num = 0;
        len = 4;
        arr = (Item []) new Object[4];
    }
    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return num == 0;
    }
    public int size()                        // return the number of items on the randomized queue
    {
        return num;
    }
    public void enqueue(Item item)           // add the item
    {
        if (item == null)
        {
            throw new java.lang.IllegalArgumentException("item can not be null");
        }
        if (num >= len) // new  doubled array
        {
            Item [] tmp = (Item []) new Object[len * 2]; // double
            for (int i = 0; i < num; i++) // copy
            {
                tmp[i] = arr[i];
            }
            arr = tmp;
            arr[num++] = item;
            len = len * 2;
        }
        else //capacity is enough
        {
            arr[num++] = item;
        }
    }
    public Item dequeue()                    // remove and return a random item
    {
        if (num == 0)
        {
            throw new java.util.NoSuchElementException("Randomized queue is empty");
        }
        if (num == 1)
        {
            num --;
            return arr[0];
        }
        int idx = StdRandom.uniform(num);
        Item tmp = arr[idx];
        arr[idx] = arr[--num];

        if (num < len/2)  // shrink
        {
            Item [] tmparr = (Item []) new Object[len / 2];
            for (int i = 0; i < num; i++)
            {
                tmparr[i] = arr[i];
            }
            arr = tmparr;
            len = len / 2;
        }

        return tmp;
    }
    public Item sample()                     // return a random item (but do not remove it)
    {
        if (num == 0)
        {
            throw new java.util.NoSuchElementException("Randomized queue is empty");
        }
        int idx = StdRandom.uniform(num);
        return arr[idx];
    }
    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        int [] order = new int[num];
        for (int i = 0; i < num; i++) order[i] = i;
        StdRandom.shuffle(order);
        Item [] listitem = (Item []) new Object[num];

        for (int i = 0; i < num; i++)
        {
            listitem[i] = arr[order[i]];
        }
        Iterator<Item> it = new Iterator<Item>() {
            private int idx = 0;
            public boolean hasNext() {
                if (idx >= num || listitem[idx] == null ) return false;
                else return true;
            }

            public Item next() {
                if (idx >= num)
                {
                    throw new java.util.NoSuchElementException("No next");
                }
                return listitem[idx++];
            }

            public void remove() {
                throw new java.lang.UnsupportedOperationException();
            }
        };
        return it;
    }
    public static void main(String[] args)   // unit testing (optional)
    {

    }
}
/*
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Node firstNode;
    private Node lastNode;
    private int numNode;
    private class Node
    {
        Item item;
        Node next;
        Node()
        {
            Node next = null;
        }
    }
    public RandomizedQueue()                 // construct an empty randomized queue
    {
        firstNode = null;
        lastNode = null;
        numNode = 0;
    }
    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return firstNode == null;
    }
    public int size()                        // return the number of items on the randomized queue
    {
        return numNode;
    }
    public void enqueue(Item item)           // add the item
    {
        if (item == null)
        {
            throw new java.lang.IllegalArgumentException("item can not be null");
        }
        if (firstNode == null) // there is no node
        {
            firstNode = new Node();
            firstNode.item = item;
            lastNode = firstNode;
        }
        else {
            lastNode.next = new Node();
            lastNode = lastNode.next;
            lastNode.item = item;
        }
        numNode ++;
    }
    public Item dequeue()                    // remove and return a random item
    {
        if (firstNode == null)
        {
            throw new java.util.NoSuchElementException("RandomizedQueue is empty");
        }
        if (numNode == 1)
        {
            Item tmp = firstNode.item;
            firstNode = null;
            lastNode = null;
            numNode --;
            return tmp;
        }
        else {
            int index = StdRandom.uniform(numNode);
            if (index == 0) // remove first node
            {
                Item tmp = firstNode.item;
                firstNode = firstNode.next;
                numNode --;
                return tmp;
            }
            else {
                int cnt = 0;
                Node nd = firstNode;
                while (cnt != (index - 1)) // find the node before the target
                {
                    nd = nd.next;
                    cnt ++;
                }
                Item tmp = nd.next.item; // get the value
                nd.next = nd.next.next; // skip the node
                if (nd.next == null)
                {
                    lastNode = nd; // this is the last node
                }
                numNode --;
                return tmp;
            }
        }


    }
    public Item sample()                     // return a random item (but do not remove it)
    {
        if (numNode == 0)
        {
            throw new java.util.NoSuchElementException("Randomized queue is empty");
        }
        int index = StdRandom.uniform(numNode);
        int cnt = 0;
        Node nd = firstNode;
        while (cnt != index)
        {
            nd = nd.next;
            cnt ++;
        }
        Item tmp = nd.item;
        return tmp;
    }
    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        int [] order = new int[numNode];
        for (int i = 0; i < numNode; i++) order[i] = i;
        StdRandom.shuffle(order);
        Item [] listitem = (Item []) new Object[numNode];
        Node start = firstNode;
        int cnt = 0;
        while(start != null)
        {
            listitem[order[cnt++]] = start.item; // arrange item list according to order
            start = start.next;
        }
        Iterator<Item> it = new Iterator<Item>() {
            private int idx = 0;
            public boolean hasNext() {
                if (idx >= numNode || listitem[idx] == null ) return false;
                else return true;
            }

            public Item next() {
                if (idx >= numNode)
                {
                    throw new java.util.NoSuchElementException("No next");
                }
                return listitem[idx++];
            }

            public void remove() {
                throw new java.lang.UnsupportedOperationException();
            }
        };
        return it;
    }
    public static void main(String[] args)   // unit testing (optional)
    {

    }
}
*/
