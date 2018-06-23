package org.redwings.cameratest2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 이준희 on 2018-06-24.
 */

public class DeleteRequest extends StringRequest {

    final static private String URL="http://172.21.57.27:8000/delete-ingredient";
    private Map<String, String> parameters;


    DeleteRequest(String ingredient, String user, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("ingredient", ingredient);
        parameters.put("userid", user);
    }

    @Override
    public  Map<String, String>getParams(){return parameters;}
}
