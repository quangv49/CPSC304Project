package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

class SelectProjectPanel extends JPanel {
    JButton submit;
    private JComboBox<String> relationComboBox; //box to choose relation
    private JComboBox<String> numFields;
    private ArrayList<JComboBox<String>> chooseFields; // box to choose fields to project from relation
    private ArrayList<JLabel> chooseFieldLabels;
    private JLabel[] permanentLabels = new JLabel[] {new JLabel("Choose relation"),
                                                    new JLabel("Choose number of fields")};
    private JTextField condition;
    ResultTablePanel resultDisplay;
    private JPanel options;
    private DBHandler dbh;
    private static final int MAX_COLS = 4;

    public SelectProjectPanel(String[] relations, DBHandler dbh){
        // Set handler
        this.dbh = dbh;

        // Prepare drop-down menus to be used later
        chooseFields = new ArrayList<>();
        chooseFieldLabels = new ArrayList<>();
        for (int i = 1; i <= MAX_COLS; i++) {
            chooseFields.add(new JComboBox<>());
            chooseFieldLabels.add(new JLabel("Choose field " + i));
        }

        options = new JPanel();
        options.setLayout(new GridLayout(1, 15));
        options.setPreferredSize(new Dimension(150, 30));
        relationComboBox = new JComboBox<>(relations);
        numFields = new JComboBox<>();
        condition = new JTextField();
        relationComboBox.setBounds(80, 50, 140, 20);
        JLabel tableHeader = new JLabel("Choose relation");
        JLabel numHeader = new JLabel("Choose number of fields");
        JLabel fieldHeader = new JLabel("Choose field");
        JLabel condHeader = new JLabel("Type in condition");
        this.resultDisplay = new ResultTablePanel(new QueryResult(new String[] {"Select a query up top!"},
                new Object[][] {{"Your choices are limited though"}}));


        submit = new JButton("Submit"); // action = select, project, join, etc
        SelectProjectAction actionListener = new SelectProjectAction(this);

        submit.addActionListener(actionListener);
        relationComboBox.addActionListener(actionListener);
        numFields.addActionListener(actionListener);

        setLayout(new BorderLayout());
        // the 5 elements in one row
        options.add(relationComboBox);
        options.add(permanentLabels[0]);

        options.add(numFields);
        options.add(permanentLabels[1]);

        /*options.add(chooseFields);
        options.add(fieldHeader);*/

//        options.add(condition);
//        options.add(condHeader);

//        options.add(submit);
        add(options, BorderLayout.PAGE_START);
        add(resultDisplay, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public ResultTablePanel getResultDisplay(){
        return this.resultDisplay;
    }

    public JComboBox<String> getNumFields() {return this.numFields;}

    public ArrayList<JComboBox<String>> getChooseFields() {
        return this.chooseFields;
    }

    public ArrayList<JLabel> getChooseFieldLabels() {return this.chooseFieldLabels;}

    public JLabel[] getPermanentLabels() {return this.permanentLabels;}

    public JComboBox<String> getRelationComboBox(){return this.relationComboBox;}

    public JPanel getOptions(){return this.options;}

    public JTextField getCondition() {
        return condition;
    }

    public DBHandler getHandler() {return dbh;}

    public int getMaxCols() {return MAX_COLS;}


}

class SelectProjectAction implements ActionListener{
    private SelectProjectPanel myPanel;

    public SelectProjectAction(SelectProjectPanel mainPanel){
        this.myPanel = mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String relation = (String) myPanel.getRelationComboBox().getSelectedItem();
        ResultSetMetaData meta = myPanel.getHandler().getRelationInfo(relation);
        try {
            switch (e.getActionCommand()) { // add more cases for each button action
                case "comboBoxChanged": {
                    int colNum = meta.getColumnCount();
                    int colNumSelectable = Math.min(colNum,myPanel.getMaxCols());
                    if (e.getSource().equals(myPanel.getRelationComboBox())) {
                        for (Component c: myPanel.getOptions().getComponents())
                            if (!c.equals(myPanel.getRelationComboBox()) &&
                                    !c.equals(myPanel.getNumFields()) &&
                                    !c.equals(myPanel.getPermanentLabels()[0]) &&
                                    !c.equals(myPanel.getPermanentLabels()[1]))
                                myPanel.getOptions().remove(c);

                        String[] numbers = new String[colNumSelectable];
                        String[] relationFields = new String[colNum]; // get all fields selectable by this relation

                        for (int i = 1; i <= colNum; i++) {
                            if (i <= colNumSelectable) numbers[i-1] = ""+i;
                            relationFields[i - 1] = meta.getColumnName(i);
                        }

                        myPanel.getNumFields().setModel(new DefaultComboBoxModel<>(numbers));
                        for (int i = 1; i <= colNumSelectable; i++) {
                            myPanel.getChooseFields().get(i-1).setModel(new DefaultComboBoxModel<>(relationFields));
                        }
                        myPanel.getOptions().revalidate();
                        myPanel.getOptions().repaint();
                    } else if (e.getSource().equals(myPanel.getNumFields())) {
                        for (Component c: myPanel.getOptions().getComponents())
                            if (!c.equals(myPanel.getRelationComboBox()) &&
                            !c.equals(myPanel.getNumFields()) &&
                            !c.equals(myPanel.getPermanentLabels()[0]) &&
                                    !c.equals(myPanel.getPermanentLabels()[1]))
                                myPanel.getOptions().remove(c);

                        int numFieldsSelected = Integer.parseInt((String) myPanel.getNumFields().getSelectedItem());

                        for (int i = 1; i <= numFieldsSelected; i++) {
                            myPanel.getOptions().add(myPanel.getChooseFields().get(i-1));
                            myPanel.getOptions().add(new JLabel("Choose field " + i));
                        }

                        myPanel.getOptions().add(myPanel.getCondition());
                        myPanel.getOptions().add(new JLabel("Type in condition"));

                        myPanel.getOptions().add(myPanel.submit);

                        myPanel.getOptions().revalidate();
                        myPanel.getOptions().repaint();
                    }
                    break;
                }
                case "Submit": {
                    String fields = "";
                    int numFieldsSelected = Integer.parseInt((String) myPanel.getNumFields().getSelectedItem());
                    for (int i = 1; i <= numFieldsSelected; i++) {
                        Object field = myPanel.getChooseFields().get(i-1).getSelectedItem();
                        if (field != null) {
                            if (i == numFieldsSelected) fields = fields + (String) field;
                            else fields = fields + (String) field + ", ";
                        }
                    }
                    String condition = myPanel.getCondition().getText();

                    if (condition.equals(""))
                        myPanel.resultDisplay.setQueryResult(myPanel.getHandler().project(fields,
                                (String) myPanel.getRelationComboBox().getSelectedItem()));
                    else
                        myPanel.resultDisplay.setQueryResult(myPanel.getHandler().select(fields,
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
