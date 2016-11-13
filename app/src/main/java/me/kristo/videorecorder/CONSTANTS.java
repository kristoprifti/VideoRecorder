package me.kristo.videorecorder;

import android.os.Environment;

class CONSTANTS {
	
	final static String FILE_START_NAME = "Sourab_";
	final static String VIDEO_EXTENSION = ".mp4";
	final static String IMAGE_EXTENSION = ".jpg";
	final static String WATERMARK_NAME = "video_watermark";
	final static String WATERMARK_EXTENSION = ".png";
	private final static String DCIM_FOLDER = "/DCIM";
	final static String IMAGE_FOLDER = "/image";
	final static String VIDEO_FOLDER = "/video";
	private final static String TEMP_FOLDER = "/Temp";


	final static String TEMP_FOLDER_PATH = Environment.getExternalStorageDirectory().toString() + CONSTANTS.DCIM_FOLDER + CONSTANTS.VIDEO_FOLDER + CONSTANTS.TEMP_FOLDER;
	final static String  VIDEO_CONTENT_URI = "content://media/external/video/media";
	
	final static int RESOLUTION_HIGH_VALUE = 2;
	final static int RESOLUTION_MEDIUM_VALUE = 1;
	final static int RESOLUTION_LOW_VALUE = 0;

	final static int OUTPUT_WIDTH = 480;
	final static int OUTPUT_HEIGHT = 480;

	final static  boolean DO_YOU_WANT_WATER_MARK_ON_VIDEO = true;
}
