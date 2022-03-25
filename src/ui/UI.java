package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UI extends JFrame implements ActionListener {

    private JLabel label;
    private JTextField field;
    private JTabbedPane tabs;
    private OptionPanel simplePanel;

    public UI(){
        super("WaterQualityDatabaseManagement");
    }

    public void showFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1500, 1000));
        add(new TabbedPane(), BorderLayout.CENTER);


        pack();
        setVisible(true);
        setResizable(true);
        repaint();
        revalidate();
    }

    public static void main(String[] args) { new UI(); }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("project")){
            label.setText(field.getText());
        }
    }
}

// combines the options and result panels in one panel
class OnePanel extends JPanel{

    public OnePanel(String action, String[] relationOptions){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        ResultTablePanel resultTable = new ResultTablePanel(new QueryResult(new String[] {"Select a query up top!"},
                new Object[][] {{"Your choices are limited though"}}));
        OptionPanel simplePanel = new OptionPanel(action, relationOptions, resultTable); // change this to relations once we get the relations
        add(simplePanel);
        add(resultTable);
        revalidate();
    }
}

class OnePanelNew extends JPanel{

    public OnePanelNew(JPanel optionPanel){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        ResultTablePanel resultTable = new ResultTablePanel(new QueryResult(new String[] {"Select a query up top!"},
                new Object[][] {{"Your choices are limited though"}}));
        add(optionPanel);
        add(resultTable);
        revalidate();
    }
}



class OptionPanel extends JPanel{
    JButton submit;
    private JComboBox<String> relationComboBox; //box to choose relation
    private JComboBox<String> fieldComboBox; // box to choose fields to project from relation

    private String[] relation_options = {"UserBusiness", "UserHousehold", "BodyOfWater"};

    public OptionPanel(String action, String[] relations, ResultTablePanel resultDisplay){
        relationComboBox = new JComboBox<>(relations);
        fieldComboBox = new JComboBox<>();
        relationComboBox.setBounds(80, 50, 140, 20);
        relationComboBox.addActionListener(new ComboBoxAction(relationComboBox, fieldComboBox));
        JLabel tableHeader = new JLabel("Choose relation");
        JLabel fieldHeader = new JLabel("Choose field");

        setLayout(new GridLayout(1, 3));

        submit = new JButton(action); // action = select, project, join, etc
        submit.addActionListener(new ButtonAction(submit, resultDisplay, this));

        setLayout(new FlowLayout());
        // the 5 elements in one row
        add(tableHeader);
        add(relationComboBox);
        add(fieldHeader);
        add(fieldComboBox);
        add(submit);
    }

    public JComboBox<String> getRelationComboBox() {
        return this.relationComboBox;
    }
}

class ResultTablePanel extends JPanel {
    private JTable table;

    public ResultTablePanel(QueryResult myResult){
        super(new GridLayout(1,0));
        table = new JTable(myResult.getTuples(), myResult.getColNames());
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }

    public void setQueryResult(QueryResult queryResult) {
        this.table = new JTable(queryResult.getTuples(), queryResult.getColNames());

        for (Component c: getComponents()) {
            remove(c);
        }

        repaint();
        revalidate();

        add(new JScrollPane(table));

        repaint();
        revalidate();
    }
}

// field box and relation box go together
class ComboBoxAction implements ActionListener{
    private JComboBox comboBox;
    private JComboBox fieldBox;

    public ComboBoxAction(JComboBox myComboBox, JComboBox myFieldBox) {
        this.comboBox = myComboBox;
        this.fieldBox = myFieldBox;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        DBHandler dbHandler = new DBHandler();
        String[] relationFields = {"userID", "addres", "isWaterEnough", "isWaterClean", "username", "password", "location"}; // get all fields selectable by this relation
        System.out.println(comboBox.getSelectedItem());

        fieldBox.setModel(new DefaultComboBoxModel(relationFields));
    }
}


class ButtonAction implements ActionListener{
    private JButton button;
    private ResultTablePanel resultDisplay;
    private OptionPanel optionPanel;

    public ButtonAction(JButton myButton, ResultTablePanel resultDisplay, OptionPanel option){
        this.button = myButton;
        this.resultDisplay = resultDisplay;
        this.optionPanel = option;
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
        switch(e.getActionCommand()){ // add more cases for each button action
            case "select":
                System.out.println("pressed select");
                resultDisplay.setQueryResult(new QueryResult(columnNames, data));
                break;
                //dbHandler.select("", "", ""); uncomment/modity to dbHandler method
            case "project":
                System.out.println("pressed project");
                break;
            case "add_tuple":
                Object userChoice = optionPanel.getRelationComboBox().getSelectedItem();
                // call dbhandler on the userchoice

        }
        resultDisplay.repaint();
    }
}

class TabbedPane extends JPanel{

    public TabbedPane() {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
        String[] insertTabRelations = new String[]{"UserBusiness", "UserHousehold", "BodyOfWater", "GroundWaterLicense", "SurfaceWaterLicense"};
        InsertPanel panel1 = new InsertPanel(insertTabRelations);
        tabbedPane.addTab("Add Tuples",panel1);
        System.out.println("addedTabbedPane");

        SelectProjectPanel panel2 = new SelectProjectPanel(new String[]{"UserBusiness", "UserHousehold", "BodyOfWater", "GroundWaterLicense", "SurfaceWaterLicense",
                "BodyOfWater", "SewagePlant"}); // add more tabs like this
        tabbedPane.addTab("Select/Project", panel2);

        //Add the tabbed pane to this panel.
        add(tabbedPane, BorderLayout.CENTER);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
}
