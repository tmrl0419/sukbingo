package org.redwings.cameratest2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class MainBingo extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Button btn_photo = (Button) findViewById(R.id.btn_photo);
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBingo.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        Button btn_ingredient = (Button)findViewById(R.id.btn_ingredient);
        btn_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ingredientintent = new Intent(MainBingo.this, IngredientActivity.class );
                MainBingo.this.startActivity(ingredientintent);
            }
        });
    }
}