package cc.pat.fchat.requests;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class LoginRequest extends Request<String> {

    private final Listener<String> mListener;
    private Map<String, String> mParams;

    public LoginRequest(String username, String password, Listener<String> listener,
            ErrorListener errorListener) {
        super(Method.POST, getLoginUrl(), errorListener);
        mListener = listener;
        mParams = new HashMap<String, String>();
        mParams.put("account", username);
        mParams.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return mParams;
    };
    
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
    	super.deliverError(error);
    }
    
	@Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success(new String(response.data), getCacheEntry());
    }

    private static String getLoginUrl() {
        return "http://www.f-list.net/json/getApiTicket.php";
    }
}