package messagemanager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agents.AID;

public class ACLMessage {

	public AID sender;
	public List<AID> receivers;
	public AID replyTo;
	public String content;
	public Object contentObj;
	public HashMap<String, Object> userArgs;
	public String language;
	public String encoding;
	public String ontology;
	public String protocol;
	public String conversationId;
	public String replyWith;
	public String inReplyTo;
	public long replyBy;
}
