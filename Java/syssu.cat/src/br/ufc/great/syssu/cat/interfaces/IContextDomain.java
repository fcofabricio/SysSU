package br.ufc.great.syssu.cat.interfaces;

import java.util.List;

import br.ufc.great.syssu.cat.Query;
import br.ufc.great.syssu.coordubi.*;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.interfaces.IReaction;

public interface IContextDomain {
	public String getName();

	public void put(Tuple tuple) throws TupleSpaceException;

	public List<Tuple> read(Query query) throws TupleSpaceException;

	public List<Tuple> readSync(Query query) throws TupleSpaceException;

	public Tuple readOne(Query query) throws TupleSpaceException;

	public Tuple readOneSync(Query query) throws TupleSpaceException;

	public List<Tuple> take(Query query) throws TupleSpaceException;

	public List<Tuple> takeSync(Query query) throws TupleSpaceException;

	public Tuple takeOne(Query query) throws TupleSpaceException;

	public Tuple takeOneSync(Query query) throws TupleSpaceException;

	public Object addReaction(IContextReaction reaction, String event) throws TupleSpaceException;
	
	public void removeReaction(Object reactionId) throws TupleSpaceException;
}
