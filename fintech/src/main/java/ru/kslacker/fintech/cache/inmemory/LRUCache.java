package ru.kslacker.fintech.cache.inmemory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LRUCache<K, V> {
    private final Map<K, Node> map;
    private final int capacity;
    private final Node head;
    private final Node tail;
    private final AtomicInteger size;

    public LRUCache(int capacity) {
        this.map = new ConcurrentHashMap<>(capacity);
        this.size = new AtomicInteger(0);
        this.capacity = capacity;
        this.head = new Node();
        this.tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    public synchronized V find(K key) {
        if (!contains(key)) {
            return null;
        }

        Node node = map.get(key);
        node.moveToHead();
        return node.val;
    }

    public synchronized void put(K key, V value) {
        if (contains(key)) {
            Node node = map.get(key);
            node.val = value;
            node.moveToHead();
        } else {
            if (size.get() == capacity) {
                Node node = popLast();
                map.remove(node.key);
            } else {
                size.incrementAndGet();
            }
            Node node = new Node(key, value);
            addNode(node);
            map.put(key, node);
        }
    }

    public synchronized void remove(K key) {
        if (!contains(key)) {
            return;
        }

        removeNode(map.get(key));
        map.remove(key);
        size.decrementAndGet();
    }

    public boolean contains(K key) {
        return map.containsKey(key);
    }

    public class Node {
        Node prev;
        Node next;
        K key;
        V val;

        public Node() {

        }

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }

        public void moveToHead() {
            removeNode(Node.this);
            addNode(Node.this);
        }
    }

    private void addNode(Node node) {
        if (head.next != null) {
            head.next.prev = node;
        }
        node.next = head.next;
        head.next = node;
        node.prev = head;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
    }

    private Node popLast() {
        Node removed = tail.prev;
        removeNode(removed);
        return removed;
    }

}
