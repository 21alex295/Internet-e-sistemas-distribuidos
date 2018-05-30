package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientUsuarioServiceFactory {
	private final static String CLASS_NAME_PARAMETER = "ClientUsuarioServiceFactory.className";
	private static Class<ClientUsuarioService> serviceClass = null;
	
	private ClientUsuarioServiceFactory(){		
	}
	
    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientUsuarioService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientUsuarioService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }
    
    public static ClientUsuarioService getService() {

        try {
            return (ClientUsuarioService) getServiceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
