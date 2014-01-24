package br.ufc.great.syssu.cat.utils;

import java.util.Map;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.utils.MapTuple;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JSONTuple implements JSONable<Tuple> {

    private Tuple tuple;

    public JSONTuple(String json) {
    	this.tuple = fromJSON(json);
    }

    public JSONTuple(Tuple tuple) {
        this.tuple = tuple;
    }

    @Override
    public String getJSON() {
        return tuple != null ? JSONObject.toJSONString(new MapTuple(tuple).getMap()) : null;
    }

    @Override
    public Tuple getObject() {
    	return tuple;
    }
    
    @SuppressWarnings("unchecked")
	private Tuple fromJSON(String json) {
        try {
            Object jsonObj = new JSONParser().parse(json);
            return new MapTuple((Map<String, Object>) jsonObj).getObject();
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

}

