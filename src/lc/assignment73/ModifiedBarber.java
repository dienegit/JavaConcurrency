package lc.assignment73;

/**
 *
 * @author Luke Chen
 */
class BarberShop {

    private int barbers;
    private int waitingCustomers;
    boolean cashRegister;
    UI ui;

    public BarberShop(UI u) {
        barbers = 3;
        waitingCustomers = 0;
        cashRegister = false;
        ui = u;
    }

    public synchronized boolean enter() throws InterruptedException {
        if (waitingCustomers == 10) {
            ui.appendText("Barbershop is full; Customer " + Thread.currentThread().getName() + " leaves.");
            return false;
        }
        waitingCustomers++;
        if (waitingCustomers > 4) {
            ui.setSeating(Integer.toString(4));
            ui.setStanding(Integer.toString(waitingCustomers - 4));
        } else {
            ui.setSeating(Integer.toString(waitingCustomers));
            ui.setStanding(Integer.toString(0));
        }
        notifyAll();
        return true;
    }

    public synchronized void startCutting() throws InterruptedException {
        while (barbers == 0) {
            wait();
            ui.appendText("Customer " + Thread.currentThread().getName() + " is waiting.");
        }
        if (waitingCustomers == 1 && barbers > 0) {
            ui.appendText("Customer " + Thread.currentThread().getName() + " wakes a barber up.");
        }
        waitingCustomers--;
        if (waitingCustomers > 4) {
            ui.setSeating(Integer.toString(4));
            ui.setStanding(Integer.toString(waitingCustomers - 4));
        } else {
            ui.setSeating(Integer.toString(waitingCustomers));
            ui.setStanding(Integer.toString(0));
        }
        barbers--;
        ui.setBarbers(Integer.toString(barbers));
        notifyAll();
    }

    public synchronized void pay() throws InterruptedException {
        while (cashRegister) {
            wait();
            ui.appendText("Customer " + Thread.currentThread().getName() + " is waiting at the cash register.");
        }
        cashRegister = true;
        ui.setReg("busy");
        Thread.sleep((long) (Math.random() * 1000));
        notifyAll();
    }

    public synchronized void leave() throws InterruptedException {
        cashRegister = false;
        ui.setReg("idle");
        barbers++;
        ui.setBarbers(Integer.toString(barbers));
        if (waitingCustomers == 0) {
            ui.appendText("A barber sleeps.");
        }
        Thread.sleep((long) (Math.random() * 1000));
        notifyAll();
    }
}

class Customer implements Runnable {

    BarberShop bs;
    UI ui;

    Customer(BarberShop b, UI u) {
        bs = b;
        ui = u;
    }

    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 10000));
            ui.appendText("Customer " + Thread.currentThread().getName() + " enters.");
            if (bs.enter()) {
                bs.startCutting();
                ui.appendText("Customer " + Thread.currentThread().getName() + " is getting hair cut.");
                Thread.sleep((long) (Math.random() * 5000));
                ui.appendText("Customer " + Thread.currentThread().getName() + " is going to pay.");
                bs.pay();
                ui.appendText("Customer " + Thread.currentThread().getName() + " paid.");
                bs.leave();
                ui.appendText("Customer " + Thread.currentThread().getName() + " leaves.");
            }
        } catch (InterruptedException e) {
        }
    }
}

public class ModifiedBarber {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UI ui = new UI();
                BarberShop b = new BarberShop(ui);
                ui.setVisible(true);
                for (int i = 1; i <= 20; i++) {
                    new Thread(new Customer(b, ui), Integer.toString(i)).start();
                }
            }
        });
    }
}
