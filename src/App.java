import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 1400;
        int boardHeight = 730;

        JFrame fream = new JFrame("ملا نصرالدین");

        fream.setSize(boardWidth, boardHeight);
        fream.setLocationRelativeTo(null);
        fream.setResizable(false);
        fream.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MollaNasruddin mollaNasruddin = new MollaNasruddin();
        fream.add(mollaNasruddin);

        // makes full screen up to header
        fream.pack();
        mollaNasruddin.requestFocus();
        fream.setVisible(true);

    }
}
