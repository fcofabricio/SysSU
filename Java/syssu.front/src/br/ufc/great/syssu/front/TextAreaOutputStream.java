package br.ufc.great.syssu.front;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TextAreaOutputStream extends OutputStream {
	
	private JTextArea textArea;

	TextAreaOutputStream(JTextArea textArea) {
		super();
		this.textArea = textArea;
	}

	@Override
	public void write(int b) throws IOException {
		updateTextArea(String.valueOf((char) b));
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		updateTextArea(new String(b, off, len));
	}

	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(text);
			}
		});
	}

}
