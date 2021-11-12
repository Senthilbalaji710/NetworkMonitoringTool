package com.network.entityhandler;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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
import com.network.entity.RoutingTable;

public class RoutingPacketService {
	static Logger logger = LoggerFactory.getLogger(RoutingPacketService.class);
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

		sessionObj = RoutingPacketService.buildSessionFactory().openSession();
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

		sessionObj = RoutingPacketService.buildSessionFactory().openSession();
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
				System.out.print("Routing NextHop" + rTable.getNextHop()+"   ");
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

	public static void update(String destination, String nextHop,String linkNode) throws Exception {
		logger.info("RoutingPacket Update");
		Session session = RoutingPacketService.buildSessionFactory().openSession();
		
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(RoutingTable.class);
			RoutingTable rTable1 = (RoutingTable)  criteria.add(Restrictions.eq("destination",destination)).uniqueResult();
			if(rTable1==null)
			{
				throw new Exception("No router present in routing table that needs to be updated");
			}
			else
			{
			int id=rTable1.getRouteId();
			RoutingTable rTable = (RoutingTable) session.get(RoutingTable.class, id);
			rTable.setNextHop(nextHop);
			rTable.setLinkNode(linkNode);
			rTable.setDestination(destination);
			session.update(rTable);
			tx.commit();}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static void delete(String destinationRouter) throws Exception {
		logger.info("Routing Table deletion");
		Session session = RoutingPacketService.buildSessionFactory().openSession();
		;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(RoutingTable.class);
			RoutingTable rTable1 = (RoutingTable)  criteria.add(Restrictions.eq("destination",destinationRouter)).uniqueResult();
			System.out.println(rTable1);
			if(rTable1==null)
			{
				throw new  Exception("Router not present in RoutingTable");
			}
			else
			{
				int id = rTable1.getRouteId();
				RoutingTable rTable = (RoutingTable) session.load(RoutingTable.class, id);
				//RoutingTable rTable = (RoutingTable) session.get(RoutingTable.class, routeId);
				session.delete(rTable);
				tx.commit();
			}
			
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
