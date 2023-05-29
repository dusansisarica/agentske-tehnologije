package agentcenter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.ws.rs.Path;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import models.Host;

@Singleton
@Startup
@Remote(AgentCenter.class)
@Path("/connection")
public class AgentCenterBean implements AgentCenter {

	private Host localNode;
	private List<String> nodeCluster = new ArrayList<>();
	
	private static final String HTTP_PREFIX = "http://";
	private static final String PORT_EXTENSION = ":8080";
	
	@PostConstruct
	private void init() {
		String address = getNodeAddress();
		String alias = getNodeAlias() + PORT_EXTENSION;
		
		localNode = new Host(alias, address);
		System.out.println("*** " + localNode.alias + " started at: " + localNode.address);
		if(getMasterAlias() != null && !getMasterAlias().equals(PORT_EXTENSION)) {
			// HANDSHAKE [0]
			ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = resteasyClient.target(HTTP_PREFIX + getMasterAlias() + "/Chat-war/api/connection");
			AgentCenter rest = rtarget.proxy(AgentCenter.class);

			// HANDSHAKE [1] - New node notifies master
//			nodeCluster = rest.registerNewNode(localNode.getAlias());	
//			
//			// HANDSHAKE [2] - Master requests list of performatives
//			rest.getAgentClassesFromRemote(performativeEnumToString());

			nodeCluster.add(getMasterAlias());
			nodeCluster.removeIf(n -> n.equals(localNode.getAlias()));
			resteasyClient.close();
			System.out.println("\n*** Handshake successful. Number of connected nodes: " + nodeCluster.size() + "\n");
		}
	}
	
	private String getNodeAddress() {		
		try {
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			ObjectName http = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
			return (String) mBeanServer.getAttribute(http, "boundAddress");			
		} catch (MalformedObjectNameException | InstanceNotFoundException | AttributeNotFoundException | ReflectionException | MBeanException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String getNodeAlias() {		
		return System.getProperty("jboss.node.name");
	}
	
	public String getMasterAlias() {
		try {
			InputStream fileInput  = AgentCenterBean.class.getClassLoader().getResourceAsStream("../properties/connections.properties");
			Properties connectionProperties = new Properties();
			connectionProperties.load(fileInput);
			fileInput.close();
			return connectionProperties.getProperty("master_node") + PORT_EXTENSION; 
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	
	

}
