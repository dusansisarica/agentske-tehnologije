package models;

import java.io.Serializable;

public class Host implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	public String alias;
	public String address;
	
	public Host(String alias, String address) {
		super();
		this.alias = alias;
		this.address = address;
	}

	public Host() {
		super();
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
