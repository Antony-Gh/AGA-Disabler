package com.aga.disabler.pro.license;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorServiceII {
    private final Executor executor;
    private final Handler handler;
    private final ExecutorServiceII.Tasks tasks;

    public ExecutorServiceII(Tasks tasks) {
        super();
        this.tasks = tasks;
        executor =  Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public static class ExecutorBuilder {
        private ExecutorServiceII.Tasks tas;

        public ExecutorServiceII.ExecutorBuilder setTasks(ExecutorServiceII.Tasks z) {
            this.tas = z;
            return this;
        }

        public ExecutorServiceII build() {
            return new ExecutorServiceII(tas);
        }
    }

    public void execute() {
        executor.execute(() -> {
            handler.post(tasks::onpreexecute);
            tasks.doinbackground();
            handler.post(tasks::onpostexecute);
        });
    }

    public interface Tasks{
        void doinbackground();
        void onpreexecute();
        void onpostexecute();
    }

}
