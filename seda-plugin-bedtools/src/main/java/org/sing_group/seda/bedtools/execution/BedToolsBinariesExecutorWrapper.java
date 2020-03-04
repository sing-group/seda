package org.sing_group.seda.bedtools.execution;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BedToolsBinariesExecutorWrapper {
  @XmlAnyElement(lax = true)
  private BedToolsBinariesExecutor binariesExecutor;
  
  public BedToolsBinariesExecutor get() {
    return binariesExecutor;
  }
  
  public void set(BedToolsBinariesExecutor binariesExecutor) {
    this.binariesExecutor = binariesExecutor;
  }
}
