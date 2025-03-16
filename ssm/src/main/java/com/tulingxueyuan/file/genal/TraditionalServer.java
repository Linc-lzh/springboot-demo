package com.tulingxueyuan.file.genal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TraditionalServer {
    public static void main(String[] args) {  
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("服务器已启动，等待连接...");  

            while (true) {  
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("客户端已连接: " + clientSocket.getRemoteSocketAddress());  

                    // 文件路径  
                    String filePath = "D:\\FFOutput\\temp\\xushu.7z";
                    
                    try (FileInputStream fileInputStream = new FileInputStream(filePath);
                         OutputStream outputStream = clientSocket.getOutputStream()) {
                         
                        byte[] buffer = new byte[1024*1024];
                        int bytesRead;  
                        
                        // 读取文件并写入到输出流中  
                        while ((bytesRead = fileInputStream.read(buffer)) != -1) {  
                            outputStream.write(buffer, 0, bytesRead);  
                        }  
                    }  
                    System.out.println("文件传输完成.");  
                } catch (IOException e) {
                    e.printStackTrace();  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}  