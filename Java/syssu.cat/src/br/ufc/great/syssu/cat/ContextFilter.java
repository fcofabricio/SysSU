package br.ufc.great.syssu.cat;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.syssu.coordubi.FieldUtils;
import br.ufc.great.syssu.coordubi.Pattern;
import br.ufc.great.syssu.coordubi.PatternField;
import br.ufc.great.syssu.coordubi.Tuple;
import br.ufc.great.syssu.coordubi.interfaces.IFilter;

public class ContextFilter implements IFilter {

	private Query query;

	public ContextFilter(Query query) {
		this.query = query;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	@Override
	public List<Tuple> filter(List<Tuple> tuples) {
		List<Tuple> result = new ArrayList<Tuple>();

		for (Tuple t : tuples) {
			Tuple tuple = filter(t);
			if (tuple != null) {
				result.add(tuple);
			}
		}

		return result.isEmpty() ? null : result;
	}

	@Override
	public Tuple filter(Tuple tuple) {
		if (query == null || query.getVirtualTuple() == null
				|| query.getVirtualTuple().getPattern() == null
				|| query.getVirtualTuple().getVirtualizationFunction() == null) {
			return tuple;
		}
		
		if (tuple.matches(query.getVirtualTuple().getPattern())) {
			try {
				return TupleVirtualizer.virtualize(
								tuple,
								createVirtualTuple(query.getPattern()),
								query.getVirtualTuple().getVirtualizationFunction());
			} catch (CheckException e) {
				return null;
			}
		}
		return null;
	}

	private Tuple createVirtualTuple(Pattern pattern) {
		Tuple tuple = new Tuple();

		while (pattern.iterator().hasNext()) {
			PatternField pf = pattern.iterator().next();
			if (pf.getName().equals("?")) {
				throw new IllegalArgumentException("Field with name '?'");
			}
			tuple.addField(pf.getName(), FieldUtils.getDefaultValue(pf.getValue()));
		}

		return tuple;
	}

}
