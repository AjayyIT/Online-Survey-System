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

    public String getTitle() {
        return title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
    
    @Override
    public String toString() {
        return title; // Helps the JList display the title automatically
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

public class SurveyManager extends JFrame implements ActionListener {
    private ArrayList<Survey> surveys;
    
    // UI Components
    private DefaultListModel<Survey> surveyListModel;
    private JList<Survey> surveyList;
    private JTextField surveyTitleField;
    
    private JTextArea questionTextField;
    private JTextField optionField;
    
    private DefaultListModel<String> optionListModel;
    private JList<String> optionList;

    public SurveyManager() {
        surveys = new ArrayList<>();
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Survey Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fixes the "X" button issue
        setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL: Survey Creation ---
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Survey Title:"));
        surveyTitleField = new JTextField(15);
        topPanel.add(surveyTitleField);
        
        JButton createSurveyButton = new JButton("Create Survey");
        createSurveyButton.addActionListener(this);
        topPanel.add(createSurveyButton);
        
        JButton deleteSurveyButton = new JButton("Delete Selected Survey");
        deleteSurveyButton.addActionListener(this);
        topPanel.add(deleteSurveyButton);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL: Lists and Question Creation ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Left side: Survey List
        surveyListModel = new DefaultListModel<>();
        surveyList = new JList<>(surveyListModel);
        centerPanel.add(new JScrollPane(surveyList));

        // Right side: Question Builder
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        
        questionPanel.add(new JLabel("Question:"));
        questionTextField = new JTextArea(3, 20);
        questionPanel.add(new JScrollPane(questionTextField));
        
        questionPanel.add(new JLabel("Option:"));
        optionField = new JTextField(20);
        questionPanel.add(optionField);
        
        JButton addOptionButton = new JButton("Add Option");
        addOptionButton.addActionListener(this);
        questionPanel.add(addOptionButton);
        
        optionListModel = new DefaultListModel<>();
        optionList = new JList<>(optionListModel);
        questionPanel.add(new JScrollPane(optionList));
        
        JButton addQuestionButton = new JButton("Add Question to Selected Survey");
        addQuestionButton.addActionListener(this);
        questionPanel.add(addQuestionButton);

        centerPanel.add(questionPanel);
        add(centerPanel, BorderLayout.CENTER);

        // --- BOTTOM PANEL: Actions ---
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton takeSurveyButton = new JButton("Take Selected Survey");
        takeSurveyButton.addActionListener(this);
        bottomPanel.add(takeSurveyButton);
        
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(700, 500);
        setLocationRelativeTo(null); // Centers the window
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        Survey selectedSurvey = surveyList.getSelectedValue();

        if (command.equals("Create Survey")) {
            String title = surveyTitleField.getText().trim();
            if (!title.isEmpty()) {
                Survey newSurvey = new Survey(title);
                surveys.add(newSurvey);
                surveyListModel.addElement(newSurvey);
                surveyTitleField.setText("");
            }
        } 
        else if (command.equals("Delete Selected Survey")) {
            if (selectedSurvey != null) {
                surveys.remove(selectedSurvey);
                surveyListModel.removeElement(selectedSurvey);
            }
        } 
        else if (command.equals("Add Option")) {
            String optionText = optionField.getText().trim();
            if (!optionText.isEmpty()) {
                optionListModel.addElement(optionText);
                optionField.setText("");
            }
        } 
        else if (command.equals("Add Question to Selected Survey")) {
            if (selectedSurvey != null && !questionTextField.getText().trim().isEmpty()) {
                ArrayList<String> options = new ArrayList<>();
                for (int i = 0; i < optionListModel.getSize(); i++) {
                    options.add(optionListModel.getElementAt(i));
                }
                
                selectedSurvey.addQuestion(questionTextField.getText(), options);
                
                // Clear fields after adding
                questionTextField.setText("");
                optionListModel.clear();
                JOptionPane.showMessageDialog(this, "Question added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a survey and enter a question.");
            }
        } 
        else if (command.equals("Take Selected Survey")) {
            if (selectedSurvey != null) {
                if (selectedSurvey.getQuestions().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "This survey has no questions yet.");
                } else {
                    takeSurvey(selectedSurvey);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a survey to take.");
            }
        }
    }

    private void takeSurvey(Survey survey) {
        JFrame surveyFrame = new JFrame("Taking Survey: " + survey.getTitle());
        surveyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only closes this window
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Question question : survey.getQuestions()) {
            panel.add(new JLabel(question.getQuestionText()));
            ButtonGroup optionsGroup = new ButtonGroup();
            
            for (String option : question.getOptions()) {
                JRadioButton optionRadio = new JRadioButton(option);
                optionsGroup.add(optionRadio);
                panel.add(optionRadio);
            }
            panel.add(Box.createRigidArea(new Dimension(0, 15))); // Adds spacing
        }
        
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(surveyFrame, "Survey submitted!");
            surveyFrame.dispose();
        });
        panel.add(submitButton);

        surveyFrame.add(new JScrollPane(panel));
        surveyFrame.setSize(400, 500);
        surveyFrame.setLocationRelativeTo(this);
        surveyFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread (Best practice for Swing)
        SwingUtilities.invokeLater(() -> new SurveyManager());
    }
}
