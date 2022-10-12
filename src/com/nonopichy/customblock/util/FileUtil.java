package com.nonopichy.customblock.util;

import java.io.*;

public class FileUtil {
    public static String read(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile().toString()));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            br.close();

            return sb.toString();
        } catch (Exception e) {

            return null;
        }
    }

    public static void write(File file, String... str) {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file));
            for (String s : str) {
                output.write(s);
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
