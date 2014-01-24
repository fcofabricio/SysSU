package br.ufc.great.syssu.coordubi.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.interfaces.TupleSpaceException;
import br.ufc.great.syssu.coordubi.TupleSpace;

public class DomainTest {

	private Tuple tuple01;
	private Tuple tuple02;
	private Tuple tuple03;
	private Pattern pattern;

	public DomainTest() {
		tuple01 = (Tuple) new Tuple()
			.addField("field 1", "value");	
		tuple02 = (Tuple) new Tuple()
			.addField("field 1", "value").addField("field 2", 1234567890);
		tuple03 = (Tuple) new Tuple()
			.addField("field 1", "value").addField("field 2", 1234567890).addField("field 3", 12345.67890);
		
		pattern = (Pattern) new Pattern().addField("field 1", "value");
	}

	@Test
	public void testGetName() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			assertNotNull(domain);
			assertEquals("DOMAIN", domain.getName());
		} catch (TupleSpaceException ex) {
			fail("Domain constructor not accept valid name.");
		}
	}

	@Test
	public void testGetDomain() {
		IDomain domain;
		try {
			domain = TupleSpace.getInstance().getDomain("DOMAIN");
			IDomain sub = domain.getDomain("SUB");
			assertEquals("SUB", sub.getName());
		} catch (TupleSpaceException ex) {
			fail("Domain constructor not accept valid name.");
		}

		try {
			domain = TupleSpace.getInstance().getDomain("DOMAIN");
			domain.getDomain("DOMAIN.SUB");
			fail("Domain constructor accept valid name.");
		} catch (TupleSpaceException ex) {
			// Ok for that.
		}
	}

	@Test
	public void testPutAndRead() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");

			List<Tuple> tuples = domain.read(pattern, "");
			assertTrue(tuples.isEmpty());

			domain.put(tuple01);

			tuples = domain.read(pattern, "");
			assertEquals(1, tuples.size());
			assertTrue(tuples.contains(tuple01));

			domain.put(tuple02);

			tuples = domain.read(pattern, "");
			assertEquals(2, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple02));

			IDomain subDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			tuples = subDomain.read(pattern, "");
			assertTrue(tuples.isEmpty());

			subDomain.put(tuple02);
			subDomain.put(tuple01);

			tuples = subDomain.read(pattern, "");
			assertEquals(2, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple02));

			tuples = domain.read(pattern, "");
			assertEquals(4, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple02));

			IDomain subDomain2 = TupleSpace.getInstance().getDomain("DOMAIN.SUB2");
			tuples = subDomain2.read(pattern, "");
			assertTrue(tuples.isEmpty());

			subDomain2.put(tuple01);
			subDomain2.put(tuple03);

			tuples = subDomain2.read(pattern, "");
			assertEquals(2, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple03));

			tuples = domain.read(pattern, "");
			assertEquals(6, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple02));
			assertTrue(tuples.contains(tuple03));

			IDomain subsubDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB");
			tuples = subsubDomain.read(pattern, "");
			assertTrue(tuples.isEmpty());

			subsubDomain.put(tuple03);
			tuples = subsubDomain.read(pattern, "");
			assertEquals(1, tuples.size());
			assertTrue(tuples.contains(tuple03));

			tuples = subDomain.read(pattern, "");
			assertEquals(3, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple02));
			assertTrue(tuples.contains(tuple03));

			tuples = domain.read(pattern, "");
			assertEquals(7, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple02));
			assertTrue(tuples.contains(tuple03));

		} catch (TupleSpaceException e) {
			fail("Domain constructor not accept valid name.");
		}
	}

	@Test
	public void testReadSync() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			domain.take(pattern, "");

			new PutTestWorker(domain).start(tuple01);

			List<Tuple> tuples = domain.readSync(pattern, "");
			assertEquals(1, tuples.size());
			assertTrue(tuples.contains(tuple01));

			IDomain subDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			IDomain subsubDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB");

			new PutTestWorker(subsubDomain).start(tuple02);

			tuples = subDomain.readSync(pattern, "");
			assertEquals(1, tuples.size());
			assertTrue(tuples.contains(tuple02));
			
			tuples = domain.read(pattern, "");
			assertEquals(2, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple02));
		} catch (TupleSpaceException e) {
			fail("Domain constructor not accept valid name.");
		}
	}

	@Test
	public void testReadOne() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			assertTrue(domain.readOne(pattern, "") instanceof Tuple);

			IDomain subDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			assertTrue(subDomain.readOne(pattern, "") instanceof Tuple);

			IDomain subDomain2 = TupleSpace.getInstance().getDomain("DOMAIN.SUB2");
			assertNull(subDomain2.readOne(pattern, ""));

			IDomain subsubDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB");
			assertTrue(subsubDomain.readOne(pattern, "") instanceof Tuple);

			IDomain subsubDomain2 = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB2");
			assertNull(subsubDomain2.readOne(pattern, ""));
		} catch (TupleSpaceException e) {
			fail("Domain constructor not accept valid name.");
		}
	}

	@Test
	public void testReadOneSync() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			domain.take(pattern, "");

			new PutTestWorker(domain).start(tuple01);

			assertNotNull(domain.readOneSync(pattern, ""));

			IDomain subDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			IDomain subsubDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB");

			new PutTestWorker(subsubDomain).start(tuple02);

			assertNotNull(subDomain.readOneSync(pattern, ""));

		} catch (TupleSpaceException e) {
			fail("Domain constructor not accept valid name.");
		}
	}

	@Test
	public void testTake() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			domain.take(pattern, "");

			List<Tuple> tuples = domain.take(pattern, "");
			assertTrue(tuples.isEmpty());

			domain.put(tuple01);

			tuples = domain.take(pattern, "");
			assertEquals(1, tuples.size());
			assertTrue(tuples.contains(tuple01));

			domain.put(tuple02);

			tuples = domain.take(pattern, "");
			assertEquals(1, tuples.size());
			assertTrue(tuples.contains(tuple02));
			assertFalse(tuples.contains(tuple01));			

			IDomain subDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			tuples = subDomain.take(pattern, "");
			assertTrue(tuples.isEmpty());

			subDomain.put(tuple02);
			subDomain.put(tuple01);

			tuples = subDomain.take(pattern, "");
			assertEquals(2, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple02));

			tuples = domain.take(pattern, "");
			assertEquals(0, tuples.size());

			IDomain subDomain2 = TupleSpace.getInstance().getDomain("DOMAIN.SUB2");
			tuples = subDomain2.take(pattern, "");
			assertTrue(tuples.isEmpty());

			subDomain2.put(tuple01);
			subDomain2.put(tuple03);

			tuples = subDomain2.take(pattern, "");
			assertEquals(2, tuples.size());
			assertTrue(tuples.contains(tuple01));
			assertTrue(tuples.contains(tuple03));

			tuples = domain.take(pattern, "");
			assertEquals(0, tuples.size());

			IDomain subsubDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB");
			tuples = subsubDomain.take(pattern, "");
			assertTrue(tuples.isEmpty());

			subsubDomain.put(tuple03);
			tuples = subsubDomain.take(pattern, "");
			assertEquals(1, tuples.size());
			assertTrue(tuples.contains(tuple03));

			tuples = subDomain.take(pattern, "");
			assertEquals(0, tuples.size());

			tuples = domain.take(pattern, "");
			assertEquals(0, tuples.size());
		} catch (TupleSpaceException e) {
			fail("Domain constructor not accept valid name.");
		}
	}

	@Test
	public void testTakeSync() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			domain.take(pattern, "");

			new PutTestWorker(domain).start(tuple01);

			List<Tuple> tuples = domain.takeSync(pattern, "");

			assertEquals(1, tuples.size());
			assertTrue(tuples.contains(tuple01));

			IDomain subDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			IDomain subsubDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB");

			new PutTestWorker(subsubDomain).start(tuple02);

			tuples = subDomain.takeSync(pattern, "");
			assertEquals(1, tuples.size());
			assertTrue(tuples.contains(tuple02));
			
			tuples = domain.take(pattern, "");
			assertEquals(0, tuples.size());

		} catch (TupleSpaceException e) {
			fail("Domain constructor not accept valid name.");
		}
	}

	@Test
	public void testTakeOne() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			domain.take(pattern, "");
			domain.put(tuple01);
			
			assertTrue(domain.takeOne(pattern, "") instanceof Tuple);
			assertEquals(0, domain.read(pattern, "").size());

			IDomain subDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			subDomain.put(tuple01);
			subDomain.put(tuple02);
			assertTrue(subDomain.takeOne(pattern, "") instanceof Tuple);
			assertEquals(1, subDomain.read(pattern, "").size());
			
			IDomain subDomain2 = TupleSpace.getInstance().getDomain("DOMAIN.SUB2");
			subDomain2.put(tuple01);			
			assertTrue(subDomain2.takeOne(pattern, "") instanceof Tuple);
			assertEquals(0, subDomain2.read(pattern, "").size());	
			
			IDomain subsubDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB");
			subsubDomain.put(tuple01);
			subsubDomain.put(tuple02);
			subsubDomain.put(tuple03);
			assertTrue(subsubDomain.takeOne(pattern, "") instanceof Tuple);
			assertEquals(2, subsubDomain.read(pattern, "").size());

			assertTrue(domain.takeOne(pattern, "") instanceof Tuple);
			assertEquals(2, domain.read(pattern, "").size());
		} catch (TupleSpaceException e) {
			fail("Domain constructor not accept valid name.");
		}
	}

	@Test
	public void testTakeOneSync() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			domain.take(pattern, "");

			new PutTestWorker(domain).start(tuple01);

			Tuple tuple = domain.takeOneSync(pattern, "");

			assertEquals(0, domain.read(pattern, "").size());
			assertTrue(tuple instanceof Tuple);

			IDomain subDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			IDomain subsubDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB");

			new PutTestWorker(subsubDomain).start(tuple02);

			tuple = subDomain.takeOneSync(pattern, "");
			assertEquals(0, subDomain.read(pattern, "").size());
			assertTrue(tuple instanceof Tuple);
		} catch (TupleSpaceException e) {
			fail("Domain constructor not accept valid name.");
		}
	}

	@Test
	public void testAddReaction() {
		try {
			IDomain domain = TupleSpace.getInstance().getDomain("DOMAIN");
			PutReaction putReaction = new PutReaction(pattern, "");
			Object reactionId = domain.addReaction(putReaction, "put");			
			new PutTestWorker(domain).start(tuple01);	
			
			assertEquals(tuple01, putReaction.getTuple());		
			putReaction.setTuple();
			
			PutReaction putReaction2 = new PutReaction(pattern, "");
			domain.addReaction(putReaction2, "put");	
			IDomain subDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB");
			new PutTestWorker(subDomain).start(tuple02);		
						
			assertEquals(tuple02, putReaction.getTuple());
			assertEquals(tuple02, putReaction2.getTuple());
			
			domain.removeReaction(reactionId);
			putReaction2.setTuple();
			IDomain subsubDomain = TupleSpace.getInstance().getDomain("DOMAIN.SUB.SUB");
			new PutTestWorker(subsubDomain).start(tuple03);
			
			assertEquals(tuple02, putReaction.getTuple());
			assertEquals(tuple03, putReaction2.getTuple());
		} catch (TupleSpaceException e) {
			fail("Domain constructor not accept valid name.");
		}
	}

}
