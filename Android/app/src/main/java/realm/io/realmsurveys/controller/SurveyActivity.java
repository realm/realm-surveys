package realm.io.realmsurveys.controller;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import realm.io.realmsurveys.BuildConfig;
import realm.io.realmsurveys.R;
import realm.io.realmsurveys.model.Answer;
import realm.io.realmsurveys.model.Question;

public class SurveyActivity extends AppCompatActivity  {

    public static final String REALM_URL = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/survey";
    public static final String AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";
    public static final String ID = "survey@demo.io";
    public static final String PASSWORD = "password";

    @BindView(R.id.questionsList) public RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
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

    private void postLogin() {

        RealmResults<Question> questions = realm
                .where(Question.class)
                .isEmpty("answers")
                .or()
                .beginGroup()
                .not()
                .contains("answers.userId", uniqueUserId())
                .endGroup()
                .findAllSortedAsync("timestamp");

        recyclerView.setAdapter(new QuestionViewAdapter(this, questions, true));

    }

    private void logError(Throwable e) {
        Log.e("SurveyActivity", e.getMessage());
    }

    public void onQuestionAnswered(final String questionId, final boolean response) {
        final String deviceUserId = uniqueUserId();
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        Question question = bgRealm.where(Question.class).equalTo("questionId", questionId).findFirst();
                        if(question != null) {
                            Answer answer = question.getAnswers().where()
                                    .equalTo("userId",deviceUserId)
                                    .findFirst();
                            if(answer == null) {
                                answer = bgRealm.createObject(Answer.class);
                                answer.setUserId(deviceUserId);
                                answer.setQuestion(question);
                            }
                            answer.setResponse(response);
                            question.getAnswers().add(answer);
                        }
                    }
                });
            }
        }, 200);
    }

    private String uniqueUserId() {

        if(sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        }

        String idForCurrentUser = sharedPreferences.getString(BuildConfig.APPLICATION_ID, null);
        if (idForCurrentUser == null) {
            idForCurrentUser = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(BuildConfig.APPLICATION_ID, idForCurrentUser).apply();
        }

        return idForCurrentUser;
    }

}
