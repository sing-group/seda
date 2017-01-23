package org.sing_group.seda.datatype;

import java.io.Serializable;

public class DefaultSequence implements Sequence, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final String chain;

	public DefaultSequence(String name, String chain) {
		this.name = name;
		this.chain = chain;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getChain() {
		return this.chain;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((chain == null) ? 0 : chain.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultSequence other = (DefaultSequence) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (chain == null) {
			if (other.chain != null)
				return false;
		} else if (!chain.equals(other.chain))
			return false;
		return true;
	}

}
