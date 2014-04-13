package cc.pat.fchat.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatCharacter {

	public String status; //TODO possibly enum
	public String identity;
	public String statusMessage = "";
	public String gender; //TODO possibly enum

	public void initLIS(JSONObject characterJSON) throws JSONException{
		identity = characterJSON.getString("identity");
		status = characterJSON.getString("status");
		gender = characterJSON.getString("gender");
	}
	
	//[["Alexandrea", "Female", "online", ""]]
	public void initLIS(JSONArray characterJSON) throws JSONException{
		identity = characterJSON.getString(0);
		gender = characterJSON.getString(1);
		status = characterJSON.getString(2);
		statusMessage = characterJSON.getString(3);
	}
}
