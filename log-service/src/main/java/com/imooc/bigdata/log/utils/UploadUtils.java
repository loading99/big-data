package com.imooc.bigdata.log.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

//upload the data from client
public class UploadUtils {
    public static void upload(String path,String log){
        try{
            URL url=new URL(path);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/json");
            OutputStream out =connection.getOutputStream();
            out.write(log.getBytes());
            out.flush();
            out.close();

            int responseCode = connection.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
}
