package me.kristo.videorecorder.util;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import me.kristoprifti.android.recorder.FFmpegInvoke;

final class Processor {
    private ArrayList mCommand;
    private ArrayList mFilters;
    private final FFmpegInvoke mInvoker;
    private HashMap mMetaData;
    private final int mNumCores;

    static {
        System.loadLibrary("ffmpeginvoke");
    }

    Processor(String paramString, Context context) {
        this.mInvoker = new FFmpegInvoke(paramString);
        this.mNumCores = getNumCores();
    }

    private static int getNumCores() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File paramAnonymousFile) {
                    return Pattern.matches("cpu[0-9]", paramAnonymousFile.getName());
                }
            }).length;
        } catch (Exception ignored) {
        }
        return 1;
    }

    Processor addInputPath(String paramString) {
        this.mCommand.add("-i");
        this.mCommand.add(paramString);
        return this;
    }

    Processor enableOverwrite() {
        this.mCommand.add("-y");
        return this;
    }

    Processor newCommand() {
        this.mMetaData = new HashMap();
        this.mFilters = new ArrayList();
        this.mCommand = new ArrayList();
        this.mCommand.add("ffmpeg");

        return this;
    }

    private int process(String[] paramArrayOfString) {
        return this.mInvoker.run(paramArrayOfString);
    }


    int processToOutput(String paramString) {
        if (this.mFilters.size() > 0) {
            this.mCommand.add("-vf");
            StringBuilder localStringBuilder = new StringBuilder();
            for (Object mFilter : this.mFilters) {
                localStringBuilder.append((String) mFilter);
                localStringBuilder.append(",");
            }
            String str2 = localStringBuilder.toString();
            this.mCommand.add(str2.substring(0, -1 + str2.length()));
        }
        for (Object o : this.mMetaData.keySet()) {
            String str1 = (String) o;
            this.mCommand.add("-metadata");
            this.mCommand.add(str1 + "=" + "\"" + this.mMetaData.get(str1) + "\"");
        }
        if (this.mNumCores > 1) ;
        this.mCommand.add(paramString);
        for (Object aMCommand : this.mCommand) Log.i("FFMPEG ARGUMENTS '{}'", (String) aMCommand);
        return process((String[]) this.mCommand.toArray(new String[this.mCommand.size()]));
    }

    Processor setStrict() {
        this.mCommand.add("-strict");
        this.mCommand.add("experimental");
        return this;
    }

    Processor setThread() {
        this.mCommand.add("-threads");
        this.mCommand.add("1");
        return this;
    }

    Processor setPreset() {
        this.mCommand.add("-preset");
        this.mCommand.add("ultrafast");
        return this;
    }

    Processor setWaterMark(String imagePath) {
        this.mCommand.add("-vf");
        this.mCommand.add("movie=" + imagePath + "  [watermark]; [in][watermark] overlay=main_w-overlay_w-10:10 [out]");
        return this;
    }
}