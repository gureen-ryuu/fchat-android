package cc.pat.fchat.utils;

import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

import cc.pat.fchat.FApp;
import cc.pat.fchat.objects.Commands;

@SuppressWarnings("unchecked")
public class CommandsBuilder {

	public static String IDN(String character) {
		return IDN(FApp.getInstance().ticket, FApp.getInstance().account, character);
	}

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
}
