package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinPanel extends JPanel {
    JButton submit;
    JComboBox<String> relationComboBox;
    private JTextField waterIdField;
    ResultTablePanel resultDisplay;
    private JPanel options;
    private JLabel changingText1;
    private JLabel find;
    DBHandler dbh;

    //damInfo, sourceMeasurements
    public JoinPanel(String[] relations, DBHandler dbh) {
        this.dbh = dbh;
        options = new JPanel();
        options.setLayout(new GridLayout(1, 15));
        options.setPreferredSize(new Dimension(50, 30));
        relationComboBox = new JComboBox<>(relations);
        waterIdField = new JTextField();
        relationComboBox.setBounds(500, 500, 100, 20);
        changingText1 = new JLabel();
        changingText1.setBounds(100, 300, 100, 20);
        find = new JLabel("Find: ");

        this.resultDisplay = new ResultTablePanel(new QueryResult(new String[] {"Select a query up top!"},
                new Object[][] {{"Join away!"}}));


        submit = new JButton("Submit");
        JoinAction actionListener = new JoinAction(this);
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

    public void addWaterIDField() {
        options.add(waterIdField);
    }

    public void removeWaterIDField() {
        options.remove(waterIdField);
        this.options.revalidate();
        this.options.repaint();
    }

    public void removeChangingText() {
        options.remove(changingText1);
        this.options.revalidate();
        this.options.repaint();
    }

    public JTextField getWaterIdField() {return waterIdField;}

}

class JoinAction implements ActionListener{
    private JoinPanel myJoinPanel;

    public JoinAction(JoinPanel joinPanel) {
        this.myJoinPanel = joinPanel;
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
        String relation = (String) myJoinPanel.relationComboBox.getSelectedItem();

        switch(e.getActionCommand()){
            case "comboBoxChanged": {

                System.out.println(myJoinPanel.getRelationComboBox().getSelectedItem());

                if (relation.equals("River names upstream and downstream to each dam, and the dam names")) {
                    //damInfo: Return a list of names of dams and names of rivers upstream and downstream to each dam
                    myJoinPanel.removeChangingText();
                    myJoinPanel.removeWaterIDField();

                } else if (relation.equals("All Measurements by stations measuring water source: ")) {
                    //sourceMeasurements: Return all measurements made by stations measuring a particular water source
                    myJoinPanel.setChangingText1("water ID: ");
                    myJoinPanel.addWaterIDField();
                } else if (relation.equals("Licenses and water sources")) {
                    //damInfo: Return a list of names of dams and names of rivers upstream and downstream to each dam
                    myJoinPanel.removeChangingText();
                    myJoinPanel.removeWaterIDField();

                }
                myJoinPanel.getOptions().revalidate();
                myJoinPanel.getOptions().repaint();
                break;

            }
            case "Submit":{
                // get query result
                QueryResult result = null;
                switch ((String) myJoinPanel.relationComboBox.getSelectedItem()) {
                    case "River names upstream and downstream to each dam, and the dam names":
                        result = myJoinPanel.dbh.damInfo();
                        break;
                    case "All Measurements by stations measuring water source: ":
                        result = myJoinPanel.dbh.sourceMeasurements(myJoinPanel.getWaterIdField().getText());
                        break;
                    case "Licenses and water sources":
                        result = myJoinPanel.dbh.licenseSource();
                        break;
                }
                myJoinPanel.resultDisplay.setQueryResult(result);
                System.out.println("join: pressed Submit");
                break;
            }
        }
    }

}

