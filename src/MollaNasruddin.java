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

    // bat
    int BatX = boardWidth;
    int BatY = -50;
    int BatWidth = 200;
    int BatHeight = 250;

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
    int BatVelocityX = -8; // move bat to the left
    int gravity = 1; // جاذبه بری پایین آوردن
    int velocityY = -10; // ضد جاذبه بری بالا بردن

    ArrayList<Trunk> trunks;
    ArrayList<Bat> bats;
    Random random = new Random();

    Timer gameLoop;
    Timer placeTrnukTimer;
    Timer placeBatTimer;

    boolean gameOver = false;
    boolean flag = false;
    int score = 0;
    int bestScore;

    // molla constructor
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
        // bats
        bats = new ArrayList<Bat>();

        // place trunk timer
        placeTrnukTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeTrunk();
            }
        });
        placeTrnukTimer.start();

        // place bat timer
        placeBatTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeBat();
            }
        });
        placeBatTimer.start();
        // game timer
        gameLoop = new Timer(1000 / 80, this);
        gameLoop.start();
    }

    public void placeTrunk() {
        int randomTrunkY = (int) (Math.random() * 100) + 50;

        Trunk trunk = new Trunk(trunkImg);
        trunk.y -= randomTrunkY;
        trunks.add(trunk);

    }

    public void placeBat() {
        int randomBatY = (int) (Math.random() * 180 + 15);

        Bat bat = new Bat(batImg);
        bat.y += randomBatY;
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
        // left thorn خارها سمت چپ
        g.drawImage(thornLeftImg, -50, 0, 100, boardHeight + 500, null);
        g.drawImage(thornLeftImg, -30, 0, 100, boardHeight + 475, null);
        // molla ملا
        g.drawImage(molla.img, molla.x, molla.y, molla.width, molla.height, null);
        // trunk تنه درخت
        for (int i = 0; i < trunks.size(); i++) {
            Trunk trunk = trunks.get(i);
            g.drawImage(trunk.img, trunk.x, trunk.y, trunk.width, trunk.height, null);
        }
        // bat خفاش
        for (int i = 0; i < bats.size(); i++) {
            Bat bat = bats.get(i);
            g.drawImage(bat.img, bat.x, bat.y, bat.width, bat.height, null);
        }

        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("وی خداجو. تو که بُمُردی بچِم؟!", boardWidth / 2 - 270, 160);
        } else {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("امتیاز : " + String.valueOf(score), 50, 32);
        }

    }

    // for moving trunk and bat
    public void moveObstakles() {
        velocityY += gravity;
        molla.y += velocityY;

        molla.y = Math.max(molla.y, 0);
        molla.y = Math.min(molla.y, 445);

        // trunk and bat
        for (int i = 0; i < (bats.size()); i++) {

            Trunk trunk = trunks.get(i);
            trunk.x += trunkVelocityX;

            Bat bat = bats.get(i);
            bat.x += BatVelocityX;

            if (!trunk.passed && molla.x > trunk.x + trunk.width) {
                trunk.passed = true;
                score += 1;
            }

            if (batCollision(molla, bat) || trunkCollision(molla, trunk)) {
                gameOver = true;
            }
        }
        if (molla.x <= 40) {
            gameOver = true;
        }
    }

    // collision to the bottom trunk
    public boolean trunkCollision(Molla molla, Trunk trunk) {
        return molla.x + 10 < trunk.width + trunk.x && molla.x + molla.width - 30 > trunk.x
                && molla.y + 10 < trunk.height + trunk.y && molla.y + molla.height > trunk.y + 10;
    }

    // collision to the top bat
    public boolean batCollision(Molla molla, Bat bat) {
        return molla.x + 80 < bat.width + bat.x && molla.x + molla.width - 100 > bat.x
                && molla.y < bat.y + bat.height - 100 && molla.y + molla.height > bat.y - 50;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveObstakles();
        repaint();
        update();

        // restart and close the game
        if (gameOver) {
            placeTrnukTimer.stop();
            placeBatTimer.stop();
            gameLoop.stop();
            if (score >= bestScore) {
                bestScore = score;
            }
            // change button text to farsi
            UIManager.put("OptionPane.yesButtonText", "دوباره بازی میکنم");
            UIManager.put("OptionPane.noButtonText", "بسه دیگه، مونده شدم");
            // for changing text direction to farsi
            JFrame frame = new JFrame();
            frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            int result = JOptionPane.showConfirmDialog(
                    frame,
                    "بهترین امتیاز : " + bestScore + " \nامتیاز تو : " + score,
                    "خودی تو یکبار دیگه بازی نکنیم جوان؟؟",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                System.out.println("User chose Yes");
            } else if (result == JOptionPane.NO_OPTION) {
                System.out.println("User chose No");
            }

            if (result == JOptionPane.YES_OPTION) {
                // If User clicked YES reset the game conditions and variables
                molla.y = mollaY;
                molla.x = mollaX;
                trunks.clear();
                bats.clear();
                score = 0;
                gameOver = false;
                tap3 = true;
                isSpacePressed = false;
                isRightArrowPressed = false;
                isLeftArrowPressed = false;
                isDownArrowPressed = false;
                gameLoop.start();
                placeBatTimer.start();
                placeTrnukTimer.start();
            } else {
                System.exit(0);
            }

        }
    }

    int tapCount = 0;
    boolean tap3 = true;
    boolean isSpacePressed = false;
    boolean isRightArrowPressed = false;
    boolean isLeftArrowPressed = false;
    boolean isDownArrowPressed = false;

    // code for jumping 1 time whith simultaneously movement
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            isSpacePressed = true;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            isRightArrowPressed = true;
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            isDownArrowPressed = true;
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            isLeftArrowPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            isSpacePressed = false;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            isRightArrowPressed = false;
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            isLeftArrowPressed = false;
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            isDownArrowPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void update() {
        if (isSpacePressed && tap3) {
            velocityY = -23;
            tapCount++;
            if (tapCount == 3) {
                tap3 = false;
                tapCount = 0;
            }
        }
        if (molla.y >= 430) {
            tap3 = true;
        }
        if (isRightArrowPressed) {
            molla.x += 10;
            molla.x = Math.min(molla.x, 1000);
        }
        if (isLeftArrowPressed) {
            molla.x -= 10;
        }
        if (isDownArrowPressed) {
            molla.y += 7;
        }
    }

    // code for jumping no more 3 times and not simultaneously movement

    // public void keyPressed(KeyEvent e) {
    // if ((e.getKeyCode() == KeyEvent.VK_SPACE) && tap3) {
    // velocityY = -20;
    // tapCount++;
    // if (tapCount == 3) {
    // tap3 = false;
    // tapCount = 0;
    // }
    // }
    // if (molla.y >= 430) {
    // tap3 = true;
    // }
    // if (e.getKeyCode() == KeyEvent.VK_DOWN) {
    // molla.y += 7;
    // }

    // if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
    // molla.x += 12;
    // molla.x = Math.min(molla.x, 900);
    // }
    // if (e.getKeyCode() == KeyEvent.VK_LEFT) {
    // molla.x -= 12;
    // }
    // }

    // @Override
    // public void keyTyped(KeyEvent e) {
    // }

    // @Override
    // public void keyReleased(KeyEvent e) {
    // }
}
