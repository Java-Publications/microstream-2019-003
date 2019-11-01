package org.rapidpm.publication.microstream.m03;

import org.rapidpm.publication.microstream.m01.HelloWorldImmutable;

import java.util.Map;

public class HashMapRootNode {

  private final Map<Integer, HelloWorldImmutable> elements;

  public HashMapRootNode(Map<Integer, HelloWorldImmutable> elements) {
    this.elements = elements;
  }

  public Map<Integer, HelloWorldImmutable> getElements() {
    return elements;
  }
}
