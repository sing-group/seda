package org.sing_group.seda.bio;

import static java.util.Arrays.stream;

import java.util.stream.Stream;

import org.sing_group.seda.datatype.Sequence;

public final class SequenceUtils {

	private SequenceUtils() {}

	public static Stream<String> toCodons(Sequence sequence) {
		return toCodons(sequence.getChain());
	}

	public static Stream<String> toCodons(String chain) {
		if (chain.length() % 3 != 0) {
			throw new IllegalArgumentException("Sequence length must be multiple of 3");
		} else if (!isGene(chain)) {
			throw new IllegalArgumentException("Only gene sequences are allowed");
		} else {
			final int numCodons = chain.length() / 3;

			final String[] codons = new String[numCodons];
			for (int i = 0; i < numCodons; i++) {
				codons[i] = chain.substring(i * 3, (i + 1) * 3);
			}
			
			return stream(codons);
		}
	}
	
	public static boolean isDNA(Sequence sequence) {
		return isDNA(sequence.getChain());
	}
	
	public static boolean isDNA(String chain) {
		return chain.replaceAll("[ACGT]", "").isEmpty();
	}
	
	public static boolean isRNA(Sequence sequence) {
		return isRNA(sequence.getChain());
	}
	
	public static boolean isRNA(String chain) {
		return chain.replaceAll("[ACGU]", "").isEmpty();
	}
	
	public static boolean isGene(Sequence sequence) {
		return isGene(sequence.getChain());
	}
	
	public static boolean isGene(String chain) {
		return chain.replaceAll("[ACGTU]", "").isEmpty();
	}
	
	public static boolean isProtein(Sequence sequence) {
		return isProtein(sequence.getChain());
	}
	
	public static boolean isProtein(String chain) {
		return chain.replaceAll("[ABCDEFGHIKLMNPQRSTVWYZ]", "").isEmpty();
	}
}
