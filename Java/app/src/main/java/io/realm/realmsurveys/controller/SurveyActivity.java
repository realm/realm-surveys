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

package io.realm.realmsurveys.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.realmsurveys.BuildConfig;
import io.realm.realmsurveys.R;
import io.realm.realmsurveys.model.Answer;
import io.realm.realmsurveys.model.Question;

public class SurveyActivity extends AppCompatActivity implements QuestionViewAdapter.SurveyResponseDelegate {

    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        recyclerView = (RecyclerView) findViewById(R.id.questionsList);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        realm.close();
    }

    @Override
    public void onQuestionAnswered(final String questionId, final boolean response) {
        final String deviceUserId = uniqueUserId();
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
                        answer.setResponse(response);
                        question.getAnswers().add(answer);
                    }
                }
            }
        });
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
