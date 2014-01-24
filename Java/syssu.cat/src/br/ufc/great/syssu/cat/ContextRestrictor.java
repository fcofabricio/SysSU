package br.ufc.great.syssu.cat;
import br.ufc.great.syssu.coordubi.Tuple;
import br.ufc.great.syssu.coordubi.interfaces.IRestrictor;

public class ContextRestrictor implements IRestrictor {
	
	private String restriction;
	
	public ContextRestrictor(String restriction) {
		this.restriction = restriction;
	}
	
	public String getRestriction() {
		return restriction;
	}
	
	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	@Override
	public boolean restrict(Tuple tuple) {
		if(restriction == null || restriction.equals("")) {
			return true;
		} else if(tuple == null || tuple.isEmpty()) {
			return false;
		} else {
			try {
				return TupleRestrictioner.restrict(tuple, restriction);
			} catch (CheckException e) {
				return false;
			}
		}
	}

}
