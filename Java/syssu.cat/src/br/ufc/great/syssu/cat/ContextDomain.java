package br.ufc.great.syssu.cat;

import java.util.List;

import br.ufc.great.syssu.cat.interfaces.IContextDomain;
import br.ufc.great.syssu.cat.interfaces.IContextReaction;
import br.ufc.great.syssu.coordubi.Tuple;
import br.ufc.great.syssu.coordubi.TupleSpaceException;
import br.ufc.great.syssu.coordubi.interfaces.IDomain;

public class ContextDomain implements IContextDomain {

	private IDomain domain;
	private ContextFilter filter;
	private ContextRestrictor restrictor;

	public ContextDomain(IDomain domain) {
		this.domain = domain;
		this.filter = new ContextFilter(null);
		this.restrictor = new ContextRestrictor(null);
		
		this.domain.setFilter(this.filter);
		this.domain.setRestrictor(this.restrictor);
	}

	@Override
	public String getName() {
		return domain.getName();
	}

	@Override
	public void put(Tuple tuple) throws TupleSpaceException {		
		domain.put(tuple);
	}

	@Override
	public List<Tuple> read(Query query) throws TupleSpaceException {
		filter.setQuery(query);
		restrictor.setRestriction(query.getRestriction());
		return domain.read(query.getPattern());
	}

	@Override
	public List<Tuple> readSync(Query query) throws TupleSpaceException {
		filter.setQuery(query);
		restrictor.setRestriction(query.getRestriction());
		return domain.readSync(query.getPattern());
	}

	@Override
	public Tuple readOne(Query query) throws TupleSpaceException {
		filter.setQuery(query);
		restrictor.setRestriction(query.getRestriction());
		return domain.readOne(query.getPattern());
	}

	@Override
	public Tuple readOneSync(Query query) throws TupleSpaceException {
		filter.setQuery(query);
		restrictor.setRestriction(query.getRestriction());
		return domain.readOneSync(query.getPattern());
	}

	@Override
	public List<Tuple> take(Query query) throws TupleSpaceException {
		filter.setQuery(query);
		restrictor.setRestriction(query.getRestriction());
		return domain.take(query.getPattern());
	}

	@Override
	public List<Tuple> takeSync(Query query) throws TupleSpaceException {
		filter.setQuery(query);
		restrictor.setRestriction(query.getRestriction());
		return domain.takeSync(query.getPattern());
	}

	@Override
	public Tuple takeOne(Query query) throws TupleSpaceException {
		filter.setQuery(query);
		restrictor.setRestriction(query.getRestriction());
		return domain.takeOne(query.getPattern());
	}

	@Override
	public Tuple takeOneSync(Query query) throws TupleSpaceException {
		filter.setQuery(query);
		restrictor.setRestriction(query.getRestriction());
		return domain.takeOneSync(query.getPattern());
	}

	@Override
	public Object addReaction(IContextReaction reaction, String event) throws TupleSpaceException {
		return domain.addReaction(reaction, event);
	}

	@Override
	public void removeReaction(Object reactionId) throws TupleSpaceException {
		domain.removeReaction(reactionId);
	}

}
