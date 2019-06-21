import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int maxSize;
    private int sizeStore;
    private void resize() {
        if (sizeStore != 0) {
            if (sizeStore == maxSize) {
                Item[] temp = (Item[]) new Object[maxSize * 2];
                for (int i = 0; i < sizeStore; i++) temp[i] = q[i];
                q = temp;
                maxSize *= 2;
            } else if (sizeStore <= (maxSize / 4)) {
                Item[] temp = (Item[]) new Object[maxSize / 2];
                for (int i = 0; i < sizeStore; i++) temp[i] = q[i];
                q = temp;
                maxSize /= 2;
            }
        }
    }
    private int getRandom() {
        return StdRandom.uniform(sizeStore);
    }
    private class Itera<Item2> implements Iterator<Item2> {
        private int count;
        private final Item2[] qu;
        public Itera() {
            count = 0;
            qu = (Item2[]) new Object[sizeStore];
            for (int i = 0; i < sizeStore; i++) qu[i] = (Item2) q[i];
            StdRandom.shuffle(qu);
        }
        public boolean hasNext() {
            return (count < sizeStore);
        }
        public Item2 next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                return qu[count++];
            }
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    public RandomizedQueue() {
        q = (Item[]) new Object[1];
        maxSize = 1;
        sizeStore = 0;
    }
    public boolean isEmpty() {
        return (sizeStore == 0);
    }
    public int size() {
        return sizeStore;
    }
    public void enqueue(Item i) {
        if (i == null) {
            throw new IllegalArgumentException("Argument cannot be empty!");
        } else {
            resize();

            q[sizeStore] = i;

            ++sizeStore;
        }
    }
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            int pos = getRandom(); // get random array position in pos
            Item delet = q[pos]; // store element to be deleted so it can be returned.

            // since ordering of elements does not matter:
            q[pos] = q[sizeStore - 1]; // store last element in q[pos], effectively removing q[pos]
            q[sizeStore - 1] = null; // set last element as null
            
            --sizeStore;
            
            resize();

            return delet;
        }        
    }
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return q[getRandom()];
        }
    }
    public Iterator<Item> iterator() {
        Iterator<Item> itera = new Itera<Item>();
        return itera;
    }
    public static void main(String[] args) {
        RandomizedQueue<Integer> que = new RandomizedQueue<Integer>();
        que.enqueue(3);
        que.enqueue(4);
        que.enqueue(9);
        que.enqueue(1);
        que.enqueue(6);
        que.dequeue();
        que.dequeue();
        que.dequeue();
        for (Integer i : que) System.out.println(i);
    }
}