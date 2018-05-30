
package es.udc.ws.app.model.conductorservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ConductorServiceFactory {

	private final static String CLASS_NAME_PARAMETER = "ConductorServiceFactory.classname";
	private static ConductorService service = null;
	
	private ConductorServiceFactory(){
		
	}
	
	@SuppressWarnings("rawtypes")
	private static ConductorService getInstance() {
		try {
			// Error NullPointer Exception
			String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class serviceClass = Class.forName(serviceClassName);
			return (ConductorService) serviceClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public synchronized static ConductorService getService(){
		if (service == null){
			service = getInstance();
		}
		return service;
	}
	
}
