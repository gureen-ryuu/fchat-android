package cc.pat.fchat.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class Channel {

	public int charactersNumber = 0;
	public HashMap<String, ChatCharacter> roomCharacters = new HashMap<String, ChatCharacter>();
	public ArrayList<ChatRoomMessage> roomMessages;
	public String channelID;
	public String channelTitle;
	public enum ChannelMode {
		BOTH,
		CHAT,
		ADS;
	}
	
	public enum ChannelType {
		WHISPER,
		PRIVATE,
		PUBLIC;		
	}
	
	public ChannelType channelType;
	public ChannelMode channelMode;
	/**Syntax
	>> ORS { "channels": [object] }
	Raw sample
	ORS { channels: [{"name":"ADH-300f8f419e0c4814c6a8","characters":0,"title":"Ariel's Fun Club"},
					{"name":"ADH-d2afa269718e5ff3fae7","characters":6,"title":"Monster Girl Dungeon RPG"},
					{"name":"ADH-75027f927bba58dee47b","characters":2,"title":"Naruto Descendants OOC"} ...] }
	*/
	
	public Channel (JSONObject channelJSON, ChannelType channelType, ChannelMode channelMode) throws JSONException {
		charactersNumber = channelJSON.getInt("characters");
		channelTitle = channelJSON.getString("title");
		channelID = channelJSON.getString("name");
		this.channelType = channelType;
		this.channelMode = channelMode;
	}
	
}
