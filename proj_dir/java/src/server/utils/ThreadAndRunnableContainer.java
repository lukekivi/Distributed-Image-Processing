package server.utils;

import server.TaskRequestRunnable;

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