package com.network.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="Packetslogging",schema = "public")
public class Packets {
	
		
	
	@Override
	public String toString() {
		return "Packets [packetId=" + packetId + ", message=" + message + ", source=" + source + ", destination="
				+ destination + ", portNumber=" + portNumber + ", time2ms=" + time2ms + "]";
	}
	public int getPacketId() {
		return packetId;
	}
	public void setPacketId(int packetId) {
		this.packetId = packetId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public long getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(long portNumber) {
		this.portNumber = portNumber;
	}
	public int getTime2ms() {
		return time2ms;
	}
	public void setTime2ms(int time2ms) {
		this.time2ms = time2ms;
	}
	public Packets(int packetId, String message, String source, String destination, long portNumber, int time2ms) {
		super();
		this.packetId = packetId;
		this.message = message;
		this.source = source;
		this.destination = destination;
		this.portNumber = portNumber;
		this.time2ms = time2ms;
	}
	public Packets() {
		super();
	}
	@Id
	@SequenceGenerator(name = "packetId", sequenceName = "Packet_Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator="packetId")
    private int packetId;
	@Column(name="message")
	private String message;
	@Column(name="source")
	private String source;
	@Column(name="destination")
	private String destination;
	@Column(name="portnumber")
	private long portNumber;
	@Column(name="Time2ms")
	private int time2ms;

	
}
