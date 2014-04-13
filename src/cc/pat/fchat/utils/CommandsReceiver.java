package cc.pat.fchat.utils;

import java.util.Calendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import cc.pat.fchat.FApp;
import cc.pat.fchat.objects.ChatCharacter;
import cc.pat.fchat.objects.Commands;

public class CommandsReceiver {

	private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();	
	private final CommandHandlerThread commandHandlerThread = new CommandHandlerThread();
	
	public CommandsReceiver(){
		commandHandlerThread.start();
	}
	
	public void receiveCommand(String payload) {
		try {
			queue.put(payload);
		} catch (InterruptedException e1) {
		}
	}

	private class CommandHandlerThread extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					handleNextCommand();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} // Blocks until queue isn't empty.
			}
		}
		
		private void handleNextCommand() throws InterruptedException{
			String payload = queue.take();					
			try {
				if (payload.startsWith(Commands.CON))
					CON(payload.substring(4));
				else if (payload.startsWith(Commands.LIS))
					LIS(payload.substring(4));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public static void CON(String payload) throws JSONException {
		JSONObject payloadJSON = new JSONObject(payload);
		FApp.getInstance().onlineCharactersCount = payloadJSON.getInt("count");
	}

	public static void LIS(String payload) throws JSONException {
		long startTime = Calendar.getInstance().getTimeInMillis();

		JSONObject payloadJSON = new JSONObject(payload);
		JSONArray characters = payloadJSON.getJSONArray("characters");

		for (int i = 0; i < characters.length(); i++) {
			ChatCharacter character = new ChatCharacter();
			character.initLIS(characters.getJSONArray(i));
			FApp.getInstance().onlineCharacters.put(character.identity, character);
		}

		if (FApp.getInstance().onlineCharacters.size() == FApp.getInstance().onlineCharactersCount) {
			FApp.getInstance().LISDone();
		}

		long endTime = Calendar.getInstance().getTimeInMillis();

		Log.v("Pat", "Finished LIS:  " + (endTime - startTime));
	}
}
