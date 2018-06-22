package org.redwings.cameratest2;

import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL = "http://172.21.57.27:8000/login";
    private Map<String, String> parameters;


    LoginRequest(String userID, String userPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HttpClient", "error: " + error.toString());
            }
        });
        parameters = new HashMap<>();
        parameters.put("user", userID);
        parameters.put("password", userPassword);
    }

    @Override
    public Map<String, String>getParams(){
        return parameters;
    }
}
