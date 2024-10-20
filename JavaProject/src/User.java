package JavaProject.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.sql.*;
public class User extends JFrame implements ActionListener {
    private ArrayList<AccountData> loadedcustomerlist; // List of customers
    private JButton depositButton, withdrawButton, transferButton, deleteButton, changePinButton, seeBalanceButton, backButton, backButton2 ,loginButton;
    private JTextField pinField;  // Field for entering PIN
    private String currentPIN;  // The currently logged-in user's PIN
    private JPanel rightPanel;
    private JFrame dashboardFrame;
    public User() {
        loadedcustomerlist = loadCustomersFromFile("Recording.txt");  // Load customer data

        // Set up the frame
        setTitle("Customer Login");
        setSize(600, 400);
        this.setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Left panel (50% of space) - Contains the background image
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE); // Set background to white
        JLabel imageLabel = new JLabel(new ImageIcon("C:\\Users\\ASUS\\Java\\JavaProject\\src\\Login.jpg"));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image
        leftPanel.add(imageLabel, BorderLayout.CENTER); // Add the image to the left panel

        // Right panel (50% of space) - Contains the buttons and fields
        rightPanel = new JPanel(new GridBagLayout());
      //  rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(375, 475)); // Adjust size
       // rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        // Welcome label for the login screen
        JLabel welcomeLabel = new JLabel("USER LOGIN FORM ", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        Color buttonColor = new Color(70, 130, 180);
        welcomeLabel.setForeground(buttonColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(welcomeLabel, gbc);

        // PIN label and field
        JLabel pinLabel = new JLabel("Enter PIN: ");
        pinLabel.setForeground(buttonColor); // Set text color to blue
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(pinLabel, gbc);

        // PIN input field
        pinField = new JTextField(10);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(pinField, gbc);

        // Login and Back buttons in the same row
        Dimension buttonSize = new Dimension(120, 40);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // A panel to hold both buttons
      //  buttonPanel.setBackground(Color.WHITE);
        loginButton = new JButton("Login");
        backButton = new JButton("Back");
        loginButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        // Style buttons with the same blue color
        loginButton.setBackground(buttonColor);  // Blue background
        loginButton.setForeground(Color.WHITE);  // White text
        backButton.setBackground(buttonColor);   // Blue background
        backButton.setForeground(Color.WHITE);   // White text

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        // Add button panel to the right panel in the same row
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(buttonPanel, gbc);

        // Add action listeners
        loginButton.addActionListener(this);
        backButton.addActionListener(this);

        // Set the layout of the main window to GridLayout (1 row, 2 columns)
        this.setLayout(new GridLayout(1, 2));
        this.add(leftPanel);  // Add left panel (with background image) to the window
        this.add(rightPanel); // Add right panel (with buttons) to the window

        setVisible(true);
    }

    // Show the user dashboard after successful login
    private void showDashboard() {
        // Find the user's name based on the PIN
        String userName = "";
        for (AccountData account : loadedcustomerlist) {
            if (account.pincode.equals(currentPIN)) {
                userName = account.customername;
                break;
            }
        }

        // Create a new JFrame for the dashboard
        dashboardFrame = new JFrame("User Dashboard");
        dashboardFrame.setSize(700, 410);
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setLayout(new BorderLayout());

        // Create a split pane
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(0);

        // Left panel with an image as background
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("C:\\Users\\ASUS\\Java\\JavaProject\\src\\AdminImage.jpg");
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        leftPanel.setPreferredSize(new Dimension(400, 600));
        leftPanel.setLayout(new BorderLayout());

        // Right panel for dashboard text and buttons
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BorderLayout());

        // Welcome label with padding after it
        JLabel welcomeLabel = new JLabel("Hello !! " + userName + ". Explore Your Account", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Padding added here
        rightPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 10, 10)); // Keep a smaller grid layout for buttons
        buttonPanel.setBackground(Color.WHITE);

        // Buttons with smaller size
        depositButton = createStyledButton("Deposit");
        withdrawButton = createStyledButton("Withdraw");
        transferButton = createStyledButton("Transfer Money");
        deleteButton = createStyledButton("Delete Account");
        changePinButton = createStyledButton("Change PIN");
        seeBalanceButton = createStyledButton("Your Account");
        backButton2 = createStyledButton("Back");

        // Add the buttons to the button panel
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(transferButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(changePinButton);
        buttonPanel.add(seeBalanceButton);
        buttonPanel.add(backButton2);

        // Add button panel to a wrapper for centering
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(buttonPanel);

        // Add space at the bottom (smaller spacer with padding)
        JPanel spacerPanel = new JPanel();
        spacerPanel.setPreferredSize(new Dimension(150, 10)); // Reduced the height for less space
        spacerPanel.setBackground(Color.WHITE);
        rightPanel.add(spacerPanel, BorderLayout.SOUTH);  // Adjust the bottom space

        // Add button wrapper to the right panel
        rightPanel.add(buttonWrapper, BorderLayout.CENTER);

        // Add action listeners to the buttons
        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);
        transferButton.addActionListener(this);
        deleteButton.addActionListener(this);
        changePinButton.addActionListener(this);
        seeBalanceButton.addActionListener(this);
        backButton2.addActionListener(this);

        // Add components to split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        // Add the split pane to the dashboard frame
        dashboardFrame.add(splitPane, BorderLayout.CENTER);

        // Show the dashboard
        dashboardFrame.setVisible(true);
    }


    // Utility method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));  // Blue background
        button.setForeground(Color.WHITE);  // White text
        button.setPreferredSize(new Dimension(150, 25));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

         if (e.getSource() == backButton) {
           this.dispose();
             LoginRegistration loginRegistration = new LoginRegistration();
             loginRegistration.setVisible(true);
         }
        else if(e.getSource() == loginButton) {
            String enteredPIN = pinField.getText();
            boolean isValidPIN = false;

            // Check if entered PIN is valid
            for (AccountData account : loadedcustomerlist) {
                if (account.pincode.equals(enteredPIN)) {
                    isValidPIN = true;
                    currentPIN = enteredPIN;  // Save the logged-in user's PIN
                    break;
                }
            }

            if (isValidPIN) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                this.dispose();
                showDashboard();  // Show the user dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Invalid PIN. Try again.");
            }
        } else if (e.getSource() == depositButton) {
            deposit();
        } else if (e.getSource() == withdrawButton) {
            withdraw();
        } else if (e.getSource() == transferButton) {
            transfer();
        } else if (e.getSource() == deleteButton) {
            deleteAccount();
        } else if (e.getSource() == changePinButton) {
            changePin();
        } else if (e.getSource() == seeBalanceButton) {
            seeBalance();
        } else if (e.getSource() == backButton2) {
             dashboardFrame.dispose();
             LoginRegistration loginRegistration = new LoginRegistration();
             loginRegistration.setVisible(true);
        }
    }

    // Example deposit method
    private void deposit() {
        String depositAmountStr = JOptionPane.showInputDialog("Enter amount to deposit:");
        try {
            double depositAmount = Double.parseDouble(depositAmountStr);
            for (AccountData account : loadedcustomerlist) {
                if (account.pincode.equals(currentPIN)) {

                    double currentBalance = Double.parseDouble(account.startbalance);
                    currentBalance += depositAmount;
                    account.startbalance = String.valueOf(currentBalance);

                    //sql code for updated PIN
                    String url = "jdbc:mysql://127.0.0.1:3306/my_db2";
                    String username = "root";
                    String password = "mysql";

                    // sql query
                    String sql = "UPDATE banking_project SET Start_balance = ? WHERE PINCODE = ?";

                    try {
                        //  database connection
                        Connection connection = DriverManager.getConnection(url, username, password);
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, String.valueOf(currentBalance)); // setting updated balance
                        preparedStatement.setString(2,  currentPIN); // replace the balance

                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Deposit record  successful!");
                        } else {
                            System.out.println("No record found for deposit .");
                        }
                        preparedStatement.close();
                        connection.close();

                    } catch (SQLException e) {

                        e.printStackTrace();
                    }
                    //databse done

                    JOptionPane.showMessageDialog (null, String.format("Deposit successful. New balance: " + account.startbalance+
                            "\n\nYOUR ACCOUNT DETAILS :\nPINCODE: %s\nCustomer name: %s\nAccount Type: %s\nAccount Number: %s\nTotal Balance: %s",
                            account.pincode, account.customername, account.accounttype,
                            account.accountnumber, account.startbalance));
                  //  JOptionPane.showMessageDialog(this, "Deposit successful. New balance: " + account.startbalance);
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
        }
        savePerson();
    }
    // Withdraw method
    private void withdraw() {
        String withdrawAmountStr = JOptionPane.showInputDialog("Enter amount to withdraw:");
        try {
            double withdrawAmount = Double.parseDouble(withdrawAmountStr);
            for (AccountData account : loadedcustomerlist) {
                if (account.pincode.equals(currentPIN)) {
                    double currentBalance = Double.parseDouble(account.startbalance);
                    if (currentBalance >= withdrawAmount) {
                        currentBalance -= withdrawAmount;
                        account.startbalance = String.valueOf(currentBalance);


                        //sql code for updated PIN
                        String url = "jdbc:mysql://127.0.0.1:3306/my_db2";
                        String username = "root";
                        String password = "mysql";
                        String sql = "UPDATE banking_project SET Start_balance = ? WHERE PINCODE = ?";//query

                        try {
                            //  database connection
                            Connection connection = DriverManager.getConnection(url, username, password);
                            PreparedStatement preparedStatement = connection.prepareStatement(sql);

                            preparedStatement.setString(1, String.valueOf(currentBalance)); // setting updated balance
                            preparedStatement.setString(2,  currentPIN); // replace the balance

                            int rowsAffected = preparedStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("Deposit record  successful!");
                            } else {
                                System.out.println("No record found for deposit .");
                            }
                            preparedStatement.close();
                            connection.close();

                        } catch (SQLException e) {

                            e.printStackTrace();
                        }
                        //databse done

                        JOptionPane.showMessageDialog (null, String.format("Withdrawal successful. New balance: " + account.startbalance+
                                        "\n\nYOUR ACCOUNT DETAILS :\nPINCODE: %s\nCustomer name: %s\nAccount Type: %s\nAccount Number: %s\nTotal Balance: %s",
                                account.pincode, account.customername, account.accounttype,
                                account.accountnumber, account.startbalance));
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient balance!");
                    }
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
        }
        savePerson();
    }

    // Transfer method
    private void transfer() {
        String targetAccountNumber = JOptionPane.showInputDialog("Enter recipient's account number:");
        String transferAmountStr = JOptionPane.showInputDialog("Enter amount to transfer:");

        try {
            double transferAmount = Double.parseDouble(transferAmountStr);
            AccountData sender = null, recipient = null;

            for (AccountData account : loadedcustomerlist) {
                if (account.pincode.equals(currentPIN)) {
                    sender = account;
                }
                if (account.accountnumber.equals(targetAccountNumber)) {
                    recipient = account;
                }
            }

            if (sender != null && recipient != null) {
                double senderBalance = Double.parseDouble(sender.startbalance);
                if (senderBalance >= transferAmount) {
                    senderBalance -= transferAmount;
                    recipient.startbalance = String.valueOf(Double.parseDouble(recipient.startbalance) + transferAmount);
                    sender.startbalance = String.valueOf(senderBalance);

                     // SQL Code
                    try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/my_db2", "root", "mysql")) {
                        // Update sender's balance
                        String senderUpdateQuery = "UPDATE banking_project SET Start_balance = ? WHERE PINCODE = ?";
                        try (PreparedStatement senderStmt = connection.prepareStatement(senderUpdateQuery)) {
                            senderStmt.setString(1, sender.startbalance);
                            senderStmt.setString(2, sender.pincode);
                            senderStmt.executeUpdate();
                        }

                        // Update recipient's balance
                        String recipientUpdateQuery = "UPDATE banking_project SET Start_balance = ? WHERE Account_number = ?";
                        try (PreparedStatement recipientStmt = connection.prepareStatement(recipientUpdateQuery)) {
                            recipientStmt.setString(1, recipient.startbalance);
                            recipientStmt.setString(2, recipient.accountnumber);
                            recipientStmt.executeUpdate();
                        }

                        JOptionPane.showMessageDialog(this, "Transfer successful to " + recipient.customername + "!");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error updating database: " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid account number.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
        }
        savePerson();
    }


    // Delete account method
    private void deleteAccount() {
        for (int i = 0; i < loadedcustomerlist.size(); i++) {
            AccountData account = loadedcustomerlist.get(i);
            if (account.pincode.equals(currentPIN)) {
                int confirm = JOptionPane.showConfirmDialog(null, String.format("Do you really want to delete your account?\n\nPINCODE: %s\nCustomer name: %s\nAccount Type: %s\nAccount Number: %s\nTotal Balance: %s",
                        account.pincode, account.customername, account.accounttype, account.accountnumber, account.startbalance), "CONFIRMATION ABOUT DELETION", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    loadedcustomerlist.remove(i);


                    //  MySQL JDBC driver
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    // Database connection
                    String url = "jdbc:mysql://127.0.0.1:3306/my_db2";
                    String username = "root";
                    String password = "mysql";
                    try {
                        Connection connection = DriverManager.getConnection(url, username, password);

                        // SQL query
                        String sql = "DELETE FROM banking_project WHERE PINCODE = ?";

                        //  statement
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);

                        // setting the PIN number (replacing the ? with the actual PIN)
                        preparedStatement.setString(1, currentPIN);

                        // Execute the (delete)
                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Record deleted successfully!");
                        } else {
                            System.out.println("No record foud for delete .");
                        }

                        // Close the statement and connection
                        preparedStatement.close();
                        connection.close();

                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }  //database done
                    JOptionPane.showMessageDialog(this, "Account deleted.");
                    break;
                }
            }
        }
        savePerson();
    }

    // Change PIN method
    private void changePin() {
        String oldPIN = JOptionPane.showInputDialog("Enter current PIN:");
        if (!oldPIN.equals(currentPIN)) {
            JOptionPane.showMessageDialog(this, "Incorrect PIN.");
            return;  // Exit if the old PIN is incorrect
        }

        String newPIN = JOptionPane.showInputDialog("Enter new PIN:");
        if (newPIN != null && newPIN.length() > 0) {
            for (AccountData account : loadedcustomerlist) {
                if (account.pincode.equals(currentPIN)) {
                    account.pincode = newPIN;
                    JOptionPane.showMessageDialog (null, String.format("PIN change  successful. New PIN: " + account.pincode+
                                    "\n\nYOUR ACCOUNT DETAILS :\nPINCODE: %s\nCustomer name: %s\nAccount Type: %s\nAccount Number: %s\nTotal Balance: %s",
                            account.pincode, account.customername, account.accounttype,
                            account.accountnumber, account.startbalance));
                  //  JOptionPane.showMessageDialog(this, "PIN changed successfully.");


                    //sql code for updated PIN
                    String url = "jdbc:mysql://127.0.0.1:3306/my_db2";
                    String username = "root";
                    String password = "mysql";

                    // sql query
                    String sql = "UPDATE banking_project SET PINCODE = ? WHERE PINCODE = ?";

                    try {
                        //  database connection
                        Connection connection = DriverManager.getConnection(url, username, password);

                        // prepare the SQL statement with placeholders
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);

                        // set the values for the placeholders
                        preparedStatement.setString(1, newPIN); // Set new PIN
                        preparedStatement.setString(2,currentPIN); // Replace old PIN

                        // execute the update
                        int rowsAffected = preparedStatement.executeUpdate();


                        if (rowsAffected > 0) {
                            System.out.println("PIN updated in record  successfully!");
                        } else {
                            System.out.println("No record found with the old PIN.");
                        }

                        // Close the connection and statement
                        preparedStatement.close();
                        connection.close();

                    } catch (SQLException e) {

                        e.printStackTrace();
                    }
                    //databse done

                    currentPIN = newPIN;  // Update the current PIN
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid PIN.");
        }
        savePerson();
    }



    // Method to save only the updated record to both text files
    // Method to save data to text files
    private void savePerson() {
        try (FileWriter fr = new FileWriter("Recording.txt");
             PrintWriter pw = new PrintWriter(fr);
             FileWriter fr1 = new FileWriter("Recordings.txt");
             PrintWriter pw1 = new PrintWriter(fr1)) {

            // captions
            pw1.printf("%-10s %-20s %-15s %-20s %-10s\n", "PINCODE", "CUSTOMER NAME", "ACCOUNT TYPE", "ACCOUNT NUMBER", "BALANCE");

            for (AccountData atm : loadedcustomerlist) {
                String line = String.format("%s,%s,%s,%s,%s\n", atm.pincode, atm.customername, atm.accounttype, atm.accountnumber, atm.startbalance);
                String line1 = String.format("%-10s %-20s %-15s %-20s %-10s", atm.pincode, atm.customername, atm.accounttype, atm.accountnumber, atm.startbalance);

                pw1.println(line1);
                pw.print(line);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Load customer data from file
    private ArrayList<AccountData> loadCustomersFromFile(String fileName) {
        ArrayList<AccountData> customerList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    customerList.add(new AccountData(data[0], data[1], data[2], data[3], data[4]));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading customer data: " + e.getMessage());
        }
        return customerList;
    }

    // Helper method to exit dashboard
    private void back() {
        this.dispose();  // Close the user dashboard window
    }

    // Balance checking method
    private void seeBalance() {
        for (AccountData account : loadedcustomerlist) {
            if (account.pincode.equals(currentPIN)) {
                JOptionPane.showMessageDialog (null, String.format("YOUR ACCOUNT \n\nPINCODE: %s\nCustomer name: %s\nAccount Type: %s\nAccount Number: %s\nTotal Balance: %s",
                        account.pincode, account.customername, account.accounttype,
                        account.accountnumber, account.startbalance));
                break;
            }
        }
    }
}

/*class BackgroundPanel3 extends JPanel {
    private Image backgroundImage;

    // Constructor to set the background image
    public BackgroundPanel3(String imagePath) {
        this.backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }
} */

