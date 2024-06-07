package com.example.shopapp.Helpers;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileHelper {

    private static final String TAG = "FileHelper";

    public static boolean writeToFile(Context context, String fileName, String data) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error writing to file: " + e.getMessage());
            return false;
        }
    }

    public static String readFromFile(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fis.close();
        } catch (Exception e) {
            Log.e(TAG, "Error reading from file: " + e.getMessage());
        }
        return stringBuilder.toString();
    }

    public static boolean appendToFile(Context context, String fileName, String data) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.append(data);
            outputStreamWriter.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error appending to file: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteFile(Context context, String fileName) {
        try {
            return context.deleteFile(fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting file: " + e.getMessage());
            return false;
        }
    }
}
