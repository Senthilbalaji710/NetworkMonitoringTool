package com.network.toolhandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.network.entity.RoutingPort;

public class Client {

	
	public static void main(String[] args) throws IOException {
		Logger logger = LoggerFactory.getLogger(Client.class);
		InetAddress ip = InetAddress.getByName("localhost");
		Socket s = new Socket(ip, 5061);
		try {
			Scanner scn = new Scanner(System.in);

			// getting localhost ip
			
			RoutingPort rPort = new RoutingPort();

			// establish the connection with server port 5060
			

			// obtaining input and out streams
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());

			// the following loop performs the exchange of
			// information between client and client handler
			while (true) {
				System.out.println(dis.readUTF());
				String tosend = scn.nextLine();
				//logger.info("For Sending"+tosend);
				dos.writeUTF(tosend);

				// If client sends exit,close this connection
				// and then break from the while loop
				if (tosend.equals("Exit")) {
					System.out.println("Closing this connection : " + s);
					s.close();
					logger.info("Client Connection closed");
					break;
				}

				// printing date or time as requested by client
				String received = dis.readUTF();
				System.out.println(received);
				//logger.info(received);
			}

			// closing resources
			scn.close();
			dis.close();
			dos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
}
