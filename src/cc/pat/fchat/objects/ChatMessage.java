package cc.pat.fchat.objects;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatMessage {

	public ChatCharacter from;
	public String message;
	public Date sentTime;
	public boolean isSender;
	
	public ChatMessage(JSONObject messageJSON) throws JSONException{
		from = (ChatCharacter) messageJSON.get("character");
		message = messageJSON.getString("message");
		sentTime = Calendar.getInstance().getTime();
	}
	
	public ChatMessage(ChatCharacter from, String message, Date sentTime, boolean isSender){
		this.from = from;
		this.message = message;
		this.sentTime = sentTime;
		this.isSender = isSender;			
	}
}
