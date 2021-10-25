package com.network.toolhandler;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
	static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	static SessionFactory sessionFactory;

	static SessionFactory buildSessionFactory() {
		logger.info("Creating Configuration Instance & Passing Hibernate Configuration File");
		Configuration configObj = new Configuration();
		configObj.configure("hibernate.cfg.xml");

		logger.info("Since Hibernate Version 4.x, ServiceRegistry Is Being Used");
		ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder()
				.applySettings(configObj.getProperties()).build();

		logger.info("Creating Hibernate SessionFactory Instance");
		sessionFactory = configObj.buildSessionFactory(serviceRegistryObj);
		return sessionFactory;
	}
}
