package realm.io.realmsurveys.controller;

public interface SurveyResponseHandler {
    void onQuestionAnswered(String questionId, boolean response);
}
