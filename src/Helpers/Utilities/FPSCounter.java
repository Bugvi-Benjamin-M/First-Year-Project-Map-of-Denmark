package Helpers.Utilities;


public class FPSCounter extends Thread {
    private long lastInterupt;
    private double fps;
    private boolean stopThread;

    private static final long NANO_TO_SECOND = 1000000000;

    public FPSCounter() { stopThread = false; }

    @Override
    public void run()
    {
        while (!stopThread) {
            lastInterupt = System.nanoTime();
            try {
                sleep(1000); // longer than one frame (one second)
            } catch (InterruptedException e) {
                // System.out.println("Thread has been interrupted.");
            }
            // one second(nano) divided by amount of time it takes for one frame to
            // finish
            fps = NANO_TO_SECOND / (System.nanoTime() - lastInterupt);
        }
    }

    public void stopThread() { stopThread = true; }

    public double getFPS() { return fps; }

    @Override
    public String toString()
    {
        return "FPS: " + getFPS();
    }
}
