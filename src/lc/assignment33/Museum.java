package lc.assignment33;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class Control {
    private boolean opened = true;
    private int visitors = 0;

    public synchronized void arrive() throws InterruptedException {
        while (!opened) wait();
        ++visitors;
        System.out.println("Vistors: "+visitors);
        notifyAll();
    }

    public synchronized void depart() throws InterruptedException {
        while (visitors <= 0) wait();
        --visitors;
        System.out.println("Vistors: "+visitors);
        notifyAll();
    }

    public synchronized void open() throws InterruptedException {
        while (visitors > 0) wait();
        opened = true;
        notifyAll();
    }

    public synchronized void close() {
        opened = false;
    }
}

class East implements Runnable {

    Control ctr;

    East(Control c) {
        ctr = c;
    }

    public void run() {
        try {
            while (true) {
                System.out.println("East: A visitor arrives.");
                Thread.sleep((long) (Math.random() * 2000));
                ctr.arrive();
            }
        } catch (InterruptedException e) {}
    }
}

class West implements Runnable {

    Control ctr;

    West(Control c) {
        ctr = c;
    }

    public void run() {
        try {
            while (true) {
                ctr.depart();
                System.out.println("West: A visitor departs.");
                Thread.sleep((long) (Math.random() * 3000));
            }
        } catch (InterruptedException e) {}
    }
}

class Director implements Runnable {

    Control ctr;

    Director(Control c) {
        ctr = c;
    }

    public void run() {
        try {
            
            while (true) {
                ctr.open();
                System.out.println("===== Director: Open. =====");
                Thread.sleep((long) (10000));
                ctr.close();
                System.out.println("===== Director: Close.=====");
                Thread.sleep((long) (10000));
            }
        } catch (InterruptedException e) {}
    }
}

public class Museum {

    public static void main(String[] args) {
        Control c = new Control();
        East e = new East(c);
        West w = new West(c);
        Director d = new Director(c);
        Thread est = new Thread(e);
        Thread wst = new Thread(w);
        Thread dir = new Thread(d);

        est.start();
        wst.start();
        dir.start();
    }
}
