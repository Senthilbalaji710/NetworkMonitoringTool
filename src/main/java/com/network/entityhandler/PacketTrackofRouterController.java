package com.network.entityhandler;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.network.entity.Packets;
import com.network.entity.RoutingPort;

public class PacketTrackofRouterController {
           
	static SessionFactory sessionFactoryObj;
	static Session sessionObj;
    static Logger logger=LoggerFactory.getLogger(RoutingPortController.class);
	static SessionFactory buildSessionFactory() {
		// Creating Configuration Instance & Passing Hibernate Configuration File
		Configuration configObj = new Configuration();
		configObj.configure("hibernate.cfg.xml");

		// Since Hibernate Version 4.x, ServiceRegistry Is Being Used
		ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder()
				.applySettings(configObj.getProperties()).build();

		// Creating Hibernate SessionFactory Instance
		sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
		return sessionFactoryObj;}
		public static void select(String router) {
			logger.info(".......RoutingPort get all details.......\n");
            System.out.println("Entered select");
			sessionObj = RoutingPortController.buildSessionFactory().openSession();
			Transaction tx = sessionObj.beginTransaction();
			try {
				// tx = sessionObj.beginTransaction();
				List routingPacket = sessionObj.createQuery("FROM Packets").list();
				System.out.println(routingPacket);
				/*
				 * for (RoutingPort rTable : RoutingPort) { System.out.print("Routing NextHop"
				 * + rTable.getNextHop()); System.out.print(" Routing Destination" +
				 * rTable.getDestination()); System.out.println("Routing LinkNode: " +
				 * rTable.getLinkNode()); }
				 */

				for (Iterator iterator = routingPacket.iterator(); iterator.hasNext();) {
					Packets rPort = (Packets) iterator.next();
					System.out.print("Destination Router" + rPort.getDestination()+" ");
					System.out.print(" Message" + rPort.getMessage()+"  ");
					//packetslogging
					
					
				}

				tx.commit();
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
			} finally {
				sessionObj.close();
			}
		}
	}

