package com.shanglan.erp.utils;

import com.shanglan.erp.interf.IStringGetter;
import com.shanglan.erp.interf.OutHandler;
import com.shanglan.erp.interf.StreamGobbler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CmdExecuter {

    /**
     * 执行指令
     * @param cmd 执行指令
     * @param getter 指令返回处理接口，若为null则不处理输出
     */
    static public void exec(List<String> cmd, IStringGetter getter ){
        Process process = null;
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(cmd);
        builder.redirectErrorStream(true);

        try {
            process = builder.start();

            StreamGobbler errorGobbler  =  new StreamGobbler(process.getErrorStream(),  "ERROR");
            errorGobbler.start();//  kick  off  stderr
            StreamGobbler  outGobbler  =  new  StreamGobbler(process.getInputStream(),  "STDOUT");
            outGobbler.start();//  kick  off  stdout

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            process.destroy();
        }
    }

    /**
     * ok
     *
     * @param cmd
     * @param getter
     */
    static public void exec(String cmd, IStringGetter getter ){
        String[] commandSplit = cmd.split(" ");
        List<String> lcommand = new ArrayList<String>();
        for (int i = 0; i < commandSplit.length; i++) {
            lcommand.add(commandSplit[i]);
        }

        Process process = null;
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(lcommand);
        processBuilder.redirectErrorStream(true);
        try{
            process = processBuilder.start();
            StreamGobbler  errorGobbler  =  new StreamGobbler(process.getErrorStream(),  "ERROR");
            errorGobbler.start();//  kick  off  stderr
            StreamGobbler  outGobbler  =  new  StreamGobbler(process.getInputStream(),  "STDOUT");
            outGobbler.start();//  kick  off  stdout

            process.waitFor();
        }catch (Exception e){
            e.printStackTrace();
            process.destroy();
        }
    }


    static public void execRuntime(String cmd){

        cmd = "ffmpeg -i rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8";

        Process process = null;
        try{
            process = Runtime.getRuntime().exec(new String[]{"sh","-c",cmd});

            System.out.println("当前线程"+Thread.currentThread().getName());

            StreamGobbler  errorGobbler  =  new StreamGobbler(process.getErrorStream(),  "ERROR");
            errorGobbler.start();//  kick  off  stderr
            StreamGobbler  outGobbler  =  new  StreamGobbler(process.getInputStream(),  "STDOUT");
            outGobbler.start();//  kick  off  stdout

            process.waitFor();
        }catch (Exception e){
            e.printStackTrace();
            process.destroy();
        }
    }

    public static ConcurrentMap<String, Object> push(Map<String, Object> paramMap) throws IOException{
        String cmd = "ffmpeg -i rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8";

        ConcurrentMap<String, Object> resultMap = null;

        // 执行命令行
        final Process proc = Runtime.getRuntime().exec(cmd);
        System.out.println("执行命令----start commond");
        OutHandler errorGobbler = new OutHandler(proc.getErrorStream(), "Error");
        OutHandler outputGobbler = new OutHandler(proc.getInputStream(), "Info");

        errorGobbler.start();
        outputGobbler.start();
        // 返回参数
        resultMap = new ConcurrentHashMap<String, Object>();
        resultMap.put("info", outputGobbler);
        resultMap.put("error", errorGobbler);
        resultMap.put("process", proc);
        return resultMap;
    }

    private static ConcurrentMap<String, ConcurrentMap<String, Object>> handlerMap = new ConcurrentHashMap<String, ConcurrentMap<String, Object>>(20);
//    public void removePush(String pushId){
//        if (hd.isHave(pushId)){
//            ConcurrentMap<String, Object> map = hd.get(pushId);
//            //关闭两个线程
//            ((OutHandler)map.get("error")).destroy();
//            ((OutHandler)map.get("info")).destroy();
//
//            system.out.println("停止命令-----end commond");
//            //关闭命令主进程
//            ((Process)map.get("process")).destroy();
//            hd.delete(pushId);
//        }
//    }


}
