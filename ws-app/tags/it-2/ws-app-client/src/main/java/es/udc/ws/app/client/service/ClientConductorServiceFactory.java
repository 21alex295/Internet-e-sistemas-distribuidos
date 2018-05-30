package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientConductorServiceFactory {
	
	private final static String CLASS_NAME_PARAMETER = "ClientConductorServiceFactory.className";
	private static Class<ClientConductorService> serviceClass = null;
	
	private ClientConductorServiceFactory(){		
	}
	
    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientConductorService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientConductorService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }
    
    public static ClientConductorService getService() {

        try {
            return (ClientConductorService) getServiceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

}
