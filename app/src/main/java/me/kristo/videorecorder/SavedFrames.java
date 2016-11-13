package me.kristo.videorecorder;

class SavedFrames {

    private byte[] frameBytesData = null;
    private long timeStamp = 0L;
    private boolean isRotateVideo = false;
    private boolean isFrontCam = false;

    SavedFrames(byte[] frameBytesData, long timeStamp, boolean isRotateVideo, boolean isFrontCam) {
        setFrameBytesData(frameBytesData);
        setTimeStamp(timeStamp);
        setIsRotateVideo(isRotateVideo);
        setIsFrontCam(isFrontCam);
    }

    byte[] getFrameBytesData() {
        return frameBytesData;
    }

    private void setFrameBytesData(byte[] frameBytesData) {
        this.frameBytesData = frameBytesData;
    }

    long getTimeStamp() {
        return timeStamp;
    }

    private void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    boolean isRotateVideo() {
        return isRotateVideo;
    }

    private void setIsRotateVideo(boolean isRotateVideo) {
        this.isRotateVideo = isRotateVideo;
    }

    boolean isFrontCam() {
        return isFrontCam;
    }

    private void setIsFrontCam(boolean isFrontCam) {
        this.isFrontCam = isFrontCam;
    }
}
