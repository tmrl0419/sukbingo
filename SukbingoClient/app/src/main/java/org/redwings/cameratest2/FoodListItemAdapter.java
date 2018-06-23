package org.redwings.cameratest2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 이준희 on 2018-06-24.
 */

public class FoodListItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<FoodListItem> data;
    private int layout;
    public int userid;
    private Context mContext;
    private AlertDialog dialog;
    Handler handler = new Handler();

    public FoodListItemAdapter(Context context, int layout, ArrayList<FoodListItem> data, int userid){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
        this.userid = userid;
        this.mContext = context;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getName();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        Context context = parent.getContext();
        final FoodListItem listviewitem=data.get(position);
        TextView number = (TextView) convertView.findViewById(R.id.FoodListItemNumber);
        number.setText(String.valueOf(listviewitem.getNumber()));
        TextView name=(TextView)convertView.findViewById(R.id.FoodListItemName);
        name.setText(listviewitem.getName());
        final View convertViewFinal = convertView;
        ImageView imageView = (ImageView)convertView.findViewById(R.id.FoodListItemImageView);
        Picasso.get().load(listviewitem.getImageURL()).into(imageView);
        Button foodListItemButton1 = (Button) convertView.findViewById(R.id.FoodListItemButton1);
        foodListItemButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) v.getTag();
                String foodName = name.getText().toString();
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

                                mContext.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder((FoodActivity)mContext);
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
                RequestQueue queue = Volley.newRequestQueue((FoodActivity)mContext);
                queue.add(foodInfoRequest);
            }
        });
        foodListItemButton1.setTag(name);

        Button foodListItemButton2 = (Button) convertView.findViewById(R.id.FoodListItemButton2);
        foodListItemButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) v.getTag();
                final String foodName = name.getText().toString();
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
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/results?search_query=" + foodName));

                                mContext.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder((FoodActivity)mContext);
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
                RequestQueue queue = Volley.newRequestQueue((FoodActivity)mContext);
                queue.add(foodInfoRequest);
            }
        });
        foodListItemButton2.setTag(name);
        return convertView;
    }
}

