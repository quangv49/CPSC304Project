package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class InsertPanel extends JPanel {
    JButton submit;
    JComboBox<String> relationComboBox;
    private JPanel textFieldPanel;
    private JPanel optionPanel;
    ResultTablePanel resultDisplay;
    JTextField[] fieldList;
    JLabel[] labelList;

    public InsertPanel(String[] relations, DBHandler dbh){
        relationComboBox = new JComboBox<>(relations);
        setLayout(new BorderLayout());
        textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new GridLayout(15,1));
        optionPanel = new JPanel();
        this.resultDisplay = new ResultTablePanel(new QueryResult(new String[] {"Select a query up top!"},
                new Object[][] {{"Your choices are limited though"}}));

        // setting up all the options for insert
        JLabel tableHeader = new JLabel("Choose relation");
        optionPanel.add(tableHeader);
        optionPanel.add(relationComboBox);
        submit = new JButton("Submit"); // action = select, project, join, etc
        InsertComboBoxAction actionListener = new InsertComboBoxAction(textFieldPanel, this, dbh);
        submit.addActionListener(actionListener);
        optionPanel.add(submit);

        relationComboBox.setBounds(80, 50, 140, 20);
        relationComboBox.addActionListener(actionListener);

        add(optionPanel, BorderLayout.PAGE_START);
        add(textFieldPanel, BorderLayout.LINE_START);
        add(this.resultDisplay, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void setTextFieldPanel(JPanel textFieldPanel){
        for (Component c: this.textFieldPanel.getComponents()) {
            this.textFieldPanel.remove(c);
        }
        this.textFieldPanel.add(textFieldPanel);
        this.textFieldPanel.revalidate();
        this.textFieldPanel.repaint();
    }

    public JPanel getTextFieldPanel(){
        return this.textFieldPanel;
    }

    public ResultTablePanel getResultDisplay(){
        return this.resultDisplay;
    }

    /*
    @TODO add get number of columns per relation
     */

}

class InsertComboBoxAction implements ActionListener {
    private InsertPanel myInsertPanel;
    private DBHandler dbh;
    private ArrayList<JTextField> currentFields;
    private String currentOp;
    private boolean surface = false;

    public InsertComboBoxAction(JPanel myComboBox, InsertPanel insertPanel, DBHandler dbh) {
        this.myInsertPanel = insertPanel;
        this.dbh = dbh;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Obtain table name and ResultMetaData object to get num and names of columns
        String relation = (String) myInsertPanel.relationComboBox.getSelectedItem();
        ResultSetMetaData currentRelationInfo = dbh.getRelationInfo(relation);
        // "comboBoxChanged" = action when comboxBox changed
        if (e.getActionCommand() == "Submit") {
            System.out.println("pressed insert");

            // Mostly query construction. Implemented DML methods in DBHandler ended up being hard to use.
            switch (currentOp) {
                case "Insert":
                    StringBuilder columnPart = new StringBuilder();
                    StringBuilder valuePart = new StringBuilder();

                    for (int i = 0; i < currentFields.size(); i++) {
                        if (i < currentFields.size() - 1) {
                            columnPart.append(currentFields.get(i).getName() + ", ");
                            if (i == 0) valuePart.append("('" + currentFields.get(i).getText() + "', ");
                            else valuePart.append("'" + currentFields.get(i).getText() + "', ");
                        }
                        else {
                            columnPart.append(currentFields.get(i).getName());
                            valuePart.append("'" + currentFields.get(i).getText() + "')");
                        }
                    }

                    dbh.sendCommand("INSERT INTO " + relation + " (" + columnPart + ") VALUES " + valuePart);

                    if (relation == "BodyOfWater") {
                        if (surface) dbh.sendCommand("INSERT INTO SurfaceWater VALUES (" + currentFields.get(0).getText() + ")");
                        else dbh.sendCommand("INSERT INTO GroundWater VALUES (" + currentFields.get(0).getText() + ")");
                    }

                    break;
                case "Delete":
                    dbh.sendCommand("DELETE FROM " + relation + " WHERE " + currentFields.get(0).getName() +
                            " = '" + currentFields.get(0).getText() + "'");
                    break;
                case "Update":
                    dbh.sendCommand("UPDATE " + relation +
                            " SET " + currentFields.get(1).getName() + " = '" + currentFields.get(1).getText() + "' " +
                            "WHERE " + currentFields.get(0).getName() + " = '" + currentFields.get(0).getText() + "'");
                    break;
            }
            myInsertPanel.getResultDisplay().setQueryResult(dbh.sendCommand("SELECT * FROM " + relation));
        } else {

            try {
                switch (e.getActionCommand()) {
                    case "comboBoxChanged": {
                        JPanel textFields = new JPanel();
                        // ResultMetaData used to get number of columns
                        int numColumns = currentRelationInfo.getColumnCount();
                        textFields.setLayout(new GridLayout(numColumns, 1));

                        String[] colNames = new String[numColumns];
                        // ResultMetaData used to get col names
                        for (int i = 1; i <= numColumns; i++) colNames[i - 1] = currentRelationInfo.getColumnName(i);

                        if (relation == "UserBusiness" || relation == "UserHousehold") {
                            JRadioButton add = new JRadioButton("Insert");
                            add.addActionListener(this);

                            JRadioButton delete = new JRadioButton("Delete");
                            delete.addActionListener(this);

                            JRadioButton update = new JRadioButton("Update");
                            update.addActionListener(this);

                            //Group the radio buttons.
                            ButtonGroup group = new ButtonGroup();
                            group.add(add);
                            group.add(delete);
                            group.add(update);
                            textFields.add(add);
                            textFields.add(delete);
                            textFields.add(update);

                        } else {
                            // if not user then only insert for BOW and License
                            currentOp = "Insert";
                            currentFields = new ArrayList<>();
                            /// Code to dynamically make text fields
                            for (int i = 0; i < numColumns; i++) {
                                JLabel label = new JLabel(colNames[i]);
                                JTextField field = new JTextField();
                                field.setName(currentRelationInfo.getColumnName(i+1));
                                textFields.add(label);
                                textFields.add(field);
                                currentFields.add(field);
                            }

                            // BodyOfWater has subtypes Surface and Ground
                            if (relation.equals("BodyOfWater")) {
                                JCheckBox isSurface = new JCheckBox("Surface?");
                                isSurface.addItemListener(check -> {
                                    if (check.getItemSelectable() == isSurface) surface = true;
                                    if (check.getStateChange() == ItemEvent.DESELECTED) surface = false;
                                });
                                textFields.add(isSurface);
                            }
                        }
                        myInsertPanel.setTextFieldPanel(textFields);
                        break;
                    }
                    case "Insert": {
                        if (myInsertPanel.getTextFieldPanel().getComponents().length > 1) {
                            myInsertPanel.getTextFieldPanel().remove(1);
                        }
                        currentOp = "Insert";
                        currentFields = new ArrayList<>();
                        int numColumns = currentRelationInfo.getColumnCount();
                        // int numColumns = use dbhandler to get number of columns
                        String[] colNames = new String[numColumns]; // replace with dbhandler to get colnames
                        for (int i = 1; i <= numColumns; i++) colNames[i-1] = currentRelationInfo.getColumnName(i);
                        JPanel optionFields = new JPanel();
                        optionFields.setLayout(new GridLayout(numColumns, 2));

                        /// code to dynamically make text fields
                        for (int i = 0; i < numColumns; i++) {
                            JLabel label = new JLabel(colNames[i]);
                            JTextField field = new JTextField();
                            field.setName(currentRelationInfo.getColumnName(i+1));
                            optionFields.add(label);
                            optionFields.add(field);
                            currentFields.add(field);
                        }
                        myInsertPanel.getTextFieldPanel().add(optionFields);
                        myInsertPanel.getTextFieldPanel().revalidate();

                        break;
                    }
                    case "Delete": {
                        if (myInsertPanel.getTextFieldPanel().getComponents().length > 1) {
                            myInsertPanel.getTextFieldPanel().remove(1);
                        }
                        currentOp = "Delete";
                        currentFields = new ArrayList<>();
                        JPanel optionFields = new JPanel();
                        optionFields.setLayout(new GridLayout(1, 2));
                        JLabel label = new JLabel("userID");
                        JTextField field = new JTextField();
                        field.setName("userID");
                        optionFields.add(label);
                        optionFields.add(field);
                        currentFields.add(field);
                        myInsertPanel.getTextFieldPanel().add(optionFields);
                        myInsertPanel.getTextFieldPanel().revalidate();
                        break;
                    }
                    case "Update": {
                        if (myInsertPanel.getTextFieldPanel().getComponents().length > 1) {
                            myInsertPanel.getTextFieldPanel().remove(1);
                        }
                        currentOp = "Update";
                        currentFields = new ArrayList<>();
                        JPanel optionFields = new JPanel();
                        optionFields.setLayout(new GridLayout(2, 2));
                        JLabel labelID = new JLabel("userID");
                        JTextField fieldID = new JTextField();
                        fieldID.setName("USERID");
                        optionFields.add(labelID);
                        optionFields.add(fieldID);
                        currentFields.add(fieldID);

                        JLabel labelAddress = new JLabel("Address");
                        JTextField fieldAddress = new JTextField();
                        fieldAddress.setName("ADDRESS");
                        optionFields.add(labelAddress);
                        optionFields.add(fieldAddress);
                        currentFields.add(fieldAddress);

                        myInsertPanel.getTextFieldPanel().add(optionFields);
                        myInsertPanel.getTextFieldPanel().revalidate();

                        break;
                    }
                }

            } catch (SQLException exc) {
                System.out.println(exc.getMessage());
            }


        }
    }
}