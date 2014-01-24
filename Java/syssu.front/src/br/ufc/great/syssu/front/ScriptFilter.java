package br.ufc.great.syssu.front;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.filechooser.FileFilter;

public class ScriptFilter extends FileFilter {

	private String description;
	private Set<String> filters;
	
	public ScriptFilter(String description, String[] filters) {
		super();
		this.filters = new HashSet<String>();
		for (int i = 0; i < filters.length; i++) {
			this.filters.add(filters[i].toLowerCase());
		}
		this.description = description;
	}

	@Override
	public boolean accept(File f) {
		if(f != null && f.isFile()) {
			String extension = getExtension(f);
			return filters.contains(extension) || filters.contains("." + extension);
		}
		return false;
	}

	@Override
	public String getDescription() {
		StringBuffer buffer = new StringBuffer();
		
		for (String filter: filters) {
			buffer.append('*');
			
			if (!filter.startsWith("."))
				buffer.append('.');
			
			buffer.append(filter + "; ");
		}
		
		if (!filters.isEmpty()) {
			buffer.delete(buffer.length() - 2, buffer.length());
		} else {
			buffer.setLength(0);
		}
		
		return description + " (" + buffer.toString() + ")";
	}
	
	private String getExtension(File file) {
		if (file != null) {
			String filename = file.getName();
			int i = filename.lastIndexOf('.');
			if (i >= 0 && i < filename.length() - 1)
				return filename.substring(i + 1).toLowerCase();
		}
		
		return null;
	}
	
}