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

    public InsertComboBoxAction(JPanel myComboBox, InsertPanel insertPanel, DBHandler dbh) {
        this.myInsertPanel = insertPanel;
        this.dbh = dbh;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String[] columnNames = {"First Name", "Last Name", "Sport", "# of Years", "Vegetarian"};

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

        System.out.println((String) myInsertPanel.relationComboBox.getSelectedItem());
        String relation = (String) myInsertPanel.relationComboBox.getSelectedItem();
        ResultSetMetaData currentRelationInfo = dbh.getRelationInfo(relation);
        System.out.println(e.getActionCommand()); // "comboBoxChanged" = action when comboxBox changed
        if (e.getActionCommand() == "Submit") {
            System.out.println("pressed insert");

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
                    break;
                case "Delete":
                case "Update":
            }
        /*
        TODO run dbhandler here to get the table - remember to read from the text fields
         */
            myInsertPanel.getResultDisplay().setQueryResult(dbh.sendCommand("SELECT * FROM " + relation));
        } else {
             /*
        TODO run dbhandler here to get the fields
         */
            try {
                switch (e.getActionCommand()) {
                    case "comboBoxChanged": {
                        JPanel textFields = new JPanel();
                        // int numColumns = use dbhandler to get number of columns
                        int numColumns = currentRelationInfo.getColumnCount();
                        textFields.setLayout(new GridLayout(numColumns, 1));

                        String[] colNames = new String[numColumns]; // replace with dbhandler to get colnames
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

                            /// code to dynamically make text fields
                            for (int i = 0; i < numColumns; i++) {
                                currentOp = "Insert";
                                currentFields = new ArrayList<>();
                                JLabel label = new JLabel(colNames[i]);
                                JTextField field = new JTextField();
                                field.setName(currentRelationInfo.getColumnName(i+1));
                                textFields.add(label);
                                textFields.add(field);
                                currentFields.add(field);
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
                        field.setName(currentRelationInfo.getColumnName(1));
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