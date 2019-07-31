/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
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
package org.sing_group.seda.prosplign.execution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProSplignTxtParser {

	private int sequencesCount = 1;
	private List<String> sequences;
	private List<String> resultsSequences;
	private Set<String> resultsSequencesHeaderSet;
	private Map<String, String> outputHeaders;

  public ProSplignTxtParser() {}

  public void parse(Path testFile) throws IOException {
    parse(testFile, Collections.emptyMap(), Collections.emptyMap());
  }

	public void parse(Path proSplignTxtOutputFile,
		Map<String, String> queryHeaderMapping,
		Map<String, String> subjectHeaderMapping
	) throws IOException {
		parseSequences(proSplignTxtOutputFile, queryHeaderMapping,
			subjectHeaderMapping);
	}

	public List<String> getSequences() {
		return this.sequences;
	}

	public List<String> getFullSequences() {
		return this.resultsSequences;
	}

	private void parseSequences(Path testFile,
		Map<String, String> queryHeaderMapping,
		Map<String, String> subjectHeaderMapping
	) throws IOException {
		this.sequences = new LinkedList<>();
		this.resultsSequences = new LinkedList<>();
		this.resultsSequencesHeaderSet = new HashSet<>();
		this.outputHeaders = new HashMap<>();

		int asteriskCount = 0;
		Iterator<String> lines = Files.readAllLines(testFile).iterator();

		while (lines.hasNext()) {
			String line = lines.next();

			if (line.startsWith("*")) {
				asteriskCount++;
				if (asteriskCount == 3) {
					sequences.addAll(processSequenceBlock(lines,
						queryHeaderMapping, subjectHeaderMapping));
					asteriskCount = 0;
				}
			}
		}
	}

	private List<String> processSequenceBlock(Iterator<String> lines,
		Map<String, String> queryHeaderMapping,
		Map<String, String> subjectHeaderMapping
	) {
		if (lines.hasNext()) {
			List<String> toretBlockSequences = new LinkedList<>();
			String header = lines.next();
			String[] headerFields = header.split("\t");
			String fullSequenceHeader = headerFields[1] + " " + headerFields[3] + " " + headerFields[4];

			List<String> blockSequences = extractBlockSequences(lines);

			String currentResultSequence = blockSequences
				.remove(blockSequences.size() - 1);
			if (!this.resultsSequencesHeaderSet.contains(fullSequenceHeader)) {
				this.resultsSequencesHeaderSet.add(fullSequenceHeader);

				String outputFullSequenceHeader = subjectHeaderMapping.getOrDefault(headerFields[1], headerFields[1])
				  + " (from " +
				  fullSequenceHeader.substring(fullSequenceHeader.indexOf(" ") + 1)
				  + ")";

				this.resultsSequences.add(">" + outputFullSequenceHeader);
				this.resultsSequences.add(currentResultSequence);
				this.outputHeaders.put(fullSequenceHeader, outputFullSequenceHeader);
			}

			String outputFullSequenceHeader = outputHeaders.get(fullSequenceHeader);

			for (String s : blockSequences) {
				if(!s.isEmpty()) {
					toretBlockSequences.add(">" + (sequencesCount++) + " "
					  + outputFullSequenceHeader
					  + " annotated with "
					  + queryHeaderMapping.getOrDefault(headerFields[2],
              headerFields[2])
					);
					toretBlockSequences.add(s);
				}
			}

			return toretBlockSequences;
		} else {
			throw new RuntimeException("Invalid sequence part.");
		}
	}

	private static List<String> extractBlockSequences(Iterator<String> lines) {
		List<String> blockSequences = new LinkedList<>();
		StringBuilder currentSequence = new StringBuilder();
		String firstLine;
		StringBuilder fullSequence = new StringBuilder();

		while (lines.hasNext()) {
			firstLine = lines.next();
			if (firstLine.isEmpty()) {
				break;
			} else {
				List<String> block = new LinkedList<>();
				block.add(firstLine);
				for (int i = 0; i < 4; i++) {
					if (lines.hasNext()) {
						block.add(lines.next());
					}
				}

				if (block.size() == 5) {
					for (int i = 0; i < block.get(4).length(); i++) {
						if (isNucleotide(block.get(0).toUpperCase().charAt(i))) {
							fullSequence.append(block.get(0).charAt(i));
						}

						if (block.get(4).charAt(i) == '*') {
							if (block.get(3).charAt(i) != '.' && block.get(0).charAt(i) != '-') {
								currentSequence.append(block.get(0).charAt(i));
							}
						} else {
							if (!currentSequence.toString().isEmpty() &&
								!currentSequence.toString().endsWith("|")
								&& isNucleotide(block.get(0).toUpperCase().charAt(i))
							) {
								blockSequences.add(currentSequence.toString());
								currentSequence = new StringBuilder();
							}
						}
					}
				} else {
					throw new RuntimeException("Invalid sequence block.");
				}
			}
		}

		blockSequences.add(currentSequence.toString());
		blockSequences.add(fullSequence.toString());

		return blockSequences;
	}

	private static boolean isNucleotide(char charAt) {
		return charAt == 'A' || charAt == 'C' || charAt == 'T' || charAt == 'G'
			|| charAt == 'Y' || charAt == 'R' || charAt == 'W' || charAt == 'S'
			|| charAt == 'K' || charAt == 'M' || charAt == 'D' || charAt == 'V'
			|| charAt == 'H' || charAt == 'B' || charAt == 'X' || charAt == 'N';
	}
}
