package ru.mail.polis;

import java.util.Comparator;

//TODO: write code here
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

    public void displayTable() { //TODO
        System.out.println("Table: ");
        for (int j=0; j<size; j++) {
            if (hashArray[j] != null) {
                System.out.println(hashArray[j].getKey()+" ");
            } else {
                System.out.println("** ");
            }
        }
        System.out.println(" ");
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(E value) {
        return false;
    }

    @Override
    public boolean add(E value) {
        return false;
    }

    @Override
    public boolean remove(E value) {
        return false;
    }

    //--------------------------------------------------------------------


    public void insert(int key, DataItem item) {
        int hashVal = hashFunc1(key);
        int stepSize = hashFunc2(key);

        while (hashArray[hashVal]!=null && hashArray[hashVal].getKey()!=-1) {
            hashVal += stepSize;
            hashVal %= arraySize;
        }

        hashArray[hashVal] = item;
    }

    public DataItem delete(int key) {
        int hashVal = hashFunc1(key);
        int stepSize = hashFunc2(key);

        while (hashArray[hashVal]!=null) {
            if (hashArray[hashVal].getKey()==key) {
                DataItem temp = hashArray[hashVal];
                hashArray[hashVal] = nonItem;
                return temp;
            }
            hashVal+= stepSize;
            hashVal %= arraySize;
        }
        return null;
    }

    public DataItem find(int key) {
        int hashVal = hashFunc1(key);
        int stepSize = hashFunc2(key);

        while (hashArray[hashVal]!=null) {
            if (hashArray[hashVal].getKey()==key) {
                return hashArray[hashVal];
            }
            hashVal += stepSize;
            hashVal %= arraySize;
        }
        return null;
    }

    public static void main(String[] args) {
        DataItem item1 = new DataItem(1000);
        DataItem item2 = new DataItem(234);
        DataItem item3 = new DataItem(10444);
        OpenHashTable table = new OpenHashTable(10);
        table.insert(1, item1);
        table.insert(8, item2);
        table.insert(11, item3);
        System.out.println(table.find(11));
        table.displayTable();
    }
}

