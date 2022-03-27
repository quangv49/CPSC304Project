package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DivisionPanel extends JPanel {
    JButton submit;
    JComboBox<String> relationComboBox;

    private JTextField waterIdField;
    private JTextField dateAuthorizedField;
    ResultTablePanel resultDisplay;
    private JPanel options;
    private JLabel changingText1;
    private JLabel changingText2;
    private JLabel find;

    //Division: MonoUser, ProblemPlant
    public DivisionPanel(String[] relations, DBHandler dbh) {
        options = new JPanel();
        options.setLayout(new GridLayout(1, 15));
        options.setPreferredSize(new Dimension(50, 30));
        relationComboBox = new JComboBox<>(relations);
        waterIdField = new JTextField();
        dateAuthorizedField = new JTextField();
        relationComboBox.setBounds(500, 500, 100, 20);
        changingText1 = new JLabel();
        changingText1.setBounds(100, 300, 100, 20);
        changingText2 = new JLabel();
        changingText2.setBounds(100, 300, 100, 20);
        find = new JLabel("Find: ");

        this.resultDisplay = new ResultTablePanel(new QueryResult(new String[] {"Select a query up top!"},
                new Object[][] {{"Divide away!"}}));


        submit = new JButton("Submit");
        DivisionAction actionListener = new DivisionAction(this);
        submit.addActionListener(actionListener);

        setLayout(new BorderLayout());
        options.add(find);
        options.add(relationComboBox);

        relationComboBox.addActionListener(actionListener);

        options.add(submit);
        add(options, BorderLayout.PAGE_START);
        add(resultDisplay, BorderLayout.CENTER);
        revalidate();
        repaint();

    }

    public JComboBox<String> getRelationComboBox() {
        return this.relationComboBox;
    }

    public JPanel getOptions() {
        return this.options;
    }

    public void setChangingText1(String incomingText) {
        changingText1.setText(incomingText);
        options.add(changingText1);
        this.options.revalidate();
        this.options.repaint();
    }

    public void setChangingText2(String incomingText) {
        changingText2.setText(incomingText);
        options.add(changingText2);
        this.options.revalidate();
        this.options.repaint();
    }

    public void addDateField() {
        options.add(dateAuthorizedField);
    }

    public void addWaterIDField() {
        options.add(waterIdField);
    }

    public void addSubmitButton() {
        options.add(submit);
        this.options.revalidate();
        this.options.repaint();
    }

    public void removeDateField() {
        options.remove(dateAuthorizedField);
        this.options.revalidate();
        this.options.repaint();
    }

    public void removeWaterIDField() {
        options.remove(waterIdField);
        this.options.revalidate();
        this.options.repaint();
    }

    public void removeChangingText(int i) {
        if (i == 1) {
            options.remove(changingText1);
        } else if (i == 2) {
            options.remove(changingText2);
        } else if (i == 3) {
            options.remove(changingText1);
            options.remove(changingText2);
        }

        this.options.revalidate();
        this.options.repaint();
    }

    public void removeSubmitButton() {
        options.remove(submit);
        this.options.revalidate();
        this.options.repaint();
    }

}

class DivisionAction implements ActionListener{
    private DivisionPanel myDivisionPanel;

    public DivisionAction(DivisionPanel divisionPanel) {
        this.myDivisionPanel = divisionPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};

        Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", new Integer(5), new Boolean(false)},
                {"John", "Doe",
                        "Rowing", new Integer(3), new Boolean(true)},
                {"Sue", "Black",
                        "Knitting", new Integer(2), new Boolean(false)},
                {"Jane", "White",
                        "Speed reading", new Integer(20), new Boolean(true)},
                {"Joe", "Brown",
                        "Pool", new Integer(10), new Boolean(false)}
        };

        DBHandler dbHandler = new DBHandler();
        String relation = (String) myDivisionPanel.relationComboBox.getSelectedItem();

        switch(e.getActionCommand()){
            case "comboBoxChanged": {
                System.out.println(myDivisionPanel.getRelationComboBox().getSelectedItem());

                if (relation.equals("Fully-licensed users drawing from")) {
                    // MonoUser: Returns the user that holds all licenses drawing from a water source, given dateAuthorized and waterID

                    myDivisionPanel.setChangingText1("date authorized: ");
                    myDivisionPanel.addDateField();
                    myDivisionPanel.setChangingText2("water ID: ");
                    myDivisionPanel.addWaterIDField();
                } else if (relation.equals("Sewage plant handling all locations with >1 user doesn't have clean water")){
                    //ProblemPlant: Returns sewage plants that handle all locations with at least one user with bad water.
                    myDivisionPanel.removeDateField();
                    myDivisionPanel.removeWaterIDField();
                    myDivisionPanel.removeChangingText(3);
                    myDivisionPanel.removeSubmitButton();
                    myDivisionPanel.addSubmitButton();

                }
                myDivisionPanel.getOptions().revalidate();
                myDivisionPanel.getOptions().repaint();
                break;

            }
            case "Submit":{
                // get query result

                myDivisionPanel.resultDisplay.setQueryResult(new QueryResult(columnNames, data));
                System.out.println("division: pressed Submit");
                break;
            }
        }
    }

}

