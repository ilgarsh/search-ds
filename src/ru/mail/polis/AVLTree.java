package ru.mail.polis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;



public class AVLTree<E extends Comparable<E>> implements ISortedSet<E> {

    private Node root;
    private int size;
    private final Comparator<E> comparator;

    public AVLTree() {
        this.comparator = null;
    }

    public AVLTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        Node curr = root;
        while (curr.left!=null) {
            curr=curr.left;
        }
        return curr.value;
    }

    @Override
    public E last() {
        if(isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        Node curr = root;
        while (curr.right!=null) {
            curr=curr.right;
        }
        return curr.value;
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<>(size);
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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public boolean contains(E value) {
        if (value==null) {
            throw new NullPointerException();
        }

        Node curr=root;
        while (curr!=null) {
            int comp=value.compareTo(curr.value);
            if (comp<0) {
                curr = curr.left;
            }
            else if (comp>0) {
                curr=curr.right;
            }
            else if (comp==0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E value) {
        if (contains(value)) {
            return false;
        }

        if (root==null) {
            root = new Node(value, null);
        }
        else {
            Node curr = root;
            Node parent;
            while (true) {
                if (curr.value == value)
                    return false;

                parent = curr;

                int comp = compare(value, curr.value);
                curr = comp==1 ? curr.right : curr.left;

                if (curr == null) {
                    if (comp==-1) {
                        parent.left = new Node(value, parent);
                    } else {
                        parent.right = new Node(value, parent);
                    }
                    rebalance(parent);
                    break;
                }
            }
        }
        size++;
        return true;
    }

    private void rebalance(Node n) {
        setBalance(n);

        if (n.balance == -2 && n.left!=null) {
            if (height(n.left.left) >= height(n.left.right))
                n = rotateRight(n);
            else
                n = rotateLeftThenRight(n);

        } else if (n.balance == 2 && n.right!=null) {
            if (height(n.right.right) >= height(n.right.left))
                n = rotateLeft(n);
            else
                n = rotateRightThenLeft(n);
        }

        if (n.parent != null) {
            rebalance(n.parent);
        } else {
            root = n;
        }
    }

    private Node rotateRightThenLeft(Node n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }

    private Node rotateLeft(Node a) {
        Node b = a.right;
        b.parent = a.parent;

        a.right = b.left;

        if (a.right != null)
            a.right.parent = a;

        b.left = a;
        a.parent = b;

        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }

        setBalance(a, b);

        return b;
    }

    private Node rotateLeftThenRight(Node n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }

    private Node rotateRight(Node a) {
        Node b = a.left;
        b.parent = a.parent;

        a.left = b.right;

        if (a.left != null)
            a.left.parent = a;

        b.right = a;
        a.parent = b;

        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }

        setBalance(a, b);

        return b;
    }


    private int height(Node n) {
        if (n == null)
            return -1;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    private void setBalance(Node... nodes) {
        for (Node n : nodes) {
            n.balance = height(n.left) - height(n.right);
        }
    }

    @Override
    public boolean remove(E value) {
        if (!contains(value)) {
            return false;
        }
        Node n = root;
        Node parent = root;
        Node delNode = null;
        Node child = root;

        while (child != null) {
            parent = n;
            n = child;
            child = compare(value, n.value)>0 ? n.right : n.left;
            if (value == n.value)
                delNode = n;
        }

        if (delNode != null) {
            delNode.value = n.value;

            child = n.left != null ? n.left : n.right;

            if (root.value == value) {
                root = child;
            } else {
                if (parent.left == n) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
                rebalance(parent);
            }
        }
        return true;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    private class Node {

        Node(E value, Node parent) {
            this.value = value;
            this.parent = parent;
        }

        E value;
        Node left;
        Node right;
        Node parent;
        int balance;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("N{");
            sb.append("d=").append(value);
            if (left != null) {
                sb.append(", l=").append(left);
            }
            if (right != null) {
                sb.append(", r=").append(right);
            }
            sb.append('}');
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(1);
        tree.add(6);
        tree.add(4);
        tree.add(12);
        System.out.println(tree.inorderTraverse());
        tree.remove(6);
        tree.remove(1);
        System.out.println(tree.inorderTraverse());
        tree.add(12);
        System.out.println(tree.inorderTraverse());
        System.out.println(tree.contains(12));
    }
}
