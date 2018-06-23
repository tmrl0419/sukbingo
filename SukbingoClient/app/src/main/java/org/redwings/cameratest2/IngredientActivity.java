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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class IngredientActivity extends AppCompatActivity {


    private AlertDialog dialog;
    public ArrayList<IngredientListItem> ingredientListItemArrayList;
    public IngredientListItemAdapter ingredientListItemAdapter;
    public int userid;

    public void RefreshArrayList() {


        //get-ingredient POST 구현
        Response.Listener<String>responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String success = jsonResponse.getString("result");
                    JSONArray ingredients = jsonResponse.getJSONArray("result");
                    if(success != null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(IngredientActivity.this);
                        /*dialog = builder.setMessage("재료를 입력하였습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();*/
                        ArrayList<IngredientListItem> tempArrayList = new ArrayList<>();
                        for(int i=0; i<ingredients.length(); i++){
                            tempArrayList.add(new IngredientListItem(i+1, ingredients.getString(i)));
                        }
                        ingredientListItemArrayList = tempArrayList;
                        ingredientListItemAdapter =new IngredientListItemAdapter(IngredientActivity.this,R.layout.ingredientlistitem,ingredientListItemArrayList, userid);
                        final ListView listView = (ListView) findViewById(R.id.courseListview);
                        listView.setAdapter(ingredientListItemAdapter);

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
        IngredientRequest ingredientRequest = new IngredientRequest(String.valueOf(userid),responseListener);
        RequestQueue queue = Volley.newRequestQueue(IngredientActivity.this);
        queue.add(ingredientRequest);



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        final Intent ingredientIntent = getIntent();
        final EditText IngredientText = (EditText) findViewById(R.id.IngredientText);
        final Button InputButton = (Button) findViewById(R.id.InputButton);
        final ListView listView = (ListView) findViewById(R.id.courseListview);
        final Button testasefagargargButton = (Button) findViewById(R.id.testasefagargarg);
        testasefagargargButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshArrayList();
            }
        });
        userid = ingredientIntent.getIntExtra("userid",-1);
        ingredientListItemArrayList = new ArrayList<>();
        ingredientListItemArrayList.add(new IngredientListItem(1, "대뱃살"));
        ingredientListItemArrayList.add(new IngredientListItem(2,"닭고기"));
        ingredientListItemArrayList.add(new IngredientListItem(3,"소고기"));
        ingredientListItemAdapter=new IngredientListItemAdapter(this,R.layout.ingredientlistitem,ingredientListItemArrayList, userid);
        listView.setAdapter(ingredientListItemAdapter);

        InputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ingredient = IngredientText.getText().toString();
                String user=String.valueOf(ingredientIntent.getIntExtra("userid",-1));

                Response.Listener<String>responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String success = jsonResponse.getString("result");
                            if(success=="true"){
                                AlertDialog.Builder builder = new AlertDialog.Builder(IngredientActivity.this);
                                dialog = builder.setMessage("재료를 입력하였습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                RefreshArrayList();
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
