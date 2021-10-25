package com.network.entityhandler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.network.entity.Packets;
import com.network.toolhandler.HibernateUtil;
//import com.network.toolhandler.HibernateUtil;

public class PacketTransferController {
	static SessionFactory sessionFactory;
	static Logger logger = LoggerFactory.getLogger(PacketTransferController.class);

	public static void sendDirectDestination(String source, String destination, String message, int port)
			throws UnknownHostException, IOException {
		// Session session;
		ServerSocket server = new ServerSocket(port);
		Socket socket = new Socket("127.0.0.1", port);
		// Socket serverClient;
		System.out.println("Entered");
		try {

			int counter = 0;
			logger.info("Server Started ....");

			while (true) {
				// System.out.println("Entered");
				counter++;
				Socket serverClient = server.accept();
				logger.info(" server accept the client connection request");
				logger.info(" >> " + "Client No:" + counter + " started!");
				ServerClientThread sct = new ServerClientThread(serverClient, source, destination, port, message,
						counter); // send the request to a
				// separate thread
				sct.start();
				//serverClient.close();
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {

			server.close();
			socket.close();

		}
	}

	public static void sendDirectLinkNode(String source, String linkNode, String message, int linkPort,
			String destination1, int destinyPort) throws IOException {
		logger.info("--------------If link node present for transfering packets-----------------------");
		ServerSocket server = new ServerSocket(linkPort);
		Socket socket = new Socket("127.0.0.1", linkPort);
		// System.out.println("Entered");
		String destination = linkNode;
		logger.info("Linknode" + linkNode);
		int port = linkPort;

		try {

			int counter = 0;
			logger.info("Server Started ....");

			while (true) {
				// System.out.println("Entered");
				counter++;
				Socket serverClient = server.accept(); // server accept the client connection request
				logger.info(" >> " + "Client No:" + counter + " started!");
				ServerClientThread sct = new ServerClientThread(serverClient, source, destination, port, message,
						counter); // send the request to a
				// separate thread
				sct.start();
				/*
				 * ServerClientThread sct = new
				 * ServerClientThread(serverClient,source,destination,port, counter); // send
				 * the request to a // separate thread sct.start()
				 */
				sendDirectDestination(linkNode, destination1, message, destinyPort);
				
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			server.close();
			socket.close();
		}
	}

}

class ServerClientThread extends Thread {
	Socket serverClient;
	int clientNo;
	int squre;
	String source;
	String destination;
	int port;
	private String message;
	static SessionFactory sessionFactoryObj;
	static Session session;
	static Logger logger = LoggerFactory.getLogger(ServerClientThread.class);

	private static SessionFactory buildSessionFactory() {
		logger.info("Creating Configuration Instance & Passing Hibernate Configuration File");
		Configuration configObj = new Configuration();
		configObj.configure("hibernate.cfg.xml");

		logger.info("Since Hibernate Version 4.x, ServiceRegistry Is Being Used");
		ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder()
				.applySettings(configObj.getProperties()).build();

		logger.info("Creating Hibernate SessionFactory Instance");
		sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
		return sessionFactoryObj;
	}

	ServerClientThread(Socket inSocket, String source, String destination, int port, String message, int counter) {
		serverClient = inSocket;
		clientNo = counter;
		this.source = source;
		this.destination = destination;
		this.port = port;
		this.message = message;
	}

	public void run() {
		session = ServerClientThread.buildSessionFactory().openSession();
		logger.info("Entered Thread");
		try {
			// System.out.println("Try Block...");
			DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
			DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
			Scanner sc = new Scanner(System.in);
			String clientMessage = "", serverMessage = "";
			boolean option = true;
			while (option) {
				// System.out.println("While block");

				System.out.println("session created" + session);
				Instant start = Instant.now();
				Packets packets = new Packets();
				Transaction tx = session.beginTransaction();
				/*
				 * clientMessage = inStream.readUTF(); System.out.println("From Client-" +
				 * clientNo + ": Number is :" + clientMessage); squre =
				 * Integer.parseInt(clientMessage) * Integer.parseInt(clientMessage);
				 * serverMessage = "From Server to Client-" + clientNo + " Square of " +
				 * clientMessage + " is " + squre;
				 */
				packets.setDestination(destination);
				packets.setSource(source);
				packets.setMessage(message);
				packets.setPortNumber(port);
				Instant end = Instant.now();
				Duration timeElapsed = Duration.between(start, end);
				int timeElapsed1 = (int) timeElapsed.toMillis();
				packets.setTime2ms(timeElapsed1);
				session.save(packets);
				logger.info("Data inserted");
				option = false;
				tx.commit();
				outStream.writeUTF(serverMessage);
				outStream.flush();
			}
			inStream.close();
			outStream.close();
			serverClient.close();
			session.close();
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			logger.info("Client -" + clientNo + " exit!! ");
		}
	}
}
