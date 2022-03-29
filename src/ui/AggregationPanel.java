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
    DBHandler dbh;

    // numUsersWithBadWater, stressedWaterSource
    public AggregationPanel(String[] relations, DBHandler dbh) {
        this.dbh = dbh;
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

        // relationComboBox.addActionListener(actionListener);

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


                // get query result
                QueryResult result = null;
                switch ((String) myAggregationPanel.relationComboBox.getSelectedItem()) {
                    case "Number of users without enough clean water":
                        result = myAggregationPanel.dbh.numUsersWithBadWater();
                        break;
                    case "Most used body of water":
                        result = myAggregationPanel.dbh.stressedWaterSource();
                        break;
                }
                myAggregationPanel.resultDisplay.setQueryResult(result);

            }

    }

