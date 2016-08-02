package com.ahoy.parser.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HSession {

	private static Logger logger = LoggerFactory.getLogger(HSession.class);
	
	private static ServiceRegistry serviceRegistry = null;;
	
	private static SessionFactory factory = null;
	
	static{
		try{
			Configuration configuration = new Configuration();
			configuration.configure();
			if(serviceRegistry == null){
				serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			}
			if(factory==null){
				logger.debug("[HSession] inside handling data factory configuration is :["+serviceRegistry);
				
				factory = configuration.buildSessionFactory(serviceRegistry);
				logger.debug("[HSession] inside handling data session factory :["+configuration.buildSessionFactory(serviceRegistry));
			}
		}catch(Exception e){
			logger.error("[HSession] Exception  : "+e.getMessage());
			logger.error("[HSession] Exception  : "+e );
			
		}	
	}
	
	public static SessionFactory createSessionFactory() {
		logger.debug("[HSession][createSessionFactory] inside handling data");
			
		return factory;
		
	}
}
