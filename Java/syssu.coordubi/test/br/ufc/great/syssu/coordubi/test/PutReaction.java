package br.ufc.great.syssu.coordubi.test;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IReaction;
import br.ufc.great.syssu.coordubi.*;
import br.ufc.great.syssu.coordubi.interfaces.*;

public class PutReaction implements IReaction {
	
	private Pattern pattern;
	private String restriction;
	private Object id;
	private Tuple tuple;
	
	public PutReaction(Pattern pattern, String restriction) {
		this.pattern = pattern;
		this.restriction = restriction;
	}

	@Override
	public void setId(Object id) {
		this.id = id;		
	}

	@Override
	public Object getId() {
		return this.id;
	}

	@Override
	public Pattern getPattern() {
		return pattern;
	}
	
	@Override
	public String getRestriction() {
		return restriction;
	}
		
	public synchronized Tuple getTuple() {
		if(tuple == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tuple;
	}
	
	public void setTuple() {
		this.tuple = null;
	}

	@Override
	public synchronized void react(Tuple tuple) {
		this.tuple = tuple;		
		notifyAll();
	}

}
