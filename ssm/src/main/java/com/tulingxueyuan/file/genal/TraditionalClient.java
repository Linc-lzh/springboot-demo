package com.tulingxueyuan.file.genal;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class TraditionalClient {
    public static void main(String[] args) {

        long begin = System.currentTimeMillis();
        try (Socket socket = new Socket("localhost", 8080);
             InputStream inputStream = socket.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream("D:\\FFOutput\\temp\\xushu666.7z")) {

            byte[] buffer = new byte[1024*1024];
            int bytesRead;  
            
            // 从输入流中读取并写入到文件中  
            while ((bytesRead = inputStream.read(buffer)) != -1) {  
                fileOutputStream.write(buffer, 0, bytesRead);  
            }  
            System.out.println("文件下载完成.");  
        } catch (IOException e) {
            e.printStackTrace();  
        }

        long end = System.currentTimeMillis();
        System.out.println(end-begin+"ms");
    }  
}  