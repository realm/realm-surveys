package realm.io.realmsurveys.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Date;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import realm.io.realmsurveys.BuildConfig;
import realm.io.realmsurveys.R;
import realm.io.realmsurveys.model.Question;

public class SurveyActivity extends AppCompatActivity {

    public static final String REALM_URL = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/survey";
    public static final String AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";
    public static final String ID = "default@realm";
    public static final String PASSWORD = "password";

    public RecyclerView recyclerView;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        initRealm();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm != null) {
            realm.removeAllChangeListeners();
            realm.close();
            realm = null;
        }
    }

    private void postLogin() {
        createDummyDataIfNeeded();
        // hide progress indicator.
        initRecyclerView();
    }

    private void initRecyclerView() {

        RealmResults<Question> questions = realm
                .where(Question.class)
                .findAllSortedAsync("timestamp", Sort.DESCENDING);

        recyclerView.setAdapter(new QuestionViewAdapter(this, questions));

    }

    public void createDummyDataIfNeeded() {
        boolean isDatabaseEmpty = realm.where(Question.class).count() == 0;

        final String[] dummyQuestions = new String[] {
                "Are you currently an Android Developer?",
                "Do you like chocolate icecream?"
        };
        if(isDatabaseEmpty) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm txRealm) {
                    for(String q : dummyQuestions) {
                        Question question = new Question();
                        question.setTimestamp(new Date());
                        question.setQuestionText(q);
                        realm.copyToRealm(question);
                    }
                }
            });
        }
    }

    private void initRealm() {

        SyncUser syncUser = SyncUser.currentUser();
        if(syncUser != null)  {
            syncUser.logout();
        }
        final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);
        SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL).build();
                Realm.setDefaultConfiguration(syncConfiguration);
                realm = Realm.getDefaultInstance();
                postLogin();
            }

            @Override
            public void onError(ObjectServerError error) {
                logError(error);
            }
        });

    }

    private void logError(Throwable e) {
        Log.e("SurveyActivity", e.getMessage());
    }

}
