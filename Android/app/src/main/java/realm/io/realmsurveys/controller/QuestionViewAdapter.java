package realm.io.realmsurveys.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.UUID;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import realm.io.realmsurveys.R;
import realm.io.realmsurveys.SharedPrefsUtils;
import realm.io.realmsurveys.model.Question;

public class QuestionViewAdapter extends RealmRecyclerViewAdapter<Question, QuestionViewAdapter.ViewHolder> {

    private SurveyResponseHandler surveyResponseHandler;

    public QuestionViewAdapter(@NonNull SurveyActivity surveyActivity, @Nullable OrderedRealmCollection<Question> data, boolean autoUpdate) {
        super(surveyActivity, data, autoUpdate);
        surveyResponseHandler = surveyActivity;

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

                surveyResponseHandler.onQuestionAnswered(question.getQuestionId(), v == holder.yesButton);
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
