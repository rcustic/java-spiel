package de.junkerjoerg12;

import de.junkerjoerg12.levels.Endscreen;
import de.junkerjoerg12.levels.Leveldetails;
import de.junkerjoerg12.levels.Lvlauswahl;
import de.junkerjoerg12.map.Map;
import de.junkerjoerg12.map.mapElements.Water;
import de.junkerjoerg12.tools.Console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JFrame implements ActionListener, KeyListener {

    // keybinds
    private int keyRight = 68;
    private int keyLeft = 65;
    private int keyJump = 32;
    private int keyConsole = 130;

    // auf welchem Monitor das Spiel angezeigt werden soll
    // nur während entwicklung wichtig
    private byte monitor = 1;

    private MainMenu mainMenu;
    private Map map;
    public Console console;
    private Lvlauswahl lvlauswahl;
    private Endscreen endscreen;

    private final int targetFPS = 60;

    private double delayBetweenFrames; // in Millisekunden

    private Timer timer;
    private Gameloop gameloop;
    private Timer imageSwitcher;
    // private Thread timer;

    // misst die Zeit, die das Spiel Läuft
    private double upTime;

    private boolean autostart = false;// ob sich das Spiel gleich startet oder man erst ins Main Menue kommt

    public boolean buildMode = true;

    // test
    Timer timerm;
    int calls = 0;
    public int updates = 0;
    public int draws = 0;
    long start = 0;
    long afterUpdate = 0;
    long fertig;

    public Game() {
        delayBetweenFrames = Math.floor(1.0 / targetFPS * 1000);

        timerm = new Timer(1000, this);
        imageSwitcher = new Timer(700, this);
        gameloop = new Gameloop((long) delayBetweenFrames, this);

        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        if (Toolkit.getDefaultToolkit().getScreenSize().equals(new Dimension(1920, 1080))) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            this.setSize(1920, 1080);
        }
        this.setUndecorated(true);
        this.setLayout(new BorderLayout());

        // öffnet spiel auf gewünschtem Monitor
        this.setLocation(GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getScreenDevices()[monitor - 1]
                .getDefaultConfiguration()
                .getBounds()
                .getLocation());

        mainMenu();
        this.setVisible(true);

        // timer = new Timer((int) delayBetweenFrames, this);
        // timer.setRepeats(true);
        // timer = new Thread();

        this.addKeyListener(this);

        if (autostart) {
            start();
        }
    }

    public void mainMenu() {
        this.mainMenu = new MainMenu(this);
        this.add(mainMenu, BorderLayout.CENTER);
        revalidate();
        repaint();
        requestFocus();
    }

    public void addmap(String filepath) {
        remove(lvlauswahl);
        map = new Map(this, filepath);
        map.setVisible(true);
        this.add(map, BorderLayout.CENTER);
        revalidate();
        repaint();

        this.requestFocus();
        // timer.start();
        gameloop.start();
        imageSwitcher.start();
        timerm.start();
        // run();

    }

    public void start() {
        lvlauswahl = new Lvlauswahl(this);
        remove(mainMenu);
        this.add(lvlauswahl, BorderLayout.CENTER);
        revalidate();
        repaint();
        this.requestFocus();
    }

    public void switchScene(JPanel oldpanel, JPanel newpanel) { // sollte bspw Settings removen und lvlauswahl adden
        remove(oldpanel);
        add(newpanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        this.requestFocus();
    }

    public void pause() {

    }

    /*
     * public void returntomainmenu() {
     * remove(map);
     * mainMenu();
     * }
     */

    public void setEndscreen() {
        // hier zeit abfragen und an endscreen übergeben
        endscreen = new Endscreen(this, "hier sollte die gebrauchte Zeit hin");
        remove(map);
        add(endscreen, BorderLayout.CENTER);
        revalidate();
        repaint();
        this.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // wird immer wieder vom Timer aufgerufen, ist quasi die Gameloop
        if (e.getSource() == timer) {
        } else if (e.getSource() == timerm) {
            System.out.println("calls: " + calls);
            System.out.println("updates: " + updates);
            System.out.println("draws: " + draws);
            System.out.println("update Time: " + (afterUpdate - start));
            System.out.println("drawTime: " + (fertig - afterUpdate));
            calls = 0;
            updates = 0;
            draws = 0;
        } else if (e.getSource() == imageSwitcher) {
            // switch image methode von jeder Mapobjekt klasse aufrufen
            Water.switchImage();
        }
    }

    public void tick() {
        calls++;
        upTime += delayBetweenFrames;
        start = System.currentTimeMillis();
        map.update();
        afterUpdate = System.currentTimeMillis();
        map.draw();
        fertig = System.currentTimeMillis();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // if (e.getKeyCode() == keyConsole) {
        // if (console == null) {
        // console = new Console(map);
        // } else {
        // console.setVisible(!console.isVisible());
        // }
        // }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == keyRight) {
            if (!map.getPlayer().collisionRight(map.getAllObjects())) {
                map.getPlayer().walkRight = true;
            }
        } else if (e.getKeyCode() == keyLeft) {
            if (!map.getPlayer().collisionLeft(map.getAllObjects())) {
                map.getPlayer().walkLeft = true;
            }
        } else if (e.getKeyCode() == keyJump) {
            if (map.getPlayer().collisionBottom(map.getAllObjects())) {
                map.getPlayer().jump = true;
            }
        } else if (e.getKeyCode() == keyConsole) {
            if (console == null) {
                console = new Console(this);
            } else {
                console.setVisible(!console.isVisible());
            }
        }

        /*
         * // geht nur mit final modifier vor Keybinds (eigentlich unnötig, da es auch
         * mit ifs geht aber man weiß ja nicht ob noch iwas kaputt geht)
         * switch (e.getKeyCode()) {
         * case keyRight:
         * if (!map.getPlayer().collisionRight(map.getAllObjects())) {
         * map.getPlayer().walkRight = true;
         * }
         * break;
         * case keyLeft:
         * if (!map.getPlayer().collisionLeft(map.getAllObjects())) {
         * map.getPlayer().walkLeft = true;
         * }
         * break;
         * case keyJump:
         * if (map.getPlayer().collisionBottom(map.getAllObjects())) {
         * map.getPlayer().jump = true;
         * }
         * break;
         * case keyConsole:
         * if (console == null) {
         * console = new Console(this);
         * } else {
         * console.setVisible(!console.isVisible());
         * }
         * break;
         * default:
         * break;
         * }
         */

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == keyRight) {
            map.getPlayer().walkRight = false;
        } else if (e.getKeyCode() == keyLeft) {
            map.getPlayer().walkLeft = false;
        } else if (e.getKeyCode() == keyJump) {
            map.getPlayer().jump = false;
        }

        /*
         * // geht nur mit final modifier vor Keybinds
         * switch (e.getKeyCode()) {
         * case keyRight:
         * map.getPlayer().walkRight = false;
         * break;
         * case keyLeft:
         * map.getPlayer().walkLeft = false;
         * break;
         * case keyJump:
         * map.getPlayer().jump = false;
         * default:
         * break;
         * }
         */
    }

    public double getUptime() {
        return upTime;
    }

    public Map getMap() {
        return map;
    }

    public double getDelaybetweenFrames() {
        return delayBetweenFrames;
    }

    public int getjumpkey() {
        return keyJump;
    }

    public int getleftkey() {
        return keyLeft;
    }

    public int getrightkey() {
        return keyRight;
    }

    public int getconsolekey() {
        return keyConsole;
    }

    public void setjumpkey(int key) {
        this.keyJump = key;
    }

    public void setleftkey(int key) {
        this.keyLeft = key;
    }

    public void setrightkey(int key) {
        this.keyRight = key;
    }

    public void setconsolekey(int key) {
        this.keyConsole = key;
    }

    public boolean alreadybound(int key) {
        if (key == keyJump) {
            return true;
        } else if (key == keyRight) {
            return true;
        } else if (key == keyLeft) {
            return true;
        } else if (key == keyConsole) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        new Game();
        // ArrayList<Floor> l1 = new ArrayList<>();
        // ArrayList<Floor> l2 = new ArrayList<>();
        // long start;

        // for (int i = 0; i < 50; i++) {
        // l1.add(new Floor(null));
        // l2.add(new Floor(null));
        // }

        // start = System.nanoTime();
        // int size = l2.size();
        // for (int i = 0; i < size; i++) {
        // l2.get(i).calculatePosition();
        // }
        // System.out.println(System.nanoTime() - start);

        // start = System.nanoTime();
        // for (int i = 0; i < l2.size(); i++) {
        // l2.get(i).calculatePosition();
        // }
        // System.out.println(System.nanoTime() - start);

        // start = System.nanoTime();
        // for (Floor floor : l1) {
        // floor.calculatePosition();
        // }
        // System.out.println(System.nanoTime() - start);

    }
}
