import javax.swing.JButton;
import javax.swing.JFrame;

public class AdminButton {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Admin Button Example");


        frame.setSize(400, 200);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JButton adminButton = new JButton("Admin");


        adminButton.setBounds(150, 80, 100, 30); // (x, y, width, height)


        frame.add(adminButton);


        frame.setLayout(null);


        frame.setVisible(true);
    }
}
