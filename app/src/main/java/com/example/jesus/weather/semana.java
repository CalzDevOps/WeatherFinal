package com.example.jesus.weather;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class semana extends AppCompatActivity {


    TextView t1,t2,t3,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15, t16, t17, t18;
    String id ="2160a8f44c9a22e08b1bf22d3e36bf6e";
    String city ="Madrid";
    String data= "";
    String data2= "";
    String allObjectsParsed = "";
    ImageView myImage, myImage2, myImage3, myImage4, myImage5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semana);

         ActionBar actionBar = getActionBar();
         getSupportActionBar().hide();
         //dia 1
        t1=(TextView) findViewById(R.id.num2);
        t2=(TextView) findViewById(R.id.ciudad2);
        t3=(TextView) findViewById(R.id.temNombre2);
        t5=(TextView) findViewById(R.id.temperatura2);
        t6=(TextView) findViewById(R.id.diaSemana);
        //dia 2
        t7=(TextView) findViewById(R.id.temNombre3);
        t8=(TextView) findViewById(R.id.diaSemana3);
        t9=(TextView) findViewById(R.id.temperatura3);
        //dia 3
        t10=(TextView) findViewById(R.id.temNombre4);
        t11=(TextView) findViewById(R.id.diaSemana4);
        t12=(TextView) findViewById(R.id.temperatura4);
        //dia 4
        t13=(TextView) findViewById(R.id.temNombre5);
        t14=(TextView) findViewById(R.id.diaSemana5);
        t15=(TextView) findViewById(R.id.temperatura5);
        //dia 5
        t16=(TextView) findViewById(R.id.temNombre6);
        t17=(TextView) findViewById(R.id.diaSemana6);
        t18=(TextView) findViewById(R.id.temperatura6);


        Typeface typeface= Typeface.createFromAsset(getAssets(),"RighteousRegular.ttf");
        t1.setTypeface(typeface);
        t2.setTypeface(typeface);
        t3.setTypeface(typeface);
        t7.setTypeface(typeface);
        t10.setTypeface(typeface);
        t13.setTypeface(typeface);
        t16.setTypeface(typeface);



        myImage = (ImageView) findViewById(R.id.imagen2);
        myImage2 = (ImageView) findViewById(R.id.imagen3);
        myImage3 = (ImageView) findViewById(R.id.imagen4);
        myImage4 = (ImageView) findViewById(R.id.imagen5);
        myImage5 = (ImageView) findViewById(R.id.imagen6);



        try {
            city = URLEncoder.encode(getIntent().getExtras().getString("numero") , "utf-8");
            String url = "https://www.metaweather.com/api/location/search/?query="+city;
            Load(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

                    //dia 1
                    JSONObject details = json.getJSONArray("consolidated_weather").getJSONObject(1);
                    t2.setText(json.getString("title").toUpperCase(Locale.US));
                    t3.setText(details.getString("weather_state_name").toUpperCase(Locale.US));
                    t5.setText(details.getString("the_temp").toUpperCase(Locale.US));
                    String fecha=details.getString("applicable_date").toUpperCase(Locale.US);
                    t6.setText("Fecha: "+fecha);

                    String icono  =details.getString("weather_state_abbr");
                    String url = "https://www.metaweather.com//static/img/weather/png/64/"+icono+".png";
                    Thread3 myThread = new Thread3(myImage);
                    myThread.execute(url);


                    //dia 2
                    JSONObject details2 = json.getJSONArray("consolidated_weather").getJSONObject(2);
                    t7.setText(details2.getString("weather_state_name").toUpperCase(Locale.US));
                    t9.setText(details2.getString("the_temp").toUpperCase(Locale.US));
                    String fecha2=details2.getString("applicable_date").toUpperCase(Locale.US);
                    t8.setText("Fecha: "+fecha2);

                    String icono2  =details2.getString("weather_state_abbr");
                    String url2 = "https://www.metaweather.com//static/img/weather/png/64/"+icono2+".png";
                    Thread3 myThread2 = new Thread3(myImage2);
                    myThread2.execute(url2);

                    //dia 3
                    JSONObject details3 = json.getJSONArray("consolidated_weather").getJSONObject(3);
                    t10.setText(details3.getString("weather_state_name").toUpperCase(Locale.US));
                    t12.setText(details3.getString("the_temp").toUpperCase(Locale.US));
                    String fecha3=details3.getString("applicable_date").toUpperCase(Locale.US);
                    t11.setText("Fecha: "+fecha3);

                    String icono3  =details3.getString("weather_state_abbr");
                    String url3 = "https://www.metaweather.com//static/img/weather/png/64/"+icono3+".png";
                    Thread3 myThread3 = new Thread3(myImage3);
                    myThread3.execute(url3);

                    //dia 4
                    JSONObject details4 = json.getJSONArray("consolidated_weather").getJSONObject(4);
                    t13.setText(details4.getString("weather_state_name").toUpperCase(Locale.US));
                    t15.setText(details4.getString("the_temp").toUpperCase(Locale.US));
                    String fecha4=details4.getString("applicable_date").toUpperCase(Locale.US);
                    t14.setText("Fecha: "+fecha4);

                    String icono4  =details4.getString("weather_state_abbr");
                    String url4 = "https://www.metaweather.com//static/img/weather/png/64/"+icono4+".png";
                    Thread3 myThread4 = new Thread3(myImage4);
                    myThread4.execute(url4);

                    //dia 5
                    JSONObject details5 = json.getJSONArray("consolidated_weather").getJSONObject(5);
                    t16.setText(details5.getString("weather_state_name").toUpperCase(Locale.US));
                    t18.setText(details5.getString("the_temp").toUpperCase(Locale.US));
                    String fecha5=details5.getString("applicable_date").toUpperCase(Locale.US);
                    t17.setText("Fecha: "+fecha5);

                    String icono5  =details5.getString("weather_state_abbr");
                    String url5 = "https://www.metaweather.com//static/img/weather/png/64/"+icono5+".png";
                    Thread3 myThread5 = new Thread3(myImage5);
                    myThread5.execute(url5);




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
