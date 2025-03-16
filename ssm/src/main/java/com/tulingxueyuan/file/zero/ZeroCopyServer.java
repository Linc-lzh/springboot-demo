package com.tulingxueyuan.file.zero;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;

public class ZeroCopyServer {
    public static void main(String[] args) {  
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(8080));
            System.out.println("服务器已启动，等待连接...");  

            while (true) {  
                try (SocketChannel clientSocket = serverSocket.accept()) {
                    System.out.println("客户端已连接: " + clientSocket.getRemoteAddress());  
                    
                    // 文件路径  
                    String filePath = "D:\\FFOutput\\temp\\xushu.7z";
                    
                    // 使用FileChannel传输文件  
                    try (FileChannel fileChannel = FileChannel.open(Paths.get(filePath))) {
                        long position = 0;  
                        long size = fileChannel.size();  
                        while (position < size) {  
                            position += fileChannel.transferTo(position, size - position, clientSocket);  
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