package lc.assignment31;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class OneBuf {
    private Object slot;

    public OneBuf() {
        slot = null;
        System.out.println("===== Buffer: null =====");
    }
    
    public synchronized void put(Object o) throws InterruptedException {
        while(slot != null) wait();
        slot = o;
        System.out.println("===== Buffer: "+o+"    =====");
        notifyAll();
    }

	public synchronized Object get () throws InterruptedException {
        while(slot == null) wait();
        Object o = slot;
        slot = null;
        System.out.println("===== Buffer: null =====");
        notifyAll();
        return o;
    }
}

class Producer implements Runnable {

    OneBuf buf;
    String alphabet= "abcdefghijklmnopqrstuvwxyz";
    

    Producer(OneBuf b) {
        buf = b;
    }

    public void run() {
      try {
        int ai = 0;
        while(true) {
            Thread.sleep((long) (Math.random() * 5000));
            System.out.println("Putting character \'"+alphabet.charAt(ai)+"\' into buffer...");
            buf.put(alphabet.charAt(ai));
            System.out.println("Put: "+alphabet.charAt(ai));
            ai=(ai+1) % alphabet.length();
        }
      } catch (InterruptedException e){}
    }
}

class Consumer implements Runnable {

    OneBuf buf;

    Consumer(OneBuf b) {
        buf = b;
    }

    public void run() {
      try {
        while(true) {
            Thread.sleep((long) (Math.random() * 5000));
            System.out.println("Getting character from buffer ...");
            Object c = buf.get();
            System.out.println("Get: "+c);
            
        }
      } catch(InterruptedException e ){}
    }
}

public class SingleSlot {
    public static void main(String[] args) {
        OneBuf b = new OneBuf();
        Producer p = new Producer(b);
        Consumer c = new Consumer(b);
        Thread prod= new Thread(p);
        Thread cons= new Thread(c);
        
        prod.start();
        cons.start();
    }
}