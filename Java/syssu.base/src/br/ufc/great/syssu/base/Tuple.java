package br.ufc.great.syssu.base;

import java.security.InvalidParameterException;

import br.ufc.great.syssu.base.interfaces.IMatcheable;

public class Tuple extends AbstractFieldCollection<TupleField> implements IMatcheable {
	
	private long timeToLive;
	private long putTime;

    public Tuple() {
        super();
    }
    
    public Tuple(long timeToLive) {
    	super();
    	setTimeToLive(timeToLive);
    }

    @Override
    public TupleField createField(String name, Object value) {
        return new TupleField(name, value);
    }

    @Override
    public boolean matches(Query query) throws FilterException {
    	if(query == null || query.getPattern() == null  || query.getPattern().isEmpty() || this.isEmpty()) {
			return true;
		}
        return associatesAll(query.getPattern()) && TupleFilter.doFilter(this, query.getFilter());
    }

    public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		if(timeToLive < 0) throw new InvalidParameterException("Negative time");
    	this.timeToLive = timeToLive;
	}

	public long getPutTime() {
		return putTime;
	}

	public void setPutTime(long putTime) {
		if(putTime < 0) throw new InvalidParameterException("Negative time");
		this.putTime = putTime;
	}
	
	public boolean isAlive() {
		return putTime == 0 || System.currentTimeMillis() - putTime > timeToLive;
	}

	private boolean associatesAll(Pattern pattern) {
        boolean matches = true;

        for (PatternField pField : pattern) {
            matches = (matches) ? associatesOne(pField) : false;
        }
        return matches;
    }

    private boolean associatesOne(PatternField pField) {
        for (TupleField tField : this) {
            if (tField.associates(pField)) {
                return true;
            }
        }
        return false;
    }
}
