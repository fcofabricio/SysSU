package br.ufc.great.syssu.front;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class ButtonTabComponent extends JPanel {
	public static final int SCENARIO = 0;
	public static final int AGENT = 1;

	private static Icon iconEnabled;
	private static Icon iconDisabled;
	private static Icon iconScenario;
	private static Icon iconAgent;

	private JTabbedPane tabbedPane;
	private JLabel icon;
	private JLabel label;
	private JLabel button;

	private List<CloseTabListener> listeners;

	static {
		iconEnabled = new ImageIcon(ButtonTabComponent.class.getResource("imgs/tabCloseEnabled.png"));
		iconDisabled = new ImageIcon(ButtonTabComponent.class.getResource("imgs/tabCloseDisabled.png"));
		iconScenario = new ImageIcon(ButtonTabComponent.class.getResource("imgs/page_white_gear.png"));
		iconAgent = new ImageIcon(ButtonTabComponent.class.getResource("imgs/user_gray.png"));
	}

	private ButtonTabComponent(JTabbedPane tabbedPane, int type) {
		this.tabbedPane = tabbedPane;

		this.listeners = new ArrayList<CloseTabListener>();

		this.setLayout(new BorderLayout(5, 0));
		this.setOpaque(false);

		this.initialize(type);
		this.registerListeners();
	}

	public static ButtonTabComponent createButtonTabComponent(JTabbedPane tabbedPane, int type) {
		return new ButtonTabComponent(tabbedPane, type);
	}

	public void setModified(boolean modified) {
		int i = tabbedPane.indexOfTabComponent(ButtonTabComponent.this);
		if (i > -1) {
			String currentTitle = tabbedPane.getTitleAt(i);
			if (modified && !currentTitle.endsWith("*")) {
				tabbedPane.setTitleAt(i, currentTitle + "*");
			} else if (!modified && currentTitle.endsWith("*")) {
				tabbedPane.setTitleAt(i, currentTitle.substring(0, currentTitle.length() - 1));
			}
		}
	}

	public void addCloseTabListener(CloseTabListener listener) {
		listeners.add(listener);
	}

	public void removeCloseTabListener(CloseTabListener listener) {
		listeners.remove(listener);
	}

	private void initialize(int type) {
		icon = (type == SCENARIO) ? new JLabel(iconScenario) : new JLabel(iconAgent);

		label = new JLabel() {
			public String getText() {
				int i = tabbedPane.indexOfTabComponent(ButtonTabComponent.this);
				if (i != -1) {
					if (label != null && button != null) {
						int iconWidth = icon.getIcon().getIconWidth();
						int labelWidth = label.getFontMetrics(label.getFont()).stringWidth(
							tabbedPane.getTitleAt(i));
						int buttonWidth = button.getIcon().getIconWidth();
						int totalWidth = iconWidth + 5 + labelWidth + 5 + buttonWidth;

						if (ButtonTabComponent.this.getWidth() != totalWidth) {
							ButtonTabComponent.this.setSize(totalWidth, this.getHeight());
							ButtonTabComponent.this.repaint();
						}
					}

					return tabbedPane.getTitleAt(i);
				}
				return null;
			}
		};

		button = new JLabel(iconDisabled);

		this.add(icon, BorderLayout.WEST);
		this.add(label, BorderLayout.CENTER);
		this.add(button, BorderLayout.EAST);
	}

	private void registerListeners() {
		button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int index = tabbedPane.indexOfTabComponent(ButtonTabComponent.this);
				if (index != -1) {
					for (CloseTabListener listener : listeners) {
						listener.tabClosed(index);
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
				button.setIcon(iconEnabled);
			}

			public void mouseExited(MouseEvent e) {
				button.setIcon(iconDisabled);
			}
		});
	}
}
