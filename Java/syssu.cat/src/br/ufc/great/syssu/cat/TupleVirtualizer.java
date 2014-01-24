package br.ufc.great.syssu.cat;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import br.ufc.great.syssu.cat.utils.JSONTuple;
import br.ufc.great.syssu.coordubi.*;

public class TupleVirtualizer {

	public static Tuple virtualize(Tuple tuple, Tuple virtualTuple,
					String virtualizationFunction) throws CheckException {

		Tuple newTuple = null;

		try {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

			engine.put("json", new JSONTuple(tuple).getJSON());
			engine.eval("tuple = eval('(' + json + ')')");

			engine.put("json2", new JSONTuple(virtualTuple).getJSON());
			engine.eval("virtualTuple = eval('(' + json2 + ')')");

			engine.eval(virtualizationFunction);
			engine.eval(new FileReader("json2.js"));
			Object eval = engine.eval("virtualize(tuple, virtualTuple); return virtualTuple");
		} catch (Exception ex) {
			throw new CheckException(ex.getMessage());
		}

		return newTuple;
	}

}
