package br.ufc.great.syssu.cat;

import br.ufc.great.syssu.coordubi.Pattern;

public class VirtualTupleEspc {
	private Pattern pattern;
	private String virtualizationFunction;

	public VirtualTupleEspc(Pattern pattern, String virtualizationFunction) {
		this.pattern = pattern;
		this.virtualizationFunction = virtualizationFunction;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getVirtualizationFunction() {
		return virtualizationFunction;
	}
}
