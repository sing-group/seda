Operations
**********

This section provides an overview on the different processing operations available in SEDA.

Alignment-related
=================

Clustal Omega alignment
-----------------------

This operation permits using Clustal Omega (http://www.clustal.org/omega/) to align the input FASTA files.

First, the *‘Clustal Omega configuration’* area allows to select the execution mode of Clustal Omega: *system binary* indicates that Clustal Omega will be executed directly using its binary and *Docker image* means that a Docker image will be used instead.

In the *system binary* mode, the path to the Clustal Omega binary file must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If the Clustal Omega binary is in the path (*clustalo* in Unix systems and *clustalo.exe* in Windows systems), then this can be empty and the *Check binary* would say that it is right.

.. figure:: images/operations/clustal-omega-alignment/1.png
   :align: center

In the *Docker image* mode, the default image is already set, although it is possible to choose a custom one provided that it has the Clustal Omega binary in the system path.

.. figure:: images/operations/clustal-omega-alignment/2.png
   :align: center

The configuration panel also allows to choose:

- *Num. threads*: the number of threads to use.
- *Additional parameters*: additional parameters for the Clustal Omega alignment.

.. figure:: images/operations/clustal-omega-alignment/3.png
   :align: center

Concatenate sequences
---------------------

This operation allows all the selected input FASTA files to be merged into a single output FASTA by concatenating equivalent sequences. The *‘Name’* parameter defines the name for the output file and the *‘Merge descriptions’* parameter specifies whether the sequence descriptions must be added to the concatenated sequences or not.

The *‘Sequence matching mode‘* parameter defines how sequence headers are processed in order to match those equivalent sequences that should be concatenated:

- *'Sequence name'* means that the sequences are "concatenated if they have the same sequence names (identifiers).
- *'Regular expression'* means sequences are concatenated by matching headers using the configuration specified in the *Header matcher configuration* panel.

Additionally, you can specify the FASTA format parameters in the *‘Reformat output file’* area (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

.. figure:: images/operations/concatenate-sequences/1.png
   :align: center

Regarding the *Header matcher configuration* panel, this option allows to configure the regular expression configuration to match the sequence headers that must be concatenated using the following options:

- *String to match*: the regular expression that must be matched in the sequence header.
- *Case sensitive?*: whether the string must be matched as case sensitive or not.
- *Quote pattern?*: whether the regular expression pattern must be quoted or not. When the regular expression is quoted, metacharacters or escape sequences in it will be given no special meaning.
- *Regex group?*: the regular expression group that must be extracted. Default value is *0*, meaning that the entire result must be considered. Use values higher than 0 when there are brackets in the regular expression in order to select the desired group.
- *Header target?*: the part of the sequence header where the string must be found.

Examples
++++++++

The following example illustrates how sequences with the same sequence names in the input FASTA files 1 and 2 are concatenated and written as single output FASTA.

Input 1:

.. code-block:: console

 >Homo_sapiens Part_1
 AAAATTTT
 >Mus_musculus Part_1
 ACTGACTG

Input 2:

.. code-block:: console

 >Homo_sapiens Part_2
 CCCCGGGG
 >Mus_musculus Part_2
 GTCAGTCA

Output:

.. code-block:: console

 >Homo_sapiens
 AAAATTTTCCCCGGGG
 >Mus_musculus
 ACTGACTGGTCAGTCA

If the *‘Merge descriptions’* parameter is selected, then the sequence descriptions (*Part_1* and *Part_2* in this example) are added to the output:

.. code-block:: console

 >Homo_sapiens [Part_1, Part_2]
 AAAATTTTCCCCGGGG
 >Mus_musculus [Part_1, Part_2]
 ACTGACTGGTCAGTCA

On the other hand the *'Regular expression'* matching mode allows more complex concatenations. For instance, it can be used in those scenarios where sequences from two or more species are mixed in several FASTA files and one FASTA file containing the equivalent sequences is wanted. Consider the input FASTA files below that contains sequences from three species: *Homo sapiens*, *Gallus gallus*, and *Mus musculus*. When it is processed using the configuration below, one output FASTA file is obtained. Basically, the regular expression *^[^_]*_[^_]** is able to extract the common species names from the headers so that sequences are concatenated based in them.

.. figure:: images/operations/concatenate-sequences/2.png
   :align: center

Input 1:

.. code-block:: console

 >Homo_sapiens_1
 AT
 >Mus_musculus_1
 TT
 >Gallus_gallus_1
 GG

Input 2:

.. code-block:: console

 >Homo_sapiens_2
 CG
 >Mus_musculus_2
 AA
 >Gallus_gallus_2
 CC

Output:

.. code-block:: console

 >Homo_sapiens
 ATCG
 >Mus_musculus
 TTAA
 >Gallus_gallus
 GGCC

Consensus sequence
------------------

This operation permits the creation of a consensus sequence from a set of sequences of the same length. The consensus sequence can be constructed in two ways:

1. Considering the most frequent nucleotide (DNA) or amino acid (protein) bases found at each position of the given set of sequences.
2. Considering all the nucleotide (DNA) or amino acid (protein) bases with a frequence above a defined threshold at each position of the given set of sequences.

The configuration panel allows to choose:

- *Sequence type*: the type of sequences in the selected files. For nucleotide sequences, ambiguous positions are indicated using the IUPAC ambiguity codes (http://www.dnabaser.com/articles/IUPAC%20ambiguity%20codes.html). For protein sequences, ambiguous positions are indicated as the *’Verbose’* option explains.
- *Consensus bases*: the strategy for selecting the bases at each position that should be considered to create the consensus. It can be one of:

    - *Most frequent*: considers the most frequent nucleotide (DNA) or amino acid (protein) bases at each position. Those positions where the most frequent base is under the *Minimum presence* threshold are represented by an *N* (nucleotide sequences) or *X* (protein sequences) in the consensus sequence.
    - *Above threshold*: considers all nucleotide (DNA) or amino acid (protein) bases with a frequence above the *Minimum presence* threshold at each position. Those positions where all base frequencies are below the *Minimum presence* threshold are represented by an *N* (nucleotide sequences) or *X* (protein sequences) in the consensus sequence.

- *Minimum presence*: the minimum presence for a given nucleotide or amino acid in order to be part of the consensus sequence. Read the *Consensus bases* description to understand how this option is used in each case.
- *Verbose*: in protein sequences, when this option is unselected then *X* is used for ambiguous positions in the consensus sequence. On the other hand, when this option is selected, then all amino acids in such positions are reported (e.g. [HWY]).
- *Reformat output file*: allows to specify the format parameters of the output FASTA containing the consensus sequence (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

.. figure:: images/operations/consensus-sequence/1.png
   :align: center

Examples (*Most frequent*)
++++++++++++++++++++++++++

The following example shows how nucleic acid sequences in the input FASTA are processed to create a consensus sequence with the most frequent bases using two different minimum presence thresholds: 0.2 and 0.6.

Input:

.. code-block:: console

 >Sequence1
 ACCA-C
 >Sequence2
 ACCC-C
 >Sequence3
 ATCT-A
 >Sequence4
 AGGG-A

Output (0.2):

.. code-block:: console

 >consensus
 ACCN-N

Output (0.6):

.. code-block:: console

 >consensus
 ANCN-N

The following example shows how protein sequences in the input FASTA are processed to create a consensus sequence using a threshold of 0.4 and both verbose and not verbose options.

Input:

.. code-block:: console

 >Sequence1
 SSSS
 >Sequence2
 PSSS
 >Sequence3
 HPHS
 >Sequence4
 QPQQ

Output (verbose):

.. code-block:: console

 >consensus
 X[SP]SS

Output (not verbose):

.. code-block:: console

 >consensus
 XXSS

Examples (*Above threshold*)
++++++++++++++++++++++++++++

The following example shows how nucleic acid sequences in the input FASTA are processed to create a consensus sequence with the bases above two different minimum presence thresholds: 0 and 0.5.

Input:

.. code-block:: console

 >Sequence1
 AAAA
 >Sequence2
 AAAA
 >Sequence3
 AACT
 >Sequence4
 ACCT
 >Sequence5
 ACTC
 >Sequence6
 ACTG

Output (0):

.. code-block:: console

 >consensus
 AMHN

Output (0.5):

.. code-block:: console

 >consensus
 AMNN

The following example shows how protein sequences in the input FASTA are processed to create a consensus sequence with the bases above two different minimum presence thresholds (0 and 0.5) and using both verbose and not verbose options.

Input:

.. code-block:: console

 >Sequence1
 AAAA
 >Sequence2
 AAAA
 >Sequence3
 AACT
 >Sequence4
 ACCT
 >Sequence5
 ACTC
 >Sequence6
 ACTG

Output (verbose, 0):

.. code-block:: console

 >consensus
 A[AC][ACT][ACTG]

Output (not verbose, 0):

.. code-block:: console

 >consensus
 AXXX

Output (verbose, 0.5):

.. code-block:: console

 >consensus
 A[AC]XX

Output (not verbose, 0.5):

.. code-block:: console

 >consensus
 AXXX

Trim alignment
--------------

This operation allows trimming a set of sequence alignments (i.e. selected input FASTA files) by removing alignment gap stretches at the beginning and end of each alignment. Additionally, you can specify the FASTA format parameters in the *‘Reformat output files’* area (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

.. figure:: images/operations/trim-alignment/1.png
   :align: center

The following example shows how the sequence alignments in the input FASTA file are trimmed to remove all gap stretches.

Input:

.. code-block:: console

 >Sequence1
 ----TGCTAGCTAGTGATCGCATGCT
 >Sequence2
 GCTAGCTAGTGATCGCATGCTC----
 >Sequence3
 -CTAGCTAGTGATCGCATGCTCAG--
 >Sequence4
 ----GCTAGTGATCGCATGCTCA---
 >Sequence5
 --GCTAGTGATCGCATGCTCAGGAA-
 >Sequence6
 ATGGCTAGTGATCGCATGCTCAGGAA

Output:

.. code-block:: console

 >Sequence1
 TGCTAGCTAGTGATCGCA
 >Sequence2
 GCTAGTGATCGCATGCTC
 >Sequence3
 GCTAGTGATCGCATGCTC
 >Sequence4
 GCTAGTGATCGCATGCTC
 >Sequence5
 TAGTGATCGCATGCTCAG
 >Sequence6
 CTAGTGATCGCATGCTCA

Undo alignment
--------------

This operation allows undoing a sequence alignment by removing ‘-’ from sequences. Additionally, you can specify the FASTA format parameters in the *‘Reformat output files’* area (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

.. figure:: images/operations/undo-alignment/1.png
   :align: center

The following example shows how ‘-’ are removed from the sequences in the input FASTA file.

Input:

.. code-block:: console

 >Sequence1
 ATGGTCCATGGGTACAAAGGGGT
 >Sequence2
 ATGGTCCAT--GTACAAAGGGG-
 >Sequence3
 -TGGTCCA-GGGTACAAAGGGG-

Output:

.. code-block:: console

 >Sequence1
 ATGGTCCATGGGTACAAAGGGGT
 >Sequence2
 ATGGTCCATGTACAAAGGGG
 >Sequence3
 TGGTCCAGGGTACAAAGGGG

BLAST
=====

BLAST
-----

This operation allows performing different BLAST queries using the selected FASTA files. Regarding the database to use in the queries, there are two possible modes: querying against all the selected FASTA files or querying against each FASTA file separately. Regarding the query, there are also two possibilities: using the sequences in one of the selected FASTA as queries or using the sequences in an external FASTA file as queries. When performing this operation, one BLAST query is executed for each sequence in the FASTA file.

The figure below illustrates the process followed when a query against all selected FASTA files is performed. Firstly, one BLAST database is created for each selected FASTA file. Then, one alias referencing to all the databases created before is created. Finally, each sequence in the FASTA file used as query source is executed against the alias. As a result, this mode creates as many output files as sequences in the FASTA file. To create these output files, the sequences where hits were found are retrieved from the database.

.. figure:: images/operations/blast/1.png
   :align: center

On the other hand, the figure below shows the process followed when queries against each selected FASTA file are executed separately. Firstly, one BLAST database is created for each selected FASTA file. Then, each sequence in the FASTA file used as query source is executed against each of the databases. As a result, this mode creates as many output files as sequences in the FASTA file multiplied by the number of selected FASTA files. To create these output files, the sequences where hits were found are retrieved from the corresponding database.

.. figure:: images/operations/blast/2.png
   :align: center

Configuration
+++++++++++++

First, the *‘BLAST configuration’* area allows to select the execution mode of BLAST: *system binary* indicates that BLAST will be executed directly using its binaries and *Docker image* means that a Docker image will be used instead.

In the *system binary* mode, the path where the BLAST binaries (makeblastdb, blastdb_aliastool, blastdbcmd, blastp, blastn, blastx, tblastn, and tblastx) are located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If they are available in the system path, just click the *‘Check binary’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/blast/3.png
   :align: center

In the *Docker image* mode, the default image is already set, although it is possible to choose a custom one provided that it has the BLAST binaries in the system path.

.. figure:: images/operations/blast/4.png
   :align: center

Then, the *‘DB configuration’* area allows to control some aspects related with the databases created in the process. The type of the database is automatically selected according to the BLAST type to execute. This area allows to indicate whether the databases and alias must be stored in a directory of your choice. Otherwise, temporary directories are used and they are deleted at the end of the process. Nevertheless, it may be interesting to store the databases for two reasons: use them again in SEDA or use them in BDBM (BLAST DataBase Manager, http://www.sing-group.org/BDBM/). SEDA can reuse databases since if databases with the same name exist in the selected directory they are not created again.

.. figure:: images/operations/blast/5.png
   :align: center

Finally, the *‘Query configuration’* area allows to control how queries are performed. As explained before, first you must choose the query mode in the *‘Query against’* parameter. Secondly, you must choose the BLAST type that you want to perform using the *‘BLAST type’* parameter. By selecting the BLAST type: (*i*) the type of database is automatically determined, and (*ii*) if *blastx* or *tblastn* types are selected, then you will only be allowed to select a query from an external file because the selected files used to construct the database cannot be used as query (blastx uses a database of proteins and a query of nucleotides and tblastn uses a database of nucleotides and a query of proteins).

Thirdly, the *‘Query source’* allows to select the source of the query file:

- *From selected file*: this option allows to select one of the selected files in SEDA using the *‘File query’* combobox.
- *From external file*: this option allows to select an external FASTA file to be used as query file.

Then, three parameters allow to control the query execution:

- *E-value*: the E-value threshold for saving hits.
- *Max. target. seqs*: the maximum number of aligned sequences to keep.
- *Additional parameters*: additional parameters for the BLAST command.

And finally, the *‘Extract only hit regions’* parameter allows to define how output sequences are obtained. By default, this option is not selected, meaning that the whole subject sequences where hits were found are used to construct the output FASTA files. If this option is selected, then only the part of the subject sequences where the hits were produced are used to construct the output FASTA files. Within this option, the *‘Hit regions window’* parameter allows to specify the number of bases before and after the hit region that should be retrieved.

.. figure:: images/operations/blast/6.png
   :align: center

BLAST: two-way ortholog identification
--------------------------------------

This operation allows finding the orthologs of a given sequence in a set of FASTA files. The figure below illustrates the process followed by this operation. For each sequence in a reference FASTA, this operation looks for its orthologs in the set of genomes. For each sequence in the reference FASTA, the following process is applied:

1. A BLAST query against the first FASTA (hereafter, the target FASTA) is performed using the reference sequence as query. Only the first hit is considered.
2. The sequence associated to the first hit in the target FASTA is used as query in a second BLAST query against the reference FASTA. Again, only the first is considered.
3. The sequence associated to the first hit in the reference FASTA is compared to the iteration sequence:

	A. If both sequences are the same, then the sequence found in step 2 is reported as ortholog.
	B. If both sequences are different, then the sequence found in step 2 is reported as ortholog if the *Report non-exact orthologues* is being used.

4. Steps 1 to 3 are repeated for each target FASTA available.

.. figure:: images/operations/blast-two-way/1.png
   :align: center

Configuration
+++++++++++++

First, the *‘BLAST configuration’* area allows to select the execution mode of BLAST: *system binary* indicates that BLAST will be executed directly using its binaries and *Docker image* means that a Docker image will be used instead.

In the *system binary* mode, the path where the BLAST binaries (makeblastdb, blastdb_aliastool, blastdbcmd, blastp, blastn, blastx, tblastn, and tblastx) are located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If they are available in the system path, just click the *‘Check binary’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/blast-two-way/2.png
   :align: center

In the *Docker image* mode, the default image is already set, although it is possible to choose a custom one provided that it has the BLAST binaries in the system path.

.. figure:: images/operations/blast-two-way/3.png
   :align: center

Then, the *‘DB configuration’* area allows to control some aspects related with the databases created in the process. The type of the database is automatically selected according to the BLAST type to execute. This area allows to indicate whether the databases must be stored in a directory of your choice. Otherwise, temporary directories are used and they are deleted at the end of the process. Nevertheless, you may be interested in storing the databases because SEDA can reuse them in the future: if databases with the same name exist in the selected directory they are not created again.

.. figure:: images/operations/blast-two-way/4.png
   :align: center

Finally, the *‘Query configuration’* area allows to control how queries are performed. First, you can choose the ortholog report mode using the *‘Mode‘* parameter and choose *‘Report exact orthologues’* or *‘Report non-exact orthologues’*. Secondly, you must choose the BLAST type that you want to perform using the *‘BLAST type’* parameter. By selecting the BLAST type: (*i*) the type of database is automatically determined, and (*ii*) if *blastx* or *tblastn* types are selected, then you will only be allowed to select a query from an external file because the selected files used to construct the database cannot be used as query (blastx uses a database of proteins and a query of nucleotides and tblastn uses a database of nucleotides and a query of proteins).

Thirdly, the *‘Query source’* allows to select the source of the query file:

- *From selected file*: this option allows to select one of the selected files in SEDA using the *‘File query’* combobox.
- *From external file*: this option allows to select an external FASTA file to be used as query file.

And finally, three parameters allow to control the query execution:

- *E-value*: the E-value threshold for saving hits.
- *Additional parameters*: additional parameters for the BLAST command.
- *Num. threads*: number of threads to use. Using several threads allow SEDA to execute several BLAST queries in parallel (i.e. several query vs. reference FASTA queries) and reduce running time.

.. figure:: images/operations/blast-two-way/5.png
   :align: center

NCBI BLAST
----------

This operation allows performing a BLAST query through the NCBI web server (https://blast.ncbi.nlm.nih.gov/Blast.cgi).

.. Note::
   To meet the NCBI usage guidelines and to avoid problems, this operation limits users to query one sequence at a time, thus the operation can be executed using only one selected FASTA file containing exactly one sequence.

By using the configuration panel shown below, you can select the BLAST program to execute, the NCBI database to query against, and the desired output. This output can be one of: *'Complete sequences'*, to create a FASTA file with the complete sequences of each sequence that has an alignment against the query sequence, or *'Aligned sequences'*, to create a FASTA file with the portions of the sequences aligned against the query.

.. Note::
   The *'Complete sequences'* retrieves the complete sequence by downloading the sequence identifiers of the matches from their corresponding NCBI databases. This may cause the operation to last longer, specially when these sequences are big.

.. figure:: images/operations/blast-ncbi/1.png
   :align: center

In addition, this operation have the following optional parameters:

- *Matrix*: the scoring matrix.
- *Filter*: whether to use a low complexity filtering or not.
- *Expect value*: the expect value.
- *Hit list size*: the number of databases sequences to keep.
- *Word size*: the size of word for initial matches.
- *Threshold*: the neighboring score for initial words. This parameter does not apply to BLASTN or MegaBLAST.

UniProt BLAST
-------------

This operation allows performing a BLAST query through the UniProt web server (https://www.uniprot.org/blast/).

.. Note::
   To meet the UniProt / EMBL-EBI usage guidelines and to avoid problems, this operation limits users to query one sequence at a time, thus the operation can be executed using only one selected FASTA file containing exactly one sequence.

By using the configuration panel shown below, you can select the specific database to query against and the desired output. This output can be one of: *'Complete sequences'*, to create a FASTA file with the complete sequences of each sequence that has an alignment against the query sequence, or *'Aligned sequences'*, to create a FASTA file with the portions of the sequences aligned against the query.

.. Note::
   The *'Complete sequences'* retrieves the complete sequence by downloading the sequence identifiers of the matches from UniProt. This may cause the operation to last longer, specially when these sequences are big.

.. figure:: images/operations/blast-uniprot/1.png
   :align: center

In addition, this operation have the following optional parameters:

- *E-Theshold*: the expectation value.
- *Matrix*: the scoring matrix.
- *Filtering*: whether to use a low complexity filtering or not.
- *Gapped*: whether the query is gapped or not.
- *Hits*: the number of alignments to retrieve.

.. _operations-pattern-filtering:

Filtering
=========

Base presence filtering
-----------------------

This operation permits filtering sequences based on the percentages of their nucleotides or amino acids. By using the configuration panel shown below, you can add one or more nucleotides or amino acids and specify their minimum and maximum percentages. Sequences with units whose percentage of presence is outside the specified thresholds are removed. Moreover, if you specify several units in a single row then the sum of each percentage is used for checking the thresholds.

.. figure:: images/operations/base-presence-filtering/1.png
   :align: center

Examples
++++++++

Consider the following input FASTA file with two sequences:

Input:

.. code-block:: console

 >Sequence1
 AAAAAACCCCCTTTGGGA
 >Sequence2
 AAAAAACCCTGGNNNNNN

The percentages of presence of sequence units are:

- Sequence1:

  - A: 0.38 (7/18)
  - C: 0.27(5/18)
  - T: 0.16 (3/18)
  - G: 0.16 (3/18)

- Sequence2:

  - A: 0.33 (6/18)
  - C: 0.16 (3/18)
  - T: 0.05 (1/18)
  - G: 0.11 (2/18)
  - N: 0.33 (6/18)

For instance, to filter the input FASTA in order to obtain only those sequences with a percentage of A’s between 0.35 and 0.40, the following configuration should be used. In this case, only the first sequence will be in the output file.

.. figure:: images/operations/base-presence-filtering/2.png
   :align: center

For instance, to filter the input FASTA in order to obtain only those sequences with a percentage of T’s or G’s between 0.10 and 0.20, the following configuration should be used. In this case, only the second sequence will be in the output file since the sum of T’s and G’s is 0.16 while in the first sequence is 0.32.

.. figure:: images/operations/base-presence-filtering/3.png
   :align: center

Filtering
---------

This operation allows filtering sequences based on different criteria (e.g. sequence length, non-multiple of three, or in-frame stop codons presence, among others).

The image below shows the configuration panel of the *Filtering operation*. If more than one option is selected, they are applied in the following order:

1. Valid starting codons: filters sequences and keeps only those starting with the selected codons.
2. Remove sequences with a non-multiple of three size: filters sequences and keeps only those having a length that is multiple of 3.
3. Remove sequences with in-frame stop codons: filters sequences and keeps only those without in-frame stop codons.
4. Minimum sequence length: filters sequences and keeps only those with the specified minimum sequence length. A value of 0 indicates that no minimum sequence length is required.
5. Maximum sequence length: filters sequences and keeps only those with the specified maximum sequence length. A value of 0 indicates that no minimum sequence length is required.
6. If the header count filtering option is selected at the sequences level, then it filters sequences and keeps (or removes) only those meeting the specified criteria regarding header counts. See the examples to learn how to use this filter.
7. Minimum number of sequences: filters files and keeps only those with the specified minimum number of sequences.
8. Maximum number of sequences: filters files and keeps only those with the specified maximum number of sequences.
9. If the header count filtering option is selected at the files level, then it filters files and keeps (or removes) only those where all sequences meet the specified criteria regarding header counts. See the examples to learn how to use this filter.
10. Remove by size difference: filters sequences and keeps only those with the specified difference when compared to the reference sequence.

  a)	Maximum size difference (%): the maximum sequence length difference allowed expressed as a percentage.
  b)	Reference sequence index: the index of the sequence to use as reference to compare to others. The first sequence corresponds to index 1. This option is ignored if a reference sequence file (next option) is selected.
  c)	Reference sequence file: the file containing the sequence to use as reference to compare to others. If a file is selected, then the reference sequence index is ignored.

.. figure:: images/operations/filtering/1.png
   :align: center

Examples
++++++++

Valid starting codons
^^^^^^^^^^^^^^^^^^^^^

By clicking on the *‘Codons‘* label, a list with the possible starting codons is shown, allowing to select one or more starting codons.

.. figure:: images/operations/filtering/2.png
   :align: center

The following example shows how the input FASTA is filtered to keep only those starting with *ATG*.

Input:

.. code-block:: console

 >Sequence1
 TGCCAGAGAACTGCCGGTGTGGTG
 >Sequence2
 ATGTCTTCCATTAAGATTGAGTGT
 >Sequence3
 GCACCAGGGGGCCCTGTACTCCCT

Output:

.. code-block:: console

 >Sequence2
 ATGTCTTCCATTAAGATTGAGTGT

Remove sequences with a non-multiple of three size
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This example shows how sequences with a non-multiple of three size are removed from the input FASTA. Only *Sequence1* and *Sequence2*, with 15 bases, appears in the output FASTA. *Sequence3* is removed since it has 17 bases.

Input:

.. code-block:: console

 >Sequence1
 CATTAAGATTGAGTG
 >Sequence2
 AATTAAGATTGAGAA
 >Sequence3
 CATTAAGATTGAGTGCTG

Output:

.. code-block:: console

 >Sequence1
 CATTAAGATTGAGTG
 >Sequence2
 AATTAAGATTGAGAA

Remove sequences with in-frame stop codons
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This example shows how sequences containing in-frame stop codons are removed from the input FASTA. Only *Sequence2* does not contain in-frame stop codons, so that it is the only one in the output FASTA.

Input:

.. code-block:: console

 >Sequence1
 CATTAAGATTGAGTG
 >Sequence2
 CATTCGGATTGAGTG

Output:

.. code-block:: console

 >Sequence2
 CATTCGGATTGAGTG

Minimum sequence length
^^^^^^^^^^^^^^^^^^^^^^^

This example shows how sequences with a length below 7 are removed from the input FASTA. Thus, only "Sequence3", with 15 bases, appears in the output FASTA. "Sequence1" and "Sequence2" are removed since they have 4 and 6 bases respectively.

Input:

.. code-block:: console

 >Sequence1
 CATT
 >Sequence2
 CATTAT
 >Sequence3
 CATTAAGATTGAGTG

Output:

.. code-block:: console

 >Sequence3
 CATTAAGATTGAGTG

Maximum sequence length
^^^^^^^^^^^^^^^^^^^^^^^

This example shows how sequences with a length above 5 are removed from the input FASTA. Thus, only *Sequence1*, with 4 bases, appears in the output FASTA. *Sequence2* and *Sequence3*  are removed since they have 6 and 15 bases respectively.

Input:

.. code-block:: console

 >Sequence1
 CATT
 >Sequence2
 CATTAT
 >Sequence3
 CATTAAGATTGAGTG

Output:

.. code-block:: console

 >Sequence1
 CATT

Remove by size difference
^^^^^^^^^^^^^^^^^^^^^^^^^

This example shows how sequences with a length difference compared to the first sequence (Reference sequence index = 1) less than 10% are removed from the input FASTA. Sequence lengths and the differences compared to the reference sequence are:

- *Sequence1*: 25 bases.
- *Sequence2*: 24 bases. Difference: 1 → 1/25: 4%.
- *Sequence3*: 23 bases. Difference: 2 → 2/25: 8%.
- *Sequence4*: 22 bases. Difference: 3 → 3/25: 12%.
- *Sequence5*: 21 bases. Difference: 4 → 4/25: 16%.

Thus, only *Sequence1*, *Sequence2* and *Sequence3* are kept in the output FASTA.

Input:

.. code-block:: console

 >Sequence1
 TGCCAGAGAACTGCCGGTGTGGTGA
 >Sequence2
 TGCCAGAGAACTGCCGGTGTGGTA
 >Sequence3
 TCGCCAGCGCCCTCGGCCACACA
 >Sequence4
 TCGCCAGCGCCCTCGGCCACAA
 >Sequence5
 TCGCCAGCGCCCTCGGCCACA

Output:

.. code-block:: console

 >Sequence1
 TGCCAGAGAACTGCCGGTGTGGTGA
 >Sequence2
 TGCCAGAGAACTGCCGGTGTGGTA
 >Sequence3
 TCGCCAGCGCCCTCGGCCACACA

Header count filtering (I)
^^^^^^^^^^^^^^^^^^^^^^^^^^

This example shows how to use this filter in order to keep all sequences in the input FASTA whose sequence identifier appears exactly two times among all sequences.

.. figure:: images/operations/filtering/3.png
   :align: center

By using the configuration above, only *Sequence1* and *Sequence3* are kept in the output FASTA. If the same is applied at the files level, then the input FASTA would not appear in the output directory.

Input:

.. code-block:: console

 >Sequence1
 TGCCAGAGAACTGCCGGTGTGGTGA
 >Sequence1
 TGCCAGAGAACTGCCGGTGTGGTGG
 >Sequence2
 AAAAACTGGAAAAAACTGGAAAACC
 >Sequence3
 TCGCCAGCGCCCTCGGCCACAGA
 >Sequence3
 TCGCCAGCGCCCTCGGCCACATG

Output:

.. code-block:: console

 Sequence1
 TGCCAGAGAACTGCCGGTGTGGTGA
 >Sequence1
 TGCCAGAGAACTGCCGGTGTGGTGG
 >Sequence3
 TCGCCAGCGCCCTCGGCCACAGA
 >Sequence3
 TCGCCAGCGCCCTCGGCCACATG

Header count filtering (II)
^^^^^^^^^^^^^^^^^^^^^^^^^^^

This example shows how to use this filter in order to keep all sequences in the input FASTA for which a word defined by a regular expression does not appear one or two times.

Input:

.. code-block:: console

 >Homo_sapiens_1
 TGCCAGAGAACTGCCGGTGTGGTGA
 >Homo_sapiens_2
 TGCCAGAGAACTGCCGGTGTGGTGG
 >Homo_sapiens_3
 AAAAACTGGAAAAAACTGGAAAACC
 >Mus_musculus_1
 TCGCCAGCGCCCTCGGCCACAGA
 >Gallus_gallus_1
 TCGCCAGCGCCCTCGGCCACATG
  >Gallus_gallus_2
 TCGCCAGCGCCCTCGGCCACATG

By using the configuration below to filter the input FASTA above, the regular expression  *^[^_]*_[^_]** splits the sequences in three groups:

- Those containing *Homo_sapiens*: *Homo_sapiens_1*, *Homo_sapiens_2*, and *Homo_sapiens_3*.
- Those containing *Mus_musculus*: *Mus_musculus_1*.
- Those containing *Gallus_gallus*: *Gallus_gallus_1* and *Gallus_gallus_2*.

.. figure:: images/operations/filtering/4.png
   :align: center

The operation filters the sequences so that only those for which their corresponding groups have a size between 1 and 2 are present in the output FASTA.

Output:

.. code-block:: console

 >Mus_musculus_1
 TCGCCAGCGCCCTCGGCCACAGA
 >Gallus_gallus_1
 TCGCCAGCGCCCTCGGCCACATG
 >Gallus_gallus_2
 TCGCCAGCGCCCTCGGCCACATG

Pattern filtering
-----------------

This operation allows filtering sequences based on a text pattern (note that this pattern can be also a regular expression, see section :ref:`Pattern configuration<advanced-pattern-configuration>` for further details). Filtering can be applied to either sequence headers or the sequence contents.

The image below shows the configuration panel of the *Pattern filtering* operation. This configuration panel allows to configure how the pattern filtering is applied:

- *Header* or *Sequence*: check Sequence to look for the pattern on the sequence content or Header to look for the pattern on the sequence header.
- *Convert to amino acid sequence before pattern matching*: when filtering sequences based on the sequence content, it is also possible to indicate that the sequences must be converted to amino acid sequences before applying the pattern. See below for further information on this configuration. Please note that nucleotide sequences containing ambiguity codes will not be translated generating an error.
- *Pattern*: SEDA allows to define patterns in different ways. Refer to section :ref:`Pattern configuration<advanced-pattern-configuration>` to learn how to create patterns.

.. figure:: images/operations/pattern-filtering/1.png
   :align: center

When filtering nucleotide sequences based on amino acid patterns, the *‘Convert to amino acid sequence before pattern matching* option should be enabled. This option allows to configure the translation mode using the panel below.

.. figure:: images/operations/pattern-filtering/2.png
   :align: center

This panel allows to specify:

- The frame in which translation should start. You can choose between:

  - *Starting at fixed frame*: by selecting this option, sequences are translated starting at the specified frame.
  - *Considering frames 1, 2 and 3*: by selecting this option, three translations starting at frames 1, 2 and 3 are created. This way, the pattern is applied to each translation separately and it is considered present if it is present in any of the translations.

    - If the *‘Join frames’* option is used, then the three translations are concatenated before testing the pattern. This is useful if a set of sequences is being processed and the composed pattern should be found in any of the frames, one part of the pattern being present in one frame and another part in a different frame, as in the case of intron containing gene sequences.

- *Codon table*: which can be *Predefined*, to choose from a list of predefined genetic codes, or *Custom*, to select a file containing a custom DNA codon table. In this latter case, the custom codon code must be given in the following format:

.. code-block:: console

	TTT=T
	CTT=C
	GCA=A

- *Use reverse complement sequences*: whether reverse complement of sequences is used before translation or not. If not selected, sequences are used as they are introduced.

Examples
++++++++

The following example shows how an input FASTA is filtered to obtain only those sequences containing at least one *ACTG*.

Input:

.. code-block:: console

 >Sequence1
 AGGGTTTAGCCAACTGCTGCAGCA
 >Sequence2
 AGGGTTTAGCCAACGCCTGCAGCA
 >Sequence3
 CTACTGGAATAGAACCTCTGGAAT
 >Sequence4
 CTATGGAATAGAACCTCTGGAATC

Output:

.. code-block:: console

 >Sequence1
 AGGGTTTAGCCAACTGCTGCAGCA
 >Sequence3
 CTACTGGAATAGAACCTCTGGAAT

In the following example, sequences are filtered based on their headers. By using the pattern *Homo_sapiens*, only two sequences are kept in the output FASTA.

Input:

.. code-block:: console

 >Mus_musculus_1
 TGCCAGAGAACTGCCGGTGTGGTG
 >Homo_sapiens_1
 ATGTCTTCCATTAAGATTGAGTGT
 >Mus_musculus_2
 GCACCAGGGGGCCCTGTACTCCCT
 >Homo_sapiens_2
 CGCGCAGCCGTCTTTGACCTTGAT

Output:

.. code-block:: console

 >Homo_sapiens_1
 ATGTCTTCCATTAAGATTGAGTGT
 >Homo_sapiens_2
 CGCGCAGCCGTCTTTGACCTTGAT

Remove isoforms
---------------

This operation detects isoforms and allows only one to be kept in each FASTA file by applying the following algorithm:

1.	Start with the first sequence (*FS*) and compare it against the remaining ones.
2.	Each pair of sequences (*FS* vs. Second Sequence, *SS*), is considered to be isoforms if they share a word of a given minimum length (*Minimum word length* parameter).
3. 	If they are isoforms, the *SS* is marked as isoform of the *FS* so that the *SS* will not be taken for further comparisons.
4. 	Repeat steps 1 to 3 for the remaining sequences.
5.	Now, for each group of isoforms, the *Isoform selection criteria* is applied to select which isoform should go to the output file.

This algorithm is applied to all sequences in each input FASTA file. Nevertheless, by using the *Header matcher configuration*, it is possible to split them in groups that will be processed separately. This option is meant for those scenarios where sequences from two or more species are mixed in the same FASTA file and this operation should be applied to each species separately.

The configuration panel allows to set the parameters of the operation:

- *Minimum word length*: the minimum length of word to consider that two sequences are isoforms.
- *Isoform selection criteria*: the configuration of the criteria to select which isoform should go to the output file.

	- *Reference size*: the isoform with the length closest to this reference size will be selected. In case of having two isoforms that are at the same distance, the *tie break mode* option allows specifying which one should be selected.
	- *Tie break mode*: *shortest* means that the sequence with fewer units (i.e. nucleotides or amino acids) will be selected as isoform and *longest* means that the sequence with more units will be selected as isoform.

- *Header matcher configuration*: this option allows to specify whether sequences must be grouped before the identification of the isoforms. Leave it empty if isoforms must be removed at a file level. In contrast, if you want to make groups of sequences before the identification of the isoforms, here it is possible to configure how sequence headers must be matched in order to group sequences. Check the manual for examples.

	- *String to match*: the regular expression that must be matched in the sequence header.
	- *Case sensitive?*: whether the string must be matched as case sensitive or not.
	- *Quote pattern?*: whether the regular expression pattern must be quoted or not. When the regular expression is quoted, metacharacters or escape sequences in it will be given no special meaning.
	- *Regex group?*: the regular expression group that must be extracted. Default value is *0*, meaning that the entire result must be considered. Use values higher than 0 when there are brackets in the regular expression in order to select the desired group.
	- *Header target?*: the part of the sequence header where the string must be found.

- *Removed isoforms*: this group of options allows to specify how removed isoforms should be processed.

	- *Add removed isoform headers?*: whether the removed isoform headers should be added to the header of the selected isoform.
	- *Header target*: the part of the removed isoform headers that should be added.
	- *Isoform files directory*: whether the removed isoform names should be saved into a CSV file or not. This allows an easy identification of those sequences that had isoforms in the output files. If you do not want to save them, leave this file empty. Otherwise, choose the directory where such files should be created.

.. figure:: images/operations/remove-isoforms/1.png
   :align: center

Examples
++++++++

The following example illustrates how isoforms in the input FASTA file are removed so that the output FASTA only contains those with a sequence length closest to a *Reference size* of *10*. The *Minimum word length* is *8*.

Input:

.. code-block:: console

 >S1 [Size 10]
 AAAAATTTTT
 >S2 [Size 8]
 AAAATTTT
 >S3 [Size 6]
 AAATTT
 >S4 [Size 12]
 TTTTTTGGGGGG
 >S5 [Size 10]
 TTTTTGGGGG

Output:

.. code-block:: console

 >S1 [Size 10]
 AAAAATTTTT
 >S3 [Size 6]
 AAATTT
 >S5 [Size 10]
 TTTTTGGGGG

As explained before, the *Header matcher configuration* allows to split the input sequences in groups that will be processed separately. This option is meant for those scenarios where sequences from two or more species are mixed in the same FASTA file and this operation should be applied to each species separately. Consider the input FASTA below that contains sequences from both *Homo sapiens* and *Mus musculus*. When it is processed using the configuration below, the output FASTA is obtained.

.. figure:: images/operations/remove-isoforms/2.png
   :align: center

Note how the *Mus_musculus_3* sequence is present in the output file although, without knowing its origin it could have been considered an isoform of the *Homo_sapiens_1* sequence. This is because the regular expression *^[^_]*_[^_]** splits the sequences in two groups: those containing *Homo_sapiens* and those containing *Mus_musculus*, which are processed separately.

.. code-block:: console

 >Homo_sapiens_1 [Size 10]
 AAAAATTTTT
 >Homo_sapiens_2 [Size 8]
 AAAATTTT
 >Mus_musculus_1 [Size 12]
 TTTTTTGGGGGG
 >Mus_musculus_2 [Size 10]
 TTTTTGGGGG
 >Mus_musculus_3 [Size 12]
 AAAAAATTTTTT

Output:

.. code-block:: console

 >Homo_sapiens_1 [Size 10]
 AAAAATTTTT
 >Mus_musculus_2 [Size 10]
 TTTTTGGGGG
 >Mus_musculus_3 [Size 12]
 AAAAAATTTTTT


Output (selecting also the *Add remove isoform headers* option):

.. code-block:: console

 >Homo_sapiens_1 [Size 10] [Homo_sapiens_2, Mus_musculus_3]
 AAAAATTTTT
 >Mus_musculus_2 [Size 10] [Mus_musculus_1]
 TTTTTGGGGG

Remove redundant sequences
--------------------------

This operation allows the removal of redundant sequences. Redundant sequences are sequences with exactly the same sequence nucleotides or amino acids. If the *‘Remove also subsequences’* option is selected, then sequences contained within larger sequences are also removed.

.. figure:: images/operations/remove-redundant-sequences/1.png
   :align: center

Option *‘Merge headers’* allows controlling how new sequences are created. If this option is not selected, then the header of the new sequence is the header of one of the two being merged. On the contrary, if this option is selected, the header of the new sequence is created by concatenating the headers of the two sequences being merged. You can also save a report of the merged headers into a file by selecting the *‘Save merged headers into a file’*.

When removing redundant sequences, it is also possible to indicate that the sequences must be converted to amino acid sequences before checking if they are redundant. This way, it is possible to filter nucleic acid sequences based on amino acid patterns. To do so, the *‘Convert to amino acid sequence before sequence comparison’* option should be enabled. Please note that nucleotide sequences containing ambiguity codes will not be translated generating an error. This option allows to configure the translation mode using the panel below.

.. figure:: images/operations/remove-redundant-sequences/2.png
   :align: center

This panel allows to specify:

- The frame in which translation should start. You can choose between:

  - *Starting at fixed frame*: by selecting this option, sequences are translated starting at the specified frame.
  - *Considering frames 1, 2 and 3*: by selecting this option, three translations starting at frames 1, 2 and 3 are created. This way, each translation is tested separately and the sequence is considered redundant if any of the three frames is redundant.

- *Codon table*: which can be *Predefined*, to choose from a list of predefined genetic codes, or *Custom*, to select a file containing a custom DNA codon table. In this latter case, the custom codon code must be given in the following format:

.. code-block:: console

	TTT=T
	CTT=C
	GCA=A

- *Use reverse complement sequences*: whether reverse complement of sequences is used before translation or not. If not selected, sequences are used as they are introduced.

Examples
++++++++

The following example shows how only exact sequences are removed. Since *Sequence1* and *Sequence2* have the same nucleotide sequence, they are combined in the output FASTA. The *‘Merge headers’* is selected to illustrate how sequence headers are combined.

Input:

.. code-block:: console

 >Sequence1
 ATGGTCCATGGGTACAAAGGGGT
 >Sequence2
 ATGGTCCATGGGTACAAAGGGGT
 >Sequence3
 CCATGGGTACA

Output:

.. code-block:: console

 >Sequence1 [Sequence2]
 ATGGTCCATGGGTACAAAGGGGT
 >Sequence3
 CCATGGGTACA

The following example shows how both exact sequences and subsequences are removed. Since *Sequence1* and *Sequence2* have the same nucleotide sequence, they are combined in the output FASTA. *Sequence3* is also combined with the previous combination because CCATGGGTACA is contained in it.

Input:

.. code-block:: console

 >Sequence1
 ATGGTCCATGGGTACAAAGGGGT
 >Sequence2
 ATGGTCCATGGGTACAAAGGGGT
 >Sequence3
 CCATGGGTACA

Output:

.. code-block:: console

 >Sequence1 [Sequence2] [Sequence3]
 ATGGTCCATGGGTACAAAGGGGT

Gene Annotation
===============

Augustus (SAPP)
---------------

This operation permits the annotation of a eukaryotic genome or sequence of interest by predicting genes using Augustus (https://sapp.gitlab.io/eukaryote/).

.. Important::
   This operation fails when the input FASTA file contains duplicated sequence identifiers. If so, process the input FASTA files first using the :ref:`Disambiguate sequence names<operation_disambiguate>` operation to make sure that sequence identifiers are unique.

Configuration
+++++++++++++

First, the *’SAPP configuration’* area allows to select the execution mode of SAPP: *system binary* indicates that SAPP will be executed directly using its binaries (i.e. the required jar files) and *Docker image* means that a Docker image will be used instead.

In the *system binary* mode, the path where the SAPP binaries (`Conversion.jar` and `genecaller.jar`) are located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). It is also possible to specify the path to the Java binary, although by default the Java that comes with SEDA is used. Click the *‘Check SAPP jars’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/sapp/1.png
   :align: center

Secondly, the *’bedtools configuration’* area allows to select the execution mode of bedtools: *system binary* indicates that bedtools will be executed directly using its binaries and *Docker image* means that a Docker image will be used instead.

In the *system binary* mode, the path where the bedtools binary is located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If they are available in the system path, just click the *‘Check binary’* button to make sure that SEDA can correctly execute it.

.. figure:: images/operations/sapp/2.png
   :align: center

In the *Docker image* mode, the default image is already set, although it is possible to choose a custom one provided that it has the bedtools binary in the system path.

Finally, the remaining options in the configuration panel also allows to choose the following specific settings of the *SAPP* program:

- *Species*: the species to use.

.. figure:: images/operations/sapp/3.png
   :align: center

getorf (EMBOSS)
---------------

This operation allows finding and extracting open reading frames (ORFs) using the *getorf* program from the EMBOSS suite. According to its manual (http://emboss.sourceforge.net/apps/cvs/emboss/apps/getorf.html):

    "This program finds and outputs the sequences of open reading frames (ORFs) in one or more nucleotide sequences. An ORF may be defined as a region of a specified minimum size between two STOP codons, or between a START and a STOP codon. The ORFs can be output as the nucleotide sequence or as the protein translation. Optionally, the program will output the region around the START codon, the first STOP codon, or the final STOP codon of an ORF. The START and STOP codons are defined in a Genetic Code table; a suitable table can be selected for the organism you are investigating. The output is a sequence file containing predicted open reading frames longer than the minimum size, which defaults to 30 bases (i.e. 10 amino acids)."

Configuration
+++++++++++++

First, the *’EMBOSS configuration’* area allows to select the execution mode of EMBOSS: *system binary* indicates that EMBOSS will be executed directly using its binaries and *Docker image* means that a Docker image will be used instead.

In the *system binary* mode, the path where the EMBOSS binaries (e.g. getorf) are located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If they are available in the system path, just click the *‘Check binary’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/emboss/1.png
   :align: center

Finally, the remaining options in the configuration panel also allows to choose the following specific settings of the *getorf* program:

- *Table*: the code to use.
- *Find*: the first four options are to select either the protein translation or original nucleic acid sequence of the reading frame. There are two  definitions of an open reading frame: it either be a region that is free of codons or a region that begins with a codon and ends with a STOP codon. The three options are probably only of to people who wish to investigate statistical properties of the regions potential START or STOP codons. The option assumes that ORF are                                                                                                                                                                                                                       calculated between two STOP codons.
- *Min. size*: the minimum nucleotide size of ORF to report (any integer value).
- *Max. size*: the maximum nucleotide size of ORF to report (any integer value).
- *Additional parameters*: additional parameters for the *getorf* program.

.. figure:: images/operations/emboss/2.png
   :align: center

ProSplign/ProCompart Pipeline
-----------------------------

This operation allows obtaining CDS annotations using the selected FASTA files as reference proteing sequences with ProSplign/ProCompart. This operation applies the procedure described here (https://www.ncbi.nlm.nih.gov/sutils/static/prosplign/prosplign.html) to each selected FASTA file as nucleotide subject file.

ProSplign/ProCompart can be seen as an alternative to Splign/Compart. When using this operation, protein reference sequences rather than reference nucleotide CDS are used. Since protein sequences change at a slower pace than nucleotide sequences, in principle, the reference and target sequences can be more distantly related than when using the Splign/Compart option, but it is difficult to quantify how distantly related they can be. Moreover, Splign/Compart runs considerably faster than ProSplign/ProCompart. The resulting CDS annotation is based on the homology to a given protein reference sequence, and thus may produce sequence annotations with lengths that are not multiple of three, if for instance, sequencing errors causing frameshifts are present in the genome to be annotated. Nevertheless, the existence of intron splicing signals at the exons 5’ and 3’ ends is taken into account. There will be no stop codon in the CDS annotation since the reference sequence is a protein.

Configuration
+++++++++++++

First, the *‘ProSplign/ProCompart configuration’* area allows to select the execution mode of ProSplign/ProCompart: *system binary* indicates that they will be executed directly using their binaries and *Docker image* means that a Docker image will be used instead.

In the *system binary* mode, the path where the required binaries (prosplign and procompart-wrapper) are located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If they are available in the system path, just click the *‘Check binary’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/prosplign-procompart/1.png
   :align: center

In the *Docker image* mode, the default image is already set, although it is possible to choose a custom one provided that it has the ProSplign/ProCompart binaries in the system path.

Secondly, the *‘BLAST configuration’* area allows to select the execution mode of BLAST: *system binary* indicates that BLAST will be executed directly using its binaries and *Docker image* means that a Docker image will be used instead.

In the *system binary* mode, the path where the BLAST binaries (makeblastdb, blastdb_aliastool, blastdbcmd, blastp, blastn, blastx, tblastn, and tblastx) are located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If they are available in the system path, just click the *‘Check binary’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/prosplign-procompart/2.png
   :align: center

In the *Docker image* mode, the default image is already set, although it is possible to choose a custom one provided that it has the BLAST binaries in the system path.

Finally, the configuration panel also allows to choose:

- *External file query*: the query file (proteins).
- *Max. target seqs.*: value of the *max_target_seqs* BLAST parameter.

.. figure:: images/operations/prosplign-procompart/3.png
   :align: center

Test data
+++++++++

This operation can be tested using the test data available here (https://www.sing-group.org/seda/downloads/data/test-data-prosplign-procompart.zip). First, the *‘Demo_Genome_Nucleotides.fa‘* file should be selected using the SEDA *Input* area. Then, the *‘Demo_Query_Protein.fa‘* file should be selected in the configuration panel of the operation as *External file query*. This operation produces a FASTA file like the one at the *‘Expected_Demo_ProSplign_Compart_Results.fa‘*.

In addition, this operation can be also tested using the data of this use case (https://www.sing-group.org/BDBM/usecases.html#uc7) of our BDBM software, which has the goal of obtaining the *Nicotiana attenuata PPCK1a* CDS, using the *Solanum tuberosum PPCK1a* protein sequence (*AF531415*) as the reference.

Splign/Compart Pipeline
-----------------------

This operation permits the annotation of exons or genes, as long as a CDS reference sequence is available from a closely related species. How closely related the species must be depends on how fast the gene(s) in question evolve. For instance, a few highly conserved Drosophila virilis genes can be annotated this way using as reference Drosophila melanogaster CDSs (the common ancestor of the two species lived more than 40 million years ago). Each selected FASTA file is used as target and an external file with CDS must be provided in the operation configuration.

For further information and references about this method, refer to the official NCBI documentation: https://www.ncbi.nlm.nih.gov/sutils/splign/splign.cgi

These are the steps carried out in the pipelline:

    1. Create BLAST databases for both the genome and the CDS.

    2. Run the `mklds` option of Splign (`splign --mklds`) on the working directory to create an LDS index that Splign will use to access the FASTA sequences.

    3. Run Compart to produce the preliminary cDNA-to-genomic alignments (i.e. the compartments).

    4. Run the `ldsdir` option of Splign (`splign --ldsdir`) to obtain the annotations using the obtained compartments as input.

    5. Convert the ldsdir output annotations (`output.tsv`) into four BED files (one for each one of the four possible combinations depending on the relationships between the query/subject start/end coordinates of each annotation). There are four possible cases in the `output.tsv`:

        - Case 1: query start (col. 6) < query end (col. 7) and subject start (col. 8) < subject end (9): the annotations musts be read from beginning to end.
        - Case 2: query start (col. 6) < query end (col. 7) e subject start (col. 8) > subject end (9): the annotations musts be read from beginning to end and obtain the reverse-complement of the corresponding sequences.
        - Case 3: query start (col. 6) > query end (col. 7) e subject start (col. 8) < subject end (9): the annotations musts be read from end to beginning and obtain reverse-complement of the corresponding sequences
        - Case 4: query start (col. 6) > query end (col. 7) e subject start (col. 8) > subject end (9): the annotations musts be read from end to beginning.

    6. Extract the regions in the BED files from the genome FASTA file to produce the four FASTA file with the annotations using bedtools. Then, calculate the reverse-complement of the sequences when needed (cases 2 and 3) and merge the four files into a single file.

    7. If the concatenate exons option is selected, the adjacent exons are concatenated in the output FASTA file. Using this option, if an annotation is obtained for every exon of a given gene then the resulting sequence will be the complete CDS.

Configuration
+++++++++++++

First, the *‘Splign/Compart configuration’* area allows to select the execution mode of Splign/Compart: *system binary* indicates that they will be executed directly using their binaries and *Docker image* means that a Docker image will be used instead.

In the *system binary* mode, the path where the required binaries (splign and compart) are located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If they are available in the system path, just click the *‘Check binary’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/splign-compart/1.png
   :align: center

In the *Docker image* mode, the default image is already set, although it is possible to choose a custom one provided that it has the Splign/Compart binaries in the system path.

Secondly, the *‘BLAST configuration’* area allows to select the execution mode of BLAST: *system binary* indicates that BLAST will be executed directly using its binaries and *Docker image* means that a Docker image will be used instead.

.. Warning::
   The *Compart* tool only works with files produced by specific BLAST versions. Version 2.6.0-1 of BLAST is valid (and this is the version used in the default Docker image). Later versions (such as 2.10.0) produce files that can't be used by *Compart*.

In the *system binary* mode, the path where the BLAST binaries (makeblastdb, blastdb_aliastool, blastdbcmd, blastp, blastn, blastx, tblastn, and tblastx) are located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If they are available in the system path, just click the *‘Check binary’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/splign-compart/2.png
   :align: center

In the *Docker image* mode, the default image is already set, although it is possible to choose a custom one provided that it has the BLAST binaries in the system path.

Thirdly, the *’bedtools configuration’* area allows to select the execution mode of bedtools: *system binary* indicates that bedtools will be executed directly using its binaries and *Docker image* means that a Docker image will be used instead.

.. Warning::
   This operation uses the *-name* parameter of the *bedtools getfasta* command to put the name field and the coordinates (in intermediate bed files) in the FASTA headers when creating the output with the annotations. This allows the operation to concatenate the exons if requested and to show the coordinates. For this reason, SEDA requires the version **2.29.2** (and this is the version used in the default Docker image).

In the *system binary* mode, the path where the bedtools binary is located must be specified (refer to section :ref:`Dependencies<dependencies>` for additional information about this). If they are available in the system path, just click the *‘Check binary’* button to make sure that SEDA can correctly execute it.

.. figure:: images/operations/splign-compart/3.png
   :align: center

In the *Docker image* mode, the default image is already set, although it is possible to choose a custom one provided that it has the bedtools binary in the system path.

Finally, the configuration panel also allows to choose:

- *External file query*: the CDS query file (nucleotides).
- *Concatenate exons?*: if this option is checked  then adjacent exons will be concatenated. Therefore, if an annotation is obtained for every exon of a given gene, the resulting sequence will be the complete CDS.

.. figure:: images/operations/splign-compart/4.png
   :align: center

Test data
+++++++++

This operation can be tested using the test data available here (https://www.sing-group.org/seda/downloads/data/test-data-splign-compart.zip), which is the data of this use case (https://www.sing-group.org/BDBM/usecases.html#uc3) of our BDBM software. First, the *‘dsim-all-chromosome-r2.02.fasta‘* file should be selected using the SEDA *Input* area. Then, the *‘dmel-sod.fasta‘* file should be selected in the configuration panel of the operation as *External file query*. This operation produces a FASTA file like the one at the *‘seda-output-concatenated.fasta‘* when the *Concatenate exons?* option is selected and a FASTA like the one at the *‘seda-output-without-concatenation.fasta‘* when the *Concatenate exons?* option is not selected.

General
=======

Compare
-------

This operation allows all the possible pairwise comparisons on the input FASTA files to be made. For each pair of FASTA files under comparison (e.g. *Input1* and *Input2*) three output files are produced: (i) *Input1_vs_Input2_both.fasta*, containing sequences present in both files, (ii) *Input1_vs_Input2_only_Input1.fasta*, containing those sequences that only appear in *Input1*, and (iii) *Input1_vs_Input2_only_Input2.fasta*, containing those sequences that only appear in *Input2*. Sequences can be compared by their headers or contents.

The configuration panel allows to choose the *Sequence target*, which is the part of the sequences that must be used to compare them, and also the *Reformat output file* settings, which allows to specify the format parameters of the output FASTA files containing the comparison results (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

.. figure:: images/operations/compare/1.png
   :align: center

Examples
++++++++

The following example shows how the two input FASTA files are compared using the nucleotide sequence as *Sequence target*.

Input1:

.. code-block:: console

 >Sequence1
 ACTG
 >Sequence2
 TCGA
 >Sequence3
 TTAA
 >Sequence6
 AAAA

Input2:

.. code-block:: console

 >Sequence1
 ACTG
 >Sequence4
 GGTT
 >Sequence5
 GTCA
 >Sequence6
 AAAA

Input1_vs_Input2_both.fasta:

.. code-block:: console

 >Sequence1
 ACTG
 >Sequence6
 AAAA

Input1_vs_Input2_only_Input1.fasta

.. code-block:: console

 >Sequence2
 TCGA
 >Sequence3
 TTAA

Input1_vs_Input2_only_Input2.fasta

.. code-block:: console

 >Sequence4
 GGTT
 >Sequence5
 GTCA

Grow sequences
--------------

This operation allows sequences to grow or expand sequences by merging those sequences with the specified *‘Minimum overlapping’* units (i.e. nucleotides or amino acids).

.. figure:: images/operations/grow-sequences/1.png
   :align: center

This operation applies the following algorithm to merge sequences:

1.	Take the first sequence as the reference sequence.
2.	Compare the reference sequence with the rest of sequences. For each pair of sequences, check if there is an overlapping of units of at least the minimum size specified. This overlapping is searched for at the beginning of the reference sequence and at the ending of the sequence being compared.

  a)	If an overlapping is found, merge the two sequences. The merged sequences are removed from the set of sequences and the new one is added. Return to step 1.
  b)	If an overlapping is not found between the first reference sequence and the rest of sequences, then step 2 is repeated for the rest of sequences repeatedly.

3.	The process stops when all sequences have been compared without merging any of them.

Examples
++++++++

The following example shows how sequences with a minimum overlapping of 6 in the input FASTA are merged. *Sequence1* and *Sequence2* have an overlapping region of 9 nucleotides (*CTCTCTCTC*), thus they are merged in the output FASTA.

Input:

.. code-block:: console

 >Sequence1
 AAAAAGGCTCTCTCTC
 >Sequence2
 CTCTCTCTCGGGGGGG
 >Sequence3
 ACTGACTGAAAAA

Output:

.. code-block:: console

 >Sequence3
 ACTGACTGAAAAA
 >Sequence2 [Sequence1]
 AAAAAGGCTCTCTCTC
 GGGGGGG

The following example shows how sequences with a minimum overlapping of 4 in the input FASTA are merged. *Sequence1* and *Sequence3* have an overlapping region of 5 nucleotides (*AAAAA*) in the highlighted area, thus they are merged in the first place. Then, the resulting sequence has an overlapping region of 8 nucleotides with *Sequence2*, thus there is only one sequence in the output FASTA.

Input:

.. code-block:: console

 >Sequence1
 AAAAAGGCTCTCTCTC
 >Sequence2
 CTCTCTCTCGGGGGGG
 >Sequence3
 ACTGACTGAAAAA

Output:

.. code-block:: console

 >Sequence2 [Sequence1 [Sequence3]]
 ACTGACTGAAAAAGGCTCTCTCTCGGGGGGG

Merge
-----

This operation allows merging all the selected input FASTA files into a single output FASTA. The *‘Name’* parameter defines the name for the output file. Additionally, you can specify the FASTA format parameters in the *‘Reformat output file’* area (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

.. figure:: images/operations/merge/1.png
   :align: center

The following example illustrates how input FASTA files 1 and 2 are merged into a single output FASTA file without line breaks.

Input 1:

.. code-block:: console

 >Homo_sapiens_1
 ACTG
 ACTG
 >Homo_sapiens_2
 ACTG
 ACTG

Input 2:

.. code-block:: console

 >Mus_musculus_1
 ACTG
 ACTG
 >Mus_musculus_2
 ACTG
 ACTG

Output:

.. code-block:: console

 >Homo_sapiens_1
 ACTGACTG
 >Homo_sapiens_2
 ACTGACTG
 >Mus_musculus_1
 ACTGACTG
 >Mus_musculus_2
 ACTGACTG

Regular expression split
------------------------

This operation allows each input FASTA file to be split based on regular expression patterns. This operation matches the defined regular expression pattern against the sequence headers to make groups using the matching parts.

The configuration panel allows to set the parameters of the operation:

- *Group names files directory*: whether the groups created for each file should be saved into a TXT file or not. This allows an easy identification of the sequence groups that have been created. If you do not want to save them, leave this file empty. Otherwise choose the directory where such files should be created.
- *Header matcher configuration*: this option allows to specify how sequences must be grouped to form the new files.

	- *String to match*: the regular expression that must be matched in the sequence header.
	- *Case sensitive?*: whether the string must be matched as case sensitive or not.
	- *Quote pattern?*: whether the regular expression pattern must be quoted or not. When the regular expression is quoted, metacharacters or escape sequences in it will be given no special meaning.
	- *Regex group?*: the regular expression group that must be extracted. Default value is *0*, meaning that the entire result must be considered. Use values higher than 0 when there are brackets in the regular expression in order to select the desired group.
	- *Header target?*: the part of the sequence header where the string must be found.

.. figure:: images/operations/split-regex/1.png
   :align: center

Examples
++++++++

This is a powerful option that allows complex splits. For instance, it can be used in those scenarios where sequences from two or more species are mixed in the same FASTA file and one FASTA file per species is wanted. Consider the input FASTA below that contains sequences from three species: *Homo sapiens*, *Gallus gallus*, and *Mus musculus*. When it is processed using the configuration below, three output FASTA files are obtained. Basically, the regular expression *^[^_]*_[^_]** is able to extract the common species names from the headers so that sequences are grouped based in them.

.. figure:: images/operations/split-regex/2.png
   :align: center

.. code-block:: console

 >Homo_sapiens_1
 AAAAATTTTT
 >Homo_sapiens_2
 AAAATTTT
 >Mus_musculus_1
 TTTTTTGGGGGG
 >Mus_musculus_2
 TTTTTGGGGG
 >Gallus_gallus_1
 AAAAAATTTTTT
 >Gallus_gallus_2
 TTTTTGGGGG

Output FASTA *Gallus_gallus*:

.. code-block:: console

 >Gallus_gallus_1
 AAAAAATTTTTT
 >Gallus_gallus_2
 TTTTTGGGGG

Output FASTA *Homo_sapiens*:

.. code-block:: console

 >Homo_sapiens_1
 AAAAATTTTT
 >Homo_sapiens_2
 AAAATTTT

Output FASTA *Mus_musculus*:

.. code-block:: console

 >Mus_musculus_1
 TTTTTTGGGGGG
 >Mus_musculus_2
 TTTTTGGGGG

In addition, if a folder is selected in the *Group names files directory* option, it is ceated the following file containing the list of matches obtained for this FASTA file:

.. code-block:: console

 Homo_sapiens
 Mus_musculus
 Gallus_gallus

Remove stop codons
------------------

This operation allows the sequences in each input FASTA file to be modified by removing the stop codons (*TGA*, *TAG*, and *TAA*) placed at the end of them. Additionally, you can specify the FASTA format parameters in the *‘Reformat output file’* area (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

The following example illustrates how sequences in the input FASTA are modified to remove stop codons from the end of the sequence.

Input:

.. code-block:: console

 >Sequence1
 TTGCTCCCTACTCCTATGCGGGATGA
 >Sequence2
 TTGCTCCCTACTCCTATGCGGGATAA

Output:

.. code-block:: console

 >Sequence1
 TTGCTCCCTACTCCTATGCGGGA
 >Sequence2
 TTGCTCCCTACTCCTATGCGGGA

Reverse Complement
------------------

This operation allows the conversion fo the input sequences into their reverse, complement, or reverse complement counterparts. In addition, if the *‘Rename sequence headers’* option is selected, then the sequence headers can be modified by using the renaming configuration selected below (for more details about this configuration, see the :ref:`Add prefix/suffix Rename Header documentation<operations-rename-header-add>` and the examples section).

.. figure:: images/operations/reverse-complement/1.png
   :align: center

Examples
++++++++

The following example illustrates how sequences in the input FASTA are converted into their reverse complement counterparts and also the sequence headers are modified by appending the *‘_REVERSE_COMPLEMENT’* suffix.

.. figure:: images/operations/reverse-complement/2.png
   :align: center

Input:

.. code-block:: console

 >Sequence1
 -ACTG-ACTG-ACTG-
 >Sequence2
 ATUGCYRSWKMBDHVN

Output:

.. code-block:: console

 >Sequence1_REVERSE_COMPLEMENT
 -CAGT-CAGT-CAGT-
 >Sequence2_REVERSE_COMPLEMENT
 NBDHVKMWSYRGCAAT

Split
-----

This operation allows each input FASTA file to be split into several FASTA files. The *‘Split mode’* parameter defines the way of splitting them:

- *Fixed number of sequences per file*: it divides each input FASTA into several files containing the defined *‘Number of sequences’* in each one.
- *Fixed number of files*: it divides each input FASTA into the defined *‘Number of files’* with the same number of sequences in each one.
- *Fixed number of sequences per defined number of files*: it divides each input FASTA into the defined *‘Number of files’* containing the defined *‘Number of sequences’* in each one. In this mode, the result of multiplying *‘Number of files’* by *‘Number of sequences’* should be less or equal to the number of sequences contained in the input FASTA file being processed. Nevertheless, in some occasions it may be necessary to do that. The option *‘Independent extractions’* allows doing this. See the examples section to see how this option works in detail.

.. figure:: images/operations/split/1.png
   :align: center

In addition, if the *‘Randomize’* option is selected, sequences in the input FASTA are sorted in a random order before producing the output FASTA files. The *'Seed'* number specifies the random seed to set before shuffling the sequences. This allows the same result to be reproduced in different runs and environments with same random seed.

Examples
++++++++

Fixed number of sequences per file
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The following example shows how to split an input FASTA file containing 5 sequences into files containing 2 sequences. Three output FASTA are created: two containing the specified number of sequences (2 sequences) and one containing the remaining (1 sequence).

Input:

.. code-block:: console

 >Sequence1
 ACTG
 >Sequence2
 ACTGACTG
 >Sequence3
 ACTGACTGACTG
 >Sequence4
 ACTGACTGACTGACTG
 >Sequence5
 ACTGACTGACTGACTGACTG

Output 1:

.. code-block:: console

 >Sequence1
 ACTG
 >Sequence2
 ACTGACTG

Output 2:

.. code-block:: console

 >Sequence3
 ACTGACTGACTG
 >Sequence4
 ACTGACTGACTGACTG

Output 3:

.. code-block:: console

 >Sequence5
 ACTGACTGACTGACTGACTG

Fixed number of files
^^^^^^^^^^^^^^^^^^^^^

The following example shows how to split an input FASTA file containing 5 sequences into three files. Three output FASTA are created: two containing 2 sequences and one containing 1 sequence.

Input:

.. code-block:: console

 >Sequence1
 ACTG
 >Sequence2
 ACTGACTG
 >Sequence3
 ACTGACTGACTG
 >Sequence4
 ACTGACTGACTGACTG
 >Sequence5
 ACTGACTGACTGACTGACTG

Output 1:

.. code-block:: console

 >Sequence1
 ACTG
 >Sequence2
 ACTGACTG

Output 2:

.. code-block:: console

 >Sequence3
 ACTGACTGACTG
 >Sequence4
 ACTGACTGACTGACTG

Output 3:

.. code-block:: console

 >Sequence5
 ACTGACTGACTGACTGACTG

Fixed number of sequences per defined number of files
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The following example shows how to split an input FASTA file with five sequences into three files containing one sequence.

Input:

.. code-block:: console

 >Sequence1
 ACTG
 >Sequence2
 ACTGACTG
 >Sequence3
 ACTGACTGACTG
 >Sequence4
 ACTGACTGACTGACTG
 >Sequence5
 ACTGACTGACTGACTGACTG

Output 1:

.. code-block:: console

 >Sequence1
 ACTG

Output 2:

.. code-block:: console

 >Sequence2
 ACTGACTG

Output 3:

.. code-block:: console

 >Sequence3
 ACTGACTGACTG

Note how input order is kept in the three output FASTA files that are created. If the *‘Randomize’* option is used, the following output with sequences in a random order can be obtained.

Output 1:

.. code-block:: console

 >Sequence2
 ACTGACTG

Output 2:

.. code-block:: console

 >Sequence5
 ACTGACTGACTGACTGACTG

Output 3:

.. code-block:: console

 >Sequence1
 ACTG

Finally, if you want to obtain three FASTA files with three sequences each you need to use the *‘Independent extractions’* option. This option is usually combined with the *‘Randomize’* option. By doing this, the following output could be obtained.

Output 1:

.. code-block:: console

 >Sequence2
 ACTGACTG
 >Sequence5
 ACTGACTGACTGACTGACTG
 >Sequence4
 ACTGACTGACTGACTG

Output 2:

.. code-block:: console

 >Sequence5
 ACTGACTGACTGACTGACTG
 >Sequence1
 ACTG
 >Sequence3
 ACTGACTGACTG

Output 3:

.. code-block:: console

 >Sequence1
 ACTG
 >Sequence4
 ACTGACTGACTGACTG
 >Sequence2
 ACTGACTG

Translate
---------

This operation allows nucleic acid sequences to be translated their corresponding amino acid sequences. It can translate to the three forward and three reverse frames, and output multiple frame translations at once.

The configuration panel allows to specify:

- The frame in which translation should start. You can choose between:

  - *Starting at fixed frame*: by selecting this option, sequences are translated starting at the specified frame.
  - *Considering frames 1, 2 and 3*: by selecting this option, three translations starting at frames 1, 2 and 3 are created.

- *Codon table*: which can be *Predefined*, to choose from a list of predefined genetic codes, or *Custom*, to select a file containing a custom DNA codon table. In this latter case, the custom codon code must be given in the following format:

.. code-block:: console

	TTT=T
	CTT=C
	GCA=A

- *Use reverse complement sequences*: whether reverse complement of sequences must be calculated before translation or not. If not selected, sequences are used as they are introduced and therefore the three forward frames are obtained. If selected, the three reverse frames are obtained.

.. figure:: images/operations/translate/1.png
   :align: center

Examples
++++++++

The following example shows how sequences are translated in the three frames without using the reverse complement sequences. Note that stop codons are marked with an \*.

Input:

.. code-block:: console

 >Sequence1
 TTCCTTTGTCGCAGGGGG
 >Sequence2
 GGAGATGACCACTCG

Output_frame_1:

.. code-block:: console

 >Sequence1
 FLCRRG
 >Sequence2
 GDDHS

Output_frame_2:

.. code-block:: console

 >Sequence1
 SFVAG
 >Sequence2
 EMTT

Output_frame_3:

.. code-block:: console

 >Sequence1
 PLSQG
 >Sequence2
 R*PL

The following example shows how sequences are translated in the three frames using the reverse complement sequences.

Input:

.. code-block:: console

 >Sequence1
 TTCCTTTGTCGCAGGGGG
 >Sequence2
 GGAGATGACCACTCG

Output_frame_1:

.. code-block:: console

 >Sequence1
 PPATKE
 >Sequence2
 RVVIS

Output_frame_2:

.. code-block:: console

 >Sequence1
 PLRQR
 >Sequence2
 EWSS

Output_frame_3:

.. code-block:: console

 >Sequence1
 PCDKG
 >Sequence2
 SGHL

Protein Annotation
==================

PfamScan
--------

This operation allows searching and annotating sequences against the Pfam-A HMM library using the EMBL-EBI web service (https://www.ebi.ac.uk/Tools/pfa/pfamscan/).

This operation produces as output files as input files selected. Each input sequence is submitted to the PfamScan web service and the Pfam-A HMM annotations obtained. Each sequence header is then modified to contain the original sequence identifier along with a summary of the PfamScan annotations.

.. Note::
   To meet the EMBL-EBI usage guidelines and to avoid problems, this operation runs PfamScan queries in batches of 30 sequences. In addition, the amount of time SEDA waits between batches is equal to the time required to analyze the first batch. This delay can be controlled using the *'Batch delay factor'*.

By using the configuration panel shown below, you can configure the operation parameters:

- *E-mail*: a valid e-mail address. This is required by EMBL-EBI so they can contact you in the event of: problems with the service which affect your jobs; scheduled maintenance which affects services you are using; or deprecation and retirement of a service you are using.
- *Active site prediction*: whether to predict active site residues for Pfam-A matches or not.
- *Expectation value*: optionally, the expectation value cut-off.
- *Sequence error policy*: the policy to apply with sequences that fail when analyzed with PfamScan:

    - *Annotate sequence as error*: if a sequence analysis fails, this is annotated as an error in the output FASTA.
    - *Ignore sequences*: if a sequence analysis fails, it is ignored and not included in the output FASTA.
    - *Produce an error (stop operation)*: if a sequence analysis fails an error is produced and the whole operation is stopped.

- *Batch delay factor*: the delay factor between batches. SEDA runs PfamScan queries in batches of 30 sequences to meet the EMBL-EBI guidelines regarding the usage of resources. A delay factor of 1 means that SEDA waits a time between batches equal to the time required to analyze the first batch.

.. figure:: images/operations/pfam-scan/1.png
   :align: center

Reformatting
============

.. _operation_disambiguate:

Disambiguate sequence names
---------------------------

This operation allows duplicated sequence names (identifiers) to be disambiguated. The configuration panel allows to choose the way of disambiguating them: *Rename*, to add a numeric prefix to disambiguate duplicate names, or *Remove*, to remove sequences with duplicate identifiers, keeping the first occurrence.

.. figure:: images/operations/disambiguate-sequence-names/1.png
   :align: center

The following example shows how sequences with duplicate names in the input FASTA are removed (in the Removed Output FASTA) or renamed to avoid those redundancies (in the Rename Output FASTA).

Input:

.. code-block:: console

 >SequenceA
 ATGGTCCATG
 >SequenceA
 ATGGGCTAAC
 >SequenceB
 ATGGGGCCAC
 >SequenceB
 ATGGCCAACC
 >SequenceC
 CCCCTTTGGG

*Remove* Output:

.. code-block:: console

 >SequenceA
 ATGGTCCATG
 >SequenceB
 ATGGGGCCAC
 >SequenceC
 CCCCTTTGGG

*Rename* Output:

.. code-block:: console

 >SequenceA_1
 ATGGTCCATG
 >SequenceA_2
 ATGGGCTAAC
 >SequenceB_1
 ATGGGGCCAC
 >SequenceB_2
 ATGGCCAACC
 >SequenceC
 CCCCTTTGGG

NCBI rename
-----------

This operation allows replacing NCBI accession numbers in the names of FASTA files by the associated organism name and additional information from the NCBI Taxonomy Browser (https://www.ncbi.nlm.nih.gov/Taxonomy/). An example of a FASTA file could be ‘GCF_000001735.3_TAIR10_cds_from_genomic.fna’. When this file is given to this operation, the organism name associated to the accession number ‘GCF_000001735.3’ is obtained from the NCBI (https://www.ncbi.nlm.nih.gov/assembly/GCF_000001735.3). In this case, the ‘*Arabidopsis thaliana* (thale cress)’ is the associated organism name. The *‘File name’* allows specifying how this name is added to the file name and the *‘Delimiter’* parameter specifies if a separator should be set between the name and the file name. You can choose between one of the following *‘Position’* values:

- *Prefix*: before the actual file name. In the example, with ‘Delimiter’ = ‘_’, the output FASTA would be named ‘Arabidopsis thaliana (thale cress)_GCF_000001735.3_TAIR10_cds_from_genomic.fna’.
- *Suffix*: after the actual file name.  In the example, with ‘Delimiter’ = ‘_’, the output FASTA would be named ‘GCF_000001735.3_TAIR10_cds_from_genomic.fna_Arabidopsis thaliana (thale cress)’.
- *Override*: entirely replacing the actual file name. In the example, the output FASTA would be named ‘Arabidopsis thaliana (thale cress)’.
- *Replace*: replacing the accession number.  In the example, the output FASTA would be named ‘Arabidopsis thaliana (thale cress)_TAIR10_cds_from_genomic.fna’.
- *None*: not modifying the file name.

.. figure:: images/operations/ncbi-rename/1.png
   :align: center

In addition to modifying the name of the FASTA files, this operation can also add this information to the sequence headers. This is configured in the *‘Sequence headers’* area shown below. This option does the same than the *‘Add prefix/suffix‘* rename mode of the *Rename header* operation (see section :ref:`Add prefix/suffix<operations-rename-header-add>`), being the organism name the string to add to the sequence headers.

.. figure:: images/operations/ncbi-rename/2.png
   :align: center

Moreover, some general configuration parameters can be specified in the *‘Configuration’* area. These parameters are:

- *Replace blank spaces*: whether blank spaces must be replaced or not.
- *Replace special characters*: whether special characters must be replaced or not. Special characters are ‘<‘, ‘>‘, ‘:‘, ‘\‘, ‘/‘, ‘\|‘, ‘?‘, and ‘\*‘.
- *Replacement*: the replacement string for those special characters.
- *Save replacements map*: whether the replacements map must be saved or not. This is useful to know how accession numbers have been replaced.
- *File*: the file to save the replacements map.

.. figure:: images/operations/ncbi-rename/3.png
   :align: center

Finally, this operation also allows obtaining additional information from the NCBI Taxonomy. The *‘NCBI Taxonomy information’* panel allows choosing what fields should be added to the organism name when applying the operation. Fields are added with the *‘Delimiter’* as separator. For instance, the accession number ‘GCF_000001735.3’ has this information page: https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=3702. If you select *‘Kingdom’*, then the string associated to it would be ‘*Arabidopsis thaliana* (thale cress)_Viridiplantae’. Note that some accession numbers or organisms may not have available information for all fields. In that case, those fields are ignored.

.. figure:: images/operations/ncbi-rename/4.png
   :align: center

Reallocate reference sequences
------------------------------

This operation allows finding one or more sequences (i.e. your reference sequences) using a pattern filtering option and reallocating them at the beginning of the file. For instance, this operation is useful to place at the beginning of your FASTA files the one or more sequences of interest and then specify them in the *‘Remove by size difference’* option of the filtering operation.

.. figure:: images/operations/reallocate-reference-sequences/1.png
   :align: center

The configuration of this operation is the same as the *Pattern filtering* configuration. Thus, you may refer to :ref:`Pattern filtering<operations-pattern-filtering>` section to learn how to use it.

Examples
++++++++

The following example shows how an input FASTA file is processed to reallocate those sequences containing *ACTG* at the beginning of the file.

Input:

.. code-block:: console

 >Sequence1
 AGGGTTTAGCCAACGCCTGCAGCA
 >Sequence2
 AGGGTTTAGCCAACTGCTGCAGCA
 >Sequence3
 CTACTGGAATAGAACCTCTGGAAT
 >Sequence4
 CTATGGAATAGAACCTCTGGAATC

Output:

.. code-block:: console

 >Sequence2
 AGGGTTTAGCCAACTGCTGCAGCA
 >Sequence3
 CTACTGGAATAGAACCTCTGGAAT
 >Sequence1
 AGGGTTTAGCCAACGCCTGCAGCA
 >Sequence4
 CTATGGAATAGAACCTCTGGAATC

The following example shows how an input FASTA is processed to reallocate those sequences containing *Homo_Sapiens* in their headers at the beginning of the file.

Input:

.. code-block:: console

 >Mus_musculus
 TGCCAGAGAACTGCCGGTGTGGTG
 >Pan_paniscus
 ATGTCTTCCATTAAGATTGAGTGT
 >Homo_sapiens
 GCACCAGGGGGCCCTGTACTCCCT
 >Falco_cherrug
 CGCGCAGCCGTCTTTGACCTTGAT

Output:

.. code-block:: console

 >Homo_sapiens
 GCACCAGGGGGCCCTGTACTCCCT
 >Mus_musculus
 TGCCAGAGAACTGCCGGTGTGGTG
 >Pan_paniscus
 ATGTCTTCCATTAAGATTGAGTGT
 >Falco_cherrug
 CGCGCAGCCGTCTTTGACCTTGAT

Reformat file
-------------

This operation allows changing the format of a FASTA file. This format includes:

- *Fragment length*: the fragment length or number of columns in which sequences are divided. The *’Remove line breaks’* option specifies that sequences should not be fragmented.
- *Line breaks*: the type of line breaks, which can be *‘Windows‘* or *‘Unix‘*.
- *Case*: the case of the sequences. *‘Original‘* means that original case in input sequences is kept and *‘Lower case’* and *‘Upper case’* allows converting sequences to lower or upper case respectively.

.. figure:: images/operations/reformat-file/1.png
   :align: center

Examples
++++++++

The following example illustrates how line breaks are removed from the input FASTA sequences by using this operation with the *‘Remove line breaks’* option selected.

Input:

.. code-block:: console

 >Sequence1
 ACTG
 ACTG
 AC
 >Sequence2
 ACTGACTG
 ACTGA

Output:

.. code-block:: console

 >Sequence1
 ACTGACTGAC
 >Sequence2
 ACTGACTGACTGA

The following example illustrates how the length of the input FASTA sequences is set to 4.

Input:

.. code-block:: console

 >Sequence1
 ACTGACTGAC
 >Sequence2
 ACTGACTGACTGA

Output:

.. code-block:: console

 >Sequence1
 ACTG
 ACTG
 AC
 >Sequence2
 ACTG
 ACTG
 ACTG
 A

Rename header
-------------

This operation allows editing sequence headers in different ways. These ways are specified in the *‘Rename type’* parameter, which allows choosing between: *Multipart header*, *Replace word*, *Replace interval* and *Add prefix/suffix*. Each of these methods is explained below.

Common to all these methods is the *‘Target’* parameter, which allows to specify which part of the sequence headers must be processed: *Name*, to process only the sequence identifier; *Description*, to process only the description part of the header; or *All*, to process both name and description together.

.. figure:: images/operations/rename-header/1.png
   :align: center

If a file selection has been done, the *‘Rename preview’* area shows you a preview of the current configuration applied to the first sequence of the first selected file.

Multipart header
++++++++++++++++

The *‘Multipart header’* rename allows splitting the sequence header into fields delimited by the characters specified in the *‘Field delimiter’* parameter. Then, you can select which fields you want to keep or remove and which delimiter (*‘Join delimiter’* parameter) should be used to create the new sequence header. Note that when the *‘Keep‘* mode is used, then the order of the fields is preserved in the output, meaning that it is possible to swap fields using this feature.

.. figure:: images/operations/rename-header/2.png
   :align: center

As an example, consider that you have a set of sequences that have the following header structure:

.. code-block:: console

 >SequenceIdentifier [field1=value] [field2=value] [field3=value] [field4=value]

As you can see, fields are separated by a blank space. Thus, this rename mode is useful to remove those fields you are not interested in. The following example shows how only *field4* is kept in the output fasta. The configuration applied to do this should be: *‘Target’* = *‘Description’*, *‘Field delimiter’* = *‘ ‘*, *‘Join delimiter’* = *‘ ‘*, *‘Mode’* = *‘Keep’*, *‘Fields’* = *‘4’*.

Input:

.. code-block:: console

 >Sequence1 [field1=1.1] [field2=1.2] [field3=1.3] [field4=1.4]
 ACTG
 >Sequence2 [field1=2.1] [field2=2.2] [field3=2.3] [field4=2.4]
 ACTG
 >Sequence3 [field1=3.1] [field2=3.2] [field3=3.3] [field4=3.4]
 ACTG

Output:

.. code-block:: console

 >Sequence1 [field4=1.4]
 ACTG
 >Sequence2 [field4=2.4]
 ACTG
 >Sequence3 [field4=3.4]
 ACTG

Replace word
++++++++++++

The *‘Replace word’* rename mode allows replacing one or more words (*‘Targets’* parameter) by a *‘Replacement’* word. Moreover the *‘Regex’* parameter allows to specify whether target words should be evaluated as regular expressions or not (see section :ref:`Regular expressions<advanced-regex>` to know how to define regular expressions).

.. figure:: images/operations/rename-header/3.png
   :align: center

As an example, consider that you have a set of sequences that have the following header structure:

.. code-block:: console

 >SequenceIdentifier [gen=value] [protein=value]

As you can see, there are two description fields providing information about gene and protein. Thus, this rename mode is useful to remove those words and keep only the actual information values. The following example illustrates this process. The configuration applied to do this should be: *‘Targets’* = [*‘[gen=’, ‘[protein=’*, *‘]’* ], *‘Regex’* = *‘not selected‘*, *‘Replacement’* = *‘’*.

Input:

.. code-block:: console

 >Sequence1 [gen=genA] [protein=proteinA.1]
 ACTG
 >Sequence2 [gen=genB] [protein=proteinB.2]
 ACTG
 >Sequence3 [gen=genC] [protein=proteinC.3]
 ACTG

Output:

.. code-block:: console

 >Sequence1 genA proteinA.1
 ACTG
 >Sequence2 genB proteinB.2
 ACTG
 >Sequence3 genC proteinC.3
 ACTG

Replace interval
++++++++++++++++

The *‘Replace interval’* rename mode allows replacing an interval delimited by two words (*‘From’* and *‘to’*) by a *‘Replacement’* word.

.. figure:: images/operations/rename-header/4.png
   :align: center

As an example, consider that you have a set of sequences that have the following header structure:

.. code-block:: console

 >SequenceIdentifier [gen=value] / some automatically generated information / [protein=value]

As you can see, there are two description fields providing information about gene and protein and some information delimited by *‘/’*. Thus, this rename mode is useful to remove this interval. The following example illustrates this process. The configuration applied to do this should be: *‘From’* = *‘ / ’*, *‘To’* = *‘‘ / ’*, *‘Replacement’* = *‘[DELETED]’*.

Input:

.. code-block:: console

 >Sequence1 [gen=genA] / some automatically generated information / [protein=proteinA.1]
 ACTG
 >Sequence2 [gen=genB] / some automatically generated information / [protein=proteinB.2]
 ACTG
 >Sequence3 [gen=genC] / some automatically generated information / [protein=proteinC.3]
 ACTG

Output:

.. code-block:: console

 >Sequence1 [gen=genA] [DELETED] [protein=proteinA.1]
 ACTG
 >Sequence2 [gen=genB] [DELETED] [protein=proteinB.2]
 ACTG
 >Sequence3 [gen=genC] [DELETED] [protein=proteinC.3]
 ACTG

.. _operations-rename-header-add:

Add prefix/suffix
+++++++++++++++++

The *‘Add prefix/suffix’* rename mode allows adding the word specified in the *‘String’* parameter to the sequence headers. This word can be added in three positions (*‘Position’* parameter): *Prefix*, that is, before the part of the header to modify; *Suffix*, that is, after the part of the header to modify; or *Override*, that is, entirely replacing the part of the header to modify. This mode has the following additional parameters:

- *Delimiter*: the delimiter between the word to add and the header. Note that the word to add also includes the index.
- *Add index*: whether an index should be added to the defined word or not.
- *Index delimiter*: the delimiter between the word to add and the index number.

.. figure:: images/operations/rename-header/5.png
   :align: center

As an example, consider that you are interested in adding the word ‘Sequence’ delimited by a ‘_’ with an index delimited by a ‘_’. The resulting word can be added as prefix, suffix or overriding the entire header. For the sake of simplicity, input sequences do not contain a description in their headers.

Input:

.. code-block:: console

 >Homo_Sapiens_NP.00097
 ACTG
 >Homo_Sapiens_NP.00198
 ACTG
 >Homo_Sapiens_NP.02004
 ACTG

Output (*Prefix*):

.. code-block:: console

 >Sequence_1_Homo_Sapiens_NP.00097
 ACTG
 >Sequence_2_Homo_Sapiens_NP.00198
 ACTG
 >Sequence_3_Homo_Sapiens_NP.02004
 ACTG

Output (*Suffix*):

.. code-block:: console

 >Homo_Sapiens_NP.00097_Sequence_1
 ACTG
 >Homo_Sapiens_NP.00198_Sequence_2
 ACTG
 >Homo_Sapiens_NP.02004_Sequence_3
 ACTG

Output (*Override*):

.. code-block:: console

 >Sequence_1
 ACTG
 >Sequence_2
 ACTG
 >Sequence_3
 ACTG

.. _operations-reformat-file:

Sort
----

This operation allows sequences to be sorted based either on sequence headers or on the content of the sequences. You can choose between two criteria to sort them: length or alphabetical. By default, sequences are sorted in ascending order (i.e. the shortest sequence in the first place). The *‘Descending’* option allows to sort sequences in descending order (i.e. the longest sequence in the first place).

.. figure:: images/operations/sort/1.png
   :align: center

Examples
++++++++

The following example shows an input FASTA file sorted by sequence length (i.e. number of nucleotides or amino acids) in descending order.

Input:

.. code-block:: console

 >Sequence1
 ACTGACTGAC
 >Sequence2
 ACTGACTGACTGA
 >Sequence3
 ACTG
 >Sequence4
 ACTGACTGACTGACTG

Output:

.. code-block:: console

 >Sequence4
 ACTGACTGACTGACTG
 >Sequence2
 ACTGACTGACTGA
 >Sequence1
 ACTGACTGAC
 >Sequence3
 ACTG
