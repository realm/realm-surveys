package realm.io.realmsurveys.controller;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import realm.io.realmsurveys.R;
import realm.io.realmsurveys.model.Question;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = SplashActivity.class.getName();

    public static final String HOST = "192.168.1.162"; // <-- Enter your Realm Object Server IP here
    public static final String REALM_URL = "realm://" + HOST + ":9080/~/survey";
    public static final String SERVER_URL = "http://" + HOST + ":9080";
    public static final String ID = "survey@demo.io";
    public static final String PASSWORD = "password";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        login();
    }

    private void login() {

        final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);

        SyncUser.loginAsync(syncCredentials, SERVER_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) { postLogin(user); }

            @Override
            public void onError(ObjectServerError error) {
                logError(error);
            }
        });

    }

    private void postLogin(SyncUser user) {
        setRealmDefaultConfig(user);
        goTo(SurveyActivity.class);
    }


    private void setRealmDefaultConfig(SyncUser user) {
        Log.d(TAG, "Connecting to Sync Server at : ["  + REALM_URL.replaceAll("~", user.getIdentity()) + "]");
        final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL).schemaVersion(4).build();
        Realm.setDefaultConfiguration(syncConfiguration);
    }

    private void goTo(Class<? extends AppCompatActivity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
    }

    private void logError(Throwable e) {
        Log.e(TAG, "Realm Error", e);
    }

}
