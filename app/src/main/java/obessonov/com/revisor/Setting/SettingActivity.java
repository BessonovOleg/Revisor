package obessonov.com.revisor.Setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import obessonov.com.revisor.R;
import obessonov.com.revisor.Setting.Setting;

public class SettingActivity extends AppCompatActivity {

    EditText edFtpServerName;
    EditText edFtpLogin;
    EditText edFtpPassword;
    EditText edFtpDirectory;
    Button btnSaveSetting;
    Setting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setting = new Setting(getApplicationContext());
        edFtpServerName = (EditText) findViewById(R.id.edFtpServerName);
        edFtpLogin      = (EditText) findViewById(R.id.edFtpLogin);
        edFtpPassword   = (EditText) findViewById(R.id.edFtpPassword);
        edFtpDirectory  = (EditText) findViewById(R.id.edFtpDirectory);

        btnSaveSetting  = (Button)   findViewById(R.id.btnSaveSetting);
        btnSaveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();
            }
        });

        loadSetting();
    }

    private void loadSetting(){
        edFtpServerName.setText(setting.getFtpServerName());
        edFtpLogin.setText(setting.getFtpServerLogin());
        edFtpPassword.setText(setting.getFtpServerPassword());
        edFtpDirectory.setText(setting.getFtpDirectory());
    }

    private void saveSetting() {
        setting.setFtpServerName(edFtpServerName.getText().toString());
        setting.setFtpServerLogin(edFtpLogin.getText().toString());
        setting.setFtpServerPassword(edFtpPassword.getText().toString());
        setting.setFtpDirectory(edFtpDirectory.getText().toString());
        setting.saveSetting();
        finish();
    }

}
