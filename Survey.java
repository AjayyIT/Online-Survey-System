import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

class Survey {
    private String title;
    private ArrayList<Question> questions;

    public Survey(String title) {
        this.title = title;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(String question, ArrayList<String> options) {
        questions.add(new Question(question, options));
    }

    public void removeQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            questions.remove(index);
        }
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}

class Question {
    private String questionText;
    private ArrayList<String> options;

    public Question(String questionText, ArrayList<String> options) {
        this.questionText = questionText;
        this.options = options;
    }

    public String getQuestionText() {
        return questionText;
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}

class SurveyManager extends Frame implements ActionListener {
    private ArrayList<Survey> surveys;
    private List surveyList;
    private TextField surveyTitleField;
    private TextArea questionTextField;
    private TextField optionField;
    private List questionList;
    private List optionList;

    public SurveyManager() {
        surveys = new ArrayList<>();
        setupGUI();
    }

    private void setupGUI() {
        setLayout(new FlowLayout());

        Label surveyTitleLabel = new Label("Survey Title:");
        add(surveyTitleLabel);

        surveyTitleField = new TextField(20);
        add(surveyTitleField);

        Button createSurveyButton = new Button("Create Survey");
        createSurveyButton.addActionListener(this);
        add(createSurveyButton);

        surveyList = new List();
        add(surveyList);

        Button deleteSurveyButton = new Button("Delete Survey");
        deleteSurveyButton.addActionListener(this);
        add(deleteSurveyButton);

        Label questionLabel = new Label("Question:");
        add(questionLabel);

        questionTextField = new TextArea(2, 20);
        add(questionTextField);

        Label optionLabel = new Label("Option:");
        add(optionLabel);

        optionField = new TextField(20);
        add(optionField);

        Button addOptionButton = new Button("Add Option");
        addOptionButton.addActionListener(this);
        add(addOptionButton);

        optionList = new List();
        add(optionList);

        Button addQuestionButton = new Button("Add Question");
        addQuestionButton.addActionListener(this);
        add(addQuestionButton);

        questionList = new List();
        add(questionList);

        Button takeSurveyButton = new Button("Take Survey");
        takeSurveyButton.addActionListener(this);
        add(takeSurveyButton);

        setSize(400, 500);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        String surveyTitle = surveyTitleField.getText();
        Survey survey = getSurvey(surveyTitle);

        if (command.equals("Create Survey")) {
            surveys.add(new Survey(surveyTitle));
            surveyList.add(surveyTitle);
        } else if (command.equals("Delete Survey")) {
            if (survey != null) {
                surveys.remove(survey);
                surveyList.remove(surveyTitle);
            }
        } else if (command.equals("Add Option")) {
            String optionText = optionField.getText();
            optionList.add(optionText);
            optionField.setText("");
        } else if (command.equals("Add Question")) {
            if (survey != null) {
                ArrayList<String> options = new ArrayList<>();
                for (String option : optionList.getItems()) {
                    options.add(option);
                }
                survey.addQuestion(questionTextField.getText(), options);
                questionList.add(questionTextField.getText());
                questionTextField.setText("");
                optionList.removeAll();
            }
        } else if (command.equals("Take Survey")) {
            if (survey != null) {
                takeSurvey(survey);
            }
        }
    }

    private Survey getSurvey(String title) {
        for (Survey survey : surveys) {
            if (survey.getTitle().equals(title)) {
                return survey;
            }
        }
        return null;
    }

    private void takeSurvey(Survey survey) {
        Frame surveyFrame = new Frame("Taking Survey: " + survey.getTitle());
        surveyFrame.setLayout(new FlowLayout());

        for (Question question : survey.getQuestions()) {
            Label questionLabel = new Label(question.getQuestionText());
            surveyFrame.add(questionLabel);

            CheckboxGroup optionsGroup = new CheckboxGroup();
            for (String option : question.getOptions()) {
                Checkbox optionCheckbox = new Checkbox(option, optionsGroup, false);
                surveyFrame.add(optionCheckbox);
            }
        }

        surveyFrame.setSize(400, 500);
        surveyFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new SurveyManager();
    }
}
