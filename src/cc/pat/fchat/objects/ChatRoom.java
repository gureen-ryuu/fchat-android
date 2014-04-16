package cc.pat.fchat.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoom {

	public int charactersNumber = 0;
	public HashMap<String, ChatCharacter> roomCharacters = new HashMap<String, ChatCharacter>();
	public ArrayList<ChatRoomMessage> roomMessages;
	
}
