package MarioSDK.component;

import java.awt.Graphics;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;

import javax.swing.JFrame;

import MarioSDK.helper.GameStatus;
import MarioSDK.helper.MarioActions;

public class GamePlay {

    private static final long MAX_TIME = 40;

    private static final long GRACE_TIME = 10;

    private static final int WIDTH = 256;

    private static final int HEIGHT = 256;

    private static final int TILE_WIDTH = WIDTH / 16;

    private static final int TILE_HEIGHT = HEIGHT / 16;

    private static final boolean VERBOSE = false;

    private boolean isPause = false;

    private JFrame window = null;
    private Render render = null;
    private Agent agent = null;
    private World world = null;

    public GamePlay() {

    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static int getTileHeight() {
        return TILE_HEIGHT;
    }

    public static int getTileWidth() {
        return TILE_WIDTH;
    }

    public static boolean getVerbose() {
        return VERBOSE;
    }

    private int getDelay(int fps) {
        if (fps <= 0) {
            return 0;
        }
        return 1000 / fps;
    }

    private void setAgent(Agent agent) {
        this.agent = agent;
        if (agent instanceof KeyAdapter) {
            this.render.addKeyListener((KeyAdapter) this.agent);
        }
    }

    public Result runGame(Agent agent, String level, int timer) {
        return this.runGame(agent, level, timer, 0, false, 0, 2);
    }

    public Result runGame(Agent agent, String level, int timer, int marioState, boolean visuals) {
        return this.runGame(agent, level, timer, marioState, visuals, visuals ? 40 : 0, 2);
    }

    public Result runGame(Agent agent, String level, int timer, int marioState, boolean visuals, int fps, float scale) {
        if (visuals) {
            this.window = new JFrame("Agent Vinod");
            this.render = new Render(scale);
            this.window.setContentPane(this.render);
            this.window.pack();
            this.window.setResizable(false);
            this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.render.init();
            this.window.setVisible(true);
        }
        this.setAgent(agent);
        return this.gameLoop(level, timer, marioState, visuals, fps);
    }

    private Result gameLoop(String level, int timer, int marioState, boolean visual, int fps) {
        this.world = new World();
        this.world.visuals = visual;
        this.world.initializeLevel(level, 1000 * timer);
        if (visual) {
            this.world.initializeVisuals(this.render.getGraphicsConfiguration());
        }
        this.world.mario.isLarge = marioState > 0;
        this.world.mario.isFire = marioState > 1;
        this.world.update(new boolean[MarioActions.numberOfActions()]);
        long currentTime = System.currentTimeMillis();

        VolatileImage renderTarget = null;
        Graphics backBuffer = null;
        Graphics currentBuffer = null;
        if (visual) {
            renderTarget = this.render.createVolatileImage(GamePlay.WIDTH, GamePlay.HEIGHT);
            backBuffer = this.render.getGraphics();
            currentBuffer = renderTarget.getGraphics();
            this.render.addFocusListener(this.render);
        }

        MTimer agentTimer = new MTimer(GamePlay.MAX_TIME);
        this.agent.init(new AStarModel(this.world.clone()), agentTimer);

        ArrayList<Event> gameEvents = new ArrayList<>();
        while (this.world.gameStatus == GameStatus.RUNNING) {
            if (!this.isPause) {
                agentTimer = new MTimer(GamePlay.MAX_TIME);
                boolean[] actions = this.agent.actions(new AStarModel(this.world.clone()), agentTimer);
                if (GamePlay.VERBOSE) {
                    if (agentTimer.getRemainingTime() < 0 && Math.abs(agentTimer.getRemainingTime()) > GamePlay.GRACE_TIME) {
                        System.out.println("The Agent is slowing down the game by: "
                                + Math.abs(agentTimer.getRemainingTime()) + " msec.");
                    }
                }
                this.world.update(actions);
                gameEvents.addAll(this.world.lastFrameEvents);
  }

            if (visual) {
                this.render.renderWorld(this.world, renderTarget, backBuffer, currentBuffer);
            }
            if (this.getDelay(fps) > 0) {
                try {
                    currentTime += this.getDelay(fps);
                    Thread.sleep(Math.max(0, currentTime - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        return new Result(this.world, gameEvents);
    }
}
