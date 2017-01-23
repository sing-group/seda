package org.sing_group.seda.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.MultipleSequenceAlignment;
import org.sing_group.seda.datatype.Sequence;

public class LazyFileMultipleSequenceAlignment implements MultipleSequenceAlignment {
	private final String name;
	private final Path file;
	
	public LazyFileMultipleSequenceAlignment(String name, Sequence ... sequences) {
		try {
			this.name = name;
			this.file = Files.createTempFile("seda_" + name, ".fasta");
			this.file.toFile().deleteOnExit();
			
			DatasetProcessor.writeFasta(this.file, sequences);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected error creating temporary file.", e);
		}
	}
	
	public LazyFileMultipleSequenceAlignment(Path file) {
		this.name = file.getFileName().toString();
		this.file = file;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Stream<Sequence> getSequences() {
		final Map<String, String> sequences = readMSA();
		
		return sequences.entrySet().stream()
			.map(entry -> Sequence.of(entry.getKey(), entry.getValue()));
	}
	
	@Override
	public int getSequenceCount() {
		return readMSA().size();
	}
	
	private Map<String, String> readMSA() {
		try {
			final List<String> lines = Files.readAllLines(this.file);
			final Map<String, String> sequences = new LinkedHashMap<>();
			
			
			String name = null;
			StringBuilder chain = new StringBuilder();
			for (String line : lines) {
				if (line.trim().isEmpty()) {
					if (name != null && chain.length() > 0) {
						sequences.put(name, chain.toString());
						name = null;
						chain = new StringBuilder();
					} else if (name != null || chain.length() == 0) {
						throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", this.file, line));
					}
				} else if (line.startsWith(">")) {
					if (name != null && chain.length() > 0) {
						sequences.put(name, chain.toString());
						name = null;
						chain = new StringBuilder();
					} else if (name != null || chain.length() > 0) {
						throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", this.file, line));
					}
					
					final int spaceIndex = line.indexOf(" ");
					if (spaceIndex > 0) {
						name = line.substring(0, spaceIndex);
					} else {
						name = line;
					}
				} else {
					if (name == null) {
						throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", this.file, line));
					} else {
						chain.append(line.trim());
					}
				}
			}
			
			if (name != null && chain.length() > 0) {
				sequences.put(name, chain.toString());
			}
			
			return sequences;
		} catch (IOException ioe) {
			throw new RuntimeException("Error reading file: " + this.file.toString(), ioe);
		}
	}
}
