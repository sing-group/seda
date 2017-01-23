package org.sing_group.seda.io;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.MultipleSequenceAlignment;
import org.sing_group.seda.datatype.MultipleSequenceAlignmentDataset;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.transformation.dataset.MSADatasetTransformation;

public class DatasetProcessor {
	private final DatatypeFactory factory;
	
	public DatasetProcessor(DatatypeFactory factory) {
		this.factory = factory;
	}

	public void process(Path[] inputs, Path output, MSADatasetTransformation transformation, int groupSize) throws IOException {
		process(stream(inputs), output, transformation, groupSize);
	}
	
	public void process(Path inputDirectory, Path output, MSADatasetTransformation transformation, int groupSize) throws IOException {
		process(findMSAFiles(inputDirectory), output, transformation, groupSize);
	}
	
	public void process(Stream<Path> inputs, Path output, MSADatasetTransformation transformation, int groupSize) throws IOException {
		try (final Stream<Path> sequenceFiles = inputs) {
			final MultipleSequenceAlignment[] sequences = sequenceFiles
				.map(LazyFileMultipleSequenceAlignment::new)
			.toArray(MultipleSequenceAlignment[]::new);
			
			final MultipleSequenceAlignmentDataset dataset = transformation.transform(
				this.factory.newMSADataset(sequences)
			);
			final MultipleSequenceAlignment[] alignments = dataset.getAlignments().toArray(MultipleSequenceAlignment[]::new);
			
			
			final Namer namer = new Namer();
			int count = 0;
			Path groupOutput = output;
			for (MultipleSequenceAlignment alignment : alignments) {
				if (groupSize > 1 && count % groupSize == 0) {
					groupOutput = output.resolve("group" + (count / groupSize + 1));
					Files.createDirectories(groupOutput);
					namer.clearNames();
				}
				
				final String name = namer.uniqueName(alignment.getName());
				writeFasta(groupOutput.resolve(name), alignment.getSequences());
				count++;
			};
		}
	}
	
	private static class Namer {
		private final Set<String> names;
		
		public Namer() {
			this.names = new HashSet<>();
		}
		
		public void clearNames() {
			this.names.clear();
		}
		
		public String uniqueName(String name) {
			int i = 1;
			
			String uniqueName = name;
			while (names.contains(uniqueName)) {
				uniqueName = composeName(name, i++);
			}
			names.add(uniqueName);
			
			return uniqueName;
		}
	}
	
	private static String composeName(String name, int count) {
		if (name.contains(".")) {
			final int dotIndex = name.lastIndexOf('.');
			
			if (dotIndex == name.length() - 1) {
				return name + "_" + count;
			} else {
				final String filename = name.substring(0, dotIndex);
				final String extension = name.substring(dotIndex + 1);
				
				return filename + "_" + count + "." + extension;
			}
		} else {
			return name + "_" + count;
		}
	}

	protected static Stream<Path> findMSAFiles(Path input) throws IOException {
		return Files.find(
			input, Integer.MAX_VALUE,
			(file, attrs) -> attrs.isRegularFile() && file.getFileName().toString().toLowerCase().endsWith("fasta"),
			FileVisitOption.FOLLOW_LINKS
		);
	}
	
	public static void writeFasta(Path file, Sequence ... sequences) {
		writeFasta(file, stream(sequences));
	}
	
	public static void writeFasta(Path file, Stream<Sequence> sequences) {
		try {
			final List<String> fastaLines = sequences
				.map(sequence -> new String[] { sequence.getName(), sequence.getChain() })
				.flatMap(Arrays::stream)
			.collect(toList());
			
			Files.write(file, fastaLines);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected error creating temporary file.", e);
		}
	}
	
}
