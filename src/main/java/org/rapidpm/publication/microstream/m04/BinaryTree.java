package org.rapidpm.publication.microstream.m04;

import org.rapidpm.dependencies.core.logger.HasLogger;

import java.util.*;

public class BinaryTree
    implements HasLogger {

  private Node rootNode;

  private Node add(Node current, int value) {
    if (current == null) {
      return new Node(value);
    }
    if (value < current.getId()) current.setLeft(add(current.getLeft(), value));
    else if (value > current.getId()) current.setRight(add(current.getRight(), value));
    else return current; // value already exists
    return current;
  }

  public void add(int value) {
    rootNode = add(rootNode, value);
  }


//  private boolean contains(Node current, int value) {
//    if (current == null) return false;
//    if (value == current.getId()) return true;
//    return value < current.getId()
//           ? contains(current.getLeft(), value)
//           : contains(current.getRight(), value);
//  }
//
//  public boolean contains(int value) {
//    return contains(rootNode, value);
//  }


  public List<Integer> traverseLevelOrder() {
    if (rootNode == null) return Collections.emptyList();
    List<Integer>     nodeIDs = new ArrayList<>();
    final Queue<Node> nodes   = new LinkedList<>();
    nodes.add(rootNode);
    while (!nodes.isEmpty()) {
      Node node = nodes.remove();
      nodeIDs.add(node.getId());
      if (node.getLeft() != null) {
        nodes.add(node.getLeft());
      }
      if (node.getRight() != null) {
        nodes.add(node.getRight());
      }
    }
    return nodeIDs;
  }


}
