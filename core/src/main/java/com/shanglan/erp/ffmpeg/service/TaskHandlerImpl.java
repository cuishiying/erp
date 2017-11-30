package com.shanglan.erp.ffmpeg.service;



import com.shanglan.erp.ffmpeg.config.FFmpegConfig;
import com.shanglan.erp.ffmpeg.entity.TaskEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskHandlerImpl implements TaskHandler {
    private Runtime runtime = null;

    private OutHandlerMethod ohm = null;

    public TaskHandlerImpl(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    @Override
    public TaskEntity process(String id, String command) {
        Process process = null;
        OutHandler outHandler = null;
        TaskEntity tasker = null;

        String[] commandSplit = command.split(" ");
        List<String> lcommand = new ArrayList<String>();
        for (int i = 0; i < commandSplit.length; i++) {
            lcommand.add(commandSplit[i]);
        }

        ProcessBuilder processBuilder = null;
        try {
            if(processBuilder==null){
                processBuilder = new ProcessBuilder();
            }
            processBuilder.redirectErrorStream(true);
            if(FFmpegConfig.isDebug()){
                System.out.println("执行命令："+command);
            }
            processBuilder.command(lcommand);
            process = processBuilder.start();   // 执行本地命令获取任务主进程

            outHandler = new OutHandler(process.getErrorStream(), id+"_err", this.ohm);
            outHandler.start();

            OutHandler inHandler = new OutHandler(process.getInputStream(), id+"_in", this.ohm);
            inHandler.start();

            tasker = new TaskEntity(id, process, outHandler);
        } catch (IOException e) {
            if(FFmpegConfig.isDebug())
                System.err.println("执行命令失败！正在停止进程和输出线程...");
            stop(outHandler);
            stop(process);
            // 出现异常说明开启失败，返回null
            return null;
        }
        return tasker;
    }

    @Override
    public boolean stop(Process process) {
        if (process != null) {
            process.destroy();
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean stop(Thread outHandler) {
        if (outHandler != null && outHandler.isAlive()) {
            outHandler.stop();
            outHandler.destroy();
            return true;
        }
        return false;
    }

    @Override
    public boolean stop(Process process, Thread thread) {
        boolean ret;
        ret=stop(thread);
        ret=stop(process);
        return ret;
    }
}
