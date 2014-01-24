package br.ufc.great.syssu.coordubi.test;

import static org.junit.Assert.*;

import org.junit.Test;

import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.interfaces.TupleSpaceException;
import br.ufc.great.syssu.coordubi.TupleSpace;

public class TupleSpaceTest {

	@Test
	public void testGetInstance() {
		assertNotNull(TupleSpace.getInstance());
	}

	@Test
	public void testGetDomain() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			assertNotNull(domain);
			assertEquals("DOMAIN", domain.getName());
		} catch (TupleSpaceException e) {
			fail("getDomain not accept valid name.");
		}
		
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			assertNotNull(domain);
			assertEquals("SUB", domain.getName());
		} catch (TupleSpaceException e) {
			fail("getDomain not accept valid name.");
		}
		
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.MORESUB");
			assertNotNull(domain);
			assertEquals("MORESUB", domain.getName());
		} catch (TupleSpaceException e) {
			fail("getDomain not accept valid name.");
		}
		
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN2.SUB2.MORESUB2");
			assertNotNull(domain);
			assertEquals("MORESUB2", domain.getName());
		} catch (TupleSpaceException e) {
			fail("getDomain not accept valid name.");
		}
		
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN2.SUB2");
			assertNotNull(domain);
			assertEquals("SUB2", domain.getName());
		} catch (TupleSpaceException e) {
			fail("getDomain not accept valid name.");
		}
		
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN2");
			assertNotNull(domain);
			assertEquals("DOMAIN2", domain.getName());
		} catch (TupleSpaceException e) {
			fail("getDomain not accept valid name.");
		}		
	}
}
