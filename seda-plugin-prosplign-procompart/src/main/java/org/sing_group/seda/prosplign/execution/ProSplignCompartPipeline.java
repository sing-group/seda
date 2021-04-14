/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
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
package org.sing_group.seda.prosplign.execution;

import static java.nio.file.Files.createTempDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sing_group.seda.blast.datatype.SequenceType;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.util.FileUtils;

public class ProSplignCompartPipeline {
  public static final String SEDA_PROSPLIGNCOMPART_KEEPTEMPORARYFILES = "seda.prospligncompart.keeptemporaryfiles";

	private ProSplignCompartBinariesExecutor proSplignCompartBinaries;
	private BlastBinariesExecutor blastBinariesExecutor;
  private File queryProteinFasta;
  private DirectoryManager sharedDirectoryManager;

  private boolean shouldPrepareQueryFile = true;
	
	public ProSplignCompartPipeline() {}

  public ProSplignCompartPipeline(
    ProSplignCompartBinariesExecutor proSplignCompartBinaries,
    BlastBinariesExecutor blastBinariesExecutor,
    File queryProteinFasta
  ) throws IOException {
    this.proSplignCompartBinaries = proSplignCompartBinaries;
    this.blastBinariesExecutor = blastBinariesExecutor;
    this.queryProteinFasta = queryProteinFasta;
    this.sharedDirectoryManager = new DirectoryManager();
  }

	public void proSplignCompart(
		File nucleotideSubjectFasta,
		File outputFasta,
		int maxTargetSeqs
	) throws InterruptedException, ExecutionException, IOException {
    try (
      final OperationDirectoryManager operationDirectoryManager =
        new OperationDirectoryManager(
          this.sharedDirectoryManager, nucleotideSubjectFasta
        )
    ) {
      prepareNucleotides(
        nucleotideSubjectFasta,
        operationDirectoryManager.getPreparedNucleotidesFile(),
        operationDirectoryManager.getSubjectMapFile()
      );

      prepareQueryFile(
        queryProteinFasta,
        this.sharedDirectoryManager.getPreparedQueryFile(),
        this.sharedDirectoryManager.getQueryMapFile()
      );

			File workingNucleotidesFasta = operationDirectoryManager.getPreparedNucleotidesFile();
			File workingQueryFasta = this.sharedDirectoryManager.getPreparedQueryFile();

			File databaseFile = operationDirectoryManager.getNucleotidesDbFile(); 
			  
			makeBlastDB(workingNucleotidesFasta, databaseFile);

			File tblastnResultsFile = operationDirectoryManager.getTblastNOutputFile();

			tblastn(workingQueryFasta, databaseFile, tblastnResultsFile, maxTargetSeqs);
			
			File sortedTblastnResultsFile = operationDirectoryManager.getSortedTblastNOutputFile();

			sortTblastNOutputFile(tblastnResultsFile, sortedTblastnResultsFile);
			
			proCompart(sortedTblastnResultsFile, operationDirectoryManager.getProCompartOutputFile());

			proSplign(operationDirectoryManager.getProCompartOutputFile(),
				workingNucleotidesFasta,
				workingQueryFasta,
				operationDirectoryManager.getWorkingDirectory(), "pro");
			
			extractSequences(operationDirectoryManager.getProSplignOutputTxtFile(),
				operationDirectoryManager.getProSplignOutputSequences(),
				operationDirectoryManager.getProSplignOutputCompleteSequences(),
				this.sharedDirectoryManager.getQueryMapFile(),
				operationDirectoryManager.getSubjectMapFile()
			);

			Files.move(operationDirectoryManager.getProSplignOutputSequences().toPath(), outputFasta.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
  }
	
	private static void extractSequences(File proSplignOutputTxtFile,
		File proSplignOutputSequences, File proSplignOutputCompleteSequences,
		File queryMappingFile, File subjectMappingFile
	) {
		try {
			Map<String, String> queryMapping = loadQueryMappingFile(queryMappingFile);
			Map<String, String> subjectMapping = loadSubjectMappingFile(subjectMappingFile);
			
			ProSplignTxtParser parser = new ProSplignTxtParser();
			parser.parse(proSplignOutputTxtFile.toPath(), queryMapping, subjectMapping);
			
			List<String> sequences = parser.getSequences();
			Files.write(proSplignOutputSequences.toPath(), sequences);
			
			
			List<String> completeSequences = parser.getFullSequences();
			Files.write(proSplignOutputCompleteSequences.toPath(), completeSequences);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, String> loadSubjectMappingFile(File subjectMappingFile) {
		return loadQueryMappingFile(subjectMappingFile);
	}

	private static Map<String, String> loadQueryMappingFile(File queryMappingFile) {
		try {
			return Files.readAllLines(queryMappingFile.toPath()).stream()
				.map(line -> line.split("\t"))
				.collect(Collectors.toMap(s -> s[1], s -> s[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyMap();
	}

  private void prepareQueryFile(
    File queryFile,
    File preparedQueryFile, File queryMappingFile
  ) {
    if (!shouldPrepareQueryFile) {
      return;
    }

		try (
			BufferedReader inNucleotidesFile = new BufferedReader(
				new FileReader(queryFile));
			FileWriter outNucleotidesFile = new FileWriter(
				preparedQueryFile);
			FileWriter outMappingFile = new FileWriter(
				queryMappingFile);
			) {

			Map<String, String> queryMapping = new HashMap<>();
			String line;
			int count = 1;

			while((line = inNucleotidesFile.readLine()) != null) {
				if(line.startsWith(">")) {
					queryMapping.put(line.replace(">", ""), Integer.toString(count));
					outNucleotidesFile.write(">" + (count++));
				} else {
					outNucleotidesFile.write(line);
				}
				outNucleotidesFile.write("\n");
				if(count % 100 == 0) {
					outNucleotidesFile.flush();
				}
			}
			
			for(Entry<String, String> e : queryMapping.entrySet()) {
				outMappingFile.write(e.getKey() + "\t" + e.getValue() + "\n");
			}
      this.shouldPrepareQueryFile = false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	private void prepareNucleotides(File nucleotidesFile,
		File preparedNucleotidesFile, File subjectMapFile) {

		try (
			BufferedReader inNucleotidesFile = new BufferedReader(
				new FileReader(nucleotidesFile));
			FileWriter outNucleotidesFile = new FileWriter(
				preparedNucleotidesFile);
			FileWriter outSubjectMapFile = new FileWriter(
				subjectMapFile);
		) {
			Map<String, String> subjectMapping = new HashMap<>();
			String line;
			int count = 1;

			while((line = inNucleotidesFile.readLine()) != null) {
				if(line.startsWith(">")) {
					subjectMapping.put(line.replace(">", ""), Integer.toString(count));
					outNucleotidesFile.write(">gi|" + (count++) + "|");
				} else {
					outNucleotidesFile.write(line);
				}
				outNucleotidesFile.write("\n");
				if(count % 100 == 0) {
					outNucleotidesFile.flush();
				}
			}

			for(Entry<String, String> e : subjectMapping.entrySet()) {
				outSubjectMapFile.write(e.getKey() + "\t" + e.getValue() + "\n");
			}
			inNucleotidesFile.close();
			outNucleotidesFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sortTblastNOutputFile(File tblastNOutputFile, File sortedTblastNOutputFile) {
		try (Stream<String> fileStream = Files.lines(tblastNOutputFile.toPath())) {
			final String[] sortedLines = fileStream
				.map(line -> line.split("\t"))
				.sorted((line1, line2) -> compareLines(line1, line2))
				.map(line -> String.join("\t", line))
				.toArray(String[]::new);
			
			Files.write(sortedTblastNOutputFile.toPath(), Arrays.asList(sortedLines));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int compareLines(String[] line1, String[] line2) {
		if(line1[1].equals(line2[1])) {
			return line1[0].compareTo(line2[0]);
		} else {
			return line1[1].compareTo(line2[1]);
		}
	}

	private void proCompart(File tblastNDatabaseFile,
		File proCompartOutputfile) throws ExecutionException, InterruptedException, IOException {
	  this.proSplignCompartBinaries.proCompart(tblastNDatabaseFile, proCompartOutputfile);
	}

  private void proSplign(
    File compart, File nucleotidesFasta,
    File proteinQueryFasta, File outputDir, String outputFileName
  )
    throws ExecutionException, InterruptedException, IOException {
    this.proSplignCompartBinaries.proSplign(compart, nucleotidesFasta, proteinQueryFasta, outputDir, outputFileName);
  }

  protected void makeBlastDB(
    final File fastaFile,
    final File dbFile
  ) throws InterruptedException, ExecutionException, IOException {
    this.blastBinariesExecutor.makeBlastDb(fastaFile, SequenceType.NUCLEOTIDES.getBlastName(), dbFile, false);
  }

  protected void tblastn(
    final File queryFile,
    final File database,
    File outputFile,
    int maxTargetSeqs
  ) throws IllegalStateException,
    IOException, InterruptedException, ExecutionException {
    
    this.blastBinariesExecutor.executeBlast(
      BlastType.TBLASTN,
      queryFile, database,
      0.05, maxTargetSeqs,
      outputFile,
      "6"
    );
  }
  
  public void clearTemporaryFiles() throws IOException {
    FileUtils.deleteIfExists(this.sharedDirectoryManager.getWorkingDirectory());
  }

  protected static class DirectoryManager {
    private final Path workingDirectory;

    public DirectoryManager() throws IOException {
      this.workingDirectory = createTempDirectory("seda_prosplign_procompart");
    }

    public File getWorkingDirectory() {
      return this.workingDirectory.toFile();
    }
    
    public File getPreparedQueryFile() {
      return new File(this.getWorkingDirectory(), "query.fa");
    }

    public File getQueryMapFile() {
      return new File(this.getWorkingDirectory(), "query-mapping");
    }
  }
  
  protected static class OperationDirectoryManager implements AutoCloseable {
    private final DirectoryManager directoryManager;
    private final File workingDirectory;
    private File nucleotideFasta;

    public OperationDirectoryManager(DirectoryManager directoryManager, File nucleotideFasta) throws IOException {
      this.directoryManager = directoryManager;
      this.nucleotideFasta = nucleotideFasta;
      this.workingDirectory =
        Files.createTempDirectory(
          this.directoryManager.getWorkingDirectory().toPath(), "process_" + nucleotideFasta.getName()
        ).toFile();
    }

    private File getWorkingDirectory() {
      return workingDirectory;
    }
    
    public File getPreparedNucleotidesFile() {
      return new File(this.workingDirectory, "subject.fa");
    }

    public File getSubjectMapFile() {
      return new File(this.workingDirectory, "subject-mapping");
    }

    public File getNucleotidesDbFile() {
      return new File(this.workingDirectory, nucleotideFasta.getName());
    }

    public File getProSplignOutputSequences() {
      return new File(this.getWorkingDirectory(), "pro.fasta");
    }

    public File getProSplignOutputCompleteSequences() {
      return new File(this.getWorkingDirectory(), "pro-complete-sequences.fasta");
    }

    public File getProCompartOutputFile() {
      return new File(this.getWorkingDirectory(), "comp");
    }

    public File getProSplignOutputTxtFile() {
      return new File(this.getWorkingDirectory(), "pro.txt");
    }

    public File getProSplignOutputAsnFile() {
      return new File(this.getWorkingDirectory(), "pro.asn");
    }

    public File getTblastNOutputFile() {
      return new File(this.getWorkingDirectory(), "tblastn.out");
    }

    public File getSortedTblastNOutputFile() {
      return new File(this.getWorkingDirectory(), "tblastn.sorted.out");
    }

    @Override
    public void close() throws IOException {
      if (!Boolean.valueOf(System.getProperty(SEDA_PROSPLIGNCOMPART_KEEPTEMPORARYFILES, "false"))) {
        FileUtils.deleteIfExists(this.workingDirectory);
      }
    }
  }
}
