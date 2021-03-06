package com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection;

/**
 * Class that implements LIFO logic
 */
public class MyStack<T> implements MyCollection{

    private StackNode lastNode = null;

    private class StackNode {
        private StackNode prevNode = null;
        private final T value;

        private StackNode(T element) {
            this.value = element;
        }
    }

    /**
     * Adds element to the end of the stack
     *
     * @param element element that will be added to the stack. can be any reference type
     */
    public void push(T element) {
        StackNode newNode = new StackNode(element);
        newNode.prevNode = lastNode;

        lastNode = newNode;
    }

    /**
     * retrieves the element from the top of the stack
     *
     * @return the element from the top of the stack
     * @throws Exception specifies that the stack is empty
     */
    public T pop() throws Exception {
        if (lastNode == null) {
            throw new Exception("sorry, stack is empty");
        }

        StackNode tmpNode = lastNode;

        if (lastNode.prevNode == null) { // true only one element in the stack
            lastNode = null;
        } else { // if we have more than 1 el
            lastNode = lastNode.prevNode;
        }

        return tmpNode.value;
    }

    /**
     * returns the elements from top of the stack, but that element remains at the top of the stack
     *
     * @return the element from top of the stack
     */
    public T peek() {
        return lastNode.value;
    }

    /**
     * returns false if stack is not empty and true if queue is stack
     *
     * @return false if stack is not empty and true if queue is stack
     */
    @Override
    public boolean isEmpty() {
        return lastNode == null;
    }

}
