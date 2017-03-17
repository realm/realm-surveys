/*
 * Copyright 2017 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package realm.io.realmsurveys.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.UUID;

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

    public static final String TAG = SurveyActivity.class.getName();

    public static final String REALM_URL = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/survey";
    public static final String AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";
    public static final String ID = "survey@demo.io";
    public static final String PASSWORD = "password";

    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        recyclerView = (RecyclerView) findViewById(R.id.questionsList);
        login();
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

    private void login() {

        SyncUser user = SyncUser.currentUser();
        if(user != null) {
            postLogin(user);

        } else {

            final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);

            SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) { postLogin(user); }

                @Override
                public void onError(ObjectServerError error) {
                    logError(error);
                }
            });

        }
    }

    private void postLogin(SyncUser user) {

        setRealmDefaultConfig(user);

        realm = Realm.getDefaultInstance();

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

    private void setRealmDefaultConfig(SyncUser user) {
        Log.d(TAG, "Connecting to Sync Server at : ["  + REALM_URL.replaceAll("~", user.getIdentity()) + "]");
        final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL).schemaVersion(1).build();
        Realm.setDefaultConfiguration(syncConfiguration);
    }

    private void logError(Throwable e) {
        Log.e(TAG, "Realm Error", e);
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
        }, 200); // delayed for 200ms to show the user their selection for 200ms
                 // before it is answered and disappears from the list.
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
