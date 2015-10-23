package com.timore.zipfilesoperations;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        final String zipfilePath = Environment.getExternalStorageDirectory() + "/aquery/gma/floor1.zip";


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://Www.ensignagency.com/floor.zip";
                downloadFile(url, zipfilePath);
            }
        });
    }

    void notification(String title, String content) {

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(this);
        nbuilder.setContentTitle("Donwloading Kitchen");
        nbuilder.setContentText("**");
        nbuilder.setSmallIcon(R.drawable.ic_launcher);
        nbuilder.setProgress(50, 0, true);
        Notification notif = nbuilder.build();
        nm.notify(0, notif);

    }

    public void downloadFile(String url, final String path) {

        final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(this);

        nbuilder.setContentTitle("Gemma Kitchen");
        nbuilder.setContentText("downoading...");
        nbuilder.setSmallIcon(R.drawable.ic_launcher);
        nbuilder.setProgress(100, 0, true);
        nm.notify(0, nbuilder.build());

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Download...");
        if (!new File(path).isFile()) {
            System.err.println("%%% FILE WILL DONWLOAD");

            new AQuery(MainActivity.this).progress(dialog).download(url, new File(path), new AjaxCallback<File>() {
                @Override
                public void callback(String url, File object, AjaxStatus status) {
                    super.callback(url, object, status);
                    nbuilder.setContentText("Unzipping files...");
                    nm.notify(0, nbuilder.build());
                    unzip(path, nm, nbuilder);
                }
            });
        } else {
            System.err.println("%%% FILE WAS DONWLOADED");

            nbuilder.setContentText("Unzipping files...");
            nm.notify(0, nbuilder.build());
            System.err.println("CHECKING PATH IS"+new File(path.replace(".zip", "")).getAbsolutePath());
            if (!new File(path.replace(".zip", "")).isDirectory()) {
                System.err.println("%%% FILE WILL BE EXTRACT");

                unzip(path, nm, nbuilder);
            } else {
                new AQuery(MainActivity.this).id(imageView).image(path.replace(".zip", "/") + "floor/floor1");

                System.err.println("%%% FILE WAS EXTRACTED");
            }


        }

    }

    private void unzip(final String zpath, final NotificationManager nm, final NotificationCompat.Builder nbuilder) {
        System.err.println("START TIME = "+System.currentTimeMillis()/1000);

        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            ZipInputStream zin;

            @Override
            protected Void doInBackground(String... params) {
                try {
                    File f = new File(zpath);
                    if (!f.isDirectory()) {
                        f.mkdirs();
                    }
                    ZipInputStream zin = new ZipInputStream(new FileInputStream(f));
                    ZipFile.unzip(zpath, zpath.replace(".zip", "/"), zin);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                nbuilder.setContentText("kitchen is ready now");
                nbuilder.setProgress(0, 0, true);
                nm.notify(0, nbuilder.build());
                System.err.println("END TIME = " + System.currentTimeMillis() / 1000);
//                try {
//                    zin.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                new AQuery(MainActivity.this).id(imageView).image(zpath.replace(".zip", "/") + "floor/floor1");
            }
        }.execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
