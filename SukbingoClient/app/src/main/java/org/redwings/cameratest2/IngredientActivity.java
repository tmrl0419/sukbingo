package org.redwings.cameratest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.regex.Pattern;



public class IngredientActivity extends AppCompatActivity {


    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        final EditText IngredientText = (EditText) findViewById(R.id.IngredientText);
        final Button InputButton = (Button) findViewById(R.id.InputButton);



        InputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ingredient = IngredientText.getText().toString();
                String user="tmr10419";

                Response.Listener<String>responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String success = jsonResponse.getString("result");
                            if(success=="True"){
                                AlertDialog.Builder builder = new AlertDialog.Builder(IngredientActivity.this);
                                dialog = builder.setMessage("재료를 입력하였습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(IngredientActivity.this);
                                dialog = builder.setMessage("재료를 다시 확인하세요.")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                AddRequest addRequest = new AddRequest(Ingredient, user,responseListener);
                RequestQueue queue = Volley.newRequestQueue(IngredientActivity.this);
                queue.add(addRequest);

            }
        });
    }
}