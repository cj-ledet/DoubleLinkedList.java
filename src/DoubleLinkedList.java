import java.util.*;

public class DoubleLinkedList<E> extends AbstractSequentialList<E>
{  // Data fields
    private Node<E> head = null;   // points to the head of the list
    private Node<E> tail = null;   //points to the tail of the list
    private int size = 0;    // the number of items in the list

    public void add(int index, E obj)
    {
        listIterator(index).add(obj);
    }

    public void addFirst(E obj)
    {
        add(0, obj);
    }

    public void addLast(E obj)
    {
        add(size, obj);
    }

    public E get(int index)
    {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is not >= 0 OR <= size");
        }

        ListIterator<E> iter = listIterator(index);
        return iter.next();
    }

    public E getFirst() { return head.data; }

    public E getLast() { return tail.data; }

    public int size() { return size; }

    public E remove(int index)
    {
        E returnValue = null;
        ListIterator<E> iter = listIterator(index);
        if (iter.hasNext())
        {
            returnValue = iter.next();
            iter.remove();
        }
        else { throw new IndexOutOfBoundsException(); }
        return returnValue;
    }

    public Iterator iterator() { return new ListIter(0); }
    public ListIterator listIterator() { return new ListIter(0); }
    public ListIterator listIterator(int index) { return new ListIter(index); }
    public ListIterator listIterator(ListIterator iter) { return new ListIter( (ListIter) iter); }

    // Inner Classes
    private static class Node<E>
    {
        private E data;
        private Node<E> next = null;
        private Node<E> prev = null;

        private Node(E dataItem)  //constructor
            { data = dataItem; }

        //Custom constructor that allows us to easily spin up a
        //new node given the new prev and next node values
        private Node(E dataItem, Node<E> prev, Node<E> next) {
            this(dataItem);
            this.prev = prev;
            this.next = next;
        }
    }  // end class Node

    public class ListIter implements ListIterator<E>
    {
        private Node<E> nextItem;      // the current node
        private Node<E> lastItemReturned;   // the previous node
        private int index = 0;   //

        public ListIter(int i)  // constructor for ListIter class
        {
            if (i < 0 || i > size)
                { throw new IndexOutOfBoundsException("Invalid index " + i); }

            lastItemReturned = null;

            if (i == size)  // Special case of last item
                { index = size;  nextItem = null; }
            else  // start at the beginning
            {
                nextItem = head;
                for (index = 0; index < i; index++)
                    { nextItem = nextItem.next; }
            }  // end else
        }  // end constructor

        public ListIter(ListIter other)
        {
            nextItem = other.nextItem;
            index = other.index;
        }

        public boolean hasNext() { return nextItem != null; }
        public boolean hasPrevious() { return size != 0 && (nextItem == null || nextItem.prev != null); }
        public int previousIndex() { return index - 1; }
        public int nextIndex() { return index; }

        public void set(E o)
        {
            if (lastItemReturned == null)
                { throw new IllegalStateException(); }

            lastItemReturned.data = o;
        }

        public void remove()
        {
            if (lastItemReturned == null)
                { throw new IllegalStateException(); }

            if (lastItemReturned.prev == null)
            {
                if (size > 1) {
                    head = lastItemReturned.next;
                    head.prev = null;
                } else { //one item in list
                    head = null;
                }
            } else if (lastItemReturned.next == null) { //end of list
                tail = tail.prev;
                tail.next = null;
            } else { // process to remove a node from in between two other nodes and update their prev & next values
                lastItemReturned.prev.next = lastItemReturned.next;
                lastItemReturned.next.prev = lastItemReturned.prev;
            }
            size--; //when removing a node, we must update our size
        }

        public E next()
        {
            if (!hasNext())
                { throw new NoSuchElementException(); }

            lastItemReturned = nextItem;
            nextItem = nextItem.next;
            index++;
            return lastItemReturned.data;
        }

        public E previous()
        {
            if (!hasPrevious())
                { throw new NoSuchElementException(); }

            if (!hasNext())
                { nextItem = tail; }
            else
                { nextItem = nextItem.prev; }

            lastItemReturned = nextItem;
            index--;
            return lastItemReturned.data;
        }

        public void add(E obj)
        {
            if (isEmpty()) {
                head = new Node<>(obj);
                tail = head;

            } else {
                Node<E> newAdd;
                if (!hasPrevious()) { // add Node to head of list
                    newAdd = new Node<>(obj, null, head);
                    head.prev = newAdd;
                    head = newAdd;
                } else if (!hasNext()) { // add Node at end of list
                    newAdd = new Node<>(obj, tail, null);
                    tail.next = newAdd;
                    tail = newAdd;
                } else { // add Node in middle of list
                    newAdd = new Node<>(obj, nextItem.prev, nextItem);
                    nextItem.prev.next = newAdd;
                    nextItem.prev = newAdd;
                }
            }
            size++;
            index++;
            lastItemReturned = null;
        }
    }// end of inner class ListIter
}// end of class DoubleLinkedList