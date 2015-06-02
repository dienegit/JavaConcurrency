/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lc.assignment32;

/**
 *
 * @author Luke Chen / Ankur Panchal
 */
class Semaphore {

    private int value;

    public Semaphore (int initial) {
        value = initial;
    }

    synchronized public void up() {
        ++value;
        notifyAll();  // should be notify() but does not work in some browsers
    }

    synchronized public void down() throws InterruptedException {
        while (value==0) wait();
        --value;
    }
}

class OneBuf {
    Object slot = null;
    Semaphore empty = new Semaphore(1);
    Semaphore full  = new Semaphore(0);

    public void put(Object o) throws InterruptedException {
        empty.down();
        Thread.sleep((long) (Math.random() * 3000));
        slot = o;
        System.out.println("Buffer: "+o);
        full.up();
    }

	public Object get () throws InterruptedException {
        full.down();
        Object o = slot;
        Thread.sleep((long) (Math.random() * 3000));
        slot = null;
        System.out.println("Buffer: null");
        empty.up();
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
            System.out.println("producing: "+alphabet.charAt(ai));
            buf.put(alphabet.charAt(ai));
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
            System.out.println("consuming...");
            Object c = buf.get();
            System.out.println("consumed: "+c);
        }
      } catch(InterruptedException e ){}
    }
}

public class SingleSlotSemaphores {
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