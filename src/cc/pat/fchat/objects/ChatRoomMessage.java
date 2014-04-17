package cc.pat.fchat.objects;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatRoomMessage extends ChatMessage {

	public Channel channel;

	public ChatRoomMessage(JSONObject messageJSON) throws JSONException {
		super(messageJSON);
		channel = (Channel) messageJSON.get("channel");
	}
	
	public ChatRoomMessage(ChatCharacter from, Channel channel, String message, Date timestamp, Boolean isSender ){
		super(from, message, timestamp, isSender);
		this.channel = channel;
	}
}
