import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node firstNode;
    private Node lastNode;
    private int numNode;
    private class Node
    {
        Item item;
        Node next;
        Node prev;

        Node()
        {
            Node next = null;
            Node prev = null;
        }
    }

    public Deque()                           // construct an empty deque
    {
        firstNode = null;
        lastNode = null;
        numNode = 0;
    }
    public boolean isEmpty()                 // is the deque empty?
    {
        return firstNode == null;
    }
    public int size()                        // return the number of items on the deque
    {
        return numNode;
    }
    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null)
        {
            throw new java.lang.IllegalArgumentException("item can not be null");
        }
        Node oldFirst = firstNode;
        firstNode = new Node();
        firstNode.item = item;
        firstNode.next = oldFirst;
        firstNode.prev = null;
        if (oldFirst == null) //this is the first added Node
        {
            lastNode = firstNode;
        }
        else {
            oldFirst.prev = firstNode;
        }
        numNode ++;
    }
    public void addLast(Item item)           // add the item to the end
    {
        if (item == null)
        {
            throw new java.lang.IllegalArgumentException("item can not be null");
        }
        Node oldLast = lastNode;
        lastNode = new Node();
        lastNode.item = item;
        lastNode.next = null;
        lastNode.prev = oldLast;
        if (oldLast == null) // this is the first added node
        {
            firstNode = lastNode;
        }
        else{
            oldLast.next = lastNode;
        }
        numNode ++;
    }
    public Item removeFirst()                // remove and return the item from the front
    {
        if (firstNode == null) // there is no node
        {
            throw new java.util.NoSuchElementException("Deque is empty");
        }
        Item tmp = firstNode.item;
        if (firstNode.next == null) // this is the last node
        {
            firstNode = null;
            lastNode = null;
        }
        else {
            firstNode = firstNode.next;
            firstNode.prev = null;
        }
        numNode --;
        return tmp;
    }
    public Item removeLast()                 // remove and return the item from the end
    {
        if (lastNode == null) // there is no node
        {
            throw new java.util.NoSuchElementException("Deque is empty");
        }
        Item tmp = lastNode.item;
        if (lastNode.prev == null) // this is the first node
        {
            firstNode = null;
            lastNode = null;
        }
        else {
            lastNode = lastNode.prev;
            lastNode.next = null;
        }
        numNode --;
        return tmp;
    }

    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        Iterator<Item> it = new Iterator<Item>() {
            private Node start = firstNode;
            public boolean hasNext() {
                if (start == null) return false;
                else return true;
            }

            public Item next() {
                if (start == null)
                {
                    throw new java.util.NoSuchElementException("No next");
                }
                Item tmp = start.item;
                start = start.next;
                return tmp;
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
