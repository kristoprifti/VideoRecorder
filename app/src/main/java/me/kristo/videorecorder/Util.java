package me.kristo.videorecorder;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.io.File;
import java.util.Comparator;
import java.util.List;

class Util {
    static ContentValues videoContentValues = null;
    // Orientation hysteresis amount used in rounding, in degrees
    private static final int ORIENTATION_HYSTERESIS = 5;

    @SuppressLint("ObsoleteSdkInt")
    static int determineDisplayOrientation(Activity activity, int defaultCameraId) {
        int displayOrientation = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(defaultCameraId, cameraInfo);

            int degrees = getRotationAngle(activity);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                displayOrientation = (cameraInfo.orientation + degrees) % 360;
                displayOrientation = (360 - displayOrientation) % 360;
            } else {
                displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
            }
        }
        return displayOrientation;
    }

    private static int getRotationAngle(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
    }

    static String createImagePath(Context context) {
        long dateTaken = System.currentTimeMillis();
        String title = CONSTANTS.FILE_START_NAME + dateTaken;
        String filename = title + CONSTANTS.IMAGE_EXTENSION;

        String dirPath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + CONSTANTS.IMAGE_FOLDER;
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory())
            file.mkdirs();
        return dirPath + "/" + filename;
    }

    static File createWatermarkFilePath(Context context) {
        String filename = CONSTANTS.FILE_START_NAME + CONSTANTS.WATERMARK_NAME + CONSTANTS.WATERMARK_EXTENSION;

        String dirPath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + CONSTANTS.IMAGE_FOLDER;
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory())
            file.mkdirs();
        return new File(file ,filename);
    }

    static String createFinalPath(Context context) {
        long dateTaken = System.currentTimeMillis();
        String title = CONSTANTS.FILE_START_NAME + dateTaken;
        String filename = title + CONSTANTS.VIDEO_EXTENSION;
        String filePath = genrateFilePath(context, String.valueOf(dateTaken), true, null);

        ContentValues values = new ContentValues(7);
        values.put(Video.Media.TITLE, title);
        values.put(Video.Media.DISPLAY_NAME, filename);
        values.put(Video.Media.DATE_TAKEN, dateTaken);
        values.put(Video.Media.MIME_TYPE, "video/3gpp");
        values.put(Video.Media.DATA, filePath);
        videoContentValues = values;

        return filePath;
    }


    private static String genrateFilePath(Context context, String uniqueId, boolean isFinalPath, File tempFolderPath) {
        String fileName = CONSTANTS.FILE_START_NAME + uniqueId + CONSTANTS.VIDEO_EXTENSION;
        String dirPath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + CONSTANTS.VIDEO_FOLDER;
        if (isFinalPath) {
            File file = new File(dirPath);
            if (!file.exists() || !file.isDirectory())
                file.mkdirs();
        } else
            dirPath = tempFolderPath.getAbsolutePath();
        return dirPath + "/" + fileName;
    }

    static List<Camera.Size> getResolutionList(Camera camera) {
        Parameters parameters = camera.getParameters();
        return parameters.getSupportedPreviewSizes();
    }

    static RecorderParameters getRecorderParameter(int currentResolution) {
        RecorderParameters parameters = new RecorderParameters();
        if (currentResolution == CONSTANTS.RESOLUTION_HIGH_VALUE) {
            parameters.setAudioBitrate(128000);
            parameters.setVideoQuality(0);
        } else if (currentResolution == CONSTANTS.RESOLUTION_MEDIUM_VALUE) {
            parameters.setAudioBitrate(128000);
            parameters.setVideoQuality(5);
        } else if (currentResolution == CONSTANTS.RESOLUTION_LOW_VALUE) {
            parameters.setAudioBitrate(96000);
            parameters.setVideoQuality(20);
        }
        return parameters;
    }

    static class ResolutionComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size size1, Camera.Size size2) {
            if (size1.height != size2.height)
                return size1.height - size2.height;
            else
                return size1.width - size2.width;
        }
    }

    static int getTimeStampInNsFromSampleCounted(int paramInt) {
        return (int) (paramInt / 0.0441D);
    }

    static void showDialog(Context context, String title, String content, int type, final Handler handler) {
        final Dialog dialog = new Dialog(context, R.style.Dialog_loading);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.global_dialog, null);
        Button confirmButton = (Button) view
                .findViewById(R.id.btn_confirm);
        Button cancelButton = (Button) view
                .findViewById(R.id.btn_cancel);
        TextView dialogTitle = (TextView) view
                .findViewById(R.id.txt_title);
        View line_hori_center = view.findViewById(R.id.divider);
        confirmButton.setVisibility(View.GONE);
        line_hori_center.setVisibility(View.GONE);
        TextView textView = (TextView) view.findViewById(R.id.txt_msg);

        Window dialogWindow = dialog.getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().density * 288);
        dialogWindow.setAttributes(lp);

        if (type != 1 && type != 2) {
            type = 1;
        }
        dialogTitle.setText(title);
        textView.setText(content);

        confirmButton.setVisibility(View.VISIBLE);
        confirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
                dialog.dismiss();
            }
        });
        if (type == 2) {
            cancelButton.setVisibility(View.VISIBLE);
            line_hori_center.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (handler != null) {
                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                    dialog.dismiss();
                }
            });
        }
        dialog.addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    static int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }

}
