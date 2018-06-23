package org.redwings.cameratest2;

import android.content.Context;
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

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by 이준희 on 2018-06-24.
 */

public class IngredientListItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<IngredientListItem> data;
    private int layout;
    public int userid;
    private Context mContext;
    private AlertDialog dialog;

    public IngredientListItemAdapter(Context context, int layout, ArrayList<IngredientListItem> data, int userid){
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
        IngredientListItem listviewitem=data.get(position);
        TextView number = (TextView) convertView.findViewById(R.id.ingredientListItemNumber);
        number.setText(String.valueOf(listviewitem.getNumber()));
        TextView name=(TextView)convertView.findViewById(R.id.ingredientListItemName);
        name.setText(listviewitem.getName());
        Button deleteButton = (Button)convertView.findViewById(R.id.ingredientListItemDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) v.getTag();
                String ingredientName = name.getText().toString();
                //delete-ingredient POST 구현

                Response.Listener<String>responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String success = jsonResponse.getString("result");
                            if(success=="true"){
                                AlertDialog.Builder builder = new AlertDialog.Builder(((IngredientActivity) mContext));
                                dialog = builder.setMessage("재료를 입력하였습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                ((IngredientActivity) mContext).RefreshArrayList();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(((IngredientActivity) mContext));
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
                DeleteRequest deleteRequest = new DeleteRequest(ingredientName, String.valueOf(userid),responseListener);
                RequestQueue queue = Volley.newRequestQueue(((IngredientActivity) mContext));
                queue.add(deleteRequest);
            }
        });
        deleteButton.setTag(name);
        return convertView;
    }
}
