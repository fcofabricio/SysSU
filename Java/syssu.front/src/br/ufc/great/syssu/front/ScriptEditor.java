package br.ufc.great.syssu.front;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import javax.script.ScriptException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchEngine;

@SuppressWarnings("serial")
public abstract class ScriptEditor extends JPanel implements ActionListener {

	private JTextField searchField;
	private JCheckBox regexCB;
	private JCheckBox matchCaseCB;
	private RSyntaxTextArea textArea;
	private JToolBar findToolBar;

	private File scriptFile;

	private boolean modified;

	public ScriptEditor() {
		setLayout(new BorderLayout());

		textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		RTextScrollPane sp = new RTextScrollPane(textArea);
		add(sp);

		AutoCompletion ac = new AutoCompletion(createCompletionProvider());
		ac.install(textArea);

		createFindToolBox();
		add(findToolBar, BorderLayout.SOUTH);

		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
					findToolBar.setVisible(true);
					searchField.requestFocus();
				}
				else if (Character.isDefined(e.getKeyChar())) {
					modified = true;
					getButtonTabComponent(null).setModified(modified);
				}
			}
		});
		textArea.setText(getScriptTemplate());
		textArea.requestFocus();
	}

	public abstract String getFileExtension();

	public abstract String getFileDescription();
	
	public abstract String getScriptTemplate();

	public abstract ButtonTabComponent getButtonTabComponent(JTabbedPane tabbedPane);

	public ScriptEditor(File scriptFile) {
		this();
		load(scriptFile);
	}
	
	File getScriptFile() {
		return scriptFile;
	}
	
	boolean load(File scriptFile) {
		try {
			if (scriptFile.exists()) {
				this.scriptFile = scriptFile;

				StringBuffer buffer = new StringBuffer();
				Scanner scanner = new Scanner(scriptFile);
				while (scanner.hasNextLine())
					buffer.append(scanner.nextLine() + (scanner.hasNextLine() ? "\n" : ""));

				textArea.setText(buffer.toString());
				modified = false;

				return true;
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "File not found.", JOptionPane.ERROR_MESSAGE);
		}

		return false;
	}

	File save() {
		try {
			OutputStream out = new FileOutputStream(scriptFile);
			out.write(textArea.getText().getBytes());
			out.close();
			modified = false;
			getButtonTabComponent(null).setModified(modified);
			return scriptFile;
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(this,
				"File '{0}'not found.".replace("{0}", scriptFile.getAbsolutePath()), "Error",
				JOptionPane.ERROR_MESSAGE);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this,
				"File '{0}' not saved".replace("{0}", scriptFile.getAbsolutePath()), "Error",
				JOptionPane.ERROR_MESSAGE);
		}

		return null;
	}

	File saveAs(File scriptFile) {
		this.scriptFile = scriptFile;

		if (!this.scriptFile.getName().endsWith(getFileExtension()))
			this.scriptFile = new File(this.scriptFile.getAbsolutePath() + getFileExtension());

		return save();
	}
	
	void run() throws ScriptException {
		ScriptRunner.runStript(textArea.getText());
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if ("FindNext".equals(command)) {
			String text = searchField.getText();
			if (text.length() == 0) {
				return;
			}
			boolean forward = true;
			boolean matchCase = matchCaseCB.isSelected();
			boolean wholeWord = false;
			boolean regex = regexCB.isSelected();
			boolean found = SearchEngine.find(textArea, text, forward, matchCase, wholeWord, regex);
			if (!found) {
				JOptionPane.showMessageDialog(this, "Text not found");
			}
		}

		else if ("FindPrev".equals(command)) {
			String text = searchField.getText();
			if (text.length() == 0) {
				return;
			}
			boolean forward = false;
			boolean matchCase = matchCaseCB.isSelected();
			boolean wholeWord = false;
			boolean regex = regexCB.isSelected();
			boolean found = SearchEngine.find(textArea, text, forward, matchCase, wholeWord, regex);
			if (!found) {
				JOptionPane.showMessageDialog(this, "Text not found");
			}
		}
	}

	private void createFindToolBox() {
		findToolBar = new JToolBar();
		searchField = new JTextField(30);
		findToolBar.add(searchField);
		findToolBar.setVisible(false);
		JButton b = new JButton("Next");
		b.setActionCommand("FindNext");
		b.addActionListener(this);
		findToolBar.add(b);
		b = new JButton("Previous");
		b.setActionCommand("FindPrev");
		b.addActionListener(this);
		findToolBar.add(b);
		regexCB = new JCheckBox("Regex");
		findToolBar.add(regexCB);
		matchCaseCB = new JCheckBox("Match Case");
		findToolBar.add(matchCaseCB);
		JButton closeButton = new JButton(new ImageIcon(
			ButtonTabComponent.class.getResource("imgs/cross.png")));
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				findToolBar.setVisible(false);
				textArea.requestFocus();
			}
		});
		findToolBar.add(closeButton);
	}

	private CompletionProvider createCompletionProvider() {
		DefaultCompletionProvider provider = new DefaultCompletionProvider();

		provider.addCompletion(new BasicCompletion(provider, "abstract"));
		provider.addCompletion(new BasicCompletion(provider, "boolean"));
		provider.addCompletion(new BasicCompletion(provider, "break"));
		provider.addCompletion(new BasicCompletion(provider, "byte"));
		provider.addCompletion(new BasicCompletion(provider, "case"));
		provider.addCompletion(new BasicCompletion(provider, "catch"));
		provider.addCompletion(new BasicCompletion(provider, "char"));
		provider.addCompletion(new BasicCompletion(provider, "class"));
		provider.addCompletion(new BasicCompletion(provider, "const"));
		provider.addCompletion(new BasicCompletion(provider, "continue"));
		provider.addCompletion(new BasicCompletion(provider, "debugger"));
		provider.addCompletion(new BasicCompletion(provider, "delete"));
		provider.addCompletion(new BasicCompletion(provider, "do"));
		provider.addCompletion(new BasicCompletion(provider, "double"));
		provider.addCompletion(new BasicCompletion(provider, "else"));
		provider.addCompletion(new BasicCompletion(provider, "enum"));
		provider.addCompletion(new BasicCompletion(provider, "export"));
		provider.addCompletion(new BasicCompletion(provider, "extends"));
		provider.addCompletion(new BasicCompletion(provider, "false"));
		provider.addCompletion(new BasicCompletion(provider, "final"));
		provider.addCompletion(new BasicCompletion(provider, "finally"));
		provider.addCompletion(new BasicCompletion(provider, "float"));
		provider.addCompletion(new BasicCompletion(provider, "for"));
		provider.addCompletion(new BasicCompletion(provider, "function"));
		provider.addCompletion(new BasicCompletion(provider, "goto"));
		provider.addCompletion(new BasicCompletion(provider, "if"));
		provider.addCompletion(new BasicCompletion(provider, "implements"));
		provider.addCompletion(new BasicCompletion(provider, "import"));
		provider.addCompletion(new BasicCompletion(provider, "in"));
		provider.addCompletion(new BasicCompletion(provider, "instanceof"));
		provider.addCompletion(new BasicCompletion(provider, "int"));
		provider.addCompletion(new BasicCompletion(provider, "interface"));
		provider.addCompletion(new BasicCompletion(provider, "long"));
		provider.addCompletion(new BasicCompletion(provider, "native"));
		provider.addCompletion(new BasicCompletion(provider, "new"));
		provider.addCompletion(new BasicCompletion(provider, "null"));
		provider.addCompletion(new BasicCompletion(provider, "package"));
		provider.addCompletion(new BasicCompletion(provider, "private"));
		provider.addCompletion(new BasicCompletion(provider, "protected"));
		provider.addCompletion(new BasicCompletion(provider, "public"));
		provider.addCompletion(new BasicCompletion(provider, "return"));
		provider.addCompletion(new BasicCompletion(provider, "short"));
		provider.addCompletion(new BasicCompletion(provider, "static"));
		provider.addCompletion(new BasicCompletion(provider, "super"));
		provider.addCompletion(new BasicCompletion(provider, "switch"));
		provider.addCompletion(new BasicCompletion(provider, "synchronized"));
		provider.addCompletion(new BasicCompletion(provider, "throw"));
		provider.addCompletion(new BasicCompletion(provider, "throws"));
		provider.addCompletion(new BasicCompletion(provider, "transient"));
		provider.addCompletion(new BasicCompletion(provider, "true"));
		provider.addCompletion(new BasicCompletion(provider, "try"));
		provider.addCompletion(new BasicCompletion(provider, "typeof"));
		provider.addCompletion(new BasicCompletion(provider, "var"));
		provider.addCompletion(new BasicCompletion(provider, "void"));
		provider.addCompletion(new BasicCompletion(provider, "volatile"));
		provider.addCompletion(new BasicCompletion(provider, "while"));
		provider.addCompletion(new BasicCompletion(provider, "with"));

		// provider.addCompletion(new ShorthandCompletion(provider, "sysout",
		// "System.out.println(", "System.out.println("));
		// provider.addCompletion(new ShorthandCompletion(provider, "syserr",
		// "System.err.println(", "System.err.println("));

		return provider;
	}

}
