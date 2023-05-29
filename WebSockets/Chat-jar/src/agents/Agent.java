package agents;

import java.io.Serializable;

import javax.jms.Message;

public interface Agent extends Serializable {

	public String init();
	public String init(String name);
	public void handleMessage(Message message);
	public String getAgentId();
}
