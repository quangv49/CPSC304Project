package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

class SelectProjectPanel extends JPanel {
    JButton submit;
    private JComboBox<String> relationComboBox; //box to choose relation
    private JComboBox<String> chooseFields; // box to choose fields to project from relation
    private JTextField condition;
    ResultTablePanel resultDisplay;
    private JPanel options;
    private DBHandler dbh;

    public SelectProjectPanel(String[] relations, DBHandler dbh){
        this.dbh = dbh;
        options = new JPanel();
        options.setLayout(new GridLayout(1, 15));
        options.setPreferredSize(new Dimension(150, 30));
        relationComboBox = new JComboBox<>(relations);
        chooseFields = new JComboBox<>();
        condition = new JTextField();
        relationComboBox.setBounds(80, 50, 140, 20);
        JLabel tableHeader = new JLabel("Choose relation");
        JLabel fieldHeader = new JLabel("Choose field");
        JLabel condHeader = new JLabel("Type in condition");
        this.resultDisplay = new ResultTablePanel(new QueryResult(new String[] {"Select a query up top!"},
                new Object[][] {{"Your choices are limited though"}}));


        submit = new JButton("Submit"); // action = select, project, join, etc
        SelectProjectAction actionListener = new SelectProjectAction(this);

        submit.addActionListener(actionListener);
        relationComboBox.addActionListener(actionListener);

        setLayout(new BorderLayout());
        // the 5 elements in one row
        options.add(relationComboBox);
        options.add(tableHeader);

        options.add(chooseFields);
        options.add(fieldHeader);

        options.add(condition);
        options.add(condHeader);

        options.add(submit);
        add(options, BorderLayout.PAGE_START);
        add(resultDisplay, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public ResultTablePanel getResultDisplay(){
        return this.resultDisplay;
    }

    public JComboBox<String> getChooseFields() {
        return this.chooseFields;
    }

    public JComboBox<String> getRelationComboBox(){return this.relationComboBox;};

    public JPanel getOptions(){return this.options;};

    public JTextField getCondition() {
        return condition;
    }

    public DBHandler getHandler() {return dbh;}


}

class SelectProjectAction implements ActionListener{
    private SelectProjectPanel myPanel;

    public SelectProjectAction(SelectProjectPanel mainPanel){
        this.myPanel = mainPanel;
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

        String relation = (String) myPanel.getRelationComboBox().getSelectedItem();
        ResultSetMetaData meta = myPanel.getHandler().getRelationInfo(relation);
        try {
            switch (e.getActionCommand()) { // add more cases for each button action
                case "comboBoxChanged": {
                    int colNum = meta.getColumnCount();
                    String[] relationFields = new String[colNum]; // get all fields selectable by this relation

                    for (int i = 1; i <= colNum; i++) relationFields[i-1] = meta.getColumnName(i);

                    myPanel.getChooseFields().setModel(new DefaultComboBoxModel(relationFields));
                    myPanel.getOptions().revalidate();
                    myPanel.getOptions().repaint();
                    break;
                }
                case "Submit": {
                    String condition = myPanel.getCondition().getText();

                    if (condition.equals(""))
                        myPanel.resultDisplay.setQueryResult(myPanel.getHandler().project((String) myPanel.getChooseFields().getSelectedItem(),
                                (String) myPanel.getRelationComboBox().getSelectedItem()));
                    else
                        myPanel.resultDisplay.setQueryResult(myPanel.getHandler().select((String) myPanel.getChooseFields().getSelectedItem(),
                                (String) myPanel.getRelationComboBox().getSelectedItem(), condition));

                    break;
                }

            }
            myPanel.repaint();
        } catch (SQLException exc) {
            System.out.println(exc.getMessage());
        }
    }
}
