package com.example.chatappfirebase;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MemoryData {

    public static void saveData(String data, Context context){
//        try{
//            FileOutputStream fileOutputStream = context.openFileOutput("data.txt", context.MODE_PRIVATE);
//            fileOutputStream.write(data.getBytes());
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void saveName(String data, Context context){
//        try{
//            FileOutputStream fileOutputStream = context.openFileOutput("name.txt", context.MODE_PRIVATE);
//            fileOutputStream.write(data.getBytes());
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static String getData(Context context) {
        String data = "";
//        try{
//            FileInputStream fileInputStream = context.openFileInput("data.txt");
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//            BufferedReader bufferedReaderb = new BufferedReader(inputStreamReader);
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReaderb.readLine()) != null){
//                stringBuilder.append(line);
//            }
//            data = stringBuilder.toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return data;
    }

    public static String getName(Context context) {
        String data = "";
//        try{
//            FileInputStream fileInputStream = context.openFileInput("name.txt");
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//            BufferedReader bufferedReaderb = new BufferedReader(inputStreamReader);
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReaderb.readLine()) != null){
//                stringBuilder.append(line);
//            }
//            data = stringBuilder.toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return data;
    }



}
