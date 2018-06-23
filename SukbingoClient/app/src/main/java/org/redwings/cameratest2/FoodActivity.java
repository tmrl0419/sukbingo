package org.redwings.cameratest2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class FoodActivity extends AppCompatActivity {


    private AlertDialog dialog;
    public ArrayList<FoodListItem> FoodListItemArrayList;
    public FoodListItemAdapter FoodListItemAdapter;
    public int userid;

    public void RefreshArrayList() {


        //get-ingredient POST 구현
        Response.Listener<String>responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String success = jsonResponse.getString("result");
                    JSONArray Foods = jsonResponse.getJSONArray("result");
                    if(success != null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
                        /*dialog = builder.setMessage("재료를 입력하였습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();*/
                        ArrayList<FoodListItem> tempArrayList = new ArrayList<>();
                        for(int i=0; i<Foods.length(); i++){
                            JSONArray jSONArray1 = Foods.getJSONArray(i);
                            tempArrayList.add(new FoodListItem(i+1, jSONArray1.getString(0), jSONArray1.getString(1)));
                        }
                        FoodListItemArrayList = tempArrayList;
                        FoodListItemAdapter =new FoodListItemAdapter(FoodActivity.this,R.layout.foodlistitem,FoodListItemArrayList, userid);
                        final ListView listView = (ListView) findViewById(R.id.FoodListview);
                        listView.setAdapter(FoodListItemAdapter);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
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
        FoodRequest foodRequest = new FoodRequest(String.valueOf(userid),responseListener);
        RequestQueue queue = Volley.newRequestQueue(FoodActivity.this);
        queue.add(foodRequest);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        final Intent foodIntent = getIntent();

        final ListView listView = (ListView) findViewById(R.id.FoodListview);

        userid = foodIntent.getIntExtra("userid",-1);
        RefreshArrayList();

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);
                TextView textView = (TextView)view.findViewById(R.id.FoodListItemName);
                String foodName = textView.getText().toString();


                Response.Listener<String>responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String success = jsonResponse.getString("result");
                            JSONArray foodInfo = jsonResponse.getJSONArray("result");
                            int PRIMARY_ID = foodInfo.getInt(0);
                            String DetailURL = foodInfo.getString(2);
                            if(success != null){
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DetailURL));

                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
                                dialog = builder.setMessage("FoodActivity재료를 다시 확인하세요.")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                FoodInfoRequest foodInfoRequest = new FoodInfoRequest(foodName,responseListener);
                RequestQueue queue = Volley.newRequestQueue(FoodActivity.this);
                queue.add(foodInfoRequest);
            }
        });*/

    }
}
