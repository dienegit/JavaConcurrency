package lc.assignment63;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class Barber {

    Semaphore mutex;
    int waitingCustomers;
    int waitingChairs;
    private UI ui;

    final class Semaphore {

        private int value;

        public Semaphore(int v) {
            value = v;
        }

        public synchronized void P(int customerID) {
            if (waitingCustomers == 1 && value > 0) {
                ui.appendText("Customer " + customerID + " wakes a barber up.");
            }
            while (value <= 0) {
                try {
                    ui.appendText("Customer " + customerID + " is waiting.");
                    wait();
                } catch (InterruptedException e) {
                }
            }
            value--;
            ui.setBarbers(Integer.toString(value));
        }

        public synchronized void V() {
            ++value;
            ui.setBarbers(Integer.toString(value));
            notify();
        }
    }

    public Barber(UI u) {
        waitingCustomers = 0;
        waitingChairs = 5;
        mutex = new Semaphore(2);
        ui = u;
    }

    public int startWaiting() {
        waitingCustomers++;
        return waitingCustomers;
    }

    public int endWaiting() {
        waitingCustomers--;
        return waitingCustomers;
    }

}

class Customer implements Runnable {

    Barber bb;
    int customerID;
    boolean freeChair;
    UI ui;

    public Customer(int r, Barber bb, UI u) {
        customerID = r + 1;
        this.bb = bb;
        freeChair = false;
        ui = u;
    }

    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 20000));
            ui.appendText("Customer " + customerID + " arrives.");
            if (bb.startWaiting() > bb.waitingChairs) {
                ui.appendText("Customer " + customerID + " leaves. (no free chair)");
                bb.waitingCustomers--;
            } else {
                freeChair = true;
            }
            if (freeChair == true) {
                bb.mutex.P(customerID);
                ui.appendText("Customer " + customerID + " is getting hair cut.");
                bb.endWaiting();
                Thread.sleep((long) (Math.random() * 5000));
                ui.appendText("Customer " + customerID + " leaves.");
                ui.setWaiting(Integer.toString(bb.waitingCustomers));
                bb.mutex.V();
                if (bb.waitingCustomers == 0) {
                    ui.appendText("A barber sleeps.");
                }
            }
        } catch (InterruptedException e) {
        }
    }
}

public class MultipleSleepingBarbers {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UI ui = new UI();
                Barber bb = new Barber(ui);
                ui.setVisible(true);
                Customer[] customerArray = new Customer[20];
                for (int i = 0; i < 20; i++) {
                    customerArray[i] = new Customer(i, bb, ui);
                    new Thread(customerArray[i]).start();
                }
            }
        });
    }
}
