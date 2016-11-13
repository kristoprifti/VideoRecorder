package me.kristo.videorecorder.util;

import android.content.Context;


public class CustomUtil {

    private static String getEncodingLibraryPath(Context paramContext) {
        return paramContext.getApplicationInfo().nativeLibraryDir + "/libencoding.so";
    }

    public static void addBitmapOverlayOnVideo(Context context, String videoPath, String bitmapPath, String outputPath) {
        (new Processor(getEncodingLibraryPath(context), context)).newCommand().enableOverwrite().addInputPath(videoPath).setWaterMark(bitmapPath).setThread().setPreset().setStrict().processToOutput(outputPath);
    }

}


