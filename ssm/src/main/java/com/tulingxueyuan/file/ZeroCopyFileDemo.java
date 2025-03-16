package com.tulingxueyuan.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;

public class ZeroCopyFileDemo {
    public static void main(String[] args) throws URISyntaxException {
        File sourceFile = new File("D:\\FFOutput\\temp\\xushu.7z");
        File destFile = new File("D:\\FFOutput\\temp\\xushu666.7z");

        long begin = System.currentTimeMillis();
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {

            FileChannel sourceChannel = fis.getChannel();
            FileChannel destChannel = fos.getChannel();  
            
            long position = 0;  
            long size = sourceChannel.size();
            System.out.println(size);
            long bytesTransferred;  

            while (position < size) {  
                bytesTransferred = sourceChannel.transferTo(position, size - position, destChannel);  
                position += bytesTransferred;  
            }  
            
            System.out.println("successfully.");
        } catch (IOException e) {
            e.printStackTrace();  
        }
        long end = System.currentTimeMillis();
        System.out.println(end-begin+"ms");
    }  
}  