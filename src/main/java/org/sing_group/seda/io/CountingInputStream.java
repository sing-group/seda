/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
