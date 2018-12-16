package Helpers.Utilities;

/**
 * The FPSCounter is a Thread that runs alongside the other
 * threads in the program (such as GUI event thread and main
 * thread) and calculates how often it is possible to update.
 * Used by MapCanvas to calculate how many often the canvas is
 * updated per second.
 */
public class FPSCounter extends Thread {
    private long lastInterupt;
    private double fps;
    private boolean stopThread;

    private static final long NANO_TO_SECOND = 1000000000;

    public FPSCounter() { stopThread = false; }

    /**
     * The runtime loop of the thread. Continues until stopped
     */
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
            // one second(nano) divided by amount of time it takes for
            // one frame to finish
            fps = NANO_TO_SECOND / (System.nanoTime() - lastInterupt);
        }
    }

    /**
     * Stops the thread from running for a while
     */
    public void stopThread() { stopThread = true; }

    /**
     * Retrieves the last FPS value
     */
    public double getFPS() { return fps; }

    /**
     * Retrieves a string representing the FPS value
     */
    @Override
    public String toString()
    {
        return "FPS: " + getFPS();
    }
}
