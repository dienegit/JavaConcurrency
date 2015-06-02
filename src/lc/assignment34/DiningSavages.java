package lc.assignment34;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class Pot {
    private int servings;
    private int capacity;

    Pot(int n) {
        capacity = n;
        servings = 0;
        System.out.println("===== Servings: "+servings+"    =====");
    }
    
    public synchronized void getserving() throws InterruptedException {
        while (servings == 0) wait();
        --servings;
        System.out.println("===== Servings: "+servings+"    =====");
        notifyAll();
    }
    
    public synchronized void fillpot() throws InterruptedException {
        while (servings > 0) wait();
        servings = capacity;
        System.out.println("===== Servings: "+servings+"    =====");
        notifyAll();
    }
}

class Savage implements Runnable {

    Pot pt;

    Savage(Pot p) {
        pt = p;
    }

    public void run() {
        try {
            while (true) {
                pt.getserving();
                System.out.println("Get Serving.");
                Thread.sleep((long) (Math.random() * 2000));
            }
        } catch (InterruptedException e) {}
    }
}

class Cook implements Runnable {

    Pot pt;

    Cook(Pot p) {
        pt = p;
    }

    public void run() {
        try {
            while (true) {
                pt.fillpot();
                System.out.println("Pot filled.");
                Thread.sleep((long) (Math.random() * 2000));
            }
        } catch (InterruptedException e) {}
    }
}

public class DiningSavages {
    public static final int M = 3;  //define number of servings
    
    public static void main(String[] args) {
        Pot p = new Pot(M);
        Savage s = new Savage(p);
        Cook c = new Cook(p);
        Thread svg= new Thread(s);
        Thread ck= new Thread(c);
        
        svg.start();
        ck.start();
    }
}
