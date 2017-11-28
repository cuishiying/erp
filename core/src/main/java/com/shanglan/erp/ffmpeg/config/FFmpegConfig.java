package com.shanglan.erp.ffmpeg.config;

public class FFmpegConfig {
    private String path;
    private static boolean debug = true;
    private Integer size;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        debug = debug;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FFmpegConfig [path=" + path + ", debug=" + debug + ", size=" + size + "]";
    }
}
