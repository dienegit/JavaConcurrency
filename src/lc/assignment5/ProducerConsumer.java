package lc.assignment5;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class Delivered {

    private Object slot;
    UI ui;

    public Delivered(UI u) {
        slot = null;
        ui = u;
    }

    public synchronized void put(Object o) throws InterruptedException {
        while (slot != null) {
            wait();
        }
        slot = o;
        ui.setjLabel3(o.toString());
        notifyAll();
    }

    public synchronized Object get() throws InterruptedException {
        while (slot == null) {
            wait();
        }
        Object o = slot;
        slot = null;
        ui.setjLabel3("");
        notifyAll();
        return o;
    }
}

class Producer implements Runnable {

    Delivered dlv;
    UI ui;
    String alphabet = "abcdefghijklmnopqrstuvwxyz";

    Producer(Delivered d, UI u) {
        dlv = d;
        ui = u;
    }

    public void run() {
        try {
            int ai = 0;
            while (true) {
                Thread.sleep((long) (Math.random() * 5000));
                dlv.put(alphabet.charAt(ai));
                ai = (ai + 1) % alphabet.length();
            }
        } catch (InterruptedException e) {
        }
    }
}

class Consumer implements Runnable {

    Delivered dlv;
    UI ui;

    Consumer(Delivered d, UI u) {
        dlv = d;
        ui = u;
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep((long) (Math.random() * 5000));
                Object c = dlv.get();
                ui.setjLabel4(c.toString());
            }
        } catch (InterruptedException e) {
        }
    }
}

public class ProducerConsumer {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UI ui = new UI();
                Delivered d = new Delivered(ui);
                ui.setVisible(true);
                Producer p = new Producer(d, ui);
                Consumer c = new Consumer(d, ui);
                Thread prod = new Thread(p);
                Thread cons = new Thread(c);

                prod.start();
                cons.start();
            }
        });
    }
}
