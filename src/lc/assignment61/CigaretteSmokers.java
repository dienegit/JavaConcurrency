package lc.assignment61;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class Table {

    private boolean paper;
    private boolean tobacco;
    private boolean match;
    private UI ui;

    public Table(UI u) {

        paper = false;
        tobacco = false;
        match = false;
        ui = u;
    }

    public synchronized void getPaper() throws InterruptedException {
        while (paper==true) {
            ui.model.setValueAt("waiting for paper",Integer.parseInt(Thread.currentThread().getName()),4);
            wait();
        }
        Thread.sleep(2000);
        paper = true;
        ui.model.setValueAt(true,Integer.parseInt(Thread.currentThread().getName()),1);
        ui.setjLabelPVisible(false);
    }

    public synchronized void getTobacco() throws InterruptedException {
        while (tobacco==true) {
            ui.model.setValueAt("waiting for tobacoo",Integer.parseInt(Thread.currentThread().getName()),4);
            wait();
        }
        Thread.sleep(2000);
        tobacco = true;
        ui.model.setValueAt(true,Integer.parseInt(Thread.currentThread().getName()),2);
        ui.setjLabelTVisible(false);
    }
    
    public synchronized void getMatch() throws InterruptedException {
        while (match==true) {
            ui.model.setValueAt("waiting for match",Integer.parseInt(Thread.currentThread().getName()),4);
            wait();
        }
        Thread.sleep(2000);
        match = true;
        ui.model.setValueAt(true,Integer.parseInt(Thread.currentThread().getName()),3);
        ui.setjLabelMVisible(false);
    }

    public synchronized void putPaper() {
        paper = false;
        ui.setjLabelPVisible(true);
        ui.model.setValueAt("smoking",Integer.parseInt(Thread.currentThread().getName()),4);
        notifyAll();
    }

    public synchronized void putTobacco() {
        tobacco = false;
        ui.setjLabelTVisible(true);
        ui.model.setValueAt("smoking",Integer.parseInt(Thread.currentThread().getName()),4);
        notifyAll();
    }
    
    public synchronized void putMatch() {
        match = false;
        ui.setjLabelMVisible(true);
        ui.model.setValueAt("smoking",Integer.parseInt(Thread.currentThread().getName()),4);
        notifyAll();
    }
}

class SmokerNeedPaper implements Runnable {

    Table tab;
    UI ui;

    SmokerNeedPaper(Table t, UI u) {
        tab = t;
        ui = u;
    }

    public void run() {
        try {
            while (true) {
                ui.model.setValueAt(false,Integer.parseInt(Thread.currentThread().getName()),1);
                ui.model.setValueAt("",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep((long) (Math.random() * 5000));
                ui.model.setValueAt("going to the table",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                tab.getPaper();
                ui.model.setValueAt("got papers",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                ui.model.setValueAt("making cigarette",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                ui.model.setValueAt("putting papers",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                tab.putPaper();
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
        }
    }
}

class SmokerNeedTobacco implements Runnable {

    Table tab;
    UI ui;

    SmokerNeedTobacco(Table t, UI u) {
        tab = t;
        ui = u;
    }

    public void run() {
        try {
            while (true) {
                ui.model.setValueAt(false,Integer.parseInt(Thread.currentThread().getName()),2);
                ui.model.setValueAt("",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep((long) (Math.random() * 5000));
                ui.model.setValueAt("going to the table",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                tab.getTobacco();
                ui.model.setValueAt("got tobacco",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                ui.model.setValueAt("making cigarette",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                ui.model.setValueAt("putting tobacco",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                tab.putTobacco();
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
        }
    }
}

class SmokerNeedMatch implements Runnable {

    Table tab;
    UI ui;

    SmokerNeedMatch(Table t, UI u) {
        tab = t;
        ui = u;
    }

    public void run() {
        try {
            while (true) {
                ui.model.setValueAt(false,Integer.parseInt(Thread.currentThread().getName()),3);
                ui.model.setValueAt("",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep((long) (Math.random() * 5000));
                ui.model.setValueAt("going to the table",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                tab.getMatch();
                ui.model.setValueAt("got matches",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                ui.model.setValueAt("making cigarette",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                ui.model.setValueAt("putting matches",Integer.parseInt(Thread.currentThread().getName()),4);
                Thread.sleep(2000);
                tab.putMatch();
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
        }
    }
}

public class CigaretteSmokers {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UI ui = new UI();
                Table t = new Table(ui);
                ui.setVisible(true);
                SmokerNeedPaper snp1 = new SmokerNeedPaper(t, ui);
                SmokerNeedPaper snp2 = new SmokerNeedPaper(t, ui);
                SmokerNeedPaper snp3 = new SmokerNeedPaper(t, ui);
                SmokerNeedTobacco snt1 = new SmokerNeedTobacco(t, ui);
                SmokerNeedTobacco snt2 = new SmokerNeedTobacco(t, ui);
                SmokerNeedTobacco snt3 = new SmokerNeedTobacco(t, ui);
                SmokerNeedMatch snm1 = new SmokerNeedMatch(t, ui);
                SmokerNeedMatch snm2 = new SmokerNeedMatch(t, ui);
                SmokerNeedMatch snm3 = new SmokerNeedMatch(t, ui);
                Thread s0 = new Thread(snp1, "0");
                Thread s1 = new Thread(snp2, "1");
                Thread s2 = new Thread(snp3, "2");
                Thread s3 = new Thread(snt1, "3");
                Thread s4 = new Thread(snt2, "4");
                Thread s5 = new Thread(snt3, "5");
                Thread s6 = new Thread(snm1, "6");
                Thread s7 = new Thread(snm2, "7");
                Thread s8 = new Thread(snm3, "8");
                s0.start();
                s1.start();
                s2.start();
                s3.start();
                s4.start();
                s5.start();
                s6.start();
                s7.start();
                s8.start();
            }
        });
    }
}
