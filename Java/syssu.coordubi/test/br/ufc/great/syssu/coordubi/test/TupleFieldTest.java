package br.ufc.great.syssu.coordubi.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import br.ufc.great.syssu.base.PatternField;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleField;
import br.ufc.great.syssu.coordubi.*;

public class TupleFieldTest {

	@Test
	public void testGetName() {
		TupleField field = new TupleField("field", true);
		assertEquals("field", field.getName());
		
		field = new TupleField("?", true);
		assertEquals("?", field.getName());
		
		field = new TupleField("?boolean", true);
		assertEquals("?boolean", field.getName());
		
		field = new TupleField("?integer", true);
		assertEquals("?integer", field.getName());
		
		field = new TupleField("?float", true);
		assertEquals("?float", field.getName());
		
		field = new TupleField("?array", true);
		assertEquals("?array", field.getName());
		
		field = new TupleField("?object", true);
		assertEquals("?object", field.getName());
		
		try {
			field = new TupleField("fie.ld", new Thread());
			fail("Accept invalid name.");
		} catch (Exception ex) {
			// Ok.
		}
		
		try {
			field = new TupleField("", new Thread());
			fail("Accept invalid name.");
		} catch (Exception ex) {
			// Ok.
		}
		
		try {
			field = new TupleField(null, new Thread());
			fail("Accept invalid name.");
		} catch (Exception ex) {
			// Ok.
		}
	}

	@Test
	public void testGetValue() {
		TupleField field = new TupleField("field", true);
		assertEquals(true, field.getValue());

		field = new TupleField("field", 1);
		assertEquals(1, field.getValue());

		field = new TupleField("field", 1.1);
		assertEquals(1.1, field.getValue());

		field = new TupleField("field", "str");
		assertEquals("str", field.getValue());

		List<String> list = new ArrayList<String>();
		field = new TupleField("field", list);
		assertEquals(list, field.getValue());

		Tuple tuple = new Tuple();
		field = new TupleField("field", tuple);
		assertEquals(tuple, field.getValue());
		
		field = new TupleField("field", "?");
		assertEquals("?", field.getValue());
		
		field = new TupleField("field", "?boolean");
		assertEquals("?boolean", field.getValue());
		
		field = new TupleField("field", "?integer");
		assertEquals("?integer", field.getValue());
		
		field = new TupleField("field", "?float");
		assertEquals("?float", field.getValue());
		
		field = new TupleField("field", "?array");
		assertEquals("?array", field.getValue());
		
		field = new TupleField("field", "?object");
		assertEquals("?object", field.getValue());

		try {
			field = new TupleField("field", new Thread());
			fail("Accept invalid type.");
		} catch (Exception ex) {
			// Ok.
		}		
	}

	@Test
	public void testGetType() {
		TupleField field = new TupleField("field", true);
		assertEquals("?boolean", field.getType());

		field = new TupleField("field", 1);
		assertEquals("?integer", field.getType());

		field = new TupleField("field", 1.1);
		assertEquals("?float", field.getType());

		field = new TupleField("field", "str");
		assertEquals("?string", field.getType());

		List<String> list = new ArrayList<String>();
		field = new TupleField("field", list);
		assertEquals("?array", field.getType());

		Tuple tuple = new Tuple();
		field = new TupleField("field", tuple);
		assertEquals("?object", field.getType());
		
		try {
			field = new TupleField("field", new HashSet<String>());
			fail("Accept invalid type.");
		} catch (Exception ex) {
			// Ok.
		}	
	}

	@Test
	public void testMatches() {
		TupleField field =null;
		PatternField pattern = null;		
		
		// Boolean field:
		field = new TupleField("boolean field", true);
		
		// matches:
		pattern = new PatternField("boolean field", true);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("boolean field", "?boolean");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("boolean field", "?");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", true);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?boolean");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?");		
		assertTrue(field.associates(pattern));
		
		// not matches:
		pattern = new PatternField("boolean field", 123);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("boolean field", "?array");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("boolean field", "?aass");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?", 78.56);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?", "?string");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?boolean", "?");		
		assertFalse(field.associates(pattern));
		
		// Integer field:
		field = new TupleField("integer field", 12345);
		
		// matches:
		pattern = new PatternField("integer field", 12345);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("integer field", "?integer");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("integer field", "?");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", 12345);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?integer");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?");		
		assertTrue(field.associates(pattern));
		
		// not matches:
		pattern = new PatternField("integer field", false);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("integer field", "?float");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("integer field", "?qwert");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?", 123.45);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?", "?string");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?integer", "?");		
		assertFalse(field.associates(pattern));
		
		// Float field:
		field = new TupleField("float field", 12.345);
		
		// matches:
		pattern = new PatternField("float field", 12.345);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("float field", "?float");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("float field", "?");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", 12.345);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?float");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?");		
		assertTrue(field.associates(pattern));
		
		// not matches:
		pattern = new PatternField("float field", 12345);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("float field", "?integer");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("float field", "?qwert");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?", 12345);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?", "?integer");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?float", "?");		
		assertFalse(field.associates(pattern));
		
		// String field:
		field = new TupleField("string field", "value");
		
		// matches:
		pattern = new PatternField("string field", "value");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("string field", "?string");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("string field", "?");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "value");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?string");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?");		
		assertTrue(field.associates(pattern));
		
		// not matches:
		pattern = new PatternField("string field", 123);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("string field", "?array");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("string field", "?asdfg");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?", 12345);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?", "?character");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?string", "?");		
		assertFalse(field.associates(pattern));
		
		// Array field:
		List<Integer> list = new ArrayList<Integer>();
		list.add(1); list.add(2); list.add(3);
		field = new TupleField("array field", list);
		
		// matches:
		pattern = new PatternField("array field", list);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("array field", "?array");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("array field", "?");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", list);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?array");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?");		
		assertTrue(field.associates(pattern));
		
		// not matches:
		pattern = new PatternField("array field", "list");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("array field", "?object");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("array field", "?list");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?list", list);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?", "?object");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?list", "?");		
		assertFalse(field.associates(pattern));
		
		// Object field:
		Tuple tuple = new Tuple();
		tuple.addField("field 1 ", "value 1").addField("field 2 ", "value 2").addField("field 3 ", "value 3");
		field = new TupleField("object field", tuple);
		
		// matches:
		pattern = new PatternField("object field", tuple);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("object field", "?object");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("object field", "?");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", tuple);		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?object");		
		assertTrue(field.associates(pattern));
		pattern = new PatternField("?", "?");		
		assertTrue(field.associates(pattern));
		
		// not matches:
		pattern = new PatternField("object field", list);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("object field", "?list");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("object field", "?obj");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?object", tuple);		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?object", "?object");		
		assertFalse(field.associates(pattern));
		pattern = new PatternField("?object", "?");		
		assertFalse(field.associates(pattern));
	}

}
