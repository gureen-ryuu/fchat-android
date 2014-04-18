package cc.pat.fchat.objects;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Parcel;
import android.os.Parcelable;

public class Channel implements Parcelable {

	public int charactersNumber = 0;
	public HashMap<String, ChatCharacter> roomCharacters = new HashMap<String, ChatCharacter>();
	public ArrayList<ChatRoomMessage> roomMessages;
	public String channelName;
	public String channelTitle;

	public enum ChannelMode {
		BOTH, CHAT, ADS;
	}

	public enum ChannelType {
		WHISPER, PRIVATE, PUBLIC;
	}

	public ChannelType channelType;
	public ChannelMode channelMode;

	/**
	 * Syntax >> ORS { "channels": [object] } Raw sample ORS { channels:
	 * [{"name"
	 * :"ADH-300f8f419e0c4814c6a8","characters":0,"title":"Ariel's Fun Club"},
	 * {"name":"ADH-d2afa269718e5ff3fae7","characters":6,"title":
	 * "Monster Girl Dungeon RPG"},
	 * {"name":"ADH-75027f927bba58dee47b","characters"
	 * :2,"title":"Naruto Descendants OOC"} ...] }
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		 out.writeInt(charactersNumber);
		 out.writeString(channelName);
		 out.writeString(channelTitle);
		 out.writeSerializable(roomMessages);
		 out.writeSerializable(roomCharacters);
	}

	public Channel(){
		
	}
	
	@SuppressWarnings("unchecked")
	private Channel(Parcel in) {
		charactersNumber = in.readInt();
		channelName = in.readString();
		channelTitle = in.readString();
		roomMessages = (ArrayList<ChatRoomMessage>) in.readSerializable();
		roomCharacters = (HashMap<String, ChatCharacter>) in.readSerializable();
	}

	public static final Parcelable.Creator<Channel> CREATOR = new Parcelable.Creator<Channel>() {
		public Channel createFromParcel(Parcel in) {
			return new Channel(in);
		}

		public Channel[] newArray(int size) {
			return new Channel[size];
		}
	};
}
