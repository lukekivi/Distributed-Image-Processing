/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

package server.utils;

import server.TaskRequestRunnable;

/**
 * A container to conveniently package up a thread and
 * its associated runnable. It allows them to be stored
 * together for easy thread joining followed by data 
 * extraction from the runnable.
 */
public class ThreadAndRunnableContainer {
    private Thread thread;
    private TaskRequestRunnable runnable;

    public ThreadAndRunnableContainer(
        Thread thread,
        TaskRequestRunnable runnable
    ) {
        this.thread = thread;
        this.runnable = runnable;
    }

    public TaskRequestRunnable getRunnable() {
        return runnable;
    }

    public Thread getThread() {
        return thread;
    }
}