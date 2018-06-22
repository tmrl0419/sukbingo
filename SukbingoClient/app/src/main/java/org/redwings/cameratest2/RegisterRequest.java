package org.redwings.cameratest2;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL="http://172.21.57.27:8000/regist";
    private Map<String, String> parameters;


    RegisterRequest(String userId, String userPassword, String userName,  Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user", userId);
        parameters.put("password", userPassword);
        parameters.put("name", userName);
    }

    @Override
    public  Map<String, String>getParams(){return parameters;}
}