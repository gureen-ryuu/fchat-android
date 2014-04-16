package cc.pat.fchat.objects;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatRoomMessage extends ChatMessage {

	public ChatRoom channel;

	public ChatRoomMessage(JSONObject messageJSON) throws JSONException {
		super(messageJSON);
		channel = (ChatRoom) messageJSON.get("channel");
	}
	
	public ChatRoomMessage(ChatCharacter from, ChatRoom chatroom, String message, Date timestamp, Boolean isSender ){
		super(from, message, timestamp, isSender);
		this.channel = chatroom;
	}
}
