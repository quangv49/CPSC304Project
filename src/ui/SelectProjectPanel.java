package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectProjectPanel extends JPanel {
    JButton submit;
    private JComboBox<String> relationComboBox; //box to choose relation
    private JComboBox<String> chooseFields; // box to choose fields to project from relation
    private JTextField condition;
    ResultTablePanel resultDisplay;
    private JPanel options;



    public SelectProjectPanel(String[] relations){
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
        switch(e.getActionCommand()){ // add more cases for each button action
            case "comboBoxChanged":{
                /*
                TODO: replace relationfields with the column names of the selected relation from relationCombobox
                 */
                String[] relationFields = {"userID", "addres", "isWaterEnough", "isWaterClean", "username", "password", "location"}; // get all fields selectable by this relation
                System.out.println(myPanel.getRelationComboBox().getSelectedItem());
                myPanel.getChooseFields().setModel(new DefaultComboBoxModel(relationFields));
                myPanel.getOptions().revalidate();
                myPanel.getOptions().repaint();
                break;
            }
            case "Submit":{
                /*
                TODO: get query result here
                 */
                myPanel.resultDisplay.setQueryResult(new QueryResult(columnNames, data));
                System.out.println("pressed project");
                break;
            }

        }
        myPanel.repaint();
    }
}
