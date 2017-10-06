package com.popland.pop.rss_newsreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//TextView tv;
    ListView lv;
    ArrayList<String> arrlTitle;
    ArrayList<String> arrlLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // tv = (TextView)findViewById(R.id.TV);
        lv = (ListView)findViewById(R.id.LV);
        arrlTitle = new ArrayList<>();
        arrlLink = new ArrayList<>();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                i.putExtra("link",arrlLink.get(position));
                startActivity(i);
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new XMLprocessor().execute();
            }
        });
    }
    class XMLprocessor extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_TuURL("http://www.thanhniennews.com/rss/tech-15.rss");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            String title ="";
            String link ="";
            for(int i=0;i<nodeList.getLength();i++){
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element,"title");
                arrlTitle.add(title);
                link = parser.getValue(element,"link");
                arrlLink.add(link);
            }
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,arrlTitle);
            lv.setAdapter(adapter);
        }
    }
    public String docNoiDung_TuURL(String theurl){
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theurl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line ="";
            while((line =bufferedReader.readLine())!=null){
                content.append(line+"\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
