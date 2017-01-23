package org.sing_group.seda.bio;

public enum StopCodon {
	TAA, TAG, TGA;
	
	public String getChain() {
		return this.name();
	}
	
	public static boolean isAStopCodon(String codon) {
		for (StopCodon stopCodon : values()) {
			if (stopCodon.getChain().equals(codon)) {
				return true;
			}
		}
		
		return false;
	}
}
