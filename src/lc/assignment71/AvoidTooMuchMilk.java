package lc.assignment71;

/**
 *
 * @author Luke Chen
 */
class Controller {

    private int milk;
    private boolean out;
    private UI ui;

    public Controller(UI u) {
        milk = 0;
        out = false;
        ui = u;
    }

    public synchronized void leave() throws InterruptedException {
        while (milk > 0 || out == true) {
            ui.setRoommate(Thread.currentThread().getName(), "Stay at home.");
            wait();
        }
        out = true;
    }

    public synchronized void arrive() {
        out = false;
        milk++;
        ui.setBottles(Integer.toString(milk));
        notifyAll();
    }
}

class Roommate implements Runnable {

    Controller ctr;
    UI ui;

    Roommate(Controller c, UI u) {
        ctr = c;
        ui = u;
    }

    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 10000));
            ui.setRoommate(Thread.currentThread().getName(), "Arrive home.");
            Thread.sleep(5000);
            ctr.leave();
            ui.setRoommate(Thread.currentThread().getName(), "Leave for grocery.");
            Thread.sleep(5000);
            ui.setRoommate(Thread.currentThread().getName(), "Buy milk.");
            Thread.sleep(5000);
            ctr.arrive();
            ui.setRoommate(Thread.currentThread().getName(), "Arrive home, put milk in frig.");
        } catch (InterruptedException e) {
        }
    }
}

public class AvoidTooMuchMilk {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UI ui = new UI();
                Controller c = new Controller(ui);
                ui.setVisible(true);
                Roommate roommate1 = new Roommate(c, ui);
                Roommate roommate2 = new Roommate(c, ui);
                Thread r1 = new Thread(roommate1, "r1");
                Thread r2 = new Thread(roommate2, "r2");

                r1.start();
                r2.start();
            }
        });
    }
}
