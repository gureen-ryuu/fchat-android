package cc.pat.fchat.utils;

import java.util.LinkedHashMap;

import org.json.simple.JSONObject;

import cc.pat.fchat.FApp;
import cc.pat.fchat.objects.ChatMessage;
import cc.pat.fchat.objects.ChatRoomMessage;
import cc.pat.fchat.objects.Commands;

@SuppressWarnings("unchecked")
public class CommandsBuilder {

	public static String IDN(String character) {
		return IDN(FApp.getInstance().ticket, FApp.getInstance().account, character);
	}

	/**
	 	IDN { "method": "ticket", "account": string, "ticket": string, "character": string, "cname": string, "cversion": string }
	 */
	public static String IDN(String ticket, String account, String character) {
		JSONObject jsonObject = new JSONObject(new LinkedHashMap<String, String>());
		jsonObject.put("method", "ticket");
		jsonObject.put("ticket", ticket);
		jsonObject.put("account", account);
		jsonObject.put("character", character);
		jsonObject.put("cname", "patchat");
		jsonObject.put("cversion", "1");

		return Commands.IDN + " " + jsonObject.toString();
	}
	
	public static String PIN(){
		return Commands.PIN;
	}
	
	/** Syntax <br/>
		MSG { "channel": string, "message": string } <br/>
		Raw sample <br/>
		MSG {"message": "Right, evenin'", "character": "Aensland Morrigan", "channel": "Frontpage"} 
	*/
	public static String MSG(ChatRoomMessage chatMessage){
		JSONObject jsonObject = new JSONObject(new LinkedHashMap<String, String>());
		jsonObject.put("channel", chatMessage.channel);
		jsonObject.put("character", chatMessage.from);
		jsonObject.put("message", chatMessage.message);
		
		return Commands.MSG + " " + jsonObject.toJSONString();
	}
}
