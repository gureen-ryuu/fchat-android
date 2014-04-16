package cc.pat.fchat.objects;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class PrivateMessage extends ChatMessage {

	public ChatCharacter to;

	public PrivateMessage(JSONObject messageJSON) throws JSONException {
		super(messageJSON);
		to = (ChatCharacter) messageJSON.get("recipient");
	}
	
	public PrivateMessage(ChatCharacter from, ChatCharacter to, String content, Date timestamp, Boolean isSender ){
		super(from, content, timestamp, isSender);
		this.to = to;
	}
}