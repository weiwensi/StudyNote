package com.gysoft.convert.jacob;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/2/21 14:19
 */
public class Md5Test {
    public static void main(String[] args) {
        System.out.println(Md5Test.getFileMd5Value("C:\\Users\\DELL\\Desktop", "测试ppt代码优化.ppt"));

    }

    public static String getFileMd5Value(String filePath, String fileName){
        File file = new File(filePath + File.separator + fileName);
        if(!file.exists() || !file.isFile()){
            return "";
        }
        byte[] buffer = new byte[2048];
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            FileInputStream in = new FileInputStream(file);
            while(true){
                int len = in.read(buffer,0,2048);
                if(len != -1){
                    digest.update(buffer, 0, len);
                }else{
                    break;
                }
            }
            in.close();

            byte[] md5Bytes  = digest.digest();
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static File word2PDF( String sPath,String toPath) {

       /* logger.info("启动Word..");*/
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;
        File tofile = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();

            //String path =  this.getSession().getServletContext().getRealPath("/")+"attachment/";

            doc = Dispatch.call(docs, "Open", sPath).toDispatch();
           /* logger.info("打开文档..."+docFileName);
            logger.info("转换文档到PDF..." + sPath);*/
            tofile = new File(toPath);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc,
                    "SaveAs",
                    toPath, // FileName
                    17
            );
            long end = System.currentTimeMillis();
           /* logger.info("转换完成..用时：" + (end - start) + "ms.");*/
        } catch (Exception e) {
          /*  logger.info("========Error:文档转换失败：" + e.getMessage());*/
        } finally {
            Dispatch.call(doc, "Close", false);
          /*  logger.info("关闭文档");*/
            if (app != null)
                app.invoke("Quit", new Variant[]{});
        }
        //如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
        return tofile;
    }
}
