package src.ui;

import src.DBHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI extends JFrame implements ActionListener {

    private JLabel label;
    private JTextField field;
    private JTabbedPane tabs;
    private SimplePanel simplePanel;

    public UI(){
        super("WaterQualityDatabaseManagement");
    }

    public void showFrame(DBHandler dbHandler) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1500, 1000));
//        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13) );
//        setLayout(new BorderLayout());

//        tabs = new JTabbedPane(JTabbedPane.TOP);
//        simplePanel = new SimplePanel("project");
//        simplePanel.setLayout(new BorderLayout());
//        tabs.addTab("Projection", simplePanel);
//        getContentPane().add(tabs, BorderLayout.CENTER); // add tabs to current jframe

        add(new TabbedPane(), BorderLayout.CENTER);

//        JButton btn = new JButton("Project");
//        btn.setActionCommand("project");
//        btn.addActionListener(this); // sets "this" class as an action listener
//        label = new JLabel("flag");
//        field = new JTextField(5);
//        add(field);
//        add(btn);
//        add(label);
        pack();
        setVisible(true);
        setResizable(true);
    }

    public static void main(String[] args) { new UI(); }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("project")){
            label.setText(field.getText());
        }
    }
}

class SimplePanel extends JPanel{
    JButton submit;
    private JComboBox<String> relationComboBox;
    private JComboBox<String> fieldComboBox;

    private String[] relation_options = {"UserBusiness", "UserHousehold", "BodyOfWater"};

    SimplePanel(String action, String[] relations){
        relationComboBox = new JComboBox<>(relations);
        relationComboBox.setBounds(80, 50, 140, 20);
        relationComboBox.addActionListener(new ComboBoxAction(relationComboBox));
        JLabel tableHeader = new JLabel("Choose relation");
        setLayout(new GridLayout(2, 5));

        submit = new JButton(action); // action = select, project, join
        submit.addActionListener(new ButtonAction(submit));

        setLayout(new FlowLayout());
        add(tableHeader);
        add(relationComboBox);

        add(submit);
    }
}

class ComboBoxAction implements ActionListener{
    private JComboBox comboBox;
    private String[] selectableFields;

    public ComboBoxAction(JComboBox myComboBox) {
        this.comboBox = myComboBox;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        DBHandler dbHandler = new DBHandler();
        // this.selectableFields = (String[]) comboBox.getSelectedItem(); // get all fields selectable by this relation
        System.out.println(comboBox.getSelectedItem());
    }
}

class ButtonAction implements ActionListener{
    private JButton button;

    public ButtonAction(JButton myButton){
        this.button = myButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DBHandler dbHandler = new DBHandler();
        switch(e.getActionCommand()){ // add more cases for each button
            case "select":
                System.out.println("pressed select");
                //dbHandler.select("", "", "");
            case "project":
                System.out.println("pressed project");
        }
    }
}

class TabbedPane extends JPanel{
    private String[] relation_options = {"UserBusiness", "UserHousehold", "BodyOfWater"}; // this would change per pane

    public TabbedPane() {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
        SimplePanel simplePanel = new SimplePanel("project", relation_options);
        tabbedPane.addTab("Tab 1",simplePanel);

        JComponent panel2 = new SimplePanel("select", relation_options);
        tabbedPane.addTab("Tab 2", panel2);

        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
}
