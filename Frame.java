import javax.swing.*;

public class Frame extends JFrame {

    Frame(){
        Game game = new Game();
        this.add(game);
        this.setTitle("Flappy Bird");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
