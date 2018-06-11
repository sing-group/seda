Operations
**********

This section provides you an overview on the different processing operations available in SEDA. Based on the relation between input and output files, operations can be classified in two groups: 

i. Those that process each input file to produce exactly one output file, which is a modified version of the input file: Filtering, Pattern filtering, Base presence filtering, Remove redundant sequences, Sort, Reallocate reference sequences, Rename header, Reformat file, Grow sequences, NCBI rename, Undo alignment, Disambiguate sequence names.
ii. Those that produce a different number of output files: Split, Merge, Consensus sequence, Concatenate sequences, and Blast.

.. _operations-pattern-filtering:

Filtering
=========

This operation allows filtering sequences based on different criteria (e.g. sequence length, non-multiple of three, or stop codons presence, among others).

The image below shows the configuration panel of the *Filtering operation*. If more than one option is selected, they are applied in the following order:

1. Valid starting codons: filters sequences so that only those starting with the selected codons are kept.
2. Remove stop codons: removes stop codons from the end of the sequences.
3. Remove sequences with a non-multiple of three size: filters sequences so that only those having a length that is multiple of 3 are kept.
4. Remove sequences with in-frame stop codons: filters sequences so that only those without in-frame stop codons are kept.
5. Minimum sequence length: filters sequences so that only those with the specified minimum sequence length are kept. A value of 0 indicates that no minimum sequence length is required.
6. Maximum sequence length: filters sequences so that only those with the specified maximum sequence length are kept. A value of 0 indicates that no minimum sequence length is required.
7. If the header count filtering option is selected at the sequences level, then it filters sequences so that only meeting the specified criteria regarding header counts are kept.
8. Minimum number of sequences: filters files so that only those with the specified minimum number of sequences are kept.
9. Maximum number of sequences: filters files so that only those with the specified maximum number of sequences are kept.
10. If the header count filtering option is selected at the files level, then it filters files so that only those where all sequences meet the specified criteria regarding header counts are kept.
11. Remove by size difference: filters sequences so that only those with the specified difference when compared to the reference sequence are kept.

  a)	Maximum size difference (%): the maximum sequence length difference allowed expressed as a percentage.
  b)	Reference sequence index: the index of the sequence to use as reference to compare others. The first sequence corresponds to index 1. This option is ignored if a reference sequence file (next option) is selected.
  c)	Reference sequence file: the file containing the sequence to use as reference to compare others. If a file is selected, then the reference sequence index is ignored.

.. figure:: images/operations/filtering/1.png
   :align: center
   
Examples
--------

Valid starting codons
+++++++++++++++++++++

By clicking on the *‘Codons‘* label, a list with the possible starting codons is shown, allowing you to select one or more starting codons.

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

Remove stop codons
++++++++++++++++++

The following example shows how sequences in the input FASTA are modified to remove stop codons from the end of the sequence. Note that this option actually modifies the input sequences.

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

Remove sequences with a non-multiple of three size
++++++++++++++++++++++++++++++++++++++++++++++++++

This example shows how sequences with a non-multiple of three size are removed from the input FASTA. Only *Sequence1* and *Sequence2*, with 15 bases, appear in the output FASTA. *Sequence3* is removed since it has 17 bases.

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
++++++++++++++++++++++++++++++++++++++++++

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
+++++++++++++++++++++++

This example shows how sequences with a length below 7 are removed from the input FASTA. Thus, only "Sequence3", with 15 bases, appear in the output FASTA. "Sequence1" and "Sequence2" are removed since they have 4 and 6 bases respectively.

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
+++++++++++++++++++++++

This example shows how sequences with a length above 5 are removed from the input FASTA. Thus, only *Sequence1*, with 4 bases, appear in the output FASTA. *Sequence2* and *Sequence3*  are removed since they have 6 and 15 bases respectively.

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
+++++++++++++++++++++++++

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

Header count filtering
++++++++++++++++++++++

This example shows how to use this filter in order to remove all sequences in the input FASTA whose sequence identifier appears exactly two times among all sequences. 

.. figure:: images/operations/filtering/3.png
   :align: center
   
By using the configuration above, only *Sequence1* and *Sequence3* are kept in the output FASTA. If the same filter would be applied at the files level, then the input FASTA would not appear in the output directory.

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

Pattern filtering
=================

This operation allows filtering sequences based on a text pattern (note that this pattern can be also a regular expression). Filtering can be applied to either sequence headers or the sequence content.

The image below shows the configuration panel of the *Pattern filtering* operation. This configuration panel allows you to configure how the pattern filtering is applied:

- *Header* or *Sequence*: check Sequence to look for the the pattern on the sequence content or Header to look for the pattern on the sequence header.
- *Convert to amino acid sequence before pattern matching*: when filtering sequences based on the sequence content, it is also possible to indicate that the sequences must be converted to amino acid sequences before applying the pattern. See below for further information on this configuration.
- *Pattern*: SEDA allows you to define patterns in different ways. Refer to section :ref:`Pattern configuration<advanced-pattern-configuration>` to learn how to create patterns.

.. figure:: images/operations/pattern-filtering/1.png
   :align: center
   
When filtering sequences based on the sequence content, it is also possible to indicate that the sequences must be converted to amino acid sequences before applying the pattern. This way, it is possible to filter nucleic acid sequences based on amino acid patterns. To do so, the *‘Convert to amino acid sequence before pattern matching* option should be enabled. This option allows you to configure the translation mode using the panel below.

.. figure:: images/operations/pattern-filtering/2.png
   :align: center
   
This panel allows you to specify:

- The frame in which translation should start. You can choose between:

  - *Starting at fixed frame*: by selecting this option, sequences are translated starting at the specified frame.
  - *Considering frames 1, 2 and 3*: by selecting this option, three translations starting at frames 1, 2 and 3 are created. This way, the pattern is applied to each translation separately and it is considered present if it is present in any of the translations. 
  
    - If the *‘Join frames’* option, then the three translations are concatenated before testing the pattern. This is useful if a set of sequences is being processed and the composed pattern should be found in any of the frames: one part of the pattern can be present in one frame and other part in a different frame and the pattern would be found.
	
- *Use a custom codon code*: this option allows you selecting a file containing a custom DNA codon table. This option is unselected by default and SEDA uses the standard genetic code.
- *Use reverse complement sequences*: whether reverse complement of sequences must be calculated before translation or not. If not selected, sequences are used as they are introduced.

Examples
--------

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

Base presence filtering
=======================

This operation allows filtering sequences based on the percentages of their bases (nucleotides or amino acids). By using the configuration panel shown below, you can add one or more bases and specify their minimum and maximum percentages. Sequences with bases whose percentage of presence is outside the specified thresholds are removed. Moreover, if you specify several bases in a single row then the sum of each percentage is used for checking the thresholds.

.. figure:: images/operations/base-presence-filtering/1.png
   :align: center
   
Examples
--------

Consider the following input FASTA file with two sequences:

Input:

.. code-block:: console

 >Sequence1
 AAAAAACCCCCTTTGGGA
 >Sequence2
 AAAAAACCCTGGNNNNNN

The percentages of presence of sequence bases are:

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

For instance, to filter the input FASTA in order to obtain only those sequences with a percentage of A’s between 0.35 and 0.40, the the following configuration should be used. In this case, only the first sequence will be in the output file.

.. figure:: images/operations/base-presence-filtering/2.png
   :align: center

For instance, to filter the input FASTA in order to obtain only those sequences with a percentages of T’s or G’s between 0.10 and 0.20, the the following configuration should be used. In this case, only the second sequence will be in the output file since the sum of T’s and G’s is 0.16 while in the first sequence is 0.32.

.. figure:: images/operations/base-presence-filtering/3.png
   :align: center
   
Remove redundant sequences
==========================

This operation allows removing redundant sequences. Redundant sequences are sequences with exactly the same sequence bases. If the *‘Remove also subsequences’* option is selected, then sequence bases contained in larger sequences are also removed.

.. figure:: images/operations/remove-redundant-sequences/1.png
   :align: center

Option *‘Merge headers’* allows controlling how new sequences are created. If this option is not selected, then the header of the new sequence is the header of one of the two being merged. On the contrary, if this option is selected, the header of the new sequence is created by concatenating the headers of the two sequences being merged. You can also save a report of the merged headers into a file by selecting the *‘Save merged headers into a file’*.

When removing redundant sequences, it is also possible to indicate that the sequences must be converted to amino acid sequences before checking if they are redundant. This way, it is possible to filter nucleic acid sequences based on amino acid patterns. To do so, the *‘Convert to amino acid sequence before sequence comparison’* option should be enabled. This option allows you to configure the translation mode using the panel below.

.. figure:: images/operations/remove-redundant-sequences/2.png
   :align: center
   
This panel allows you to specify:

- The frame in which translation should start. You can choose between:
  
  - *Starting at fixed frame*: by selecting this option, sequences are translated starting at the specified frame.
  - *Considering frames 1, 2 and 3*: by selecting this option, three translations starting at frames 1, 2 and 3 are created. This way, each translation is tested separately and the sequence is considered redundant if any of the three frames is redundant. 

- *Use a custom codon code*: this option allows you selecting a file containing a custom DNA codon table. This option is unselected by default and SEDA uses the standard genetic code.
- *Use reverse complement sequences*: whether reverse complement of sequences must be calculated before translation or not. If not selected, sequences are used as they are introduced.

Examples
--------

The following example shows how only exact sequences are removed. Since *Sequence1* and *Sequence2* has the same sequence of nucleic acid bases, they are combined in the output FASTA. The *‘Merge headers’* is selected to illustrate how sequence headers are combined.

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

The following example shows how both exact sequences and subsequences are removed. Since *Sequence1* and *Sequence2* has the same sequence of nucleic acid bases, they are combined in the output FASTA. *Sequence3* is also combined with the previous combination because CCATGGGTACA is contained in it.

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

Sort
====

This operation allows you to sort sequences. Sort can be made based on sequence headers or on the content of the sequences. You can choose between two criteria to sort them: length or alphabetical. By default, sequences are sort in ascending order (e.g. the shortest sequence in first place). The *‘Descending’* option allows you to sort sequences in descending order (e.g. the longest sequence in first place).

.. figure:: images/operations/sort/1.png
   :align: center
   
Examples
--------

The following example shows an input FASTA file is sorted by sequence length (i.e. number of bases) in descending order.

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

Split
=====

This operation allows you to split each input FASTA file into several FASTA files. The *‘Split mode’* parameter defines the way of splitting them:

- *Fixed number of sequences per file*: it divides each input FASTA into several files containing the defined *‘Number of sequences’* each one.
- *Fixed number of files*: it divides each input FASTA into the defined *‘Number of files’* with the same number of sequences in each one.
- *Fixed number of sequences per defined number of files*: it divides each input FASTA into the defined *‘Number of files’* containing the defined *‘Number of sequences’* each one. In this mode, the the result of multiplying *‘Number of files’* by *‘Number of sequences’* should be less or equal to the number of sequences contained in the input FASTA file being processed. Nevertheless, in some occasions it may be necessary to do that. The option *‘Independent extractions’* allows doing this. See the examples section to now how this option works.

.. figure:: images/operations/split/1.png
   :align: center
   
In addition, if the *‘Randomize’* option is selected, sequences in the input FASTA are sorted in a random order before producing the output FASTA files.

Examples
--------

Fixed number of sequences per file
++++++++++++++++++++++++++++++++++

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
+++++++++++++++++++++

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
+++++++++++++++++++++++++++++++++++++++++++++++++++++

The following example shows how to split an input FASTA file containing into three files containing one sequence.

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
 
Note how input order is kept in three output FASTA files created. If the *‘Randomize’* option is used, the following output with sequences in a random order can be obtained.

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

Reallocate reference sequences
==============================

This operation allows you to find one or more sequences (i.e. your reference sequences) using a pattern filtering and reallocate them at the beginning of the file. For instance, this operation is useful to place at the beginning of your FASTA files the reference sequence or sequences and specify them in the *‘Remove by size difference’* filtering operation.

.. figure:: images/operations/reallocate-reference-sequences/1.png
   :align: center

The configuration of this operation is the same than the *Pattern filtering* configuration. Thus, you may refer to section :ref:`Pattern filtering<operations-pattern-filtering>` section to learn how to use it.

Examples
--------

The following example shows how an input FASTA is processed to reallocate those sequences containing *ACTG* at the beginning of the file.

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

Rename header
=============

This operation allows you to modify the sequence headers in different ways. These ways are specified in the *‘Rename type’* parameter, which allows you choosing between: *Multipart header*, *Replace word*, *Replace interval* and *Add prefix/suffix*. Each of these methods is explained below.

Common to all these methods is the *‘Target’* parameter, which allows you to specify which part of the sequence headers must be processed: *Name*, to process only the sequence identifier; *Description*, to process only the description part of the header; or *All*, to process both name and description together.

.. figure:: images/operations/rename-header/1.png
   :align: center

If a file selection has been done, the *‘Rename preview’* area shows you a preview of the current configuration applied to the first sequence of the first selected file.

Multipart header
----------------

The *‘Multipart header’* rename allows you to split the sequence header into fields delimited by the characters specified in the *‘Field delimiter’* parameter. Then, you can select which fields you want to keep or remove and which delimiter (*‘Join delimiter’* parameter) should be used to create the new sequence header.

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
------------

The *‘Replace word’* rename mode allows you to replace one or more words (*‘Targets’* parameter) by a *‘Replacement’* word. Moreover the *‘Regex’* parameter allows you to specify whether target words should be evaluated as regular expressions or not (see section :ref:`Regular expressions<advanced-regex>` to know how to define regular expressions).

.. figure:: images/operations/rename-header/3.png
   :align: center
   
As an example, consider that you have a set of sequences that have the following header structure:

.. code-block:: console

 >SequenceIdentifier [gen=value] [protein=value]

As you can see, there are two description fields providing information about gene and protein. Thus, this rename mode is useful to remove those words and keep only the actual information values. The following example shows illustrates this process. The configuration applied to do this should be: *‘Targets’* = [*‘[gen=’, ‘[protein=’*, *‘]’* ], *‘Regex’* = *‘not selected‘*, *‘Replacement’* = *‘’*.

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
----------------

The *‘Replace interval’* rename mode allows you to replace an interval delimited by two words (*‘From’* and *‘to’*) by a *‘Replacement’* word.

.. figure:: images/operations/rename-header/4.png
   :align: center
   
As an example, consider that you have a set of sequences that have the following header structure:

.. code-block:: console

 >SequenceIdentifier [gen=value] / some automatically generated information / [protein=value]

As you can see, there are two description fields providing information about gene and protein and some information delimited by *‘/’*. Thus, this rename mode is useful to remove these interval. The following example shows illustrates this process. The configuration applied to do this should be: *‘From’* = *‘ / ’*, *‘To’* = *‘‘ / ’*, *‘Replacement’* = *‘[DELETED]’*.

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
-----------------

The *‘Add prefix/suffix’* rename mode allows you to add the word specified in the *‘String’* parameter to the sequence headers. This word can be added in three positions (*‘Position’* parameter): *Prefix*, that is, before the part of the header to modify; *Suffix*, that is, after the part of the header to modify; or *Override*, that is, entirely replacing the part of the header to modify. This mode has the following additional parameters:

- *Delimiter*: the delimiter between the word to add and the header. Note that the word to add also includes the index.
- *Add index*: whether an index should be added to the defined word or not.
- *Index delimiter*: the delimiter between the word to add and the index number.

.. figure:: images/operations/rename-header/5.png
   :align: center

As an example, consider that you are interested in adding the word ‘Sequence’ delimited by a ‘_’ with an index delimited by a ‘_’. The resulting word can be added as prefix, suffix or overriding the entire header. For the sake of simplicity, input sequences does not contain a description in their headers.

The following example illustrates how line breaks are removed from the input FASTA sequences by using this operation with the *‘Remove line breaks’* option selected.

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

Reformat file
=============

This operation allows you to change the format of a FASTA file. This format includes:

- *Fragment length*: the fragment length or number of columns in which sequences are divided. The *’Remove line breaks’* option specifies that sequences should not be fragmented.
- *Line breaks*: the type of line breaks, which can be *‘Windows‘* or *‘Unix‘*.
- *Case*: the case of the sequences. *‘Original‘* means that original case in input sequences is kept and *‘Lower case’* and *‘Upper case’* allows converting sequences to lower or upper case bases respectively.

.. figure:: images/operations/reformat-file/1.png
   :align: center
   
Examples
--------

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

The following example illustrates how the length the input FASTA sequences is set to 4.

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

Grow sequences
==============

This operation allows you to grow sequences by merging those sequences with the specified *‘Minimum overlapping’* bases.

.. figure:: images/operations/grow-sequences/1.png
   :align: center

This operation applies the following algorithm to merge sequences:

1.	Use the first sequence as reference sequence.
2.	Compare the reference sequence to the rest of sequences. For each pair of sequences, check if there is an overlapping of bases of at least the minimum size specified. This overlapping is searched at the beginning of the reference sequence and at the ending of the sequence being compared.
  
  a)	If an overlapping is found, fint the maximum overlapping bases and merge the two sequences. The merged sequences are removed from the set of sequences and the new one is added. Return to step 1.
  b)	If an overlapping is not found between the first reference sequence and the rest of sequences, then step 2 is repeated for the rest of sequences repeatedly.

3.	The process stops when all sequences have been compared without merging any of them.

Examples
--------

The following example shows how sequences with a minimum overlapping of 6 in the input FASTA are merged. *Sequence1* and *Sequence2* have an overlapping region of 9 bases (*CTCTCTCTC*), thus they are merged in the output FASTA.

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

The following example shows how sequences with a minimum overlapping of 4 in the input FASTA are merged. *Sequence1* and *Sequence3* have an overlapping region of 5 bases (*AAAAA*) in the highlighted area, thus they are merged in first place. Then, the resulting sequence has an overlapping region of 8 bases with *Sequence2*, thus there is only one sequence in the output FASTA.

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

NCBI rename
===========

This operation allows you to replace NCBI accession numbers in the names of FASTA files by the associated organism name and additional information from the NCBI Taxonomy Browser (https://www.ncbi.nlm.nih.gov/Taxonomy/). An example of a FASTA file could be ‘GCF_000001735.3_TAIR10_cds_from_genomic.fna’. When this file is given to this operation, the organism name associated to the accession number ‘GCF_000001735.3’ is obtained from the NCBI (https://www.ncbi.nlm.nih.gov/assembly/GCF_000001735.3). In this case, the ‘*Arabidopsis thaliana* (thale cress)’ is the associated organism name. The *‘File name’* allows specifying how this name is added to the file name and the *‘Delimiter’* parameter specifies if a separator should be set between the name and the file name. You can choose between one of the following *‘Position’* values:

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
   
Finally, this operation also allows obtaining additional information from the NCBI Taxonomy. The *‘NCBI Taxonomy information’* panel allows you choosing what fields should be added to the organism name when applying the operation. Fields are added with the *‘Delimiter’* as separator. For instance, the accession number ‘GCF_000001735.3’ has this information page: https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=3702. If you select *‘Kingdom’*, then the string associated to it would be ‘*Arabidopsis thaliana* (thale cress)_Viridiplantae’. Note that some accession numbers or organisms may not have available information for all fields. In that case, those fields are ignored.

.. figure:: images/operations/ncbi-rename/4.png
   :align: center
   
Merge
=====

This operation allows you to merge all the selected input FASTA files into a single output FASTA. The *‘Name’* parameter defines the name for the output file. Additionally, you can specify the FASTA format parameters in the *‘Reformat output file’* area (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

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

Undo alignment
==============

This operation allows you to undo a sequence alignment by removing ‘-’ from sequences. Additionally, you can specify the FASTA format parameters in the *‘Reformat output files’* area (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

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

Translate
=========

This operation allows you to translate nucleic acid sequences to their corresponding peptide sequences. It can translate to the three forward and three reverse frames, and output multiple frame translations at once. 

The configuration panel allows you to specify:

- The frame in which translation should start. You can choose between:

  - *Starting at fixed frame*: by selecting this option, sequences are translated starting at the specified frame.
  - *Considering frames 1, 2 and 3*: by selecting this option, three translations starting at frames 1, 2 and 3 are created. 
	
- *Use a custom codon code*: this option allows you selecting a file containing a custom DNA codon table. This option is unselected by default and SEDA uses the standard genetic code.
- *Use reverse complement sequences*: whether reverse complement of sequences must be calculated before translation or not. If not selected, sequences are used as they are introduced and therefore the three forward frames are obtained. If selected, the three reverse frames are obtained.

.. figure:: images/operations/translate/1.png
   :align: center

Examples
--------

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

Disambiguate sequence names
===========================

This operation allows you to disambiguate duplicated sequence names (identifiers). The configuration panel allows you to choose the way of disambiguating them: *Rename*, to add a numeric prefix to disambiguate duplicate names, or *Remove*, to remove sequences with duplicate identifiers, keeping the first occurrence. 

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

Consensus sequence
==================
 
This operation allows you to create a consensus sequence from a set of sequences of the same length. The consensus sequence is constructed by calculating the most frequent bases, either nucleotide or amino acid, found at each position in the given set of sequences. The configuration panel allows you to choose:

- *Sequence type*: the type of the sequences in the selected files. In nucleotide sequences, ambiguous positions are indicated by using the IUPAC ambiguity codes (http://www.dnabaser.com/articles/IUPAC%20ambiguity%20codes.html). In protein sequences, ambiguous positions are indicated as the *’Verbose’* option explains.
- *Minimum presence*: the minimum presence for a given nucleotide or amino acid in order to be part of the consensus sequence. Those positions where the most frequent base is under this threshold are represented by an *X* in the consensus sequence.
- *Verbose*: in protein sequences, when this option is unselected then *X* is used for ambiguous positions in the consensus sequence. On the other hand, when this option is selected, then all amino acids in such positions are reported (e.g. [HWY]).
- *Reformat output file*: allows you to specify the format parameters of the output FASTA containing the consensus sequence (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

.. figure:: images/operations/consensus-sequence/1.png
   :align: center
   
Examples
--------

The following example shows how nucleic acid sequences in the input FASTA are processed to create a consensus sequence using two different minimum presence thresholds: 0.2 and 0.6.

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
 XXSQ

Concatenate sequences
=====================

This operation allows you to merge all the selected input FASTA files into a single output FASTA by concatenating equivalent sequences. The *‘Name’* parameter defines the name for the output file. The *‘Header target‘* parameter defines how sequence headers are processed in order to match those equivalent sequences: *‘Name’* means that sequences with the same identifier are considered equivalent and *‘All’* means that the full header must be equal between two sequences in order to consider them equivalent.

Additionally, you can specify the FASTA format parameters in the *‘Reformat output file’* area (see section :ref:`Reformat file<operations-reformat-file>` to learn more about this formatting).

.. figure:: images/operations/concatenate-sequences/1.png
   :align: center

The following example illustrates how equivalent sequences in the input FASTA files 1 and 2 are concatenated and written as single output FASTA.

Input 1:

.. code-block:: console

 >Homo_sapiens
 AAAATTTT
 >Mus_musculus
 ACTGACTG

Input 2:

.. code-block:: console

 >Homo_sapiens
 CCCCGGGG
 >Mus_musculus
 GTCAGTCA

Output:

.. code-block:: console

 >Homo_sapiens
 AAAATTTTCCCCGGGG
 >Mus_musculus
 ACTGACTGGTCAGTCA

Blast
=====

This operation allows you to perform different BLAST queries using the selected FASTA files. Regarding the database to use in the queries, there are two possible modes: querying against all the selected FASTA files or querying against each FASTA file separately. Regarding the query, there are also to possibilities: using the sequences in one of the selected FASTA as queries or using the sequences in an external FASTA file as queries. Within this operation, one blast query is executed for each sequence in the FASTA file.

The figure below illustrates the process followed when a query against all selected FASTA files is performed. Firstly, one blast database is created for each selected FASTA file. Then, one alias referencing to all the databases created before is created. Finally, each sequence in the FASTA file used as query source is executed against the alias. As a result, this mode creates as many output files as sequences in the FASTA file. To create this output files, the sequences where hits were found are retrieved from the database.

.. figure:: images/operations/blast/1.png
   :align: center
   
On the other hand, the figure below shows the process followed when queries against each selected FASTA file are executed separately. Firstly, one blast database is created for each selected FASTA file. Then, each sequence in the FASTA file used as query source is executed against each of the databases. As a result, this mode creates as many output files as sequences in the FASTA file multiplied by the number of selected FASTA files. To create this output files, the sequences where hits were found are retrieved from the corresponding database.

.. figure:: images/operations/blast/2.png
   :align: center

Configuration
-------------

First, the *‘Blast configuration’* area allows you to select the path where the blast binaries (makeblastdb, blastdb_aliastool, blastdbcmd, blastp, blastn, blastx, tblastn, and tblastx) are located. If you have them in the system path, just click the *‘Check blast’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/blast/3.png
   :align: center
   
Then, the *‘DB configuration’* area allows you to control some aspects related with the databases created in the process. The type of the database is automatically selected according to the blast type to execute. This area allows you to indicate whether the databases and alias must be stored in a directory of your choice. Otherwise, temporary directories are used and they are deleted at the end of the process. Nevertheless, you may be interested in storing the databases for two reasons: use them again in SEDA or use them in BDBM (Blast DataBase Manager, http://www.sing-group.org/BDBM/). SEDA can reuse databases in the future since if databases with the same name exists in the selected directory they are not created again.

.. figure:: images/operations/blast/4.png
   :align: center
   
Finally, the *‘Query configuration’* area allows you to control how queries are performed. As explained before, first you must choose the query mode in the *‘Query against’* parameter. Secondly, you must choose the blast type that you want to perform using the *‘Blast type’* parameter. By selecting the blast type: (*i*) the type of database is automatically determined, and (*ii*) if *blastx* or *tblastn* types are selected, then you will only be allowed to select a genome query from an external file because the selected files used to construct the database cannot be used as query (blastx uses a database of proteins and a query of nucleotides and tblastn uses a database of nucleotides and a query of proteins).

Thirdly, the *‘Query source’* allows you selecting the source of the genome query file:

- *From selected file*: this option allows you selecting one of the selected files in SEDA using the *‘Genome query’* combobox.
- *From external file*: this option allows you selecting an external FASTA file to use as genome query file.

Then, three parameters allows you to control the query execution:

- *Expectation value*: the expectation value (E) threshold for saving hits.
- *Max. target. seqs*: the maximum number of aligned sequences to keep.
- *Additional parameters*: additional parameters for the blast command.

And finally, the *‘Extract only hit regions’* parameter allows you to define how output sequences are obtained. By default this option is not selected, meaning that the whole subject sequences where hits were found are used to construct the output FASTA files. If this option is selected, then only the part of the subject sequences where the hits were produced are used to construct the output FASTA files. Within this option, the *‘Hit regions window’* parameter allows you to specify the number of bases before and after the hit region that should be retrieved.

.. figure:: images/operations/blast/5.png
   :align: center

Blast: two-way ortholog identification
======================================

This operation allows you to find the orthologs of a given sequence in a set of FASTA files. The figure below illustrates the process followed by this operation. For each sequence in a reference FASTA, this operation looks for its orthologs in the set of genomes. For each sequence in the reference FASTA, the following process is applied:

1. A blast query against the first FASTA (hereafter, the reference FASTA) is performed using the reference sequence as query. Only the first hit is considered.
2. The sequence associated to the first hit in the target FASTA is used as query in a second blast query against the reference FASTA. Again, only the first is considered.
3. The sequence associated to the first hit in the reference FASTA is compared to the iteration sequence:

	A. If both sequences are the same, then the sequence found in step 2 is reported as ortholog.
	B. If both sequences are different, then the sequence found in step 2 is reported as ortholog if the *Report non-exact orthologues* is being used.
	
4. Steps 1 to 3 are repeated for each target FASTA available.

.. figure:: images/operations/blast-two-way/1.png
   :align: center

Configuration
-------------

First, the *‘Blast configuration’* area allows you to select the path where the blast binaries (makeblastdb, blastdb_aliastool, blastdbcmd, blastp, blastn, blastx, tblastn, and tblastx) are located. If you have them in the system path, just click the *‘Check blast’* button to make sure that SEDA can correctly execute them.

.. figure:: images/operations/blast-two-way/2.png
   :align: center
   
Then, the *‘DB configuration’* area allows you to control some aspects related with the databases created in the process. The type of the database is automatically selected according to the blast type to execute. This area allows you to indicate whether the databases must be stored in a directory of your choice. Otherwise, temporary directories are used and they are deleted at the end of the process. Nevertheless, you may be interested in storing the databases because SEDA can reuse them in the future: if databases with the same name exists in the selected directory they are not created again.

.. figure:: images/operations/blast-two-way/3.png
   :align: center
   
Finally, the *‘Query configuration’* area allows you to control how queries are performed. First, you can choose the ortholog report mode using the *‘Mode‘* parameter and choose *‘Report exact orthologues’* or *‘Report non-exact orthologues’*. Secondly, you must choose the blast type that you want to perform using the *‘Blast type’* parameter. By selecting the blast type: (*i*) the type of database is automatically determined, and (*ii*) if *blastx* or *tblastn* types are selected, then you will only be allowed to select a genome query from an external file because the selected files used to construct the database cannot be used as query (blastx uses a database of proteins and a query of nucleotides and tblastn uses a database of nucleotides and a query of proteins).

Thirdly, the *‘Query source’* allows you selecting the source of the genome query file:

- *From selected file*: this option allows you selecting one of the selected files in SEDA using the *‘Genome query’* combobox.
- *From external file*: this option allows you selecting an external FASTA file to use as genome query file.

And finally, two parameters allows you to control the query execution:

- *Expectation value*: the expectation value (E) threshold for saving hits.
- *Additional parameters*: additional parameters for the blast command.

.. figure:: images/operations/blast-two-way/4.png
   :align: center