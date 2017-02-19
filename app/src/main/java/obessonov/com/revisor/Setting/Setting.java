package obessonov.com.revisor.Setting;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Setting {

    SharedPreferences sPf;

    private String ftpServerName;
    private String ftpServerLogin;
    private String ftpServerPassword;
    private String ftpDirectory;

    private static final String PREF_FTP_SERVER_NAME     = "ftp_server_name";
    private static final String PREF_FTP_SERVER_LOGIN    = "ftp_server_login";
    private static final String PREF_FTP_SERVER_PASSWORD = "ftp_server_password";
    private static final String PREF_FTP_DIRECTORY       = "ftp_directory";

    public void saveSetting() {
        SharedPreferences.Editor ed = sPf.edit();
        ed.putString(PREF_FTP_SERVER_NAME,ftpServerName);
        ed.putString(PREF_FTP_SERVER_LOGIN,ftpServerLogin);
        ed.putString(PREF_FTP_SERVER_PASSWORD,ftpServerPassword);
        ed.putString(PREF_FTP_DIRECTORY,ftpDirectory);
        ed.commit();
    }

    public Setting(Context ctx) {
        sPf = PreferenceManager.getDefaultSharedPreferences(ctx);
        loadData();
    }

    private void loadData() {
        ftpServerName     = sPf.getString(PREF_FTP_SERVER_NAME, "");
        ftpServerLogin    = sPf.getString(PREF_FTP_SERVER_LOGIN,"");
        ftpServerPassword = sPf.getString(PREF_FTP_SERVER_PASSWORD,"");
        ftpDirectory      = sPf.getString(PREF_FTP_DIRECTORY,"");
    }

    public String getFtpServerName() {
        return ftpServerName;
    }

    public String getFtpServerLogin() {
        return ftpServerLogin;
    }

    public String getFtpServerPassword() {
        return ftpServerPassword;
    }

    public void setFtpServerName(String ftpServerName) {
        this.ftpServerName = ftpServerName;
    }

    public void setFtpServerLogin(String ftpServerLogin) {
        this.ftpServerLogin = ftpServerLogin;
    }

    public void setFtpServerPassword(String ftpServerPassword) {
        this.ftpServerPassword = ftpServerPassword;
    }

    public void setFtpDirectory(String ftpDirectory) {
        this.ftpDirectory = ftpDirectory;
    }

    public String getFtpDirectory() {
        return ftpDirectory;
    }
}
