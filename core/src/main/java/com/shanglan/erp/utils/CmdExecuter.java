package com.shanglan.erp.utils;

import com.shanglan.erp.interf.IStringGetter;
import com.shanglan.erp.interf.StreamGobbler;
import java.util.ArrayList;
import java.util.List;

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

    static public void exec(String cmd, IStringGetter getter ){

        String[] commandSplit = cmd.split(" ");
        List<String> lcommand = new ArrayList<String>();
        for (int i = 0; i < commandSplit.length; i++) {
            lcommand.add(commandSplit[i]);
        }

        Process process = null;
        ProcessBuilder processBuilder = new ProcessBuilder(lcommand);
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

        Process process = null;
        try{
            process = Runtime.getRuntime().exec(new String[]{"sh","-c",cmd});

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


}
