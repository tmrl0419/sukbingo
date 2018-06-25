package org.redwings.cameratest2;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import java.util.HashMap;
import java.util.Map;

public class FoodInfoRequest extends StringRequest {

    final static private String URL="http://172.21.57.27:8000/get-foodinfo";
    private Map<String, String> parameters;


    FoodInfoRequest(String foodName, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("food", foodName);
    }

    @Override
    public  Map<String, String>getParams(){return parameters;}
}

