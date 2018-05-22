package com.example.exodus.framework;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yeosu on 2018-05-22.
 */

public class CSVReader {

    public static List<int[]> read(InputStream is) {
        BufferedReader br = null;
        String line;
        String csvColum = ",";

        List<int[]> tileList = new ArrayList<int[]>();

        try {
            br = new BufferedReader(new InputStreamReader(is));
            while( (line = br.readLine()) != null) {
                String[] field = line.split(csvColum);
                int[] tiles = new int[field.length];
                for(int i=0; i<field.length; ++i) {
                    tiles[i] =  Integer.parseInt(field[i]);
                }
                tileList.add(tiles);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tileList;
        }
    }

}
