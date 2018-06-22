package org.redwings.cameratest2;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {

    final static private String URL="http://172.21.57.27:8000/add-ingredient";
    private Map<String, String> parameters;


    AddRequest(String ingredient, String user, Response.Listener<String> listener){
        super(Method.PUT, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("ingredientid", ingredient);
        parameters.put("user", user);
    }

    @Override
    public  Map<String, String>getParams(){return parameters;}
}

