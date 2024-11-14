/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2024 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.core.ncbi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NcbiDatasetProcessor {

    private File zipFile;

    public NcbiDatasetProcessor(File zipFile) throws FileNotFoundException {
        if (zipFile == null || !zipFile.exists() || !zipFile.canRead()) {
            throw new FileNotFoundException("The provided file does not exist or is not readable.");
        }
        this.zipFile = zipFile;
    }

    public List<File> process(File outputDirectory) throws IOException {
        List<File> extractedFiles = new ArrayList<>();
        File tempDir = Files.createTempDirectory("ncbi_dataset").toFile();

        // Uncompress the zip file into the temporary directory
        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(tempDir, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    try (InputStream in = zip.getInputStream(entry);
                            FileOutputStream out = new FileOutputStream(entryDestination)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                    }
                }
            }
        }

        // Process subdirectories under ncbi_dataset/ncbi_dataset/data
        File dataDir = new File(tempDir, "ncbi_dataset/data");
        if (dataDir.exists() && dataDir.isDirectory()) {
            File[] subDirs = dataDir.listFiles(File::isDirectory);
            if (subDirs != null) {
                for (File subDir : subDirs) {
                    File[] files = subDir.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            // Move and rename each file to output directory
                            File renamedFile = new File(outputDirectory, subDir.getName() + ".fasta");
                            Files.move(file.toPath(), renamedFile.toPath());
                            extractedFiles.add(renamedFile);
                        }
                    }
                }
            }
        }

        // Return a list of the processed files
        return extractedFiles;
    }

    public static void main(String[] args) {
        args = new String[] { "/home/hlfernandez/Investigacion/Work_In_Progress/SEDA_Fix_NCBI_Rename/ncbi_dataset.zip",
                "/tmp/test" };

        if (args.length < 2) {
            System.out.println("Usage: java NcbiDatasetProcessor <path_to_zip_file> <output_directory>");
            return;
        }

        try {
            File inputFile = new File(args[0]);
            File outputDirectory = new File(args[1]);
            NcbiDatasetProcessor processor = new NcbiDatasetProcessor(inputFile);
            List<File> processedFiles = processor.process(outputDirectory);
            System.out.println("Processed files:");
            for (File file : processedFiles) {
                System.out.println(file.getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error during processing: " + e.getMessage());
        }
    }
}
