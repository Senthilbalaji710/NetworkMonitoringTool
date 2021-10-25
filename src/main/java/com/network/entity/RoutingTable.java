package com.network.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "routingtable",schema = "public" )
public class RoutingTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="routeid")
    private int routeId;
    
    public RoutingTable() {
		super();
	}

	@Column(name="nexthop",nullable = false)
    private String nextHop;
	@Column(name="destination",nullable = false)
	private String destination;
	@Column(name="linknode",nullable = false)
	private String linkNode;
    
    public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public String getNextHop() {
		return nextHop;
	}

	public void setNextHop(String nextHop) {
		this.nextHop = nextHop;
	}

	public String getDestination() {
		return getDestination();
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public RoutingTable(int routeId, String nextHop, String destination, String linkNode) {
		super();
		this.routeId = routeId;
		this.nextHop = nextHop;
		this.destination = destination;
		this.linkNode = linkNode;
	}

	@Override
	public String toString() {
		return "RoutingTable [routeId=" + routeId + ", nextHop=" + nextHop + ", destination=" + destination
				+ ", linkNode=" + linkNode + "]";
	}

	public String getLinkNode() {
		return linkNode;
	}

	public void setLinkNode(String linkNode) {
		this.linkNode = linkNode;
	}
}