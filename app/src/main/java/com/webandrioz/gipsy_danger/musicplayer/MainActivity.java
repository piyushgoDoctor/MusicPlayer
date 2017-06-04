package com.webandrioz.gipsy_danger.musicplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MainActivity extends Activity {

    private ListView mainList;
    private MediaPlayer mp;

    ArrayList<String> listContent=new ArrayList<>();
     FloatingActionButton btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = new MediaPlayer();
        mp.setLooping(true);
        mainList = (ListView) findViewById(R.id.listView1);

       String json= loadAssets();

        try {
            JSONArray jsonArray=new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                listContent.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }





        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listContent);
        mainList.setAdapter(adapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                playSong(position);

            }
        });
         btn= (FloatingActionButton) findViewById(R.id.fab);
        btn.setVisibility(View.GONE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();
                    btn.setImageResource(R.drawable.ic_play_button);
                }else {
                    mp.start();
                    btn.setImageResource(R.drawable.ic_pause_black_24dp);

                }

            }
        });

    }

    public void playSong(int songIndex) {
// Play song
        btn.setVisibility(View.VISIBLE);
        Log.e("SONG NAME", "playSong: "+listContent.get(songIndex) );

        mp.reset();// stops any current playing song
        mp = MediaPlayer.create(getApplicationContext(), this.getResources().getIdentifier("raw/"+listContent.get(songIndex),null,this.getPackageName()));// create's
        mp.start(); // starting mediaplayer

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }
    public String loadAssets(){
        String json=null;
        try{
            InputStream ip=this.getAssets().open("songs.json");
            int size=ip.available();
            byte[] buffer=new byte[size];
            ip.read(buffer);
            ip.close();
            json=new String(buffer,"UTF-8");

        }catch(Exception e){
            Log.e("load assets", "loadAssets: "+e.getMessage() );

        }
        return json;

    }

}
