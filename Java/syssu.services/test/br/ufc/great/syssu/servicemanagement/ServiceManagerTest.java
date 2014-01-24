package br.ufc.great.syssu.servicemanagement;

import static org.junit.Assert.*;

import org.junit.Test;

public class ServiceManagerTest {

	@Test
	public void testServiceOperations() {
		assertNull(ServiceManager.getInstance().getService(null));
		assertNull(ServiceManager.getInstance().getService("service 1"));

		try {
			ServiceManager.getInstance().addService(null);
			fail("Accept ilegal service");
		} catch (IllegalArgumentException ex) {
			// Ok.
		}
		
		try {
			ServiceManager.getInstance().addService(new ServiceMock0());
			fail("Accept ilegal service");
		} catch (IllegalArgumentException ex) {
			// Ok.
		}

		IService serviceMock1 = new ServiceMock1();

		ServiceManager.getInstance().addService(serviceMock1);
		assertEquals("service 1",
				ServiceManager.getInstance().getService("service 1").getName());

		assertNull(ServiceManager.getInstance().getService(null));
		assertNull(ServiceManager.getInstance().getService("service 2"));

		IService serviceMock2 = new ServiceMock2();

		ServiceManager.getInstance().addService(serviceMock2);
		assertEquals("service 2",
				ServiceManager.getInstance().getService("service 2").getName());

		ServiceManager.getInstance().removeService("service 1");
		
		assertNull(ServiceManager.getInstance().getService("service 1"));
		assertEquals("service 2",
				ServiceManager.getInstance().getService("service 2").getName());
		
		ServiceManager.getInstance().removeService(serviceMock2);
		assertNull(ServiceManager.getInstance().getService("service 1"));
		assertNull(ServiceManager.getInstance().getService("service 2"));
	}

	@Test
	public void testEventServiceOperations() {
		assertNull(ServiceManager.getInstance().getEventService(null));
		assertNull(ServiceManager.getInstance().getEventService("service 1"));

		try {
			ServiceManager.getInstance().addEventService(null);
			fail("Accept ilegal service");
		} catch (IllegalArgumentException ex) {
			// Ok.
		}
		
		try {
			ServiceManager.getInstance().addEventService(new ServiceMock0());
			fail("Accept ilegal service");
		} catch (IllegalArgumentException ex) {
			// Ok.
		}

		IService serviceMock1 = new ServiceMock1();

		ServiceManager.getInstance().addEventService(serviceMock1);
		assertEquals("service 1",
				ServiceManager.getInstance().getEventService("service 1").getName());

		assertNull(ServiceManager.getInstance().getEventService(null));
		assertNull(ServiceManager.getInstance().getEventService("service 2"));

		IService serviceMock2 = new ServiceMock2();

		ServiceManager.getInstance().addEventService(serviceMock2);
		assertEquals("service 2",
				ServiceManager.getInstance().getEventService("service 2").getName());

		ServiceManager.getInstance().removeEventService("service 1");
		
		assertNull(ServiceManager.getInstance().getEventService("service 1"));
		assertEquals("service 2",
				ServiceManager.getInstance().getEventService("service 2").getName());
		
		ServiceManager.getInstance().removeEventService(serviceMock2);
		assertNull(ServiceManager.getInstance().getEventService("service 1"));
		assertNull(ServiceManager.getInstance().getEventService("service 2"));
	}

}

class ServiceMock0 implements IService {

	@Override
	public String getName() {
		return "";
	}

	@Override
	public Object doService(Object params) throws InvalidParamsException,
			OperationException {
		// TODO Auto-generated method stub
		return null;
	}

}

class ServiceMock1 implements IService {

	@Override
	public String getName() {
		return "service 1";
	}

	@Override
	public Object doService(Object params) throws InvalidParamsException,
			OperationException {
		// TODO Auto-generated method stub
		return null;
	}

}

class ServiceMock2 implements IService {

	@Override
	public String getName() {
		return "service 2";
	}

	@Override
	public Object doService(Object params) throws InvalidParamsException,
			OperationException {
		// TODO Auto-generated method stub
		return null;
	}

}
