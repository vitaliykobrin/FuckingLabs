package GI;

import support.Controller;
import support.Message;
import support.User;
import support.UsersRights;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

public class UsersGI extends JFrame {

    private Controller controller;
    private ArrayList<User> usersList;
    private int index;

    private JPanel tablePanel;
    private JPanel userInfoPanel;
    private JPanel labelsPanel;
    private JPanel centerPanel;
    private JPanel cancelPanel;
    private JPanel buttonsPanel;
    private JComboBox<Object> rightsBox;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel usernameLabel;
    private JLabel telephoneLabel;
    private JLabel mailLabel;
    private JButton prevButton;
    private JButton nextButton;
    private JButton backButton;
    private JButton addButton;
    private JButton showButton;
    private JLabel messageLabel;
    private JTable usersTable;


    public UsersGI(Controller controller) throws HeadlessException {
        super("Users");
        this.controller = controller;
        usersList = new ArrayList<>(controller.getUserSet());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        messageLabel = Message.prepareMessageLabel(Message.USER_LIST);
        getContentPane().add(messageLabel, BorderLayout.NORTH);
        prepareCenterPanel();
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        prepareCancelButtonPanel();
        getContentPane().add(cancelPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(500, 400));
        setIconImage(new ImageIcon("resources/table.png").getImage());
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prepareCenterPanel() {
        centerPanel = new JPanel();
        prepareTablePanel();
        centerPanel.add(tablePanel);
        prepareUserInfoPanel();
        centerPanel.add(userInfoPanel);
    }

    public void prepareTablePanel() {
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        prepareShowButton();
        tablePanel.add(showButton);
        prepareUsersTable();
        tablePanel.add(new JScrollPane(usersTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
    }

    public void prepareUsersTable() {
        usersTable = new JTable(new UsersTableModel());
        usersTable.setShowHorizontalLines(false);
        usersTable.setShowVerticalLines(false);
        usersTable.getSelectionModel().addListSelectionListener(e -> showButton.setEnabled(true));
    }

    public void changeInfo(int index) {
        User user = usersList.get(index);

        rightsBox.setSelectedItem(user.getRights());
        nameLabel.setText(user.getName());
        surnameLabel.setText(user.getSurname());
        usernameLabel.setText(user.getUserName());
        telephoneLabel.setText(user.getTelephoneNum());
        mailLabel.setText(user.getMailAddress());

        prevButton.setEnabled(index != 0);
        nextButton.setEnabled(index != usersList.size() - 1);
    }

    public void prepareShowButton() {
        showButton = new JButton("Show info");
        showButton.setEnabled(false);
        showButton.addActionListener(e -> {
            changeInfo(index = usersTable.getSelectedRow());
            tablePanel.setVisible(false);
            userInfoPanel.setVisible(true);
            messageLabel.setText(Message.USER_INFO);
        });
    }

    public void prepareCancelButtonPanel() {
        cancelPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setHorizontalAlignment(JButton.LEFT);
        cancelButton.setVerticalAlignment(JButton.BOTTOM);
        cancelButton.addActionListener(e -> dispose());
        cancelPanel.add(cancelButton);
    }

    public void prepareUserInfoPanel() {
        userInfoPanel = new JPanel();
        userInfoPanel.setVisible(false);
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));

        prepareButtonsPanel();
        userInfoPanel.add(buttonsPanel);
        prepareLabelsPanel();
        userInfoPanel.add(labelsPanel);
    }

    public void prepareButtonsPanel() {
        buttonsPanel = new JPanel();

        prevButton= new JButton("Prev");
        prevButton.setIcon(new ImageIcon("resources/prev.png"));
        prevButton.setHorizontalAlignment(JButton.LEFT);
        prevButton.addActionListener(e -> changeInfo(--index));
        buttonsPanel.add(prevButton);

        backButton = new JButton("Back");
        backButton.setHorizontalAlignment(JButton.CENTER);
        backButton.addActionListener(e -> {
            userInfoPanel.setVisible(false);
            tablePanel.setVisible(true);
            messageLabel.setText(Message.USER_LIST);
            index = 0;
        });
        buttonsPanel.add(backButton);

        nextButton = new JButton("Next");
        nextButton.setIcon(new ImageIcon("resources/next.png"));
        nextButton.setHorizontalTextPosition(SwingConstants.LEFT);
        nextButton.setHorizontalAlignment(JButton.RIGHT);
        nextButton.addActionListener(e -> changeInfo(++index));
        buttonsPanel.add(nextButton);
    }

    public void prepareLabelsPanel() {
        labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridLayout(6, 2));

        labelsPanel.add(new JLabel("Account type: "));
        prepareRightsBox();
        labelsPanel.add(rightsBox);

        labelsPanel.add(new JLabel("Name: "));
        labelsPanel.add(nameLabel = new JLabel());

        labelsPanel.add(new JLabel("Surname: "));
        labelsPanel.add(surnameLabel = new JLabel());

        labelsPanel.add(new JLabel("Username: "));
        labelsPanel.add(usernameLabel = new JLabel());

        labelsPanel.add(new JLabel("Telephone: "));
        labelsPanel.add(telephoneLabel = new JLabel());

        labelsPanel.add(new JLabel("E-mail address: "));
        labelsPanel.add(mailLabel = new JLabel());
    }

    public void prepareRightsBox() {
        String[] items = UsersRights.getItems();
        rightsBox = new JComboBox<>(items);
    }

    public class UsersTableModel implements TableModel {

        private String[] items = {"Name", "Surname", "Username", "Account type"};

        @Override
        public int getRowCount() {
            return controller.getUserSet().size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return items[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: return usersList.get(rowIndex).getName();
                case 1: return usersList.get(rowIndex).getSurname();
                case 2: return usersList.get(rowIndex).getUserName();
                case 3: return UsersRights.accountType(usersList.get(rowIndex).getRights());
                default: return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }
}