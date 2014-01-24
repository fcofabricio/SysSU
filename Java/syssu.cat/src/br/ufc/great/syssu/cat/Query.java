package br.ufc.great.syssu.cat;

import br.ufc.great.syssu.coordubi.Pattern;

public class Query {
	private Pattern pattern;
	private String restriction;
	private VirtualTupleEspc virtualTuple;

	public Query(Pattern pattern, String restriction, VirtualTupleEspc virtualTuple) {
		this.pattern = pattern;
		this.restriction = restriction;
		this.virtualTuple = virtualTuple;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getRestriction() {
		return restriction;
	}

	public VirtualTupleEspc getVirtualTuple() {
		return virtualTuple;
	}
}
