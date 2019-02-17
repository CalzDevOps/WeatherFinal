package com.example.jesus.weather;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    EditText myText ;
    TextView myText2 ;
    Button myBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        myText = (EditText) findViewById(R.id.editText);
        myBtn= (Button) findViewById(R.id.button);
        myText2 = (TextView) findViewById(R.id.caja1);


        Typeface typeface= Typeface.createFromAsset(getAssets(),"RighteousRegular.ttf");
        myText.setTypeface(typeface);
        myBtn.setTypeface(typeface);
        myText2.setTypeface(typeface);

        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),WeatherActivity.class);
                i.putExtra("Ciudad",myText.getText().toString());
                startActivity(i);

            }
        });


    }
}
