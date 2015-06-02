package lc.assignment64;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class Safari {

    private int museumVisitors;
    private int safariVisitors;
    private int cars;
    UI ui;

    public Safari(UI u) {
        museumVisitors = 0;
        safariVisitors = 0;
        cars = 3;
        ui = u;
    }

    public synchronized void arriveMuseum() throws InterruptedException {
        museumVisitors++;
        ui.setMuseum(Integer.toString(museumVisitors));
    }

    public synchronized void leaveMuseum() throws InterruptedException {
        museumVisitors--;
        ui.setMuseum(Integer.toString(museumVisitors));
    }

    public synchronized void beginRide() throws InterruptedException {
        safariVisitors++;
        while (cars == 0) {
            wait();
            ui.appendText("Visitor " + Thread.currentThread().getName() + " is waiting for safari ride.");
            ui.setSW(Integer.toString(safariVisitors - 3));
        }
        cars--;
        ui.setCars(Integer.toString(cars));
        notifyAll();
    }

    public synchronized void finishRide() throws InterruptedException {
        safariVisitors--;
        cars++;
        ui.setCars(Integer.toString(cars));
        notifyAll();
    }
}

class Vistor implements Runnable {

    Safari sfr;
    UI ui;

    Vistor(Safari s, UI u) {
        sfr = s;
        ui = u;
    }

    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 10000));
            long visitTime = (long) (Math.random() * 10000);
            ui.appendText("Visitor " + Thread.currentThread().getName() + " visits the museum for " + visitTime + "ns.");
            sfr.arriveMuseum();
            Thread.sleep(visitTime);
            sfr.leaveMuseum();
            ui.appendText("Visitor " + Thread.currentThread().getName() + " leaves the museum.");
            Thread.sleep(2000);
            ui.appendText("Visitor " + Thread.currentThread().getName() + " lines up to take a safari ride.");
            Thread.sleep((long) (Math.random() * 5000));
            sfr.beginRide();
            ui.appendText("Visitor " + Thread.currentThread().getName() + " is riding.");
            Thread.sleep((long) (Math.random() * 10000));
            sfr.finishRide();
            ui.appendText("Visitor " + Thread.currentThread().getName() + " leaves the safari ride.");
            Thread.sleep((long) (Math.random() * 5000));
        } catch (InterruptedException e) {
        }
    }
}

public class JurassicPark {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UI ui = new UI();
                Safari s = new Safari(ui);
                ui.setVisible(true);
                for (int i = 1; i <= 20; i++) {
                    new Thread(new Vistor(s, ui), Integer.toString(i)).start();

                }

            }
        });
    }
}
