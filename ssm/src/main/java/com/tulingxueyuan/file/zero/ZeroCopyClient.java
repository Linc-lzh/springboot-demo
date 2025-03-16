package com.tulingxueyuan.file.zero;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ZeroCopyClient {
    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        try (Socket socket = new Socket("localhost", 8080);
             InputStream in = socket.getInputStream();
             FileOutputStream out = new FileOutputStream("D:\\FFOutput\\temp\\xushu666.7z")) {

            byte[] buffer = new byte[1024*1024];
            int bytesRead;  
            while ((bytesRead = in.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesRead);  
            }  
            System.out.println("文件下载完成.");  
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end-begin+"ms");
    }  
}  