import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private final Node first;
    private final Node last;
    private int sizeStore;
    private class Node {
        Item item;
        Node next;
        Node prev;
    }
    private class Itera<Item2> implements Iterator<Item2> {
        private int count;
        private Node itemm;
        public Itera() {
            count = 0;
            itemm = first;
        }
        public boolean hasNext() {
            return (count < sizeStore);
        }
        public Item2 next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                itemm = itemm.next;
                ++count;
                
                return (Item2) itemm.item;
            }
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    public Deque() {
        first = new Node();
        last = new Node();

        first.next = last;
        first.prev = null;
        first.item = null;

        last.next = null;
        last.prev = first;
        last.item = null;

        sizeStore = 0;
    }
    public boolean isEmpty() {
        return (sizeStore == 0);
    }
    public int size() {
        return sizeStore;
    }
    public void addFirst(Item i) {
        if (i == null) {
            throw new IllegalArgumentException("Argument cannot be empty!");
        } else {
            Node temp = new Node();

            temp.item = i;

            temp.next = first.next;
            temp.prev = first;

            first.next = temp;
            (temp.next).prev = temp;

            ++sizeStore;
        }
    }
    public void addLast(Item i) {
        if (i == null) {
            throw new IllegalArgumentException("Argument cannot be empty!");
        } else {
            Node temp = new Node();

            temp.item = i;

            temp.prev = last.prev;
            temp.next = last;

            last.prev = temp;
            (temp.prev).next = temp;

            ++sizeStore;
        }
    }
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            Node delet = new Node();

            delet.next = (first.next).next; // make copy of contents
            delet.item = (first.next).item;

            (first.next).next = null; // remove all stored content so it can be garbage collected
            (first.next).prev = null;
            (first.next).item = null;

            first.next = delet.next; // set next of first to element next to deleted element
            (delet.next).prev = first; // set prev of element next to deleted element to first

            --sizeStore;

            return delet.item; // return deleted item
        }        
    }
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            Node delet = new Node();

            delet.prev = (last.prev).prev;
            delet.item = (last.prev).item;

            (last.prev).next = null;
            (last.prev).prev = null;
            (last.prev).item = null;

            last.prev = delet.prev;
            (delet.prev).next = last;

            --sizeStore;

            return delet.item;
        }
    }
    public Iterator<Item> iterator() {
        Iterator<Item> itera = new Itera<Item>();
        return itera;
    }
    public static void main(String[] args) {
        Deque<Integer> deck = new Deque<Integer>();
        deck.addFirst(32);
        deck.addLast(52);
        deck.addFirst(44);
        deck.addLast(23);
        deck.removeLast();
        deck.removeFirst();
        for (Integer i : deck) System.out.println(i);
    }
}