package com.network.entityhandler;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.network.entity.RoutingPort;

public class RoutingPortService {
	static SessionFactory sessionFactoryObj;
	static Session sessionObj;
    static Logger logger=LoggerFactory.getLogger(RoutingPortService.class);
	static SessionFactory buildSessionFactory() {
		// Creating Configuration Instance & Passing Hibernate Configuration File
		Configuration configObj = new Configuration();
		configObj.configure("hibernate.cfg.xml");

		// Since Hibernate Version 4.x, ServiceRegistry Is Being Used
		ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder()
				.applySettings(configObj.getProperties()).build();

		// Creating Hibernate SessionFactory Instance
		sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
		return sessionFactoryObj;
	}

	public static void insert() {
		logger.info(".......RoutingPort Insert.......\n");

		sessionObj = RoutingPortService.buildSessionFactory().openSession();
		
		// RoutingPort RoutingPort = new RoutingPort();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the size of how many records going to be insered");
		int numofInsert = scanner.nextInt();
		for (int i = 0; i < numofInsert; i++) {
			Transaction tx = sessionObj.beginTransaction();
			/*
			 * System.out.println("Enter the Route Id"); int routeId=scanner.nextInt();
			 */
			System.out.println("Enter the Router");
			String router = scanner.next();
			System.out.println("Enter the Port Number");
			int portNo = scanner.nextInt();
		
			RoutingPort routingPort = new RoutingPort();
			routingPort.setPort(portNo);
			routingPort.setRouter(router);
			sessionObj.save(routingPort);
			logger.info("Record Inserted ");
			tx.commit();

		}
		
	}

	public static void select() {
		logger.info(".......RoutingPort get all details.......\n");

		sessionObj = RoutingPortService.buildSessionFactory().openSession();
		Transaction tx = sessionObj.beginTransaction();
		try {
			// tx = sessionObj.beginTransaction();
			List routingPort = sessionObj.createQuery("FROM RoutingPort").list();
			/*
			 * for (RoutingPort rTable : RoutingPort) { System.out.print("Routing NextHop"
			 * + rTable.getNextHop()); System.out.print(" Routing Destination" +
			 * rTable.getDestination()); System.out.println("Routing LinkNode: " +
			 * rTable.getLinkNode()); }
			 */

			for (Iterator iterator = routingPort.iterator(); iterator.hasNext();) {
				RoutingPort rPort = (RoutingPort) iterator.next();
				System.out.print("Router" + rPort.getRouter()+" ");
				System.out.print(" Port Number" + rPort.getPort()+"       ");
				
				
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

	public static void update(String router,int port) throws Exception {
		logger.info("Routing Port Update");
		Session session = RoutingPortService.buildSessionFactory().openSession();
		
		Transaction tx = null;
        
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(RoutingPort.class);
			RoutingPort rport=(RoutingPort) criteria.add(Restrictions.eq("router",router)).uniqueResult();
			if ( rport==null )
			{
				throw new Exception("Router:  "+router+" "+"is not present in Routing Port!!!Update will be prohibited" );
			}
			int id = rport.getId();
			RoutingPort rPort = (RoutingPort) session.get(RoutingPort.class, id);
			//Query query= 
			
			rPort.setPort(port);
			session.update(rPort);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static void delete(String router) throws Exception {
		logger.info("RoutingPort Deletion");
		Session session = RoutingPortService.buildSessionFactory().openSession();
		;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(RoutingPort.class);
			RoutingPort rport=(RoutingPort) criteria.add(Restrictions.eq("router",router)).uniqueResult();
			if(rport==null)
			{
				throw new Exception("No Router is present in router port!!! Delete option is strictly prohibited");
			}
			int id1 = rport.getId();
			RoutingPort rPort = (RoutingPort) session.get(RoutingPort.class, id1);
			session.delete(rPort);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
