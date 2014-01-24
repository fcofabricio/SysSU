package br.ufc.great.syssu.cat.interfaces;

import br.ufc.great.syssu.cat.Query;
import br.ufc.great.syssu.base.interfaces.IReaction;

public interface IContextReaction extends IReaction {
	Query getQuery();
}
