package br.ufc.great.syssu.cat.utils;

import java.security.InvalidParameterException;
import java.util.*;
import br.ufc.great.syssu.cat.Query;
import br.ufc.great.syssu.cat.VirtualTupleEspc;
import br.ufc.great.syssu.coordubi.*;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Error;
import br.ufc.great.syssu.jsonrpc2.util.NamedParamsRetriever;

public class MapQuery implements IMappable<Query> {

	private Query query;

	public MapQuery(Map<String, Object> map) {
		this.query = fromMap(map);
	}

	public MapQuery(Query query) {
		this.query = query;
	}

	@Override
	public Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("pattern", query.getPattern());

		if (query.getRestriction() != null && !query.getRestriction().equals("")) {
			map.put("restriction", query.getRestriction());
		}

		if (query.getVirtualTuple() != null) {
			Map<String, Object> virtual = new HashMap<String, Object>();
			virtual.put("pattern", query.getVirtualTuple().getPattern());
			virtual.put("virtualizationFuction", query.getVirtualTuple()
							.getVirtualizationFunction());
			map.put("virtualTuple", virtual);
		}

		return map;
	}
	
	@Override
	public Query getObject() {
		return query;
	}

	@SuppressWarnings("unchecked")
	private Query fromMap(Map<String, Object> map) {
		NamedParamsRetriever retriever = new NamedParamsRetriever(map);
		try {
			Pattern pattern = new MapPattern(retriever.getMap("pattern")).getObject();
			String restriction = retriever.getOptString("restriction", true, null);

			Map<String, Object> virtualTupleMap = retriever.getOptMap("virtualTuple", true, null);

			VirtualTupleEspc virtualTuple = null;
			if (virtualTupleMap != null) {
				virtualTuple = new VirtualTupleEspc(new MapPattern((Map<String, Object>)virtualTupleMap.get("pattern")).getObject(),
							virtualTupleMap.get("virtualizationFuction").toString());
			}

			return new Query(pattern, restriction, virtualTuple);
		} catch (JSONRPC2Error ex) {
			throw new InvalidParameterException(ex.getMessage());
		}
	}

}
