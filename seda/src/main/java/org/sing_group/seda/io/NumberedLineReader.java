/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.nio.file.Files.newInputStream;
import static java.nio.file.StandardOpenOption.READ;
import static org.mozilla.universalchardet.UniversalDetector.detectCharset;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NumberedLineReader implements AutoCloseable {
  private final Reader input;
  private final CharsetEncoder encoder;
  
  private long charStart;
  private long charEnd;
  private long nextChar;
  private int charLength;

  public NumberedLineReader(Path file) throws IOException {
    this(newInputStream(file, READ), Charset.forName(detectCharset(file)));
  }
  
  public NumberedLineReader(InputStream input, Charset charset) throws IOException {
    if (!charset.canEncode())
      throw new IllegalArgumentException("Encode not supported for charset: " + charset.displayName());
    
    this.input = new InputStreamReader(input);
    
    this.encoder = charset.newEncoder();
    
    this.charStart = 0;
    this.charEnd = -1;
    this.nextChar = 0;
    this.charLength = 0;
  }
  
  private int readChar() throws IOException {
    int read = this.input.read();
    
    if (read != -1) {
      final CharBuffer charBuffer = CharBuffer.wrap(new char[] { (char) read });
      final ByteBuffer byteBuffer = this.encoder.encode(charBuffer);
      
      this.charLength = byteBuffer.rewind().remaining();
      this.charStart = this.nextChar;
      this.charEnd = this.charStart + this.charLength - 1;
      this.nextChar += this.charLength;
    }
    
    return read;
  }
  
  public long getCurrentLocation() {
    return this.charEnd + 1;
  }
  
  public Charset getCharset() {
    return this.encoder.charset();
  }
  
  public Line readLine() throws IOException {
    final StringBuilder sb = new StringBuilder();
    final List<Integer> lengths = new ArrayList<>();
    long start = -1;
    long endTextPosition = start;
    
    String endLine = null;
    
    boolean lfRead = false;
    boolean completed = false;
    
    int read;
    do {
      read = this.readChar();
      
      if (read == -1) {
        completed = true;
      } else {
        if (start == -1) {
          start = this.charStart;
          endTextPosition = this.charEnd;
        }
        lengths.add(this.charLength);
        
        final char cread = (char) read;
        switch (cread) {
          case '\r':
            if (lfRead) {
              sb.append('\r');
            }
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
            endTextPosition = this.charEnd;
        }
      }
    } while (!completed);

    final long endLinePosition = this.charEnd;
    if (read == -1 && sb.length() == 0) {
      return null;
    } else {
      final byte[] charLengths = new byte[lengths.size()];
      for (int i = 0; i < lengths.size(); i++) {
        charLengths[i] = lengths.get(i).byteValue();
      }
      
      return new Line(start, endTextPosition, endLinePosition, charLengths, sb.toString(), endLine);
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
    private final long textEnd;
    private final long lineEnd;
    private final int textLength;
    private final int lineLength;
    private final byte[] charLengths;
    private final String line;
    private final String lineEnding;

    public Line(long start, long textEnd, long lineEnd, byte[] charLengths, String line, String lineEnding) {
      this.start = start;
      this.textEnd = textEnd;
      this.lineEnd = lineEnd;
      this.charLengths = charLengths;
      this.line = line;
      this.lineEnding = lineEnding;
      
      int textLength = 0;
      int lineLength = 0;
      
      final int lineEndingLength = lineEnding == null ? 0 : lineEnding.length();
      final int lineEndingStart = this.charLengths.length - lineEndingLength;
      for (int i = 0; i < this.charLengths.length; i++) {
        if (i < lineEndingStart)
          textLength += this.charLengths[i];
        lineLength += this.charLengths[i];
      }
      
      this.textLength = textLength;
      this.lineLength = lineLength;
    }

    public long getStart() {
      return start;
    }
    
    public long getTextEnd() {
      return textEnd;
    }

    public long getLineEnd() {
      return lineEnd;
    }

    public int getTextLength() {
      return this.textLength;
    }
    
    public int getLineLength() {
      return this.lineLength;
    }
    
    public int getCharLength(int position) {
      return this.charLengths[position];
    }
    
    public int getTextLengthTo(int position) {
      int length = 0;
      
      for (int i = 0; i < position; i++) {
        length += this.charLengths[i];
      }
      
      return length;
    }
    
    public int getTextLengthFrom(int position) {
      int length = 0;
      
      for (int i = position; i < this.charLengths.length - this.lineEnding.length(); i++) {
        length += this.charLengths[i];
      }
      
      return length;
    }

    public String getLine() {
      return line;
    }

    public String getLineEnding() {
      return lineEnding;
    }
    
    public int getLineEndingLength() {
      return this.lineEnding == null ? 0 : (int) (this.lineEnd - this.textEnd);
    }
  }
}
