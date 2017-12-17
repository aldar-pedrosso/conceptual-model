package objects;

/**
 * Created by Pedro on 2017-12-17.
 */

public abstract class RunnableSmart implements Runnable {
    /**
     * Automatically does a synchronised notify
     */
    public synchronized void notifySmart(){
        notify();
    }

    /**
     * Automatically does a synchronised wait
     */
    public synchronized void waitSmart(){
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Automatically does a synchronised wait
     * @param millis the maximum time to wait in milliseconds
     */
    public synchronized void waitSmart(long millis){
        try {
            wait(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
