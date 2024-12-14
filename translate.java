package StringManipulation;

import java.io.*;
import java.util.ArrayList;

public class translate {
    public static void main(String[] args) {
        translate ac = new translate();
        ac.runner();
    }
    ArrayList<String> arr = new ArrayList<>();
    public void runner(){
        String inputFilePath = "/Users/temp/Documents/JrJavaMac2020/eclipseWS/Student/StringManipulation/info.txt";
        String outputFilePath = "/Users/temp/Documents/JrJavaMac2020/eclipseWS/Student/StringManipulation/output.csv";
        try {
            // Create a BufferedReader to read from the input file
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));

            // Create a BufferedWriter to write to the output file
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            // Read lines from the input file and write them to the output file
            writer.write("Elec pair,mA,duration,start time,end time,date,transcription,bedside description,other parameters");
            writer.newLine();
            String line = reader.readLine();
            String nextLine = reader.readLine();
            while (line != null&&nextLine!=null) {
                line = changeLine(line, nextLine);
                writer.write(line);
                writer.newLine();
                boolean check = false;
                for(int i = 0; i < arr.size(); i++) {
                    if (arr.get(i).equals(line.substring(0, line.indexOf(",")))) {
                        check = true;
                        i = arr.size();
                    }
                }
                /*if(line.startsWith("sham")){
                    line = nextLine;
                    nextLine = reader.readLine();
                }*/
                if(check){
                    line = nextLine;
                    nextLine = reader.readLine();
                }

                else {
                    line = reader.readLine();
                    nextLine = reader.readLine();
                    // comment out top line for old stuff
                }
            }
            // Close the BufferedReader and BufferedWriter
            reader.close();
            writer.close();
        } catch (IOException e) {
            // Handle any errors that occur during the reading or writing process
            e.printStackTrace();
        }
    }
    public String changeLine(String line, String line1)
    {
        String ret = "";
        ret+=startstop(line)+",";
        if(ret.startsWith("sham")||ret.startsWith("SHAM"))
            ret+=",";
        String time1 = getTime1(line);
        String time2 = getTime1(line1);
        if(time2!=null) {
            ret += subtractTime(time2, time1) + ",";
            String start1 = getTime2(line);
            String start2 = getTime2(line1);
            ret += start1 + "," + start2 + ",";
            ret += line.substring(line.indexOf("/") - 2, line.indexOf("/") + 3);
        }
        return ret;
    }
    public String startstop(String str) {
        int startIndex = str.indexOf("Stim Start ");
        if (startIndex != -1) {
            startIndex += 11;
        } else {
            startIndex = str.indexOf("Stop ");
            if (startIndex == -1) {
                //return "sham"; // "start " or "stop " not found in the string, uncomment for new stuff
                String ret = "";
                if(str.indexOf('M')!=-1) {
                     ret = str.substring(0, str.indexOf('M')).trim() + ",";
                    arr.add(str.substring(0, str.indexOf('M')).trim());
                }
                else
                {
                     ret = str.substring(0, str.indexOf('S')).trim() + ",";
                    arr.add(str.substring(0, str.indexOf('S')).trim());
                }
                return ret;
                //return str.substring(0, str.indexOf('M')).trim()+",";
            }
            startIndex += str.indexOf(" ", startIndex);
        }
        int endIndex = str.indexOf(' ', startIndex);
        if (endIndex == -1) {
            endIndex = str.length();
        }
        if (str.contains("[SH]"))
            return str.substring(startIndex, endIndex)+"[SH]"+","+str.substring(endIndex+1, endIndex+4);
        return str.substring(startIndex, endIndex)+","+str.substring(endIndex+1, endIndex+4);
    }
    public String subtractTime(String time1Str, String time2Str)
    {
        try {
            String[] time1Arr = time1Str.split(":");
            int time1Hours = Integer.parseInt(time1Arr[0]);
            int time1Minutes = Integer.parseInt(time1Arr[1]);
            int time1Seconds = Integer.parseInt(time1Arr[2]);
            String[] time2Arr = time2Str.split(":");
            int time2Hours = Integer.parseInt(time2Arr[0]);
            int time2Minutes = Integer.parseInt(time2Arr[1]);
            int time2Seconds = Integer.parseInt(time2Arr[2]);
            int totalSeconds1 = (time1Hours * 3600) + (time1Minutes * 60) + time1Seconds;
            int totalSeconds2 = (time2Hours * 3600) + (time2Minutes * 60) + time2Seconds;
            int differenceSeconds = Math.abs(totalSeconds1 - totalSeconds2);
            int differenceMinutes = differenceSeconds / 60;
            int remainingSeconds = differenceSeconds % 60;
            if (remainingSeconds < 10)
                return differenceMinutes + ":0" + remainingSeconds;
            return differenceMinutes + ":" + remainingSeconds;
        }
        catch(NullPointerException e)
        {
            return "";
        }

    }
    public String getTime2(String input)
    {
        String[] tokens = input.split("\\s+"); // split the input string by whitespace
        int a = 0;
        for (String token : tokens) {
            if (token.matches("\\d{1,2}:\\d{2}:\\d{2}")) {
                a++;// return the first match of hour:minute:sec
                if(a==2)
                    return token;
            }
        }
        return null; // return null if no match is found
    }
    public String getTime1(String input)
    {
        String[] tokens = input.split("\\s+"); // split the input string by whitespace
        for (String token : tokens) {
            if (token.matches("\\d{1,2}:\\d{2}:\\d{2}")) {
                return token; // return the first match of hour:minute:sec
            }
        }
        return null; // return null if no match is found
    }
}
