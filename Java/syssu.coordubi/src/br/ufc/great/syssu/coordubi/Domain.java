package br.ufc.great.syssu.coordubi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ufc.great.syssu.base.FilterException;
import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Query;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.TupleSpaceSecurityException;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.interfaces.IReaction;
import br.ufc.great.syssu.base.utils.JSONTuple;
import br.ufc.great.syssu.coordubi.security.DomainSecurityChecker;

public class Domain implements IDomain {

	private static long eventId = 1;

	private String name;
	private IDomain parent;

	private Map<Integer, List<Tuple>> collections;
	private Map<String, Domain> subDomains;
	private Map<String, List<IReaction>> reactions;

	Domain(String name) throws TupleSpaceException {
		verifyDomainName(name);
		this.name = name;
		this.collections = new HashMap<Integer, List<Tuple>>();
		this.subDomains = new HashMap<String, Domain>();
		this.reactions = new HashMap<String, List<IReaction>>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IDomain getDomain(String domainName) throws TupleSpaceException {
		verifyDomainName(domainName);

		if (!subDomains.containsKey(domainName)) {
			Domain domain = new Domain(domainName);
			domain.setParent(this);
			subDomains.put(domainName, domain);
		}
		return subDomains.get(domainName);
	}

	@Override
	public void put(Tuple tuple, String key) throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canPut(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to put method");
		}
		
		put(tuple);
	}

	@Override
	public List<Tuple> read(Pattern pattern, String restriction, String key) 
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canRead(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to read method");
		}
		
		return read(pattern, restriction);
	}

	@Override
	public List<Tuple> readSync(Pattern pattern, String restriction, String key, long timeout) 
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canRead(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to read sync method");
		}
		
		return readSync(pattern, restriction, timeout);
	}

	@Override
	public Tuple readOne(Pattern pattern, String restriction, String key)
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canRead(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to read one method");
		}
		return readOne(pattern, restriction);
	}

	@Override
	public Tuple readOneSync(Pattern pattern, String restriction, String key, long timeout) 
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canRead(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to read one sync method");
		}
		return readOneSync(pattern, restriction, timeout);
	}

	@Override
	public List<Tuple> take(Pattern pattern, String restriction, String key) 
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canTake(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to take method");
		}
		return take(pattern, restriction);
	}

	@Override
	public List<Tuple> takeSync(Pattern pattern, String restriction, String key, long timeout) 
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canTake(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to take sync method");
		}
		return takeSync(pattern, restriction, timeout);
	}

	@Override
	public Tuple takeOne(Pattern pattern, String restriction, String key) 
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canTake(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to take onde method");
		}
		return takeOne(pattern, restriction);
	}

	@Override
	public Tuple takeOneSync(Pattern pattern, String restriction, String key, long timeout) 
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canTake(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to take one syncmethod");
		}
		return takeOneSync(pattern, restriction, timeout);
	}

	@Override
	public Object subscribe(IReaction reaction, String event, String key) 
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canRead(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to take method");
		}
		return subscribe(reaction, event);
	}

	@Override
	public void unsubscribe(String event, Object reactionId, String key) 
		throws TupleSpaceException, TupleSpaceSecurityException {
		if(!DomainSecurityChecker.getInstance().canRead(getName(), key)) {
			throw new TupleSpaceSecurityException("No permission to take method");
		}
		unsubscribe(event, reactionId);
	}

	private void put(Tuple tuple) {
		notifyReactions(tuple, "put");
		
		tuple.setPutTime(System.currentTimeMillis());

		if (!collections.containsKey(tuple.size())) {
			collections.put(tuple.size(), new ArrayList<Tuple>());
		}
		
		synchronized (this) {
			collections.get(tuple.size()).add(tuple);
		}		

		notifyReaders();
	}

	List<Tuple> read(Pattern pattern, String restriction) throws TupleSpaceException {
		List<Tuple> tuples = new ArrayList<Tuple>();

		try {
			tuples.addAll(find(pattern, restriction));
			for (Domain domain : subDomains.values()) {
				tuples.addAll(domain.read(pattern, restriction));
			}
		} catch (FilterException ex) {
			throw new TupleSpaceException(ex);
		}
		return tuples;
	}

	private List<Tuple> readSync(Pattern pattern, String restriction, long timeout) throws TupleSpaceException {
		List<Tuple> result = read(pattern, restriction);
		while (result.isEmpty()) {
			try {
				Thread.sleep(100);
				result = read(pattern, restriction);
				synchronized (this) {
					if (result.size() == 0) {
						if(timeout > 0) {
							wait(timeout);
						} else {
							wait();
						}
					}
				}
			} catch (InterruptedException ex) {
				throw new TupleSpaceException(ex.getMessage());
			}
		}
		return result;
	}

	private Tuple readOne(Pattern pattern, String restriction) throws TupleSpaceException {
		List<Tuple> all = read(pattern, restriction);
		if (all.size() > 0) {
			Random generator = new Random();
			return all.get(generator.nextInt(all.size()));
		}
		return null;
	}

	private Tuple readOneSync(Pattern pattern, String restriction, long timeout) throws TupleSpaceException {
		Tuple result = readOne(pattern, restriction);
		while (result == null) {
			try {
				Thread.sleep(100);
				result = readOne(pattern, restriction);
				synchronized (this) {
					if (result == null) {
						if(timeout > 0) {
							wait(timeout);
						} else {
							wait();
						}
					}
				}
			} catch (InterruptedException ex) {
				throw new TupleSpaceException(ex.getMessage());
			}
		}
		return result;
	}

	List<Tuple> take(Pattern pattern, String restriction) throws TupleSpaceException {
		List<Tuple> tuples = new ArrayList<Tuple>();

		try {
			List<Tuple> tempTuples = find(pattern, restriction);
			tuples.addAll(tempTuples);
			synchronized (this) {
				for (Tuple tuple : tempTuples) {
					collections.get(tuple.size()).remove(tuple);
					notifyReactions(tuple, "take");
				}
			}

			for (Domain domain : subDomains.values()) {
				tuples.addAll(domain.take(pattern, restriction));
			}
		} catch (FilterException ex) {
			throw new TupleSpaceException(ex);
		}

		return tuples;
	}

	private List<Tuple> takeSync(Pattern pattern, String restriction, long timeout) throws TupleSpaceException {
		List<Tuple> result = take(pattern, restriction);
		while (result.size() == 0) {
			try {
				Thread.sleep(100);
				result = take(pattern, restriction);
				synchronized (this) {
					if (result.size() == 0) {
						if(timeout > 0) {
							wait(timeout);
						} else {
							wait();
						}
					}
				}
			} catch (InterruptedException ex) {
				throw new TupleSpaceException(ex.getMessage());
			}
		}
		return result;
	}

	private Tuple takeOne(Pattern pattern, String restriction) throws TupleSpaceException {
		List<Tuple> tuples = read(pattern, restriction);
		if (tuples.size() > 0) {
			Random generator = new Random();
			Tuple tuple = tuples.get(generator.nextInt(tuples.size()));
			synchronized (this) {
				removeTuple(tuple);
				notifyReactions(tuple, "take");
			}
			return tuple;
		}
		return null;
	}

	private Tuple takeOneSync(Pattern pattern, String restriction, long timeout) throws TupleSpaceException {
		Tuple result = null;
		while (result == null) {
			try {
				Thread.sleep(100);
				result = takeOne(pattern, restriction);
				synchronized (this) {
					if (result == null) {
						if(timeout > 0) {
							wait(timeout);
						} else {
							wait();
						}
					}
				}
			} catch (InterruptedException ex) {
				throw new TupleSpaceException(ex.getMessage());
			}
		}
		return result;
	}

	private Object subscribe(IReaction reaction, String event) throws TupleSpaceException {
		if (!reactions.containsKey(event)) {
			reactions.put(event, new ArrayList<IReaction>());
		}

		Long id = eventId++;
		reaction.setId(id);

		reactions.get(event).add(reaction);
		return id;
	}

	private void unsubscribe(String event,Object reactionId) throws TupleSpaceException {
		if(event == null || event.equals("")) {
			for (List<IReaction> eventReactions : reactions.values()) {
				for (IReaction r : eventReactions) {
					if (r.getId().equals(reactionId)) {
						eventReactions.remove(r);
						return;
					}
				}
			}
		}
		else {
			if(reactions.containsKey(event)) {
				List<IReaction> eventReactions = reactions.get(event);
				for (IReaction r : eventReactions) {
					if (r.getId().equals(reactionId)) {
						eventReactions.remove(r);
						return;
					}
				}
			}
		}
	}

	private List<Tuple> find(Pattern pattern, String restriction) throws FilterException {
		return matchTuples(new Query(pattern, restriction), filterTuples(pattern));
	}

	private List<Tuple> matchTuples(Query query, List<Tuple> candidates)
		throws FilterException {
		List<Tuple> tuples = new ArrayList<Tuple>();
		
		for (Tuple tuple : candidates) {
			if(!tuple.isAlive()) {
				removeTuple(tuple);
				continue;
			}
			
			if (tuple.matches(query)) {
				tuples.add(tuple);
			}
		}
		return tuples;
	}

	private List<Tuple> filterTuples(Pattern pattern) {
		List<Tuple> candidates = new ArrayList<Tuple>();
		for (Integer size : collections.keySet()) {
			if (size >= pattern.size()) {
				List<Tuple> coll = collections.get(size);
				if (coll != null) {
					candidates.addAll(coll);
				}
			}
		}
		return candidates;
	}

	private void setParent(Domain parent) {
		this.parent = parent;
	}

	private void verifyDomainName(String domainName) throws TupleSpaceException {
		if (domainName == null || domainName.equals("") || domainName.indexOf(".") != -1) {
			throw new TupleSpaceException("Invalid domain name.");
		}
	}

	private void notifyReaders() {
		synchronized (this) {
			notifyAll();
		}
		if (parent != null) {
			((Domain) parent).notifyReaders();
		}
	}

	private void notifyReactions(Tuple tuple, String event) {
		try {
			if (reactions.containsKey(event)) {
				for (IReaction reaction : reactions.get(event)) {
					if (tuple != null && tuple.matches(new Query(reaction.getPattern(), reaction.getRestriction()))) {
						Logger.getLogger("UbiCentre").log(
								Level.INFO, 
								"Reaction '" + reaction.getId() + "' notified with tuple " + new JSONTuple(tuple).getJSON());
						reaction.react(tuple);
					}
				}
			}
			if (parent != null) {
				((Domain) parent).notifyReactions(tuple, event);
			}
		} catch (Exception ex) {
			// TODO treat exception
		}
	}

	private boolean removeTuple(Tuple tuple) {
		for (Integer size : collections.keySet()) {
			if (size == tuple.size()) {
				List<Tuple> coll = collections.get(size);
				if (coll != null && coll.contains(tuple)) {
					coll.remove(tuple);
					return true;
				}
			}
		}

		for (IDomain domain : subDomains.values()) {
			if (((Domain) domain).removeTuple(tuple)) {
				return true;
			}
		}

		return false;
	}
}
