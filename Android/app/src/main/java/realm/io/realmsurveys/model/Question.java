package realm.io.realmsurveys.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Question extends RealmObject {

    private Date timestamp;

    @Required
    private String questionText;

    private RealmList<Answer> answers;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public RealmList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(RealmList<Answer> answers) {
        this.answers = answers;
    }
}
