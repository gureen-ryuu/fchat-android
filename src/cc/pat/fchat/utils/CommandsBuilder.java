package cc.pat.fchat.utils;

import java.util.LinkedHashMap;

import org.json.simple.JSONObject;

import cc.pat.fchat.FApp;
import cc.pat.fchat.objects.ChatRoomMessage;
import cc.pat.fchat.objects.Commands;
import cc.pat.fchat.objects.PrivateMessage;

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
	
	public static String ORS(){
		return Commands.ORS;
	}
	
	public static String CHA(){
		return Commands.CHA;
	}
	
	/** Syntax <br/>
		MSG { "channel": string, "message": string } <br/>
		Raw sample <br/>
		MSG {"message": "Right, evenin'", "character": "Aensland Morrigan", "channel": "Frontpage"} 
	*/
	public static String MSG(ChatRoomMessage chatMessage){
		JSONObject jsonObject = new JSONObject(new LinkedHashMap<String, String>());
		jsonObject.put("message", chatMessage.message);
		jsonObject.put("character", chatMessage.from);
		jsonObject.put("channel", chatMessage.channel);
		
		return Commands.MSG + " " + jsonObject.toJSONString();
	}
	
	/** PRI { "recipient": string, "message": string }
	 */
	public static String PRI(PrivateMessage privateMessage){
		JSONObject jsonObject = new JSONObject(new LinkedHashMap<String, String>());
		jsonObject.put("message", privateMessage.message);
		jsonObject.put("recipient", privateMessage.to);
		
		return Commands.PRI + " " + jsonObject.toJSONString();
	}
	
	/** Syntax <br/>
		VAR { "variable": string, "value": int/float } <br/>
		Raw sample <br/>
		VAR {"value":4096,"variable":"chat_max"} <br/>
		VAR {"value":50000,"variable":"priv_max"} <br/>
		VAR {"value":50000,"variable":"lfrp_max"} <br/>
		VAR {"value":600,"variable":"lfrp_flood"} <br/>
		VAR {"value":0.5,"variable":"msg_flood"} <br/>
		VAR {"value":"35868","variable":"permissions"}
	*/
	public static String VAR(){
		return Commands.VAR;
	}
}
