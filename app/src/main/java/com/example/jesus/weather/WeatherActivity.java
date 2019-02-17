package com.example.jesus.weather;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5;
    String id ="2160a8f44c9a22e08b1bf22d3e36bf6e";
    String city ="Madrid";
    String data= "";
    String data2= "";
    String uno= "holita";
    String dos;
    String tres;
    String cuatro;
    String allObjectsParsed = "";
    ImageView myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // ActionBar actionBar = getActionBar();
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_weather);
        t1=(TextView) findViewById(R.id.textView);
        t2=(TextView) findViewById(R.id.textView2);
        t3=(TextView) findViewById(R.id.textView3);
        t4=(TextView) findViewById(R.id.textView4);
        t5=(TextView) findViewById(R.id.textView5);

        Typeface typeface= Typeface.createFromAsset(getAssets(),"RighteousRegular.ttf");
        t1.setTypeface(typeface);
        t2.setTypeface(typeface);
        t3.setTypeface(typeface);
        t4.setTypeface(typeface);
        myImage = (ImageView) findViewById(R.id.imageView2);
        try {
            city =URLEncoder.encode(getIntent().getExtras().getString("Ciudad") , "utf-8");
            String url = "https://www.metaweather.com/api/location/search/?query="+city;
            Load(url);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menucito,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int num=5;
        Intent i;
        Bundle bundle=new Bundle();

        switch (item.getItemId())
        {
            case R.id.semana/*aqui va el ID de la activity a la que nos queramos ir, en este caso 'semana'*/:
                //actividad3
                i=new Intent(this, semana.class);
                i.putExtra("numero", getIntent().getExtras().getString("Ciudad"));
                bundle.putInt("num",num);
                i.putExtra("paquete", bundle);
                startActivity(i);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Load(String place)
    {
            Thread myThread = new Thread();
            myThread.execute(place);
    }
    class Thread extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
       try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                Log.d("problema2", strings[0]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                String line = "";
                while(line != null)
                {
                    line = reader.readLine();
                    data +=  line;
                }
                JSONArray array = new JSONArray(data);
                //Recorremos el JsonArray accediendo a cad uno de los JsonObjects.
                for(int i= 0; i < array.length(); i++)
                {
                    //Por cada objeto, obtenemos su campo "name" y "url".
                    JSONObject object = (JSONObject) array.get(i);
                    allObjectsParsed  = object.get("woeid").toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("miau",s);
            t1.setText(allObjectsParsed);
            String url = "https://www.metaweather.com/api/location/"+allObjectsParsed+"/";
            Thread2 myTask = new Thread2();
            myTask.execute(url);
        }
    }
    class Thread2 extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection;

            try {
                Log.d("problema2", strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                String line = "";
                while(line != null)
                {
                    line = reader.readLine();

                    data2 +=  line;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return data2;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("guau",s);

            try {
                JSONObject json = new JSONObject(s);

                if (json != null)
                {
                    JSONObject details = json.getJSONArray("consolidated_weather").getJSONObject(0);
                    JSONObject main= json.getJSONObject("parent");
                    t2.setText(json.getString("title").toUpperCase(Locale.US));
                    t3.setText(details.getString("weather_state_name").toUpperCase(Locale.US));
                    t4.setText(main.getString("title").toUpperCase(Locale.US));
                    t5.setText(details.getString("the_temp").toUpperCase(Locale.US)+"º");

                    String icono  =details.getString("weather_state_abbr");
                    String url = "https://www.metaweather.com//static/img/weather/png/"+icono+".png";
                    Thread3 myThread = new Thread3(myImage);
                    myThread.execute(url);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class Thread3 extends AsyncTask<String,Void, Bitmap>
    {
        ImageView imageView;

        public Thread3(ImageView imageView)
        {
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap logo = null;
            try {
                URL url =new URL(strings[0]);
                Log.d("guau",strings[0]);

                InputStream is= url.openStream();
                logo = BitmapFactory.decodeStream(is);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }
}
