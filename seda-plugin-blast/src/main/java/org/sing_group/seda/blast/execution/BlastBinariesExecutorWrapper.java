package org.sing_group.seda.blast.execution;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BlastBinariesExecutorWrapper {
  @XmlAnyElement(lax = true)
  private BlastBinariesExecutor binariesExecutor;

  public BlastBinariesExecutor get() {
    return binariesExecutor;
  }

  public void set(BlastBinariesExecutor binariesExecutor) {
    this.binariesExecutor = binariesExecutor;
  }
}
