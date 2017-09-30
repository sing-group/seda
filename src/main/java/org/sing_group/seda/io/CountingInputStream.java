package org.sing_group.seda.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

class CountingInputStream extends FilterInputStream {
  private long position;
  
  private long mark;
  
  public long getPosition() {
    return position;
  }
  
  public CountingInputStream(InputStream in) {
    super(in);
    
    this.position = 0;
    this.mark = -1;
  }

  @Override
  public int read() throws IOException {
    final int read = super.read();
    
    if (read != -1) {
      this.position++;
    }
    
    return read;
  }
  
  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    final int readCount = super.read(b, off, len);
    
    this.position += readCount;
    
    return readCount;
  }
  
  @Override
  public int read(byte[] b) throws IOException {
    final int readCount = super.read(b);
    
    this.position += readCount;
    
    return readCount;
  }
  
  @Override
  public long skip(long n) throws IOException {
    final long skipped = super.skip(n);
    
    this.position += skipped;
    
    return skipped;
  }
  
  @Override
  public synchronized void mark(int readlimit) {
    super.mark(readlimit);
    
    this.mark = this.position;
  }
  
  @Override
  public synchronized void reset() throws IOException {
    try {
      super.reset();
      
      this.position = this.mark;
    } catch (IOException ioe) {
      this.mark = -1;
    }
  }
}
