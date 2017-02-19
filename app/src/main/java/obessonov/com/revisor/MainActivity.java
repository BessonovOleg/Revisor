package obessonov.com.revisor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import obessonov.com.revisor.Documents.DigitalInputActivity;
import obessonov.com.revisor.Documents.DocListActivity;
import obessonov.com.revisor.Documents.Document;
import obessonov.com.revisor.Setting.Setting;
import obessonov.com.revisor.Setting.SettingActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button   btnSetting;
    Button   btnDocuments;
    Button   btnParse;
    Button   btnDDD;
    Button   button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Настройки
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);

        //Документы
        btnDocuments = (Button) findViewById(R.id.btnDocuments);
        btnDocuments.setOnClickListener(this);

        //Парсинг
        btnParse = (Button) findViewById(R.id.btnParse);
        btnParse.setOnClickListener(this);

        //
        btnDDD = (Button) findViewById(R.id.btnDDD);
        btnDDD.setOnClickListener(this);

/*
       edtest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                DAO dao       = new DAO(getBaseContext());
                String code   = edtest.getText().toString();
                Entity entity = dao.getEntityByCode(code);
                button2.setText(entity.getEnt_name());

                //edtest.requestFocus();
                return true;
                //return false;
            }
        });
*/

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSetting:
                showSetting();
                break;
            case R.id.btnDocuments:
                showDocuments();
                break;
            case R.id.btnParse:
                //test2();
                test3();
                break;
            case R.id.btnDDD:
                dddClick();
                break;
            case R.id.button2:
                test2();
                break;
        }
    }

    public void dddClick(){
        Intent inten = new Intent(this, Document.class);
        startActivity(inten);
    }

    public void test3(){
        Intent intent = new Intent(this, DigitalInputActivity.class);
        startActivity(intent);
    }


    public void showDocuments(){
        Intent intent = new Intent(this,DocListActivity.class);
        startActivity(intent);
    }



    public void showSetting() {
        Intent intentSetting = new Intent(this,SettingActivity.class);
        startActivity(intentSetting);
    }


    public void test2() {
        TaskFileDownload mt = new TaskFileDownload();
        mt.execute();
    }

    class TaskFileDownload extends AsyncTask<Void,Void,Void> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Обмен");
            pd.setMessage("Загрузка файла с FTP");
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.setIndeterminate(false);
            ParsingFileTask pst = new ParsingFileTask();
            pst.execute(pd);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            FtpFactory f = new FtpFactory(getApplicationContext());
            Setting setting = new Setting(getApplicationContext());
            try {
                f.ftpConnect(setting.getFtpServerName(),setting.getFtpServerLogin(),setting.getFtpServerPassword(),21);
                f.ftpChangeDirectory(setting.getFtpDirectory());
                f.ftpDownload("entity.csv","entity.csv");
                f.ftpDownload("agents.csv","agents.csv");
            }finally {
                f.ftpDisconnect();
            }
            return null;
        }
    }

    public int getCountRowsInFile(String fileName) {
        int i = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(fileName), "Cp1251"));
            while (br.readLine() != null) {
                i++;
            }
            br.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return i;
    }



    class ParsingFileTask extends AsyncTask<ProgressDialog,Integer,Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0] == 0) {
                int countRowInFile;
                    countRowInFile = getCountRowsInFile("entity.csv");
                pd.setMax(countRowInFile);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMessage("Загрузка товаров");
            }

            if(values[0] == 1) {
                int countRowInFile;
                countRowInFile = getCountRowsInFile("agents.csv");
                pd.setMax(countRowInFile);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMessage("Загрузка складов");
            }

            if(values[0] == 2) {
                pd.incrementProgressBy(1);
            }
        }

        @Override
        protected Void doInBackground(ProgressDialog... params) {
            pd = params[0];
            publishProgress(0);
            DAO dao = new DAO(MainActivity.this);

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("entity.csv"), "Cp1251"));
                String str = "";

                while ((str = br.readLine()) != null) {
                    dao.insertEntity(str);
                    publishProgress(2);
                }
                br.close();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            publishProgress(1);

            try {
                BufferedReader brAg = new BufferedReader(new InputStreamReader(openFileInput("agents.csv"), "Cp1251"));
                String strAgent = "";

                while ((strAgent = brAg.readLine()) != null) {
                    dao.insertAgent(strAgent);
                    publishProgress(2);
                }
                brAg.close();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}
