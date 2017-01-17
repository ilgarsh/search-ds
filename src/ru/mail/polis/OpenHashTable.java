package ru.mail.polis;

import java.util.Comparator;

public class OpenHashTable <E extends Comparable<E>> implements ISet<E> {

    private int size;
    private final int INITIAL_CAPACITY=8;
    private Object[] hashArray;

    private Comparator<E> comparator;

    public OpenHashTable() {
        this.hashArray = new Object[INITIAL_CAPACITY];
        this.comparator=null;
    }

    public OpenHashTable(Comparator<E> comparator) {
        this.hashArray = new Object[INITIAL_CAPACITY];
        this.comparator = comparator;
    }

    public int hashFunc1(E key) {
        return Math.abs(key.hashCode()) % hashArray.length;
    }

    public int hashFunc2(E key) {
        return 5 - Math.abs(key.hashCode()) % 5;
    }

    public void displayTable() {
        System.out.println("Table: ");
        for (int j=0; j<hashArray.length; j++) {
            if (hashArray[j] != null) {
                System.out.println(hashArray[j]+" ");
            } else {
                System.out.println("** ");
            }
        }
        System.out.println(" ");
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean contains(E value) {
        if (value==null) {
            throw new NullPointerException("argument is null");
        }

        int hashVal = hashFunc1(value);
        int stepSize = hashFunc2(value);
        while (hashArray[hashVal]!=null) {
            if (hashArray[hashVal]==value) {
                return true;
            }
            hashVal += stepSize;
            hashVal %= hashArray.length;
        }
        return false;
    }

    @Override
    public boolean add(E value) {
        if (contains(value)) {
            return false;
        }

        int hashVal = hashFunc1(value);
        int stepSize = hashFunc2(value);
        while (hashArray[hashVal]!=null) {
            hashVal += stepSize;
            hashVal %= hashArray.length;
        }
        hashArray[hashVal] = value;
        size++;
        resize();
        return true;
    }

    private void resize() {
        if (size * 2 < hashArray.length) {
            return;
        }
        //TODO: write code for resize array
        Object[] temp = hashArray;
        this.hashArray = new Object[hashArray.length*2];
        for (int i=0; i<size; i++) {
            int j=0;
            while(temp[j]==null) {
                j++;
            }
            int hashVal = hashFunc1((E)temp[j]);
            int stepSize = hashFunc2((E)temp[j]);
            while (hashArray[hashVal]!=null) {
                hashVal += stepSize;
                hashVal %= hashArray.length;
            }
            hashArray[hashVal] = temp[j];
            temp[j]=null;
        }
    }

    @Override
    public boolean remove(E value) {
        if (!contains(value)) {
            return false;
        }
        int hashVal = hashFunc1(value);
        int stepSize = hashFunc2(value);
        while (hashArray[hashVal]!=null) {
            if (hashArray[hashVal]==value) {
                Object temp = hashArray[hashVal];
                hashArray[hashVal] = null;
                break;
            }
            hashVal+= stepSize;
            hashVal %= hashArray.length;
        }
        size--;
        return true;
    }

    //--------------------------------------------------------------------

    public static void main(String[] args) {
        OpenHashTable<String> table = new OpenHashTable<>();
        table.add("a");
        table.add("b");
        table.add("c");
        table.add("d");
        table.add("e");
        table.add("f");
        table.add("g");
        table.add("11");
        table.add("22");
        table.remove("c");
        table.remove("a");
        table.remove("f");
        table.displayTable();
    }
}

