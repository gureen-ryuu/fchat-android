package cc.pat.fchat.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class Friend {

	public String sourceName;
	public String destName;
	
	public Friend (JSONObject jsonObject) throws JSONException{
		sourceName = jsonObject.getString("source_name");
		destName = jsonObject.getString("dest_name");
	}
	
}
