import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class MollaNasruddin extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 1400;
    int boardHeight = 670;

    Image backgroungImg;
    Image mollaImg;
    Image thornLeftImg;
    Image batImg;
    Image trunkImg;

    // Molla
    int mollaX = 370;
    int mollaY = 0;
    int mollaWidth = 190;
    int mollaHeight = 210;

    class Molla {
        int x = mollaX;
        int y = mollaY;
        int width = mollaWidth;
        int height = mollaHeight;
        Image img;

        Molla(Image img) {
            this.img = img;
        }
    }

    // trunk
    int trunkX = boardWidth;
    int trunkY = 600;
    int trunkWidth = 100;
    int trunkHeight = 500;

    class Trunk {
        int x = trunkX;
        int y = trunkY;
        int width = trunkWidth;
        int height = trunkHeight;
        Image img;
        boolean passed = false;

        Trunk(Image img) {
            this.img = img;
        }
    }

    // thorn
    int BatX = boardWidth;
    int BatY = -150;
    int BatWidth = 300;
    int BatHeight = 350;

    class Bat {
        int x = BatX;
        int y = BatY;
        int width = BatWidth;
        int height = BatHeight;
        Image img;

        Bat(Image img) {
            this.img = img;
        }
    }

    // game logic
    Molla molla;

    int trunkVelocityX = -4; // move trunk to the left
    int BatVelocityX = -7; // move trunk to the left
    int velocityY = -10;
    int gravity = 1;

    ArrayList<Trunk> trunks;
    ArrayList<Bat> bats;
    Random random = new Random();

    Timer gameLoop;
    Timer placeTrnukTimer;
    Timer placeBatTimer;

    boolean gameOver = false;
    boolean flag = false;

    MollaNasruddin() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        // make sure that the changes are on the selected element(eg : molla)
        setFocusable(true);
        // make sure that we checked three listener functions when we press button
        addKeyListener(this);

        // images
        backgroungImg = new ImageIcon(getClass().getResource("bgLight.png")).getImage();
        mollaImg = new ImageIcon(getClass().getResource("mn.png")).getImage();
        thornLeftImg = new ImageIcon(getClass().getResource("thorn.png")).getImage();
        batImg = new ImageIcon(getClass().getResource("bat.gif")).getImage();
        trunkImg = new ImageIcon(getClass().getResource("trunk2.png")).getImage();

        // molla
        molla = new Molla(mollaImg);
        // trunks
        trunks = new ArrayList<Trunk>();
        // thorn
        bats = new ArrayList<Bat>();

        // place trunk timer
        placeTrnukTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeTrunk();
            }
        });
        placeTrnukTimer.start();

        // place thorn timer
        placeBatTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeThorn();
            }
        });
        placeBatTimer.start();
        // game timer
        gameLoop = new Timer(1000 / 70, this);
        gameLoop.start();
    }

    public void placeTrunk() {
        int randomTrunky = (int) (Math.random() * 100);

        Trunk trunk = new Trunk(trunkImg);
        trunk.y -= randomTrunky;
        trunks.add(trunk);

    }

    public void placeThorn() {
        int randomThorny = (int) (Math.random() * 150);

        Bat bat = new Bat(batImg);
        bat.y += randomThorny;
        bats.add(bat);
    }

    // @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // background
        g.drawImage(backgroungImg, 0, 0, boardWidth, 700, null);
        // thorn خار
        g.drawImage(thornLeftImg, -50, 0, 100, boardHeight + 500, null);
        g.drawImage(thornLeftImg, -30, 0, 100, boardHeight + 475, null);
        // molla
        g.drawImage(molla.img, molla.x, molla.y, molla.width, molla.height, null);
        // trunk
        for (int i = 0; i < trunks.size(); i++) {
            Trunk trunk = trunks.get(i);
            g.drawImage(trunk.img, trunk.x, trunk.y, trunk.width, trunk.height, null);
        }
        // thorn
        for (int i = 0; i < bats.size(); i++) {
            Bat bat = bats.get(i);
            g.drawImage(bat.img, bat.x, bat.y, bat.width, bat.height, null);
        }

    }

    public void move() {
        velocityY += gravity;
        molla.y += velocityY;

        molla.y = Math.max(molla.y, 0);
        molla.y = Math.min(molla.y, 445);

        // trunk and thorn
        for (int i = 0; i < (bats.size()); i++) {

            Trunk trunk = trunks.get(i);
            trunk.x += trunkVelocityX;

            Bat bat = bats.get(i);
            bat.x += BatVelocityX;

            if (batCollision(molla, bat) || trunkCollision(molla, trunk)) {
                gameOver = true;
            }
        }
        if (molla.x <= 40) {
            gameOver = true;
        }
    }

    public boolean trunkCollision(Molla molla, Trunk trunk) {
        return molla.x < trunk.width + trunk.x && molla.x + molla.width > trunk.x
                && molla.y < trunk.height + trunk.y && molla.y + molla.height > trunk.y;

    }

    public boolean batCollision(Molla molla, Bat bat) {
        return molla.x - 60 < bat.width + bat.x && molla.x + molla.width - 60 > bat.x
                && molla.y < bat.y + bat.height - 100 && molla.y + molla.height > bat.y - 50;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeTrnukTimer.stop();
            placeBatTimer.stop();
            gameLoop.stop();

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -10;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            molla.y += 6;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            molla.x += 12;
            molla.x = Math.min(molla.x, 900);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            molla.x -= 12;
        }

        // restart the game
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (gameOver) {
                molla.y = mollaY;
                molla.x = mollaX;
                trunks.clear();
                bats.clear();
                gameOver = false;
                gameLoop.start();
                placeBatTimer.start();
                placeTrnukTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
