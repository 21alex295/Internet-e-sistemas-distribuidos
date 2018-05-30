package es.udc.ws.app.model.conductor;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlConductorDaoFactory {
	
	private final static String CLASS_NAME_PARAMETER = "SqlConductorDaoFactory.classname";
	private static SqlConductorDao dao = null;
	private SqlConductorDaoFactory(){
		
	}
    @SuppressWarnings("rawtypes")
	private static SqlConductorDao getInstance(){
		try {
			String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class daoClass = Class.forName(daoClassName);
			return (SqlConductorDao) daoClass.newInstance();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public synchronized static SqlConductorDao getDao(){
		if (dao == null){
			dao = getInstance();
		}
		return dao;
	}
}
