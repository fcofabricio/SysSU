package br.ufc.great.syssu.cat;

import br.ufc.great.syssu.cat.utils.JSONTuple;
import br.ufc.great.syssu.coordubi.Tuple;

import java.io.*;
import javax.script.*;

public class TupleRestrictioner {

    public static boolean restrict(Tuple tuple, String restriction) throws CheckException {
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

            engine.put("json", new JSONTuple(tuple).getJSON());
            engine.eval("tuple = eval('(' + json + ')')");

            engine.eval(restriction);
            engine.eval(new FileReader("json2.js"));
            Object eval = engine.eval("restrict(tuple);");

            return Boolean.valueOf(eval.toString());
        } catch (Exception ex) {
            throw new CheckException(ex.getMessage());
        }
    }   
}
