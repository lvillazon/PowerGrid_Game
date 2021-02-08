package com.luisvillazon.game.main;
import javax.swing.JPanel;
import java.awt.*;
import java.time.ZonedDateTime;
import java.time.ZonedDateTime;

import com.luisvillazon.game.state.State;
import com.luisvillazon.game.state.LoadState;
import com.luisvillazon.framework.util.InputHandler;

public class Game extends JPanel implements Runnable {
    public final int TARGET_DRAW_TIME = 17; // notional time in ms for each frame update
    private int gameWidth, gameHeight;
    private Image gameImage;

    private Thread gameThread;
    private volatile boolean running;   // volatile to preserve state across threads
    private volatile State currentState;
    private long updateDurationMillis;


    private InputHandler inputHandler;

    public Game(int gameWidth, int gameHeight) {
        System.out.println("SEQUENCE: Game constructor");
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        setPreferredSize(new Dimension(gameWidth, gameHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocus();
    }

    public void setCurrentState(State newState) {
        System.out.println("SEQUENCE: change state:" + newState);
        System.gc();
        currentState = newState;
        currentState.init();
        inputHandler.setCurrentState(currentState); // ensure input handler also knows what state we are in
    }

    @Override
    public void addNotify() {
        System.out.println("SEQUENCE: Game addNotify");
        // called when the Game object is added to the JFrame
        // good place to put initialisation code for the game
        super.addNotify();
        initInput();
        setCurrentState(new LoadState());
        initGame();
    }

    private void initInput() {
        // assigns a dispatcher for mouse and keyboard events
        inputHandler = new InputHandler();
        addKeyListener(inputHandler);
        addMouseListener(inputHandler);
    }

    private void initGame() {
        // give the game its own thread
        System.out.println("SEQUENCE: Game thread started");
        running = true;
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }

    public long getFrameDrawTime() {
        return updateDurationMillis;
    }

    public int getFPS() {
        return (int)(1000/updateDurationMillis);
    }

    @Override
    public void run() {
        // Game loop
        long sleepDurationMillis = 0;

        while (running) {
            long beforeFrameDraw = System.nanoTime();
            currentState.update();
            prepareGameImage();
            currentState.render(gameImage.getGraphics());
            repaint();

            updateDurationMillis = (System.nanoTime() - beforeFrameDraw) / 1000000L;
            sleepDurationMillis = Math.max(2, TARGET_DRAW_TIME - updateDurationMillis);

           try {
                Thread.sleep(sleepDurationMillis);
            } catch (InterruptedException e) {
                System.out.println("Failed to sleep game loop!");
                e.printStackTrace();
            }
        }
        // quit when not running
        System.exit(0);
    }

    private void prepareGameImage() {
        if (gameImage == null) {
            gameImage = createImage(gameWidth, gameHeight);
        }
        Graphics g = gameImage.getGraphics();
        g.clearRect(0,0, gameWidth, gameHeight);
    }

    public void exit() {
        running = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameImage == null) {
            return;
        }
        g.drawImage(gameImage, 0, 0, null);
    }
}
