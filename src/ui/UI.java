package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DatabaseMetaData;

public class UI extends JFrame implements ActionListener {

    private JLabel label;
    private JTextField field;
    private JTabbedPane tabs;
    private OptionPanel simplePanel;

    public UI(){
        super("WaterQualityDatabaseManagement");
    }

    public void showFrame(DBHandler dbHandler) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // setPreferredSize(new Dimension(1500, 1000));
        add(new TabbedPane(dbHandler), BorderLayout.CENTER);


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

    public OnePanel(String action, String[] relationOptions, DBHandler dbh){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        ResultTablePanel resultTable = new ResultTablePanel(new QueryResult(new String[] {"Select a query up top!"},
                new Object[][] {{"Your choices are limited though"}}));
        OptionPanel simplePanel = new OptionPanel(action, relationOptions, resultTable, dbh); // change this to relations once we get the relations
        add(simplePanel);
        // sample data for JTable
/*        String[] columnNames = {"First Name",
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
        };*/
        add(resultTable);
        revalidate();
    }
}

class OptionPanel extends JPanel{
    JButton submit;
    private JComboBox<String> relationComboBox; //box to choose relation
    private JComboBox<String> fieldComboBox; // box to choose fields to project from relation

    private String[] relation_options = {"UserBusiness", "UserHousehold", "BodyOfWater"};

    public OptionPanel(String action, String[] relations, ResultTablePanel resultDisplay, DBHandler dbh){
        relationComboBox = new JComboBox<>(relations);
        fieldComboBox = new JComboBox<>();
        relationComboBox.setBounds(80, 50, 140, 20);
        relationComboBox.addActionListener(new ComboBoxAction(relationComboBox, fieldComboBox));
        JLabel tableHeader = new JLabel("Choose relation");
        JLabel fieldHeader = new JLabel("Choose field");

        setLayout(new GridLayout(1, 3));

        submit = new JButton(action); // action = select, project, join, etc
        submit.addActionListener(new ButtonAction(submit, resultDisplay, dbh,
                relationComboBox, fieldComboBox));

        setLayout(new FlowLayout());
        // the 5 elements in one row
        add(tableHeader);
        add(relationComboBox);
        add(fieldHeader);
        add(fieldComboBox);
        add(submit);
    }
}

class TableResult {
    public String[] columnNames;
    public Object[][] data;

    public TableResult(String[] columns, Object[][] myData){
        this.columnNames = columns;
        this.data = myData;
    }

    public void setResult(String[] columns, Object[][] myData){
        this.columnNames = columns;
        this.data = myData;
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
        String[] relationFields = {"userID", "address", "isWaterEnough", "isWaterClean", "username", "password", "location"}; // get all fields selectable by this relation
        System.out.println(comboBox.getSelectedItem());

        fieldBox.setModel(new DefaultComboBoxModel(relationFields));
    }
}


class ButtonAction implements ActionListener {
    private JButton button;
    private ResultTablePanel resultDisplay;
    private DBHandler dbh;
    private JComboBox<String> relationComboBox;
    private JComboBox<String> fieldComboBox;

    public ButtonAction(JButton myButton, ResultTablePanel resultDisplay, DBHandler dbh,
                        JComboBox<String> relationComboBox, JComboBox<String> fieldComboBox){
        this.button = myButton;
        this.resultDisplay = resultDisplay;
        this.relationComboBox = relationComboBox;
        this.fieldComboBox = fieldComboBox;
        this.dbh = dbh;
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
            case "select":
                System.out.println("pressed select");
                resultDisplay.setQueryResult(new QueryResult(columnNames, data));
                break;
                //dbHandler.select("", "", ""); uncomment/modity to dbHandler method
            case "project":
                String relation = (String) relationComboBox.getSelectedItem();
                String field = (String) fieldComboBox.getSelectedItem();
                QueryResult result = dbh.project(field, relation);
                resultDisplay.setQueryResult(result);
                System.out.println("pressed project");
                break;
        }
        resultDisplay.repaint();
    }
}

class TabbedPane extends JPanel{
    private String[] relation_options = {"UserBusiness", "UserHousehold", "BodyOfWater"}; // this would change per pane

    public TabbedPane(DBHandler dbh) {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
        // SimplePanel simplePanel = new SimplePanel("project", relation_options); // change this to relations once we get the relations
        OnePanel panel1 = new OnePanel("project", relation_options, dbh);
        tabbedPane.addTab("Tab 1",panel1);

        OnePanel panel2 = new OnePanel("select", new String[]{"BodyOfWater", "SewagePlant"}, dbh); // add more tabs like this
        tabbedPane.addTab("Tab 2", panel2);

        //Add the tabbed pane to this panel.
        add(tabbedPane, BorderLayout.CENTER);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
}
