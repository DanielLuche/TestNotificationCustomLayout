package com.dluche.testnotificationcustomlayout;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import util.Utils;

public class MainActivity extends AppCompatActivity {

    public static final int NOTIFICATION_ID = 1;

    private String urlTeste = "http://dev.namoa.ftp.s3.amazonaws.com/APP/PRJ001/prj001-v2.4.0-development.apk";
    private String path = Environment
            .getExternalStorageDirectory().getPath() + "/TestNotification";
    private String localPath = path + "/" + "teste_downlaod_bar.apk";
    private ProgressBar mProgress;
    private Context context;
    private Button btn_test;
    private TextView tv_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniVars();

        iniActions();

    }

    private void iniVars() {

        context = this;

        mProgress = (ProgressBar) findViewById(R.id.main_progress);

        btn_test = (Button) findViewById(R.id.main_btn_test);

        tv_progress = (TextView) findViewById(R.id.main_tv_progress);

    }

    private void iniActions() {

        //Utils.createNotification(context,NOTIFICATION_ID,"teste_downlaod_bar",new Integer[]{100,5});

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new downloadTask().execute();
            }
        });
    }
    public void resetProgress(){
        mProgress.setMax(100);
        mProgress.setProgress(0);
        tv_progress.setText("0/100");
        //
        Utils.cancelNotification(context,NOTIFICATION_ID);
    }

    public class downloadTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            //mProgress.setMax(0);
            mProgress.setMax(100);
            mProgress.setProgress(0);
            resetProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {

            try {
                URL url = new URL(urlTeste);
                //
                URLConnection connection = null;

                connection = url.openConnection();
                Double fileSize = (double) connection.getContentLength();
                //int convetSize = (int) fileSize / 1024;
                //
                //Verifica se diretorio existe e não existir, cria.
                File pathDir = new File(path);
                if(!pathDir.exists()){
                    pathDir.mkdir();
                }
                //Verifica se arquivo existe
                //Se ja existir, deleta.
                File file = new File(localPath);
                if (file.exists()) {
                    file.delete();
                }
                //
                connection.setReadTimeout(60000);
                connection.setConnectTimeout(60000);
                //
                FileOutputStream outputStream = new FileOutputStream(localPath, true);
                //
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                byte[] data = new byte[1024];
                //
                int n;
                double progression = 0;
                double temp = 0;
                //
                while ((n = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, n);

                    temp += n ;
                    //progression += temp;
                    double perc = (temp * 100 /  fileSize);
                    //
                    if( Math.round(perc) >= 5 ){
                        progression += perc;
                        temp = 0 ;
                        //publishProgress(convetSize,progression);
                        publishProgress(100,(int)progression);
                    }

                    /*int temp = (int) n /1024;

                    progression += temp;
                    publishProgress(convetSize,progression);*/
                }
                //publishProgress(convetSize,convetSize);
                publishProgress(100,100);
                //
                outputStream.flush();
                outputStream.close();
                //
                inputStream.close();

                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            //super.onProgressUpdate(progress);
            if(mProgress.getMax() == 0){
                mProgress.setMax(progress[0]);
            }
            //
            mProgress.setProgress(progress[1]);
            tv_progress.setText(String.valueOf(progress[1]) + "/" + String.valueOf(progress[0]) );
            //
            Utils.createNotification(context,NOTIFICATION_ID,"teste_downlaod_bar",Utils.NOTIFICATION_TYPE_IN_PROGRESS,progress);

        }

        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(integer);
            Toast.makeText(context, "Download Finalizado", Toast.LENGTH_SHORT).show();
            //
            Utils.createNotification(context,NOTIFICATION_ID,"teste_downlaod_bar",Utils.NOTIFICATION_TYPE_DONE,new Integer[]{});
        }
    }


}
