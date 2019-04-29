/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.nio.file.StandardOpenOption.READ;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class NumberedLineReader implements AutoCloseable {
  private final InputStream input;
  private long location;

  public NumberedLineReader(Path file) throws IOException {
    this.input = new BufferedInputStream(Files.newInputStream(file, READ));
    this.location = 0;
  }

  public long getCurrentPosition() {
    return this.location;
  }
  
  public Line readLine() throws IOException {
    final StringBuilder sb = new StringBuilder();
    final long start = this.location;
    
    String endLine = null;
    
    boolean lfRead = false;
    boolean completed = false;
    do {
      final int read = this.input.read();
      final char cread = (char) (byte) read;
      
      if (read == -1) {
        completed = true;
      } else {
        this.location++;
        
        switch (cread) {
          case '\r':
            lfRead = true;
            break;
          case '\n':
            endLine = lfRead ? "\r\n" : "\n";
            lfRead = false;
            completed = true;
            break;
          default:
            if (lfRead) {
              sb.append('\r');
              lfRead = false;
            }
            sb.append(cread);
        }
      }
    } while (!completed);

    final long end = this.location;
    if (start == end) {
      return null;
    } else {
      return new Line(start, end, sb.toString(), endLine);
    }
  }

  @Override
  public void close() {
    try {
      this.input.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public final static class Line {
    private final long start;
    private final long end;
    private final String line;
    private final String lineEnding;

    public Line(long start, long end, String line, String lineEnding) {
      this.start = start;
      this.end = end;
      this.line = line;
      this.lineEnding = lineEnding;
    }

    public long getStart() {
      return start;
    }

    public long getEnd() {
      return end;
    }

    public int getLength() {
      return (int) (this.end - this.start - this.getLineEndingLength());
    }

    public String getLine() {
      return line;
    }

    public String getLineEnding() {
      return lineEnding;
    }
    
    public int getLineEndingLength() {
      return this.lineEnding == null ? 0 : this.lineEnding.getBytes().length;
    }
  }
}
