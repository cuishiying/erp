package com.shanglan.erp.ffmpeg.service;

import com.shanglan.erp.ffmpeg.config.FFmpegConfig;
import com.shanglan.erp.ffmpeg.dao.TaskDao;
import com.shanglan.erp.ffmpeg.dao.TaskDaoImpl;
import com.shanglan.erp.ffmpeg.entity.TaskEntity;

import java.util.Collection;
import java.util.Iterator;

public class FFmpegManager {
    /**
     * 任务持久化器
     */
    private TaskDao taskDao = null;
    /**
     * 任务执行处理器
     */
    private TaskHandler taskHandler = null;
    /**
     * 任务消息处理器
     */
    private OutHandlerMethod ohm = null;


    public FFmpegManager() {
        init();
    }

    public void init() {
        if (this.ohm == null) {
            this.ohm = new DefaultOutHandlerMethod();
        }
        if (this.taskDao == null) {
            this.taskDao = new TaskDaoImpl(10);
        }
        if (this.taskHandler == null) {
            this.taskHandler = new TaskHandlerImpl(this.ohm);
        }
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public TaskHandler getTaskHandler() {
        return taskHandler;
    }

    public void setTaskHandler(TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    public OutHandlerMethod getOhm() {
        return ohm;
    }

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    public String start(String id, String command) {
        if (id != null && command != null) {
            TaskEntity tasker = taskHandler.process(id, command);
            if (tasker != null) {
                int ret = taskDao.add(tasker);
                if (ret > 0) {
                    return tasker.getId();
                } else {
                    // 持久化信息失败，停止处理
                    taskHandler.stop(tasker.getProcess(), tasker.getThread());
                    if (FFmpegConfig.isDebug())
                        System.err.println("持久化失败，停止任务！");
                }
            }
        }
        return null;
    }

    public boolean stop(String id) {
        if (id != null && taskDao.isHave(id)) {
            if (FFmpegConfig.isDebug())
                System.out.println("正在停止任务：" + id);
            TaskEntity tasker = taskDao.get(id);
            if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
                taskDao.remove(id);
                return true;
            }
        }
        System.err.println("停止任务失败！id=" + id);
        return false;
    }

    public int stopAll() {
        Collection<TaskEntity> list = taskDao.getAll();
        Iterator<TaskEntity> iter = list.iterator();
        TaskEntity tasker = null;
        int index = 0;
        while (iter.hasNext()) {
            tasker = iter.next();
            if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
                taskDao.remove(tasker.getId());
                index++;
            }
        }
        if (FFmpegConfig.isDebug())
            System.out.println("停止了" + index + "个任务！");
        return index;
    }

    public TaskEntity query(String id) {
        return taskDao.get(id);
    }

    public Collection<TaskEntity> queryAll() {
        return taskDao.getAll();
    }

}