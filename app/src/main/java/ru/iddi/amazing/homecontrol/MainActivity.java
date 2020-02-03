package ru.iddi.amazing.homecontrol;

import android.content.Context;
import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.net.*;
import android.content.*;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.net.wifi.WifiManager;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner, zoom;
    private Button wifion, wifioff, lighton, lightoff;
    private URL urlOn, urlOff;
    private ProgressBar progress;
    private WifiManager wifi;
    private ArrayList<ArrayList<String>> switches;

    public void wifiGoesOn () {
        if( wifion.isEnabled() == true ) {
            wifion.setEnabled(false);
            wifioff.setEnabled(true);
            lighton.setEnabled(true);
            lightoff.setEnabled(true);
            this.progress.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    public void wifiGoesOff () {
        if( wifioff.isEnabled()==true ) {
            wifion.setEnabled(true);
            wifioff.setEnabled(false);
            lighton.setEnabled(false);
            lightoff.setEnabled(false);
            this.progress.setVisibility(ProgressBar.INVISIBLE);
        }
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ma = this;
        setContentView(R.layout.activity_main);

        wifi = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        this.progress = findViewById(R.id.progressBar);

        this.wifion = findViewById(R.id.wifion);
        this.wifion.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.wifi.setWifiEnabled(true);
                MainActivity.this.progress.setVisibility(ProgressBar.VISIBLE);
            }
        });

        this.wifioff = findViewById(R.id.wifioff);
        this.wifioff.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.wifi.setWifiEnabled(false);
                MainActivity.this.progress.setVisibility(ProgressBar.VISIBLE);
             }
        });

        this.lighton = findViewById(R.id.lighton);
        this.lighton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getURL( urlOn );
            }
        } );

        this.lightoff = findViewById(R.id.lightoff);
        this.lightoff.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getURL( urlOff );
            }
        } );

        switches = getSwitches();
        setUrls(0);
        this.spinner = findViewById(R.id.spinner);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private String text;
            private File targetFile;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setUrls(position);
              }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { setUrls(0); }
        });
        setSpinnerAdapter(switches);

        WifiReceiver wifirec = new WifiReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver( wifirec, filter );
    }

    private void getURL( URL inUrl ) {
        final URL myUrl = inUrl;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = myUrl.openStream();

                    DataInputStream dis = new DataInputStream(is);

                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = dis.read(buffer))>0) {
                        ;
                    }

                } catch (MalformedURLException mue) {
                    ;
                } catch (IOException ioe) {
                    ;
                } catch (SecurityException se) {
                    ;
                }
            }
        });
    }

    private void setSpinnerAdapter(ArrayList<ArrayList<String>> switches) {

        ArrayList<String> names = new ArrayList<String>();
        for (int counter = 0; counter < switches.size(); counter++) {
            names.add (switches.get(counter).get(0) );
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(adapter);
    }


    private  ArrayList<ArrayList<String>> getSwitches() {
        return new ArrayList<ArrayList<String>>() {{
            add( new ArrayList<String>() {{ add("Room2"); add("http://node-Room2.home.iddi?pin=ON1"); add("http://node-Room2.home.iddi?pin=OFF1"); }} );
            add( new ArrayList<String>() {{ add("Room1"); add("http://node-Room1.home.iddi?pin=ON1"); add("http://node-Room1.home.iddi?pin=OFF1"); }} );
            add( new ArrayList<String>() {{ add("Kitchen"); add("http://node-Kitchen.home.iddi?pin=ON1"); add("http://node-Kitchen.home.iddi?pin=OFF1"); }} );
            add( new ArrayList<String>() {{ add("Korridor"); add("http://node-korridor.home.iddi?pin=ON1"); add("http://node-korridor.home.iddi?pin=OFF1"); }} );
            add( new ArrayList<String>() {{ add("Bathroom"); add("http://node-korridor.home.iddi?pin=ON2"); add("http://node-korridor.home.iddi?pin=OFF2"); }} );
            add( new ArrayList<String>() {{ add("Bathroom Soft"); add("http://node-korridor.home.iddi?pin=MIN2"); add("http://node-korridor.home.iddi?pin=OFF2"); }} );
            add( new ArrayList<String>() {{ add("Storage"); add("http://node-storage.home.iddi?pin=ON1"); add("http://node-storage.home.iddi?pin=OFF1"); }} );
        }};
    }

    private void setUrls( int position) {
        try {
            urlOn = new URL( MainActivity.this.switches.get(position).get(1) );
        } catch (MalformedURLException mue) {
            ;//Log.e("SYNC getUpdate", "malformed url error", mue);
        }
        try {
            urlOff = new URL(  MainActivity.this.switches.get(position).get(2) );
        } catch (MalformedURLException mue) {
            ;//Log.e("SYNC getUpdate", "malformed url error", mue);
        }
   }

    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            NetworkInfo.State connected = info.getState();
            if (connected == NetworkInfo.State.CONNECTED) {
                MainActivity.this.wifiGoesOn();
            } else if (connected == NetworkInfo.State.DISCONNECTED) {
                MainActivity.this.wifiGoesOff();
            }
        }
    };
  }
