package br.ufc.great.syssu.front;

import java.io.File;

public class ScriptEditorFactory {
	
	public static ScriptEditor createScriptEditor(ScriptType type) {
		switch (type) {
			case AGENT :
				return new AgentEditor();
			case SCENARIO :
				return new ScenarioEditor();
			default :
				return null;
		}
	}

	public static ScriptEditor createScriptEditor(File file) {
		if (file.getName().endsWith(AgentEditor.EXTENSION)) {
			return new AgentEditor(file);
		} else if (file.getName().endsWith(ScenarioEditor.EXTENSION)) {
			return new ScenarioEditor(file);
		} else {
			return null;
		}
	}
	
}
