package es.udc.ws.app.model.viaje;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlViajeDaoFactory {

	private final static String CLASS_NAME_PARAMETER = "SqlViajeDaoFactory.classname";
	private static SqlViajeDao dao = null;
	
	private SqlViajeDaoFactory(){
		
	}
	
	@SuppressWarnings("rawtypes")
	private static SqlViajeDao getInstance(){
		try{
			String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class daoClass = Class.forName(daoClassName);
			return (SqlViajeDao) daoClass.newInstance();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	public synchronized static SqlViajeDao getDao(){
		
		if (dao == null){
			dao = getInstance();
		}
		return dao;
	}
}
