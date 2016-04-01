package org.ncfrcteams.frcscoutinghub2016.matchdata.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle Brown on 3/10/2016.
 */
public class MatchRecord extends SuperMap<String, Integer>{

    private String activeButton;
    private int mode;
    private int color;
    private int orientation;
    private int isQual;
    private int[] barriers = {0,0,0,0,0,0,0,0,0,0};
    private String phonenum;

    public static MatchRecord createMatchRecord(String data, int orientation){
        String[] parts = data.split(",");
        if(parts.length != 13)
            return null;

        MatchRecord newMatchRecord = new MatchRecord();

        try {
            newMatchRecord.put("Match Number", Integer.parseInt(parts[2]));
            newMatchRecord.put("Team Number", Integer.parseInt(parts[3]));
            newMatchRecord.put("Teleop Active", 0);

            for(int i = 2; i < 10; i++) {
                newMatchRecord.barriers[i] = Integer.parseInt(parts[i+2]);
            }

            newMatchRecord.activeButton = "None";
            newMatchRecord.orientation = orientation;
            newMatchRecord.color = (parts[0].equals("R") ? 1 : 2);
            newMatchRecord.isQual = (parts[1].equals("Q") ? 1 : 0);
            newMatchRecord.mode = (orientation == newMatchRecord.color ? 1 : 2);
            newMatchRecord.phonenum = parts[12];

            newMatchRecord.put("Shoot High Auto",0);
            newMatchRecord.put("Shoot High Auto Total",0);
            newMatchRecord.put("Shoot Low Auto",0);
            newMatchRecord.put("Shoot Low Auto Total",0);
            newMatchRecord.put("Shoot High Tele",0);
            newMatchRecord.put("Shoot High Tele Total",0);
            newMatchRecord.put("Shoot Low Tele",0);
            newMatchRecord.put("Shoot Low Tele Total",0);
            newMatchRecord.put("Block High Auto",0);
            newMatchRecord.put("Block High Auto Total",0);
            newMatchRecord.put("Block Low Auto",0);
            newMatchRecord.put("Block Low Auto Total",0);
            newMatchRecord.put("Block High Tele",0);
            newMatchRecord.put("Block High Tele Total",0);
            newMatchRecord.put("Block Low Tele",0);
            newMatchRecord.put("Block Low Tele Total",0);

            for(int i=0; i < 9; i++) {
                int val;
                if(newMatchRecord.arraycontains(newMatchRecord.barriers, i)){
                    val = 0;
                } else{
                    val = 15;
                }
                newMatchRecord.put("Barrier " + i + " Auto", val);
                newMatchRecord.put("Barrier " + i + " Auto Total", val);
                newMatchRecord.put("Barrier " + i + " Tele", val);
                newMatchRecord.put("Barrier " + i + " Tele Total", val);
            }

            newMatchRecord.put("Challenged", 0);
            newMatchRecord.put("Climbed", 0);

            newMatchRecord.clearHistory();

            return newMatchRecord;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private synchronized boolean arraycontains(int[] barriers, int num){
        for (int val:barriers) {
            if(val==num){
                return true;
            }
        }
        return false;
    }

    public synchronized Integer get(String s){
        switch(s){
            case "Mode":
                return mode;
            case "Color":
                return color;
            case "Orientation":
                return orientation;
            case "isQual":
                return isQual;
            case "None":
                return 0;
            default:
                return super.get(s);
        }
    }

    public synchronized String getPhoneNum(){
        return phonenum;
    }

    public synchronized void put(String s, int i){
        switch(s){
            case "Mode":
                mode = i;
            case "Color":
                color = i;
            case "Orientation":
                orientation = i;
            case "isQual":
                isQual = i;
            default:
                super.put(s, i);
        }
    }

    public synchronized void setActiveButton(String button){
        activeButton = button;
    }

    public synchronized String getActiveButton(){
        return activeButton;
    }

    public synchronized int[] getBarriers(){
        return barriers;
    }

    public synchronized String findKey(String button){
        String mykey;
        switch (button) {
            case "Left High":
                mykey = get("Mode").equals(1) ? "Shoot High" : "Block High";
                return mykey + (get("Teleop Active").equals(0) ? " Auto" : " Tele");
            case "Left Low":
                mykey = get("Mode").equals(1) ? "Shoot Low" : "Block Low";
                return mykey + (get("Teleop Active").equals(0) ? " Auto" : " Tele");
            case "Right High":
                mykey = get("Mode").equals(2) ? "Shoot High" : "Block High";
                return mykey + (get("Teleop Active").equals(0) ? " Auto" : " Tele");
            case "Right Low":
                mykey = get("Mode").equals(2) ? "Shoot Low" : "Block Low";
                return mykey + (get("Teleop Active").equals(0) ? " Auto" : " Tele");
            default:
                return "None";
        }
    }

    public synchronized String findBarrierKey(String button){
        return button + (get("Teleop Active").equals(0) ? " Auto" : " Tele");
    }

    public synchronized void increment(String button, int val) {
        if (! button.equals("None")){
            String key;
            if(val < 9){
                key = findKey(button);
            } else{
                key = findBarrierKey(button);
            }
            put(key, get(key) + (val % 10));
            put(key + " Total", get(key + " Total") + 1);
        }
    }

    public synchronized String getString(String button, boolean shoot) {
        if (button.equals("None")) {
            return "None";
        }
        if (shoot){
            String key;
            key = findKey(button);
            int top = get(key);
            int bottom = get(key + " Total");
            String beg = key.substring(0, key.length() - 5);
            return beg + ":\n" + top + "/" + bottom;
        } else{
            String key;
            key = findBarrierKey(button);
            int top = get(key);
            int bottom = get(key + " Total");
            return ": " + top + "/" + bottom;
        }
    }

    public synchronized String getData(){
        List <Integer> smallnums = new ArrayList<>();
        String[] strings = {"Shoot High Auto","Shoot High Auto Total","Shoot Low Auto","Shoot Low Auto Total",
                "Shoot High Tele","Shoot High Tele Total","Shoot Low Tele","Shoot Low Tele Total",
                "Block High Auto","Block High Auto Total","Block Low Auto","Block Low Auto Total",
                "Block High Tele","Block High Tele Total","Block Low Tele","Block Low Tele Total"};
        for (String item:strings) {
            smallnums.add(get(item));
        }

        for(int i=0; i < 9; i++) {
            smallnums.add(get("Barrier " + i + " Auto"));
            smallnums.add(get("Barrier " + i + " Auto Total"));
            smallnums.add(get("Barrier " + i + " Tele"));
            smallnums.add(get("Barrier " + i + " Tele Total"));
        }

        smallnums.add(get("Challenged"));
        smallnums.add(get("Climbed"));

        StringBuilder data = new StringBuilder();
        for (int i=0; i<54; i++){
            data.append(smallnums.get(i)).append(",");
        }

        return data.substring(0, data.length()-1);
    }

    /*

    public synchronized String getCompressed(){
        List <Integer> smallnums = new ArrayList<>();
        String[] strings = {"Shoot High Auto","Shoot High Auto Total","Shoot Low Auto","Shoot Low Auto Total",
                "Shoot High Tele","Shoot High Tele Total","Shoot Low Tele","Shoot Low Tele Total",
                "Block High Auto","Block High Auto Total","Block Low Auto","Block Low Auto Total",
                "Block High Tele","Block High Tele Total","Block Low Tele","Block Low Tele Total"};
        for (String item:strings) {
            smallnums.add(get(item));
        }

        for(int i=0; i < 9; i++) {
            smallnums.add(get("Barrier " + i + " Auto"));
            smallnums.add(get("Barrier " + i + " Auto Total"));
            smallnums.add(get("Barrier " + i + " Tele"));
            smallnums.add(get("Barrier " + i + " Tele Total"));
        }

        smallnums.add(get("Challenged"));
        smallnums.add(get("Climbed"));

        int team1 = (get("Team Number") & 0xff00) >> 8;
        int team2 = get("Team Number") & 0x00ff;
        String team = Character.toString((char) team1) + Character.toString((char) team2);
        String match = Character.toString((char) (int) get("Match Number"));
        String compressed = team + match;

        for (int i=0; i<54; i+=2){
            byte b = (byte)(((byte)(smallnums.get(i) & 0x0f) << 4) | (byte)(smallnums.get(i+1) & 0x0f));
            compressed += Character.toString((char) b);
        }

        return compressed;
    }

    public String getExtracted(String compressed){
        char[] cs = compressed.toCharArray();

        int team = cs[0] << 8 | cs[1];
        int match = (byte) cs[2];
        String extracted = team + "," + match + ",";

        for (int i=3; i<29; i++) { //use 30 to include "0,0," for challenge and climb
            byte b = (byte) cs[i];
            int num1 = (byte) (b >> 4) & (byte) 0x0f;
            int num2 = b & (byte) 0x0f;
            extracted += num1 + "," + num2 + ",";
        }
        return extracted;
    }

    */

}