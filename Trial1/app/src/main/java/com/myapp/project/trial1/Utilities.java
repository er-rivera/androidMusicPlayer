package com.myapp.project.trial1;

import android.util.Log;

public class Utilities {

    public String millisecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        if(milliseconds == 0){
            return "0:00";
        }
        //converting total duration into min:sec
        int hours = (int) (milliseconds / (1000*60*60));
        int minutes = (int) (milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        //Log.i("CheckSeconds", String.valueOf(seconds));
        //Add hours if needed
        if(hours > 0){
            finalTimerString = hours + ": ";
        }

        //check for tenths place
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }

    public int getProgressPercentage(long cur, long total){
        Double percentage = (double) 0;

        long currentSeconds = (int) (cur /1000);
        long totalSeconds = (int) (total / 1000);

        percentage = (((double) currentSeconds) /totalSeconds) * 100;

        return percentage.intValue();
    }


    public int progressToTimer(int progress, int totalDuration){
        int currentDuration = 0;
        totalDuration = (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        return currentDuration*1000;
    }
}

