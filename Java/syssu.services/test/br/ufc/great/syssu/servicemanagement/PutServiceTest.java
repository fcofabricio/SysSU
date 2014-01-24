package br.ufc.great.syssu.servicemanagement;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.Test;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.interfaces.TupleSpaceException;
import br.ufc.great.syssu.coordubi.TupleSpace;
import br.ufc.great.syssu.servicemanagement.services.PutService;

public class PutServiceTest {

	private PutService putService = new PutService();

	@Test
	public void testGetName() {
		assertEquals("put", putService.getName());
	}

	@Test
	public void testDoService() {
		testExceptions();
		testSuccess();
	}

	private void testSuccess() {
		Map<String, Object> mapParams = new LinkedHashMap<String, Object>();
		List<Object> listParams = new ArrayList<Object>();
		Map<String, Object> tuple = new LinkedHashMap<String, Object>();
		tuple.put("field", "value");

		try {
			mapParams.clear();
			mapParams.put("domain", "test_domain");
			mapParams.put("tuple", tuple);
			putService.doService(mapParams);
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.clear();
			listParams.add("test_domain");
			listParams.add(tuple);
			putService.doService(listParams);
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		Pattern pattern = new Pattern();
		pattern.addField("field", "value");

		try {
			assertEquals("Tuple not putted", 2,
							TupleSpace.getInstance().getDomain("test_domain").read(pattern, "").size());
		} catch (TupleSpaceException e) {
			fail(e.getMessage());
		}
	}

	private void testExceptions() {
		Map<String, Object> mapParams = new LinkedHashMap<String, Object>();
		List<Object> listParams = new ArrayList<Object>();

		try {
			putService.doService(new HashSet<Object>());
			fail("PutServcice accept invalid params (set)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (empty map)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			putService.doService(listParams);
			fail("PutServcice accept invalid params (empty list)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			mapParams.put("domain", null);
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (null domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.add(null);
			putService.doService(listParams);
			fail("PutServcice accept invalid params (null domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			mapParams.clear();
			mapParams.put("domain", "");
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (empty domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.clear();
			listParams.add("");
			putService.doService(listParams);
			fail("PutServcice accept invalid params (empty domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			mapParams.clear();
			mapParams.put("domain", 123);
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (invalid domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.clear();
			listParams.add(123);
			putService.doService(listParams);
			fail("PutServcice accept invalid params (invalid domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			mapParams.clear();
			mapParams.put("domain", "test_domain");
			mapParams.put("tuple", null);
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (null tuple)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.clear();
			listParams.add("test_domain");
			listParams.add(null);
			putService.doService(listParams);
			fail("PutServcice accept invalid params (null tuple)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			mapParams.clear();
			mapParams.put("domain", "test_domain");
			mapParams.put("tuple", new LinkedHashMap<String, Object>());
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (empty tuple)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.clear();
			listParams.add("test_domain");
			listParams.add(new LinkedHashMap<String, Object>());
			putService.doService(listParams);
			fail("PutServcice accept invalid params (empty tuple)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		List<Object> invalidTuple = new ArrayList<Object>();
		invalidTuple.add(1);

		try {
			mapParams.clear();
			mapParams.put("domain", "test_domain");
			mapParams.put("tuple", invalidTuple);
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (invalid tuple)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.clear();
			listParams.add("test_domain");
			listParams.add(invalidTuple);
			putService.doService(listParams);
			fail("PutServcice accept invalid params (invalid tuple)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		Map<String, Object> tuple = new LinkedHashMap<String, Object>();
		tuple.put("field", "value");

		try {
			mapParams.clear();
			mapParams.put("domain", null);
			mapParams.put("tuple", tuple);
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (null domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.clear();
			listParams.add(null);
			listParams.add(tuple);
			putService.doService(listParams);
			fail("PutServcice accept invalid params (null domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			mapParams.clear();
			mapParams.put("domain", "");
			mapParams.put("tuple", tuple);
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (empty domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.clear();
			listParams.add("");
			listParams.add(tuple);
			putService.doService(listParams);
			fail("PutServcice accept invalid params (empty domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			mapParams.clear();
			mapParams.put("domain", 123);
			mapParams.put("tuple", tuple);
			putService.doService(mapParams);
			fail("PutServcice accept invalid params (invalid domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}

		try {
			listParams.clear();
			listParams.add(123);
			listParams.add(tuple);
			putService.doService(listParams);
			fail("PutServcice accept invalid params (invalid domain)");
		} catch (InvalidParamsException e) {
			// OK.
		} catch (OperationException e) {
			// OK.
		}
	}

}
