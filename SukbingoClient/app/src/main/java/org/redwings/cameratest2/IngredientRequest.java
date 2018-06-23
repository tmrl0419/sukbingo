package org.redwings.cameratest2;

/**
 * Created by 이준희 on 2018-06-24.
 */

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import java.util.HashMap;
import java.util.Map;

public class IngredientRequest extends StringRequest {

    final static private String URL="http://172.21.57.27:8000/get-useringredient";
    private Map<String, String> parameters;


    IngredientRequest(String user, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userid", user);
    }

    @Override
    public  Map<String, String>getParams(){return parameters;}
}