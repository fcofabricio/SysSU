package br.ufc.great.syssu.front;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintStream;

import javax.script.ScriptException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.omg.CORBA.Environment;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener, CloseTabListener {

	private static int scenarioId = 1;
	private static int agentId = 1;

	private JSplitPane splitPane;
	private JTabbedPane tabbedPane;
	private JTextArea textArea;

	public MainFrame() {
		initialize();
	}

	public void appendText(String text) {

	}

	private void initialize() {
		setTitle("SysSU Front");
		setBounds(100, 100, 800, 640);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createMenus();
		configPane();
	}

	private void configPane() {
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		JPanel upPanel = new JPanel();
		upPanel.setLayout(new BorderLayout());
		splitPane.setLeftComponent(upPanel);

		JToolBar toolBar = createTollbar(this);
		upPanel.add(toolBar, BorderLayout.PAGE_START);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		upPanel.add(tabbedPane, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		splitPane.setRightComponent(bottomPanel);

		JLabel lblOutput = new JLabel("Output:");
		lblOutput.setHorizontalAlignment(SwingConstants.LEFT);
		bottomPanel.add(lblOutput, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		bottomPanel.add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		System.setOut(new PrintStream(new TextAreaOutputStream(textArea), true));
		scrollPane.setViewportView(textArea);
	}

	private JToolBar createTollbar(ActionListener actionListener) {
		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		toolBar.setAlignmentY(Component.CENTER_ALIGNMENT);

		JButton addScenarioButton = new JButton("");
		addScenarioButton.setToolTipText("New Scenario");
		addScenarioButton.setActionCommand("addScenario");
		addScenarioButton.addActionListener(actionListener);
		addScenarioButton.setIcon(new ImageIcon(MainFrame.class
			.getResource("/br/ufc/great/syssu/front/imgs/page_white_gear.png")));
		toolBar.add(addScenarioButton);

		JButton addAgentButton = new JButton("");
		addAgentButton.setIcon(new ImageIcon(MainFrame.class
			.getResource("/br/ufc/great/syssu/front/imgs/user_gray.png")));
		addAgentButton.setToolTipText("New Agent");
		addAgentButton.setActionCommand("addAgent");
		addAgentButton.addActionListener(actionListener);
		toolBar.add(addAgentButton);

		JButton openButton = new JButton("");
		openButton.setToolTipText("Open script");
		openButton.setActionCommand("open");
		openButton.addActionListener(actionListener);
		openButton.setIcon(new ImageIcon(MainFrame.class
			.getResource("/br/ufc/great/syssu/front/imgs/folder_page.png")));
		toolBar.add(openButton);

		toolBar.add(new JSeparator(SwingConstants.VERTICAL) {
			public Dimension getMaximumSize() {
				return new Dimension(getPreferredSize().width, Integer.MAX_VALUE);
			}
		});

		JButton saveButton = new JButton("");
		saveButton.setIcon(new ImageIcon(MainFrame.class
			.getResource("/br/ufc/great/syssu/front/imgs/disk.png")));
		saveButton.setToolTipText("Save");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(actionListener);
		toolBar.add(saveButton);

		JButton saveAllButton = new JButton("");
		saveAllButton.setIcon(new ImageIcon(MainFrame.class
			.getResource("/br/ufc/great/syssu/front/imgs/disk_multiple.png")));
		saveAllButton.setToolTipText("Save all");
		saveAllButton.setActionCommand("saveAll");
		saveAllButton.addActionListener(actionListener);
		toolBar.add(saveAllButton);

		toolBar.add(new JSeparator(SwingConstants.VERTICAL) {
			public Dimension getMaximumSize() {
				return new Dimension(getPreferredSize().width, Integer.MAX_VALUE);
			}
		});

		JButton runButton = new JButton("");
		runButton.setIcon(new ImageIcon(MainFrame.class
			.getResource("/br/ufc/great/syssu/front/imgs/control_play_blue.png")));
		runButton.setToolTipText("Run");
		runButton.setActionCommand("run");
		runButton.addActionListener(actionListener);
		toolBar.add(runButton);

		return toolBar;
	}

	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu(this));
		menuBar.add(createHelpMenu(this));
	}

	private JMenu createFileMenu(ActionListener actionListener) {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');

		JMenu newMenuItem = new JMenu("New script");
		newMenuItem.setIcon(new ImageIcon(MainFrame.class.getResource("imgs/document-attribute-s.png")));
		newMenuItem.addActionListener(actionListener);
		fileMenu.add(newMenuItem);

		JMenuItem newAgentMenuItem = new JMenuItem("Agent");
		newAgentMenuItem.setIcon(new ImageIcon(MainFrame.class.getResource("imgs/user_gray.png")));
		newAgentMenuItem.addActionListener(actionListener);
		newAgentMenuItem.setActionCommand("addAgent");
		newMenuItem.add(newAgentMenuItem);

		JMenuItem newScenarioMenuItem = new JMenuItem("Scenario");
		newScenarioMenuItem.setIcon(new ImageIcon(MainFrame.class.getResource("imgs/page_white_gear.png")));
		newScenarioMenuItem.addActionListener(actionListener);
		newScenarioMenuItem.setActionCommand("addScenario");
		newMenuItem.add(newScenarioMenuItem);

		JMenuItem openScript = new JMenuItem("Open script...");
		openScript.setIcon(new ImageIcon(MainFrame.class.getResource("imgs/folder_page.png")));
		openScript.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, 
			Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		openScript.addActionListener(actionListener);
		openScript.setActionCommand("open");
		fileMenu.add(openScript);

		fileMenu.add(new JSeparator());

		JMenuItem saveScript = new JMenuItem("Save");
		saveScript.setIcon(new ImageIcon(MainFrame.class.getResource("imgs/disk.png")));
		saveScript.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
			Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		saveScript.addActionListener(actionListener);
		saveScript.setActionCommand("save");
		fileMenu.add(saveScript);

		JMenuItem saveAsScript = new JMenuItem("Save as...");
		saveAsScript.setIcon(new ImageIcon(MainFrame.class.getResource("imgs/disk.png")));
		saveAsScript.addActionListener(actionListener);
		saveAsScript.setActionCommand("saveAs");
		fileMenu.add(saveAsScript);

		JMenuItem saveAllScript = new JMenuItem("Save all");
		saveAllScript.setIcon(new ImageIcon(MainFrame.class.getResource("imgs/disk_multiple.png")));
		saveAllScript.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
			Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK));
		saveAllScript.addActionListener(actionListener);
		saveAllScript.setActionCommand("saveAll");
		fileMenu.add(saveAllScript);

		fileMenu.add(new JSeparator());

		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.setIcon(new ImageIcon(MainFrame.class.getResource("imgs/door_in.png")));
		quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 
			Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		quitMenuItem.addActionListener(actionListener);
		quitMenuItem.setActionCommand("quit");
		fileMenu.add(quitMenuItem);

		return fileMenu;
	}

	private JMenu createHelpMenu(ActionListener actionListener) {
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('a');

		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(actionListener);
		helpMenu.add(aboutMenuItem);

		return helpMenu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addScenario")) {
			newScenario();
		} else if (e.getActionCommand().equals("addAgent")) {
			newAgent();
		} else if (e.getActionCommand().equals("open")) {
			open();
		} else if (e.getActionCommand().equals("save")) {
			save();
		} else if (e.getActionCommand().equals("saveAll")) {
			saveAll();
		} else if (e.getActionCommand().equals("saveAs")) {
			saveAs();
		} else if (e.getActionCommand().equals("run")) {
			runScripts();
		} else if (e.getActionCommand().equals("quit")) {
			System.exit(0);
		}
	}

	@Override
	public void tabClosed(int index) {
		tabbedPane.remove(index);
	}

	private void newScenario() {
		ScriptEditor editor = ScriptEditorFactory.createScriptEditor(ScriptType.SCENARIO);
		tabbedPane.add("Scenario" + scenarioId++, editor);
		ButtonTabComponent buttonTab = editor.getButtonTabComponent(tabbedPane);
		buttonTab.addCloseTabListener(this);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, buttonTab);
	}

	private void newAgent() {
		ScriptEditor editor = ScriptEditorFactory.createScriptEditor(ScriptType.AGENT);
		tabbedPane.add("Agent" + agentId++, editor);
		ButtonTabComponent buttonTab = editor.getButtonTabComponent(tabbedPane);
		buttonTab.addCloseTabListener(this);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, buttonTab);
	}

	private void open() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new ScriptFilter("SysSU script files", new String[] { ".agt", ".scn" }));
		int result = fc.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			ScriptEditor editor = ScriptEditorFactory.createScriptEditor(file);
			tabbedPane.add(file.getName(), editor);
			ButtonTabComponent buttonTab = editor.getButtonTabComponent(tabbedPane);
			buttonTab.addCloseTabListener(this);
			tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, buttonTab);
		}
	}

	private void save() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp != null && comp instanceof ScriptEditor) {
			ScriptEditor editor = (ScriptEditor) comp;
			if (editor.getScriptFile() == null) {
				saveAs();
			} else {
				editor.save();
			}
		}
	}

	private void saveAll() {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			Component comp = tabbedPane.getComponentAt(i);
			if (comp != null && comp instanceof ScriptEditor) {
				ScriptEditor editor = (ScriptEditor) comp;
				editor.save();
			}
		}
	}

	private void saveAs() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp != null && comp instanceof ScriptEditor) {
			ScriptEditor editor = (ScriptEditor) comp;
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setFileFilter(new ScriptFilter(editor.getFileDescription(), new String[] { editor
				.getFileExtension() }));
			int result = fc.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				file = editor.saveAs(file);
				tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), file.getName());
			}
		}
	}

	private void runScripts() {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			Component comp = tabbedPane.getComponentAt(i);
			if (comp != null && comp instanceof ScriptEditor) {
				ScriptEditor editor = (ScriptEditor) comp;
				try {
					editor.run();
				} catch (ScriptException e) {
					textArea.append("\n");
					textArea.append(e.getMessage());
					textArea.append("\n");
				}
			}
		}
	}

}
