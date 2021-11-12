package com.network.toolhandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.network.entityhandler.PacketTrackofRouterService;
import com.network.entityhandler.PacketTransferService;
import com.network.entityhandler.RoutingPacketService;
import com.network.entityhandler.RoutingPortService;

public class Server {

	public static void main(String[] args) throws IOException {
		// server is listening on port 5056
		Logger logger = LoggerFactory.getLogger(Server.class);
		ServerSocket ss = new ServerSocket(5061);
		logger.info("Port used by Server:" + ss);
		SessionFactory sFactory = HibernateUtil.buildSessionFactory();

		while (true) {
			Socket s = null;

			try {
				// socket object to receive incoming client requests
				s = ss.accept();

				logger.info("A new client is connected : " + s);

				// obtaining input and out streams
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());

				logger.info("Assigning new thread for this client");

				// create a new thread object
				Thread t = new ClientHandler(s, dis, dos);
				logger.info(t.getName());
				// Invoking the start() method
				t.start();
				

			} catch (Exception e) {
				s.close();
				logger.error("Unexpected Error", e);

			} finally {
				// s.close();
				ss.close();
			}
			
		}
	}
}

//ClientHandler class
class ClientHandler extends Thread {
	// private static SessionFactory factory;
	Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;

	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}

	@Override
	public void run() {
		String received;

		SessionFactory sessionFactory;
		Configuration configObj = new Configuration();
		configObj.configure("hibernate.cfg.xml");

		// Since Hibernate Version 4.x, ServiceRegistry Is Being Used
		ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder()
				.applySettings(configObj.getProperties()).build();

		// Creating Hibernate SessionFactory Instance
		sessionFactory = configObj.buildSessionFactory(serviceRegistryObj);
		while (true) {
			try {

				// Ask user what he wants
				dos.writeUTF(
						"What do you want in RoutingTable?1.RoutingTable 2. RoutingPortCRUD 3. PacketTransfer 4.PacketlogDestination ..\n"
								+ "Type Exit to terminate connection.");
				System.out.println("Entered");
				// receive the answer from client
				received = dis.readUTF();
				// logger.info("Received:" + received);

				System.out.println(received);
				if (received.equals("Exit")) {
					logger.info("Client " + this.s + " sends exit...");
					logger.info("Closing this connection.");
					this.s.close();
					logger.info("Connection closed");
					break;
				}

				// Session session = null;
				// write on output stream based on the
				// answer from the client
				switch (received) {

				case "RoutingTable":
					logger.info("Inside Routing Table CRUD operations");
					System.out.println("Routing Table CRUD 1. Insert 2. Select 3. Delete 4. Update /n 5 Exit for out");
					Scanner scanner = new Scanner(System.in);
					String routingTable =scanner.next();
					switch (routingTable) {
					case "Insert":
						logger.info("--------Inside the Insert Statement-----------");
						RoutingPacketService.insert();
						break;
					case "Select":
						logger.info("--------Inside the Select Statement-----------");
						RoutingPacketService.select();
						break;
					case "Update":
						logger.info("--------Inside the Update Statement-----------");
						System.out.println("Enter the destination changes Router");
						String destination = scanner.next();
						// logger.info("routeId" + routeId);
						System.out.println("Enter the nextHop");
						String nextHop = scanner.next();
						logger.info("NetHop" + nextHop);
						System.out.println("Enter the linknode");
						String linkNode = scanner.next();
						try {
							RoutingPacketService.update(destination, nextHop, linkNode);
							// break;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					case "delete":
						logger.info("-----------Inside the delete statement");
						System.out.println("Enter the router that needs to be deleted");
						String destinationRouter = scanner.next();
						try {
							RoutingPacketService.delete(destinationRouter);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					default:

						System.out.println("Invalid Input for routing table");
						break;

					/*
					 * System.out.println("RoutingTableCRUD out"); break;
					 */
					}
					
					break;
				case "RoutingPortCRUD":
					logger.info("Inside Routing Port CRUD");
					System.out.println("RoutingPort CRUD 1.Insert 2.Select 3.Delete 4.Update 5. Exit for out");
					Scanner scanner1 = new Scanner(System.in);
					String routingPortOption = scanner1.next();
					logger.info("routingPortOption" + routingPortOption);
					switch (routingPortOption) {
					case "Insert":
						logger.info("-------PortCrud Insert-------");
						RoutingPortService.insert();
						break;
					case "Select":
						logger.info("-------PortCrud Select-------");
						RoutingPortService.select();
						break;
					case "Update":
						logger.info("-------PortCrud Update-------");
						logger.info("For updating portnumber");
						System.out.println("Enter the Router for Changing port number");
						String router = scanner1.next();
						int port = scanner1.nextInt();
						// logger.info("Port",port);
						try {
							RoutingPortService.update(router, port);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					case "delete":
						logger.info("-------PortCrud Delete-------");
						System.out.println("Enter the id");
						String Router = scanner1.next();
						// int id = scanner1.nextInt();
						logger.info("Router for Deleting" + Router);
						try {
							RoutingPortService.delete(Router);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					default:

						dos.writeUTF("Invalid input for routing port");
						break;

					}
					break;
				case "PacketTransfer":
					logger.info("-------------Packet Transfer for link or destination node");
					System.out.println("Packet Transfer...");
					Scanner scanner2 = new Scanner(System.in);
					System.out.println("Enter Source");
					String source = scanner2.next();
					logger.info("source" + source);
					System.out.println("Enter Destination");
					String destination = scanner2.next();
					logger.info("Destination" + destination);
					System.out.println("Enter the message to be sended");
					String message = scanner2.next();
					logger.info("Message" + message);
					RoutingTable rTable = new RoutingTable();
					Transaction tx = null;
					Session session1 = HibernateUtil.buildSessionFactory().openSession();
					Criteria criteria = session1.createCriteria(RoutingTable.class);
					Criteria criteria1 = session1.createCriteria(RoutingPort.class);
					Criteria criteria2 = session1.createCriteria(RoutingPort.class);

					RoutingTable rTable1 = (RoutingTable) criteria.add(Restrictions.eq("destination", destination))
							.uniqueResult();
					String linkNode = rTable1.getLinkNode();
					// System.out.println(linkNode);
					logger.info("Linknode" + linkNode);
					RoutingPort rPort = (RoutingPort) criteria1.add(Restrictions.eq("router", destination))
							.uniqueResult();
					logger.debug("router Port", rPort);
					RoutingPort rPort2 = (RoutingPort) criteria2.add(Restrictions.eq("router", linkNode))
							.uniqueResult();
					int port = rPort.getPort();
					if (!(rTable1.getNextHop().equalsIgnoreCase(source))
							&& !(rTable1.getLinkNode().equalsIgnoreCase(source))) {
						System.out.println("if entered");
						System.out.println("No router path is available");
						break;
					}

					else if (linkNode.equals("NO")) {
						System.out.println("else if entered");
						logger.info("If link node is NO");
						PacketTransferService.sendDirectDestination(source, destination, message, port);
						System.out.println("if closed");
						break;
					}

					else {
						// System.out.println(rPort1.getPort());
						logger.info("If link node is present");
						System.out.println("else entered");
						int linkPort = rPort2.getPort();
						int destinyPort = rPort.getPort();
						PacketTransferService.sendDirectLinkNode(source, linkNode, message, linkPort, destination,
								destinyPort);
						System.out.println("else closed");
						 break;
					}
                  
				case "PacketlogDestination":
					System.out.println("Track the packets message of destination");
					System.out.println("Enter the router for tracking package");
					Scanner scannerp = new Scanner(System.in);
					String router = scannerp.next();
					PacketTrackofRouterService.select(router);
					break;

				default:
					
					dos.writeUTF("Invalid input given by client");
					break;
				}
				

			} 
			
			catch (IOException e) {
				
				e.printStackTrace();
			}
			
            
		}
	}

}
