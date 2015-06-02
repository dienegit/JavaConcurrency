package lc.assignment62;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class Controller {

    private int readers;
    private int writers;
    private boolean reading;
    private boolean writing;
    private UI ui;

    public Controller(UI u) {
        readers = 0;
        writers = 0;
        reading = false;
        writing = false;
        ui = u;
    }

    public synchronized void write() throws InterruptedException {
        ++writers;
        while (reading == true || writing == true) {
            if (Thread.currentThread().getName().equals("w1")) {
                ui.setW1("waiting");
            }
            if (Thread.currentThread().getName().equals("w2")) {
                ui.setW2("waiting");
            }
            if (Thread.currentThread().getName().equals("w3")) {
                ui.setW3("waiting");
            }
            wait();
        }
        writing = true;
    }

    public synchronized void read() throws InterruptedException {
        ++readers;
        while (ui.isReaderPriority() ? writing == true : writers > 0) {
            if (Thread.currentThread().getName().equals("r1")) {
                ui.setR1("waiting");
            }
            if (Thread.currentThread().getName().equals("r2")) {
                ui.setR2("waiting");
            }
            if (Thread.currentThread().getName().equals("r3")) {
                ui.setR3("waiting");
            }
            wait();
        }
        if (readers >= 1) {
            reading = true;
        }
    }

    public synchronized void writeRelease() {
        --writers;
        writing = false;
        if (writers + readers == 0) {
            ui.setSd("idle");
        }
        notifyAll();
    }

    public synchronized void readRelease() {
        --readers;
        if (readers == 0) {
            reading = false;
        }
        if (writers + readers == 0) {
            ui.setSd("idle");
        }
        notifyAll();
    }
}

class Writer implements Runnable {

    Controller ctr;
    UI ui;

    Writer(Controller d, UI u) {
        ctr = d;
        ui = u;
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep((long) (Math.random() * 5000));
                ctr.write();
                if (Thread.currentThread().getName().equals("w1")) {
                    ui.setW1("writing");
                }
                if (Thread.currentThread().getName().equals("w2")) {
                    ui.setW2("writing");
                }
                if (Thread.currentThread().getName().equals("w3")) {
                    ui.setW3("writing");
                }
                ui.setSd("busy");
                Thread.sleep((long) (Math.random() * 3000));
                ctr.writeRelease();
                if (Thread.currentThread().getName().equals("w1")) {
                    ui.setW1("");
                }
                if (Thread.currentThread().getName().equals("w2")) {
                    ui.setW2("");
                }
                if (Thread.currentThread().getName().equals("w3")) {
                    ui.setW3("");
                }
            }
        } catch (InterruptedException e) {
        }
    }
}

class Reader implements Runnable {

    Controller ctr;
    UI ui;

    Reader(Controller d, UI u) {
        ctr = d;
        ui = u;
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep((long) (Math.random() * 5000));
                ctr.read();
                if (Thread.currentThread().getName().equals("r1")) {
                    ui.setR1("reading");
                }
                if (Thread.currentThread().getName().equals("r2")) {
                    ui.setR2("reading");
                }
                if (Thread.currentThread().getName().equals("r3")) {
                    ui.setR3("reading");
                }
                ui.setSd("busy");
                Thread.sleep((long) (Math.random() * 3000));
                ctr.readRelease();
                if (Thread.currentThread().getName().equals("r1")) {
                    ui.setR1("");
                }
                if (Thread.currentThread().getName().equals("r2")) {
                    ui.setR2("");
                }
                if (Thread.currentThread().getName().equals("r3")) {
                    ui.setR3("");
                }
            }
        } catch (InterruptedException e) {
        }
    }
}

public class ReaderWriter {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UI ui = new UI();
                Controller c = new Controller(ui);
                ui.setVisible(true);
                Writer writer1 = new Writer(c, ui);
                Writer writer2 = new Writer(c, ui);
                Writer writer3 = new Writer(c, ui);
                Reader reader1 = new Reader(c, ui);
                Reader reader2 = new Reader(c, ui);
                Reader reader3 = new Reader(c, ui);
                Thread w1 = new Thread(writer1, "w1");
                Thread w2 = new Thread(writer2, "w2");
                Thread w3 = new Thread(writer3, "w3");
                Thread r1 = new Thread(reader1, "r1");
                Thread r2 = new Thread(reader2, "r2");
                Thread r3 = new Thread(reader3, "r3");

                w1.start();
                w2.start();
                w3.start();
                r1.start();
                r2.start();
                r3.start();
            }
        });
    }
}
