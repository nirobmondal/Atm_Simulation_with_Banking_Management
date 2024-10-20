package JavaProject.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class CustomerRegister extends JFrame implements ActionListener {
    private JTextField pincodeField, nameField, accountNumberField, balanceField;
    private JComboBox<String> accountTypeComboBox;
    private ArrayList<AccountData> loadedCustomerList;

    public CustomerRegister() {
        // set up the jframe (form window)
        setTitle("Customer Registration Form");
        setSize(800, 500);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // left panel (40%)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(320, 500));
        leftPanel.setBackground(new Color(70, 130, 180));
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));



        JLabel welcomeLabelLeft = new JLabel("Welcome to Sign In", SwingConstants.CENTER);
        welcomeLabelLeft.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 28));
        welcomeLabelLeft.setForeground(Color.BLACK);
        leftPanel.add(welcomeLabelLeft, BorderLayout.CENTER);

        // Right panel (60%)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // welcome label at the top of the right panel (showing the text )

        JLabel welcomeLabelRight = new JLabel("Welcome", SwingConstants.CENTER);
        welcomeLabelRight.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 24)); //
        rightPanel.add(welcomeLabelRight, BorderLayout.NORTH); //adding jlable to thr right panel

        // form container with a grey border
        JPanel formContainer = new JPanel(new GridLayout(7, 2, 10, 10));
        formContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // grey border
        formContainer.setBackground(getBackground()); // default background
        formContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // inner padding for spce

        // labels and text fields for the form
        JLabel pincodeLabel = new JLabel("Enter PIN:");
        pincodeField = new JTextField(20);

        JLabel nameLabel = new JLabel("Enter Name:");
        nameField = new JTextField(20);

        JLabel accountTypeLabel = new JLabel("Account Type:");
        accountTypeComboBox = new JComboBox<>(new String[]{"Saving", "Current"});

        JLabel accountNumberLabel = new JLabel("Enter Account Number:");
        accountNumberField = new JTextField(20);

        JLabel balanceLabel = new JLabel("Enter Starting Balance:");
        balanceField = new JTextField(20);

        // submit button
        JButton submitButton = new JButton("Register");
        submitButton.setPreferredSize(new Dimension(70, 30)); //button size
        submitButton.setBackground(new Color(70, 130, 180)); // bluue color code
        submitButton.setForeground(Color.WHITE); // white text colour
        submitButton.addActionListener(this);

        // back button
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(70, 30));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginRegistration loginRegistration = new LoginRegistration();
                loginRegistration.setVisible(true);
            }
        });

        // adding form fields to the form container
        formContainer.add(pincodeLabel);
        formContainer.add(pincodeField);
        formContainer.add(nameLabel);
        formContainer.add(nameField);
        formContainer.add(accountTypeLabel);
        formContainer.add(accountTypeComboBox);
        formContainer.add(accountNumberLabel);
        formContainer.add(accountNumberField);
        formContainer.add(balanceLabel);
        formContainer.add(balanceField);
        formContainer.add(submitButton);
        formContainer.add(backButton);

        // adding container to the right panel (center
        rightPanel.add(formContainer, BorderLayout.CENTER);

        // adding panels to the jframe
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.4;
        add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.6;
        add(rightPanel, gbc);

        // loading existing accounts
        loadExistingAccounts();

        // setting JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // center the window on the screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String pincode = pincodeField.getText();
        String customerName = nameField.getText();
        String accountType = accountTypeComboBox.getSelectedItem().toString();
        String accountNumber = accountNumberField.getText();
        String balance = balanceField.getText();

        if (isPincodeUsed(pincode)) {
            JOptionPane.showMessageDialog(null, "This PIN is already in use. Please enter a different PIN.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            AccountData newAccount = new AccountData(pincode, customerName, accountType, accountNumber, balance);
            saveAccount(newAccount);
            JOptionPane.showMessageDialog(null, "Account Created Successfully! Login to explore your Account!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // database , god save me
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch(ClassNotFoundException e1) {
                e1.printStackTrace();
            }

            String url ="jdbc:mysql://127.0.0.1:3306/my_db2";
            String username= "root";
            String password="mysql";

            try {
                Connection connection= DriverManager.getConnection(url,username,password);
                Statement statement =connection.createStatement();
                String sql ="INSERT INTO banking_project( PINCODE , Customer_name, Account_type,Account_number,Start_balance)" +
                        "VALUES(?,?,?,?,?)";
                PreparedStatement preparedStatement= connection.prepareStatement(sql);
                preparedStatement.setString(1,pincode);
                preparedStatement.setString(2,customerName);
                preparedStatement.setString(3,accountType);
                preparedStatement.setString(4,accountNumber);
                preparedStatement.setString(5,balance);
                // preparedStatement.executeUpdate();
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Record inserted successfully!");
                }
                preparedStatement.close();
                connection.close();


            }catch (SQLException e2)
            {
                System.out.println(e2.getMessage());
            }

            //datbase done


            dispose();
            LoginRegistration loginRegistration = new LoginRegistration();
            loginRegistration.setVisible(true);
        }
    }

    private void loadExistingAccounts() {
        loadedCustomerList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Recording.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    String pincode = data[0];
                    String customername = data[1];
                    String accounttype = data[2];
                    String accountnumber = data[3];
                    String startbalance = data[4];
                    AccountData atm = new AccountData(pincode, customername, accounttype, accountnumber, startbalance);
                    loadedCustomerList.add(atm);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading accounts: " + e.getMessage());
        }
    }

    private boolean isPincodeUsed(String pincode) {
        for (AccountData atm : loadedCustomerList) {
            if (atm.pincode.equals(pincode)) {
                return true;
            }
        }
        return false;
    }

    private void saveAccount(AccountData account) {//writing tp txt file
        try (FileWriter fw1 = new FileWriter("Recording.txt", true);
             PrintWriter pw1 = new PrintWriter(fw1);
             FileWriter fw2 = new FileWriter("Recordings.txt", true);
             PrintWriter pw2 = new PrintWriter(fw2)) {

            String record = String.format("%s,%s,%s,%s,%s\n", account.pincode, account.customername, account.accounttype, account.accountnumber, account.startbalance);
            pw1.print(record);

            String recordFormatted = String.format("%-10s %-20s %-15s %-20s %-10s", account.pincode, account.customername, account.accounttype, account.accountnumber, account.startbalance);
            pw2.println(recordFormatted);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while saving account: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
