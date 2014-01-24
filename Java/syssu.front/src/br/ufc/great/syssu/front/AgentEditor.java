package br.ufc.great.syssu.front;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class AgentEditor extends ScriptEditor {

	public static final String EXTENSION = ".agt";

	private ButtonTabComponent buttonTabComponentInstance;

	public AgentEditor() {
		super();
	}

	public AgentEditor(File scriptFile) {
		super(scriptFile);
	}

	@Override
	public String getFileExtension() {
		return EXTENSION;
	}

	@Override
	public String getFileDescription() {
		return "Agent script";
	}

	@Override
	public ButtonTabComponent getButtonTabComponent(JTabbedPane tabbedPane) {
		if (buttonTabComponentInstance == null) {
			buttonTabComponentInstance = ButtonTabComponent.createButtonTabComponent(tabbedPane,
				ButtonTabComponent.AGENT);
		}
		return buttonTabComponentInstance;
	}

	@Override
	public String getScriptTemplate() {
		try {
			StringBuffer contents = new StringBuffer();
			BufferedReader reader = new BufferedReader(new FileReader(AgentEditor.class.getResource(
				"files/agentTemplate.js").getFile()));

			String text = null;
			while ((text = reader.readLine()) != null) {
				contents.append(text).append(System.getProperty("line.separator"));
			}

			return contents.toString();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

		return null;
	}
}
