package br.ufc.great.syssu.coordubi.test;

import static org.junit.Assert.*;
import org.junit.Test;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.PatternField;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleField;
import br.ufc.great.syssu.coordubi.*;

public class TupleTest {

	@Test
	public void testMatches() {
		Tuple tuple = new Tuple();
		Pattern pattern = new Pattern();
		TupleField tupleField = new TupleField("field 2", true);
		PatternField patternField = new PatternField("field 2", true);

		assertTrue(tuple.matches(null));
		assertTrue(tuple.associates(pattern));
		
		tuple.addField("field 1", true);
		assertEquals(1, tuple.size());
		assertTrue(tuple.matches(null));
		assertTrue(tuple.associates(pattern));
		
		tuple.addField(tupleField);
		assertEquals(2, tuple.size());
		assertTrue(tuple.matches(null));
		assertTrue(tuple.associates(pattern));
		
		tuple.addField("field 3", true);
		assertEquals(3, tuple.size());
		assertTrue(tuple.matches(null));	
		assertTrue(tuple.associates(pattern));
		
		pattern.addField("field 1", true);
		assertEquals(1, pattern.size());
		assertTrue(tuple.associates(pattern));
		
		pattern.addField(patternField);
		assertEquals(2, pattern.size());
		assertTrue(tuple.associates(pattern));
		
		pattern.addField("field 3", true);
		assertEquals(3, pattern.size());
		assertTrue(tuple.associates(pattern));
		
		tuple.removeField(2);
		assertEquals(2, tuple.size());
		assertFalse(tuple.associates(pattern));
		
		tuple.removeField(tupleField);
		assertEquals(1, tuple.size());
		assertFalse(tuple.associates(pattern));
		
		tuple.removeField(tuple.getField(0));
		assertEquals(0, tuple.size());
		assertTrue(tuple.associates(pattern));
		
		pattern.removeField(pattern.getField(2));
		assertEquals(2, pattern.size());
		assertTrue(tuple.associates(pattern));
		
		pattern.removeField(patternField);
		assertEquals(1, pattern.size());
		assertTrue(tuple.associates(pattern));
		
		pattern.removeField(0);
		assertEquals(0, pattern.size());
		assertTrue(tuple.associates(pattern));
	}

}
