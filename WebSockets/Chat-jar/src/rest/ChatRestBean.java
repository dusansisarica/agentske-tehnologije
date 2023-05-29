package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.Agent;
import agents.AgentType;
import agents.UserAgent;
import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import models.Message;
import models.User;
import util.JNDILookup;

@Stateless
@Path("/agents")
public class ChatRestBean implements ChatRest {

	@EJB
	private MessageManagerRemote messageManager;
	
	
	@Override
	public void register(User user) {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "REGISTER");
		message.userArgs.put("username", user.getUsername());
		message.userArgs.put("password", user.getPassword());
		
		messageManager.post(message);
	}

	@Override
	public void login(User user) {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "LOG_IN");
		message.userArgs.put("username", user.getUsername());
		message.userArgs.put("password", user.getPassword());
		
		messageManager.post(message);
	}

	@Override
	public void getloggedInUsers() {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "GET_LOGGEDIN");
		
		messageManager.post(message);
	}

	@Override
	public List<AgentType> getAgentTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AID> getRunningAgents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AID startAgent(String agentType, String agentName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AID stopAgent(String aid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout(String username) {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "LOG_OUT");
		message.userArgs.put("username", username);
		
		messageManager.post(message);
	}

	@Override
	public void sendMessage(Message msg) {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", msg.getSender());
		message.userArgs.put("command", "MESSAGE");
		message.userArgs.put("sender", msg.getSender());
		message.userArgs.put("receiverUser", msg.getReceiver());
		message.userArgs.put("subject", msg.getSubject());
		message.userArgs.put("content", msg.getContent());
		//message.userArgs.put("date", msg.getDate());
		System.out.println("Poruka stigla");
		messageManager.post(message);
	}

	@Override
	public void getMessages(String user1, String user2) {
		// TODO Auto-generated method stub
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", user1);
		message.userArgs.put("command", "GET_MESSAGES");
		message.userArgs.put("user1", user1);
		message.userArgs.put("user2", user2);
		//message.userArgs.put("date", msg.getDate());
		System.out.println("Poruka stigla");
		messageManager.post(message);

	}

	@Override
	public void getRegisteredUsers() {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "GET_REGISTERED");
		messageManager.post(message);
		
	}
	

}
