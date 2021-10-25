package com.network.entityhandler;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.network.entity.RoutingTable;

public class RoutingPacketController {
	static Logger logger = LoggerFactory.getLogger(RoutingPacketController.class);
	static SessionFactory sessionFactoryObj;
	static Session sessionObj;

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
		logger.info(".......RoutingTable Insert.......\n");

		sessionObj = RoutingPacketController.buildSessionFactory().openSession();
		Transaction tx = sessionObj.beginTransaction();
		// RoutingTable routingTable = new RoutingTable();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the size of how many records going to be insered");
		int numofInsert = scanner.nextInt();
		logger.info("Number of records to be inserted");
		for (int i = 0; i < numofInsert; i++) {
			/*
			 * System.out.println("Enter the Route Id"); int routeId=scanner.nextInt();
			 */
			System.out.println("Enter the next hop");
			String nextHop = scanner.next();
			System.out.println("Eneter the Destination");
			String destination = scanner.next();
			System.out.println("Enter the linknode if not keep NO");
			String linknode = scanner.next();
			RoutingTable routingTable = new RoutingTable();
			// routingTable.setRouteId(routeId);
			routingTable.setDestination(destination);
			routingTable.setLinkNode(linknode);
			routingTable.setNextHop(nextHop);
			sessionObj.save(routingTable);
			tx.commit();

		}
	}

	public static void select() {
		logger.info(".......RoutingTable get all details.......\n");

		sessionObj = RoutingPacketController.buildSessionFactory().openSession();
		Transaction tx = sessionObj.beginTransaction();
		try {
			// tx = sessionObj.beginTransaction();
			List routingTable = sessionObj.createQuery("FROM RoutingTable").list();
			/*
			 * for (RoutingTable rTable : routingTable) { System.out.print("Routing NextHop"
			 * + rTable.getNextHop()); System.out.print(" Routing Destination" +
			 * rTable.getDestination()); System.out.println("Routing LinkNode: " +
			 * rTable.getLinkNode()); }
			 */

			for (Iterator iterator = routingTable.iterator(); iterator.hasNext();) {
				RoutingTable rTable = (RoutingTable) iterator.next();
				System.out.print("Routing NextHop" + rTable.getNextHop());
				System.out.print(" Routing Destination" + rTable.getDestination());
				System.out.println("Routing LinkNode: " + rTable.getLinkNode());
				logger.debug("Routing Table selected", rTable);
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

	public static void update(int routeId, String nextHop) {
		logger.info("RoutingPacket Update");
		Session session = RoutingPacketController.buildSessionFactory().openSession();
		;
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			RoutingTable rTable = (RoutingTable) session.get(RoutingTable.class, routeId);
			rTable.setNextHop(nextHop);
			session.update(rTable);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static void delete(int routeId) {
		logger.info("Routing Table deletion");
		Session session = RoutingPacketController.buildSessionFactory().openSession();
		;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			RoutingTable rTable = (RoutingTable) session.get(RoutingTable.class, routeId);
			session.delete(rTable);
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
