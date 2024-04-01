package com.example.Thymeleaf_demo.utils;


import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

import javax.sql.rowset.serial.SerialBlob;


public class general {
    public static String fileToBase64(InputStream inputStream) {
        if (inputStream == null)
            return "";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            return base64Image;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
