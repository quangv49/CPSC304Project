package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertPanel extends JPanel {
    JButton submit;
    JComboBox<String> relationComboBox;
    private JPanel textFieldPanel;
    private JPanel optionPanel;
    ResultTablePanel resultDisplay;
    JTextField[] fieldList;
    JLabel[] labelList;

    public InsertPanel(String[] relations){
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
        InsertComboBoxAction actionListener = new InsertComboBoxAction(textFieldPanel, this);
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

class InsertComboBoxAction implements ActionListener{
    private InsertPanel myInsertPanel;

    public InsertComboBoxAction(JPanel myComboBox, InsertPanel insertPanel) {
        this.myInsertPanel = insertPanel;
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

        DBHandler dbHandler = new DBHandler();
        System.out.println((String) myInsertPanel.relationComboBox.getSelectedItem());
        String relation = (String) myInsertPanel.relationComboBox.getSelectedItem();
        System.out.println(e.getActionCommand()); // "comboBoxChanged" = action when comboxBox changed
        if(e.getActionCommand() == "Submit"){
            System.out.println("pressed insert");

        /*
        TODO run dbhandler here to get the table - remember to read from the text fields
         */
            myInsertPanel.getResultDisplay().setQueryResult(new QueryResult(columnNames, data));
        } else{
             /*
        TODO run dbhandler here to get the fields
         */
            switch (e.getActionCommand()){
                case "comboBoxChanged": {
                    JPanel textFields = new JPanel();
                    // int numColumns = use dbhandler to get number of columns
                    int numColumns = 3;
                    textFields.setLayout(new GridLayout(numColumns, 1));

                    String[] colNames = new String[]{"userID", "address", "isWaterClean"}; // replace with dbhandler to get colnames
                    JTextField[] fieldList = new JTextField[numColumns];
                    JLabel[] labelList = new JLabel[numColumns];

                    if(relation == "UserBusiness" || relation == "UserHousehold"){
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

                    } else{
                        // if not user then only insert for BOW and License

                        /// code to dynamically make text fields
                        for(int i = 0; i < numColumns; i++){
                            JLabel label = new JLabel(colNames[i]);
                            JTextField field = new JTextField();
                            textFields.add(label);
                            textFields.add(field);
                        }
                    }
                    myInsertPanel.setTextFieldPanel(textFields);
                    break;
                }
                case "Insert": {
                    if(myInsertPanel.getTextFieldPanel().getComponents().length > 1){
                        myInsertPanel.getTextFieldPanel().remove(1);
                    }
                    ;
                    int numColumns = 3;
                    // int numColumns = use dbhandler to get number of columns
                    String[] colNames = new String[]{"userID", "address", "isWaterClean"}; // replace with dbhandler to get colnames
                    JTextField[] fieldList = new JTextField[numColumns];
                    JLabel[] labelList = new JLabel[numColumns];
                    JPanel optionFields = new JPanel();
                    optionFields.setLayout(new GridLayout(numColumns, 2));

                    /// code to dynamically make text fields
                    for(int i = 0; i < numColumns; i++){
                        JLabel label = new JLabel(colNames[i]);
                        JTextField field = new JTextField();
                        optionFields.add(label);
                        optionFields.add(field);
                    }
                    myInsertPanel.getTextFieldPanel().add(optionFields);
                    myInsertPanel.getTextFieldPanel().revalidate();

                    break;
                }
                case "Delete": {
                    if(myInsertPanel.getTextFieldPanel().getComponents().length > 1){
                        myInsertPanel.getTextFieldPanel().remove(1);
                    }
                    JPanel optionFields = new JPanel();
                    optionFields.setLayout(new GridLayout(1, 2));
                    JLabel label = new JLabel("userID");
                    JTextField field = new JTextField();
                    optionFields.add(label);
                    optionFields.add(field);
                    myInsertPanel.getTextFieldPanel().add(optionFields);
                    myInsertPanel.getTextFieldPanel().revalidate();
                    break;
                }
                case "Update": {
                    if(myInsertPanel.getTextFieldPanel().getComponents().length > 1){
                        myInsertPanel.getTextFieldPanel().remove(1);
                    }
                    JPanel optionFields = new JPanel();
                    optionFields.setLayout(new GridLayout(2, 2));
                    JLabel labelID = new JLabel("userID");
                    JTextField fieldID = new JTextField();
                    optionFields.add(labelID);
                    optionFields.add(fieldID);

                    JLabel labelAddress = new JLabel("Address");
                    JTextField fieldAddress = new JTextField();
                    optionFields.add(labelAddress);
                    optionFields.add(fieldAddress);

                    myInsertPanel.getTextFieldPanel().add(optionFields);
                    myInsertPanel.getTextFieldPanel().revalidate();

                    break;
                }
            }

        }


    }
}