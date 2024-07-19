import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class MollaNasruddin extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 1400;
    int boardHeight = 640;

    Image backgroungImg;
    Image mollaImg;
    Image thornLeftImg;
    Image thornTopImg;
    Image trunkImg1;

    // Molla
    int mollaX = 370;
    int mollaY = 0;
    int mollaWidth = 100;
    int mollaHeight = 150;

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
    int trunkWidth = 150;
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
    int thornX = boardWidth;
    int thornY = -150;
    int thornWidth = 150;
    int thornHeight = 350;

    class Thorn {
        int x = thornX;
        int y = thornY;
        int width = thornWidth;
        int height = thornHeight;
        Image img;

        Thorn(Image img) {
            this.img = img;
        }
    }

    // game logic
    Molla molla;

    int trunkVelocityX = -4; // move trunk to the left
    int ThornVelocityX = -7; // move trunk to the left
    int velocityY = -10;
    int gravity = 1;

    ArrayList<Trunk> trunks;
    ArrayList<Thorn> thorns;
    Random random = new Random();

    Timer gameLoop;
    Timer placeTrnukTimer;
    Timer placeThornTimer;

    boolean gameOver = false;
    boolean flag = false;

    MollaNasruddin() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        // make sure that the changes are on the selected element(eg : molla)
        setFocusable(true);
        // make sure that we checked three listener functions when we press button
        addKeyListener(this);

        // images
        backgroungImg = new ImageIcon(getClass().getResource("img/bgdark.png")).getImage();
        mollaImg = new ImageIcon(getClass().getResource("img/molllla.png")).getImage();
        thornLeftImg = new ImageIcon(getClass().getResource("img/thorn.png")).getImage();
        thornTopImg = new ImageIcon(getClass().getResource("img/bat.gif")).getImage();
        trunkImg1 = new ImageIcon(getClass().getResource("img/b.png")).getImage();

        // molla
        molla = new Molla(mollaImg);
        // trunks
        trunks = new ArrayList<Trunk>();
        // thorn
        thorns = new ArrayList<Thorn>();

        // place trunk timer
        placeTrnukTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeTrunk();
            }
        });
        placeTrnukTimer.start();

        // place thorn timer
        placeThornTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeThorn();
            }
        });
        placeThornTimer.start();
        // game timer
        gameLoop = new Timer(1000 / 70, this);
        gameLoop.start();
    }

    public void placeTrunk() {
        int randomTrunky = (int) (Math.random() * 100);

        Trunk trunk = new Trunk(trunkImg1);
        trunk.y -= randomTrunky;
        trunks.add(trunk);

    }

    public void placeThorn() {
        int randomThorny = (int) (Math.random() * 150);

        Thorn thorn = new Thorn(thornTopImg);
        thorn.y += randomThorny;
        thorns.add(thorn);
    }

    // @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // background
        g.drawImage(backgroungImg, 0, 0, boardWidth, 780, null);
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
        for (int i = 0; i < thorns.size(); i++) {
            Thorn thorn = thorns.get(i);
            g.drawImage(thorn.img, thorn.x, thorn.y, thorn.width, thorn.height, null);
        }

    }

    public void move() {
        velocityY += gravity;
        molla.y += velocityY;

        molla.y = Math.max(molla.y, 0);
        molla.y = Math.min(molla.y, 480);

        // trunk and thorn
        for (int i = 0; i < (thorns.size()); i++) {

            Trunk trunk = trunks.get(i);
            trunk.x += trunkVelocityX;

            Thorn thorn = thorns.get(i);
            thorn.x += ThornVelocityX;

            if (thornCollision(molla, thorn) || trunkCollision(molla, trunk)) {
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

    public boolean thornCollision(Molla molla, Thorn thorn) {
        return molla.x - 60 < thorn.width + thorn.x && molla.x + molla.width - 60 > thorn.x
                && molla.y < thorn.y + thorn.height - 100 && molla.y + molla.height > thorn.y - 50;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeTrnukTimer.stop();
            placeThornTimer.stop();
            gameLoop.stop();

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -10;
            if (gameOver) {
                molla.y = mollaY;
                molla.x = mollaX;
                trunks.clear();
                thorns.clear();
                gameOver = false;
                gameLoop.start();
                placeThornTimer.start();
                placeTrnukTimer.start();

            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            molla.y += 6;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            molla.x += 12;
            molla.x = Math.min(molla.x, 900);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            molla.x -= 12;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
