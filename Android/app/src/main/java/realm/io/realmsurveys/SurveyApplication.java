package realm.io.realmsurveys;

import android.app.Application;

import io.realm.Realm;

public class SurveyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefsUtils.init(this);
        Realm.init(this);
    }
}
