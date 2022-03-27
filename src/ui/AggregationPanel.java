package src.ui;

import src.DBHandler;
import src.QueryResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AggregationPanel extends JPanel {
    JButton submit;
    JComboBox<String> relationComboBox;
    ResultTablePanel resultDisplay;
    private JPanel options;
    private JLabel find;

    // numUsersWithBadWater, stressedWaterSource
    public AggregationPanel(String[] relations, DBHandler dbh) {
        options = new JPanel();
        options.setLayout(new GridLayout(1, 15));
        options.setPreferredSize(new Dimension(50, 30));
        relationComboBox = new JComboBox<>(relations);
        relationComboBox.setBounds(500, 500, 100, 20);
        find = new JLabel("Find: ");

        this.resultDisplay = new ResultTablePanel(new QueryResult(new String[] {"Select a query up top!"},
                new Object[][] {{"Aggregate away!"}}));


        submit = new JButton("Submit");
        AggregationAction actionListener = new AggregationAction(this);
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

}

class AggregationAction implements ActionListener{
    private AggregationPanel myAggregationPanel;

    public AggregationAction(AggregationPanel aggregationPanel) {
        this.myAggregationPanel = aggregationPanel;
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
        String relation = (String) myAggregationPanel.relationComboBox.getSelectedItem();

        switch(e.getActionCommand()){
            case "comboBoxChanged": {

                System.out.println(myAggregationPanel.getRelationComboBox().getSelectedItem());


                if (relation.equals("Number of users without enough clean water")) {
                    //numUsersWithBadWater: find number of users without enough clean water

                } else if (relation.equals("Most used body of water")){
                    //stressedWaterSource: Returns water source that is used by the highest number of businesses

                }
                myAggregationPanel.getOptions().revalidate();
                myAggregationPanel.getOptions().repaint();
                break;

            }
            case "Submit":{
                // get query result

                myAggregationPanel.resultDisplay.setQueryResult(new QueryResult(columnNames, data));
                System.out.println("aggregation: pressed Submit");
                break;
            }
        }
    }

}

