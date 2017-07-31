package org.sing_group.seda.datatype;

public interface Sequence {
  public static Sequence of(String name, String chain) {
    return new DefaultDatatypeFactory().newSequence(name, chain);
  }
  
  public String getName();

  public String getChain();
  
  public default int getLength() {
    return this.getChain().length();
  }
}
