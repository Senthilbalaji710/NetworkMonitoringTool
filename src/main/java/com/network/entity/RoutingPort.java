package com.network.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "RoutingPort",schema = "public")
public class RoutingPort {
 
	@Override
	public String toString() {
		return "RoutingPort [id=" + id + ", router=" + router + ", port=" + port + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRouter() {
		return router;
	}
	public void setRouter(String router) {
		this.router = router;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public RoutingPort() {
		super();
	}
	@Id
	@SequenceGenerator(name = "id", sequenceName = "Port_Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator="id")
	private int id;
	@Column(name="Router")
	private String router;
	@Column(name="Port")
	private int port;
	public RoutingPort(int id, String router, int port) {
		super();
		this.id = id;
		this.router = router;
		this.port = port;
	}
}
