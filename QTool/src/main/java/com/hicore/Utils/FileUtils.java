package com.hicore.Utils;

import com.hicore.qtool.HookEnv;
import com.hicore.qtool.XposedInit.EnvHook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    public static void WriteToFile(String File,String FileContent){
        try{
            File parent = new File(File).getParentFile();
            if (!parent.exists())parent.mkdirs();
            FileOutputStream fOut = new FileOutputStream(File);
            fOut.write(FileContent.getBytes(StandardCharsets.UTF_8));
            fOut.close();
        }catch (Exception e){
        }
    }
    public static void WriteToFile(String File,byte[] FileContent){
        try{
            FileOutputStream fOut = new FileOutputStream(File);
            fOut.write(FileContent);
            fOut.close();
        }catch (Exception e){
        }
    }
    public static String ReadFileString(File f){
        try{
            FileInputStream fInp = new FileInputStream(f);
            String Content = new String(DataUtils.readAllBytes(fInp),StandardCharsets.UTF_8);
            fInp.close();
            return Content;
        }catch (Exception e){
            return null;
        }
    }
    public static String ReadFileString(String f){
        return ReadFileString(new File(f));
    }
    public static void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            //System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        if(file.isFile())
        {
            file.delete();
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        if(files == null)return;
        //遍历该目录下的文件对象
        for (File f: files){
            //打印文件名
            String name = file.getName();
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFile(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }
    public static String requireForAvailPath(String Name){
        EnvHook.requireCachePath();
        String NewName = Name;
        String Path = HookEnv.ExtraDataPath+"/Cache/"+NewName;
        try {
            if (new File(Path).createNewFile()){
                new File(Path).delete();
                return NewName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewName = Name.substring(0,2)+Name.hashCode();
        Path = HookEnv.ExtraDataPath+"/Cache/"+NewName;
        try {
            if (new File(Path).createNewFile()){
                new File(Path).delete();
                return NewName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewName = ""+Name.hashCode();
        return NewName;
    }
}
