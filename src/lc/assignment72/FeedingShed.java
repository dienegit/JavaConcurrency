package lc.assignment72;

/**
 *
 * @author Luke Chen
 */
class Shed {

    private int insideBirds;
    private boolean door[] = new boolean[4];
    UI ui;

    public Shed(UI u) {
        insideBirds = 0;
        ui = u;
    }

    public synchronized void open(int doorID) throws InterruptedException {
        while (door[doorID] == true) {
            wait();
            ui.appendText("Bird " + Thread.currentThread().getName() + " is waiting at door " + doorID);
        }
        door[doorID] = true;
        ui.setDoor(doorID, "opened");
        notifyAll();
    }

    public synchronized void close(int doorID) throws InterruptedException {
        door[doorID] = false;
        ui.setDoor(doorID, "closed");
        notifyAll();
    }

    public synchronized boolean enter() throws InterruptedException {
        if (insideBirds == 3) {
            ui.appendText("Shed is full; Bird " + Thread.currentThread().getName() + " flies away, and will try again later.");
            return false;
        } else {
            ui.appendText("Bird " + Thread.currentThread().getName() + " enters.");
            insideBirds++;
            ui.setShed(insideBirds);
            return true;
        }
    }

    public synchronized void leave() throws InterruptedException {
        insideBirds--;
        ui.setShed(insideBirds);
    }
}

class Bird implements Runnable {

    Shed shed;
    boolean isEntered;
    UI ui;
    int doorID;

    Bird(Shed s, UI u) {
        shed = s;
        isEntered = false;
        ui = u;
    }

    public void run() {
        try {
            do {
                Thread.sleep((long) (Math.random() * 10000));
                ui.appendText("Bird " + Thread.currentThread().getName() + " wish to enter.");
                Thread.sleep(5000);
                doorID = (int) (Math.random() * 4);
                shed.open(doorID);
                ui.appendText("Bird " + Thread.currentThread().getName() + " opens door " + doorID + " to enter.");
                Thread.sleep(5000);
                isEntered = shed.enter();
                shed.close(doorID);
                ui.appendText("Door " + doorID + " is closed.");
            } while (!isEntered);
            Thread.sleep((long) (Math.random() * 10000));
            doorID = (int) (Math.random() * 4);
            shed.open(doorID);
            ui.appendText("Bird " + Thread.currentThread().getName() + " opens door " + doorID + " to leave.");
            Thread.sleep(5000);
            shed.leave();
            ui.appendText("Bird " + Thread.currentThread().getName() + " leaves.");
            shed.close(doorID);
            ui.appendText("Door " + doorID + " is closed.");
        } catch (InterruptedException e) {
        }
    }
}

public class FeedingShed {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UI ui = new UI();
                Shed s = new Shed(ui);
                ui.setVisible(true);
                for (int i = 1; i <= 10; i++) {
                    new Thread(new Bird(s, ui), Integer.toString(i)).start();
                }
            }
        });
    }
}
