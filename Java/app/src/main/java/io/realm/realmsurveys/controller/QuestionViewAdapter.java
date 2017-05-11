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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.realmsurveys.R;
import io.realm.realmsurveys.model.Question;

public class QuestionViewAdapter extends RealmRecyclerViewAdapter<Question, QuestionViewAdapter.ViewHolder> {

    public interface SurveyResponseDelegate {
        void onQuestionAnswered(String questionId, boolean response);
    }

    private SurveyResponseDelegate surveyResponseDelegate;

    public QuestionViewAdapter(@NonNull SurveyResponseDelegate surveyResponseDelegate, @Nullable OrderedRealmCollection<Question> data, boolean autoUpdate) {
        super(data, autoUpdate);
        this.surveyResponseDelegate = surveyResponseDelegate;
     }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Question question = getData().get(position);

        holder.questionText.setText(question.getQuestionText());
        holder.yesButton.setEnabled(true);
        holder.noButton.setEnabled(true);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v == holder.yesButton) {
                    holder.yesButton.setEnabled(false);
                    holder.noButton.setEnabled(true);
                } else if (v == holder.noButton) {
                    holder.noButton.setEnabled(false);
                    holder.yesButton.setEnabled(true);
                }

                surveyResponseDelegate.onQuestionAnswered(question.getQuestionId(), v == holder.yesButton);
            }
        };

        holder.yesButton.setOnClickListener(clickListener);
        holder.noButton.setOnClickListener(clickListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView questionText;
        public final Button yesButton;
        public final Button noButton;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            questionText = (TextView) view.findViewById(R.id.questionText);
            yesButton = (Button) view.findViewById(R.id.yesBtn);
            noButton = (Button) view.findViewById(R.id.noBtn);
        }

    }

}
