package lc.assignment65;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class Buffer {

    private Object slot;
    UI ui;

    public Buffer(UI u) {
        slot = null;
        ui = u;
    }

    public synchronized void put(Object o) throws InterruptedException {
        while (slot != null) {
            ui.setjLabelP("waiting");
            wait();
        }
        slot = o;
        ui.setjLabelP("");
        ui.setjLabelB(o.toString());
        Thread.sleep(1000);
        notifyAll();
    }

    public synchronized Object get() throws InterruptedException {
        while (slot == null) {
            wait();
        }
        Object o = slot;
        slot = null;
        ui.setjLabelB("");
        notifyAll();
        return o;
    }
}

class Producer implements Runnable {

    Buffer buf;
    UI ui;
    String alphabet = "abcdefghijklmnopqrstuvwxyz";

    Producer(Buffer d, UI u) {
        buf = d;
        ui = u;
    }

    public void run() {
        try {
            int ai = 0;
            while (true) {
                Thread.sleep((long) (Math.random() * 5000));
                ui.setjLabelP("producing '"+alphabet.charAt(ai)+"'");
                Thread.sleep((long) (Math.random() * 2000));
                buf.put(alphabet.charAt(ai));
                ai = (ai + 1) % alphabet.length();
            }
        } catch (InterruptedException e) {
        }
    }
}

class Consumer implements Runnable {

    Buffer buf;
    UI ui;

    Consumer(Buffer d, UI u) {
        buf = d;
        ui = u;
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep((long) (Math.random() * 8000));
                ui.setjLabelC("waiting next");
                Object c = buf.get();
                ui.setjLabelC("consumed '"+c.toString()+"'");
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
                Buffer d = new Buffer(ui);
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
