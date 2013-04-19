package com.tqxktbyw.study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    private static final int WAIT_TIME = 100;
    private final int maxThreadCount;
    private volatile boolean shutdown;
    private final List<WorkThread> workThreads = new ArrayList<WorkThread>();
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();

    public ThreadPool(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    public synchronized void execute(Runnable task) {
        try {
            taskQueue.put(task);
            Thread.sleep(WAIT_TIME);

            if (maxThreadCount > workThreads.size() && !taskQueue.isEmpty()) {
                WorkThread thread = new WorkThread();
                workThreads.add(thread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Runnable> shutdown(long timeout) {
        shutdown = true;
        stopAllThreads(timeout);
        List<Runnable> taskList = new ArrayList<Runnable>();

        for (Runnable task = taskQueue.poll(); task != null; task = taskQueue.poll()) {
            if (!isQuitTask(task)) {
                taskList.add(task);
            }
        }

        return taskList;
    }

    public Object[] getTasks() {
        return taskQueue.toArray();
    }

    public boolean remove(Runnable task) {
        return taskQueue.remove(task);
    }

    public void removeAll(Collection<?> c) {
        taskQueue.removeAll(c);
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public synchronized boolean isTaskRunning() {
        for (int i = workThreads.size() - 1; i >= 0; --i) {
            if (workThreads.get(i).isRunning()) {
                return true;
            }
        }

        return false;
    }

    public synchronized Runnable getExecutingTask(int index) {
        WorkThread thread = workThreads.get(index);
        return (thread.isRunning() ? thread.task : null);
    }

    public synchronized int getThreadCount() {
    	return workThreads.size();
    }

    private synchronized void stopAllThreads(long timeout) {
        try {
            int size = workThreads.size();
            for (int i = 0; i < size; ++i) {
                taskQueue.put(new QuitTask());
            }

            for (int i = 0; i < size; ++i) {
                workThreads.get(i).join(timeout);
            }

            workThreads.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final boolean isQuitTask(Runnable task) {
        return (task instanceof QuitTask);
    }

    private class WorkThread extends Thread {
        private Runnable task;

        public boolean isRunning() {
            return (task != null && !isQuitTask(task) && isAlive());
        }

        @Override
        public void run() {
            while (!isShutdown()) {
                try {
                    task = taskQueue.take();
                    if (isQuitTask(task)) {
                        break;
                    }

                    task.run();
                    task = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final class QuitTask implements Runnable {
        @Override
        public void run() {
        }
    }
}