package br.ufc.great.syssu.front;

import java.io.File;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class ScenarioEditor extends ScriptEditor {

	public static final String EXTENSION = ".scn";

	public ScenarioEditor() {
		super();
	}

	public ScenarioEditor(File scriptFile) {
		super(scriptFile);
	}

	@Override
	public String getFileExtension() {
		return EXTENSION;
	}
	
	@Override
	public String getFileDescription() {
		return "Scenario script";
	}

	@Override
	public ButtonTabComponent getButtonTabComponent(JTabbedPane tabbedPane) {
		return ButtonTabComponent.createButtonTabComponent(tabbedPane, ButtonTabComponent.SCENARIO);
	}

	@Override
	public String getScriptTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

}
