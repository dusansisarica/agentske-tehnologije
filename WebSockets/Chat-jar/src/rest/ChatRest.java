package rest;

import java.util.List;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import agents.AID;
import agents.AgentType;
import models.Message;
import models.User;

@Remote
public interface ChatRest {
	@POST
	@Path("users/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public void register(User user);
	
	@POST
	@Path("users/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public void login(User user);
	
	@GET
	@Path("users/loggedIn")
	public void getloggedInUsers();
	
	@GET
	@Path("users/registered")
	public void getRegisteredUsers();
	
	@GET
	@Path("/classes")
	public List<AgentType> getAgentTypes();
	
	@GET
	@Path("/running")
	public List<AID> getRunningAgents();
	
	@PUT
	@Path("/running/{type}/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public AID startAgent(@PathParam("type") String agentType,  @PathParam("name") String agentName);
	
	@DELETE
	@Path("/running/{aid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public AID stopAgent(@PathParam("aid") String aid);
	
	@DELETE
	@Path("/users/loggedIn/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public void logout(@PathParam("username") String username);
	
	@POST
	@Path("/messages/user")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendMessage(Message message);
	
	@GET
	@Path("/messages/{user1}/{user2}")
	public void getMessages(@PathParam("user1") String user1, @PathParam("user2") String user2);

}
