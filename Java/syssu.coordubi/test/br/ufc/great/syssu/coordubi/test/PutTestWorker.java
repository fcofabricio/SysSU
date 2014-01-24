package br.ufc.great.syssu.coordubi.test;

import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.coordubi.*;

public class PutTestWorker implements Runnable {
	
	private IDomain domain;	
	private Tuple tuple;

	public PutTestWorker(IDomain domain) {
		this.domain = domain;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			domain.put(tuple);
			tuple = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void start(Tuple tuple) {
		this.tuple = tuple;
		new Thread(this).start();
	}
}
