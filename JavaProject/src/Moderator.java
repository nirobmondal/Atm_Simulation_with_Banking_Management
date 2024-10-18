import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

class Moderator extends JFrame implements ActionListener {
    private JButton addAccount;
    private JButton deleteAccount;
    private JButton PINchange;
    private JButton saveToFile;
    private JButton logOut;
    private JButton seeAccounts;
    private JLabel atmLab;
    private Container con;
    private ArrayList<AccountData> customerlist;//new
    private ArrayList<AccountData> loadedcustomerlist;

    // Constructor to set up the GUI
    public Moderator() {
        super("ADMIN");

        //for loading previous accounts to cheking PIN used or not
        loadedcustomerlist = new ArrayList<>();
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

                        // Create a new AccountData object and add it to the list
                        AccountData atm = new AccountData(pincode, customername, accounttype, accountnumber, startbalance);
                        loadedcustomerlist.add(atm);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading accounts: " + e.getMessage());
            }//load done

        // Initialize components and layout
        con = this.getContentPane();
        con.setLayout(new GridBagLayout()); // Changed to GridBagLayout for flexibility
        con.setBackground(Color.CYAN);
        customerlist = new ArrayList<>();

        atmLab = new JLabel(new ImageIcon("admin2.png"));
        addAccount = new JButton("Add Account");
        deleteAccount = new JButton("Delete Account");
        PINchange = new JButton("Change PIN number");
        saveToFile = new JButton("Save to File");
        seeAccounts = new JButton("See All Accounts");
        logOut = new JButton("Logout");

        // Setup layout using GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add components to the container with specific positions
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        con.add(atmLab, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        con.add(addAccount, gbc);

        gbc.gridx = 1;
        con.add(deleteAccount, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        con.add(PINchange , gbc);

        gbc.gridx = 1;
        con.add(saveToFile, gbc);

        gbc.gridy = 3; // Row 3
        gbc.gridx = 0; // First column
        con.add(seeAccounts, gbc);

        gbc.gridx = 1; // Second column
        con.add(logOut, gbc);

        // Set the frame properties
        this.setSize(550, 350);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Attach event listeners
        addAccount.addActionListener(this);
        deleteAccount.addActionListener(this);
        PINchange.addActionListener(this);
        saveToFile.addActionListener(this);
        seeAccounts.addActionListener(this);
        logOut.addActionListener(this);
    }

    // Method to add a new person/account
    private void addPersons() {
        String pincode;
        do {
            pincode = JOptionPane.showInputDialog(null, "Please enter PINCODE NO", "PINCODE ENTRY", JOptionPane.INFORMATION_MESSAGE);
            if (isPincodeUsed(pincode)) {
                JOptionPane.showMessageDialog(null, "This pincode is already in use. Please enter another pincode.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (isPincodeUsed(pincode));

        String customername = JOptionPane.showInputDialog(null, "Please Enter Customer Name", "CUSTOMER NAME", JOptionPane.INFORMATION_MESSAGE);
        String accounttype = JOptionPane.showInputDialog(null, "Please Enter Account Type (Saving or Current)", "ACCOUNT TYPE ENTRY", JOptionPane.INFORMATION_MESSAGE);
        String accountnumber = JOptionPane.showInputDialog(null, "Enter Account Number", "ACCOUNT NUMBER ENTRY", JOptionPane.INFORMATION_MESSAGE);
        String startbalance = JOptionPane.showInputDialog(null, "Enter Starting Balance", "STARTING BALANCE ENTRY", JOptionPane.INFORMATION_MESSAGE);

        AccountData atm = new AccountData(pincode, customername, accounttype, accountnumber, startbalance);
        loadedcustomerlist.add(atm);
    }

    // Method to check if a pincode is already in use
    private boolean isPincodeUsed(String pincode) {
        for (AccountData atm : loadedcustomerlist) { //loaded
            if (pincode.equals(atm.pincode)) {
                return true;
            }
        }
        return false;
    }

    // Method to save all accounts to a file
    private void savePerson() {
      //  File file1 = new File("Recording.txt"); // Checking if file1 exists and has conten
       //File file2 = new File("Recordings.txt"); // Checking if file1 exists and has conten

       // boolean file1Exists = file1.exists() && file1.length() > 0;
       // boolean file2Exists = file2.exists() && file2.length() > 0;


        try (FileWriter fr = new FileWriter("Recording.txt");
             PrintWriter pw = new PrintWriter(fr);
             FileWriter fr1 = new FileWriter("Recordings.txt");
             PrintWriter pw1 = new PrintWriter(fr1)) {

           // captions
            pw1.printf("%-10s %-20s %-15s %-20s %-10s\n",
                    "PINCODE", "CUSTOMER NAME", "ACCOUNT TYPE", "ACCOUNT NUMBER", "BALANCE");

            for (AccountData atm : loadedcustomerlist) {
                String line = String.format("%s,%s,%s,%s,%s\n", atm.pincode, atm.customername, atm.accounttype, atm.accountnumber, atm.startbalance);
                String line1 = String.format("%-10s %-20s %-15s %-20s %-10s" ,
                        atm.pincode, atm.customername, atm.accounttype, atm.accountnumber, atm.startbalance);

                pw1.println(line1);
                pw.print(line);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to delete a person/account
    private void delete(String pincode) {
        for (int i = 0; i < loadedcustomerlist.size(); i++) {
            AccountData atm = loadedcustomerlist.get(i);
            if (pincode.equals(atm.pincode)) {
                int confirm = JOptionPane.showConfirmDialog(null, String.format("Do you really want to delete the following record?\n\nPINCODE: %s\nCustomer name: %s\nAccount Type: %s\nAccount Number: %s\nTotal Balance: %s",
                        atm.pincode, atm.customername, atm.accounttype, atm.accountnumber, atm.startbalance), "CONFIRMATION ABOUT DELETION", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    loadedcustomerlist.remove(i);
                    JOptionPane.showMessageDialog(null, "Account deleted successfully.");
                }
                break;
            }
        }
    }

    // Method to change PIN
    private void edit(String pincode) {
        for (AccountData atm : loadedcustomerlist) {
            if (pincode.equals(atm.pincode)) {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        String.format(
                                "Do you want to edit the PIN code for the following record?\n\n" +
                                        "PINCODE: %s\nCustomer name: %s\nAccount Type: %s\nAccount Number: %s\nStarting Balance: %s",
                                atm.pincode, atm.customername, atm.accounttype, atm.accountnumber, atm.startbalance
                        ),
                        "CONFIRMATION BOX",
                        JOptionPane.YES_NO_OPTION
                );

                if (option == JOptionPane.YES_OPTION) {
                    String newPincode = JOptionPane.showInputDialog(
                            null,
                            "Enter new PIN code to replace the old one",
                            "EDIT PINCODE",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    if (newPincode != null && !newPincode.trim().isEmpty()) {
                        atm.pincode = newPincode;
                        JOptionPane.showMessageDialog(
                                null,
                                "PIN code updated successfully.",
                                "SUCCESS",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        savePerson(); // Save changes to file
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "PIN code update was cancelled or invalid.",
                                "CANCELLED",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
                break;
            }
        }
    }


    //method to see all accounts
    private void seeAccount() {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("Recordings.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the file: " + e.getMessage());
            return;
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12)); //colum same gap (monospace )

        JScrollPane scrollPane = new JScrollPane(textArea);

       // scrollPane.setPreferredSize(new Dimension(500, 400));//scroll panel
        JOptionPane.showMessageDialog(this, scrollPane, "All Accounts", JOptionPane.INFORMATION_MESSAGE);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        String s1;
        if (sourceButton == addAccount) {
            addPersons();
        } else if (sourceButton == deleteAccount) {
            s1 = JOptionPane.showInputDialog(null, "Enter PinCode To Delete Account", "DELETION MENU", JOptionPane.INFORMATION_MESSAGE);
            delete(s1);
        }  else if (sourceButton == PINchange ) {
            s1 = JOptionPane.showInputDialog(null, "Enter PinCode for the  Account", "PIN Change ", JOptionPane.INFORMATION_MESSAGE);
            edit(s1);
        } else if (sourceButton == saveToFile) {
            savePerson(); }
           else if (sourceButton == seeAccounts) {
                seeAccount();
        } else if (sourceButton == logOut) {
            int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Good Bye", "ATM", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }

}
