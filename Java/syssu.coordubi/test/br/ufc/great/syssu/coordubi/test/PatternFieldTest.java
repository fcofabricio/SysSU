package br.ufc.great.syssu.coordubi.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import br.ufc.great.syssu.base.PatternField;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.coordubi.*;

public class PatternFieldTest {

	@Test
	public void testGetName() {
		PatternField pattern = new PatternField("field", true);
		assertEquals("field", pattern.getName());
		
		pattern = new PatternField("?boolean", true);
		assertEquals("?boolean", pattern.getName());
		
		pattern = new PatternField("?interger", true);
		assertEquals("?interger", pattern.getName());
		
		pattern = new PatternField("?float", true);
		assertEquals("?float", pattern.getName());
		
		pattern = new PatternField("?array", true);
		assertEquals("?array", pattern.getName());
		
		pattern = new PatternField("?object", true);
		assertEquals("?object", pattern.getName());
		
		pattern = new PatternField("?", true);
		assertEquals("?", pattern.getName());
		
		try {
			pattern = new PatternField("fie.ld", new Thread());
			fail("Accept invalid name.");
		} catch (Exception ex) {
			// Ok.
		}
		
		try {
			pattern = new PatternField("", new Thread());
			fail("Accept invalid name.");
		} catch (Exception ex) {
			// Ok.
		}
		
		try {
			pattern = new PatternField(null, new Thread());
			fail("Accept invalid name.");
		} catch (Exception ex) {
			// Ok.
		}
	}

	@Test
	public void testGetValue() {
		PatternField pattern = new PatternField("field", true);
		assertEquals(true, pattern.getValue());

		pattern = new PatternField("field", 1);
		assertEquals(1, pattern.getValue());

		pattern = new PatternField("field", 1.1);
		assertEquals(1.1, pattern.getValue());

		pattern = new PatternField("field", "str");
		assertEquals("str", pattern.getValue());

		List<String> list = new ArrayList<String>();
		pattern = new PatternField("field", list);
		assertEquals(list, pattern.getValue());

		Tuple tuple = new Tuple();
		pattern = new PatternField("field", tuple);
		assertEquals(tuple, pattern.getValue());

		try {
			pattern = new PatternField("field", new Thread());
			fail("Accept invalid type.");
		} catch (Exception ex) {
			// Ok.
		}

		try {
			pattern = new PatternField("field", null);
			fail("Accept invalid type.");
		} catch (Exception ex) {
			// Ok.
		}
	}

	@Test
	public void testGetType() {
		PatternField pattern = new PatternField("field", true);
		assertEquals("?boolean", pattern.getType());

		pattern = new PatternField("field", 1);
		assertEquals("?integer", pattern.getType());

		pattern = new PatternField("field", 1.1);
		assertEquals("?float", pattern.getType());

		pattern = new PatternField("field", "str");
		assertEquals("?string", pattern.getType());

		List<String> list = new ArrayList<String>();
		pattern = new PatternField("field", list);
		assertEquals("?array", pattern.getType());

		Tuple tuple = new Tuple();
		pattern = new PatternField("field", tuple);
		assertEquals("?object", pattern.getType());
		
		try {
			pattern = new PatternField("field", new HashSet<String>());
			fail("Accept invalid type.");
		} catch (Exception ex) {
			// Ok.
		}	
		
		try {
			pattern = new PatternField("field", null);
			fail("Accept invalid type.");
		} catch (Exception ex) {
			// Ok.
		}
	}

	@Test
	public void testHasWildCardValue() {
		PatternField pattern = new PatternField("field", true);
		assertFalse(pattern.hasWildCardValue());

		pattern = new PatternField("field", 1);
		assertFalse(pattern.hasWildCardValue());

		pattern = new PatternField("field", 1.1);
		assertFalse(pattern.hasWildCardValue());

		pattern = new PatternField("field", "str");
		assertFalse(pattern.hasWildCardValue());

		List<String> list = new ArrayList<String>();
		pattern = new PatternField("field", list);
		assertFalse(pattern.hasWildCardValue());

		Tuple tuple = new Tuple();
		pattern = new PatternField("field", tuple);
		assertFalse(pattern.hasWildCardValue());
		
		pattern = new PatternField("field", "?boolean");
		assertTrue(pattern.hasWildCardValue());
		
		pattern = new PatternField("field", "?integer");
		assertTrue(pattern.hasWildCardValue());
		
		pattern = new PatternField("field", "?float");
		assertTrue(pattern.hasWildCardValue());
		
		pattern = new PatternField("field", "?array");
		assertTrue(pattern.hasWildCardValue());
		
		pattern = new PatternField("field", "?object");
		assertTrue(pattern.hasWildCardValue());
	}

}
