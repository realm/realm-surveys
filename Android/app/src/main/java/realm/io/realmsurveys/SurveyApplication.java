package realm.io.realmsurveys;

import android.app.Application;

import io.realm.Realm;

public class SurveyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        SharedPrefsUtils.init(this);
    }
}
