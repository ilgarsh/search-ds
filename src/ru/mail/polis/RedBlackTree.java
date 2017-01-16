package ru.mail.polis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class RedBlackTree<E extends Comparable<E>> implements ISortedSet<E> {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;

    private class Node {
        private E value;
        private Node left, right;
        private boolean color;
        private int size;

        private Node(E value, boolean color, int size) {
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    private final Comparator<E> comparator;

    public RedBlackTree() {
        this.comparator = null;
    }

    public RedBlackTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    private boolean isRed(Node n) {
        if (n==null) {
            return false;
        }
        return n.color==RED;
    }

    @Override
    public E first() {
        return min(root).value;
    }

    @Override
    public E last() {
        return max(root).value;
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<>(size());
        inorderTraverse(root, list);
        return list;
    }

    private void inorderTraverse(Node curr, List<E> list) {
        if (curr==null) {
            return;
        }
        inorderTraverse(curr.left, list);
        list.add(curr.value);
        inorderTraverse(curr.right, list);
    }

    public int size() {
        return size(root);
    }

    private int size(Node n) {
        if (n == null) return 0;
        return n.size;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    private E get(E value) {
        if (value == null) throw new NullPointerException("argument to get() is null");
        return get(root, value);
    }

    private E get(Node n, E value) {
        while (n != null) {
            int cmp = value.compareTo(n.value);
            if      (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else
                return n.value;
        }
        return null;
    }

    @Override
    public boolean contains(E value) {
        return get(value) != null;
    }

    @Override
    public boolean add(E value) {
        if (value == null) throw new NullPointerException("argument to put() is null");
        if (contains(value)) {
            return false;
        }
        root=put(root, value);
        root.color = BLACK;
        return true;
    }

    private Node put(Node n, E value) {
        if (n == null) return new Node(value, RED, 1);

        int cmp = value.compareTo(n.value);
        if (cmp < 0) {
            n.left = put(n.left, value);
        }
        else if (cmp > 0) {
            n.right = put(n.right, value);
        }
        else {
            n.value=value;
        }

        if (isRed(n.right) && !isRed(n.left))      n = rotateLeft(n);
        if (isRed(n.left)  &&  isRed(n.left.left)) n = rotateRight(n);
        if (isRed(n.left)  &&  isRed(n.right))     flipColors(n);
        n.size = size(n.left) + size(n.right) + 1;
        return n;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    @Override
    public boolean remove(E value) {
        if (value == null) throw new NullPointerException("argument to remove() is null");
        if (!contains(value)) return false;
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = remove(root, value);
        if (!isEmpty()) root.color = BLACK;
        return true;
    }

    private Node remove(Node n, E value) {
        if (value.compareTo(n.value) < 0)  {
            if (!isRed(n.left) && !isRed(n.left.left))
                n = moveRedLeft(n);
            n.left = remove(n.left, value);
        }
        else {
            if (isRed(n.left))
                n = rotateRight(n);
            if (value.compareTo(n.value) == 0 && (n.right == null))
                return null;
            if (!isRed(n.right) && !isRed(n.right.left))
                n = moveRedRight(n);
            if (value.compareTo(n.value) == 0) {
                Node x = min(n.right);
                n.value = x.value;
                n.right = deleteMin(n.right);
            }
            else n.right = remove(n.right, value);
        }
        return balance(n);
    }

    private Node balance(Node h) {
        if (isRed(h.right))                      h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    private Node min(Node x) {
        if (isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        if (x.left == null) {
            return x;
        } else {
            return min(x.left);
        }
    }

    private Node max(Node x) {
        if (isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        if (x.right == null) {
            return x;
        }
        else {
            return max(x.right);
        }
    }

    private Node deleteMin(Node h) {
        if (h.left == null)
            return null;

        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);

        h.left = deleteMin(h.left);
        return balance(h);
    }


    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    public static void main(String[] args) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.add(3);
        tree.add(9);
        tree.add(4);
        tree.add(13);
        System.out.println(tree.inorderTraverse());
        System.out.println(tree.contains(4));
        System.out.println(tree.contains(1));
        tree.remove(9);
        tree.remove(3);
        System.out.println(tree.inorderTraverse());
    }
}
