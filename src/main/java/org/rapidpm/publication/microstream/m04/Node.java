package org.rapidpm.publication.microstream.m04;

public class Node {

  public Node(int id) {
    this.id = id;
  }

  private final int  id;
  private       Node left;
  private       Node right;
  private       Node parent;


  public Node getLeft() {
    return left;
  }

  public Node getRight() {
    return right;
  }

  public void setLeft(Node left) {
    this.left = left;
  }

  public void setRight(Node right) {
    this.right = right;
  }

  public Node getParent() {
    return parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public int getId() {
    return id;
  }
}
