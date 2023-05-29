package usermanager;

import java.util.List;

import javax.ejb.Remote;

import models.Message;
import models.User;

@Remote
public interface UserManagerRemote {
	public void sendMessage(Message message);
	public List<User> getLoggedIn();
	public void addMessageSender(User u, Message message);
	public void addMessageReceiver(User u, Message message);
	
}
