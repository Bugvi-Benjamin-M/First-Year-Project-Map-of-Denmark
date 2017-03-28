package Helpers.Utilities;

import java.util.Observable;
import java.util.Observer;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 28-03-2017.
 * @project BFST
 */
public class FPSCounter extends Thread {
    private long lastInterupt;
    private int fps;
    private boolean stopThread;

    private static final long NANO_TO_SECOND = 1000000000;

    public FPSCounter() {
        stopThread = false;
    }

    @Override
    public void run() {
        while (!stopThread) {
            lastInterupt = System.nanoTime();
            try {
                sleep(1000); // longer than one frame
            } catch (InterruptedException e){
                System.out.println("Thread has been interrupted.");
            }
            //one second(nano) divided by amount of time it takes for one frame to finish
            fps = (int) (NANO_TO_SECOND / (System.nanoTime() - lastInterupt));
            lastInterupt = System.nanoTime();
        }
    }

    public void stopThread() {
        stopThread = true;
    }

    public int getFPS() {
        return fps;
    }

    @Override
    public String toString() {
        return "FPS: "+getFPS();
    }
}
