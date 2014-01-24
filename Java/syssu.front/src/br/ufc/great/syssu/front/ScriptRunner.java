package br.ufc.great.syssu.front;

import java.io.FileReader;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import br.ufc.great.syssu.ubibroker.UbiBroker;

public class ScriptRunner {

	private static int reactionPort = 9099;

	public static void runStript(String script) throws ScriptException{
		try {
			UbiBroker ubibroker = UbiBroker.createUbibroker("localhost", 9090, reactionPort++);
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
			engine.eval(new FileReader(AgentEditor.class.getResource("files/json2.js").getFile()));
			
			engine.eval(script);
			Invocable invokeEngine = (Invocable) engine;
			invokeEngine.invokeFunction("start", ubibroker);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
