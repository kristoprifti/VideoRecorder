package me.kristo.videorecorder;

import org.bytedeco.javacpp.avcodec;

class RecorderParameters {

    private static boolean AAC_SUPPORTED = true;
    private int videoQuality = 5;
    private int audioCodec = AAC_SUPPORTED ? avcodec.AV_CODEC_ID_AAC : avcodec.AV_CODEC_ID_AMR_NB;
    private int audioBitrate = 96000;//192000;//AAC_SUPPORTED ? 96000 : 12200;
    private int audioSamplingRate = AAC_SUPPORTED ? 44100 : 8000;
    private String videoOutputFormat = AAC_SUPPORTED ? "mp4" : "3gp";

    String getVideoOutputFormat() {
        return videoOutputFormat;
    }

    int getAudioSamplingRate() {
        return audioSamplingRate;
    }

    int getVideoCodec() {
        return avcodec.AV_CODEC_ID_H264;
    }

    int getVideoFrameRate() {
        return 30;
    }

    int getVideoQuality() {
        return videoQuality;
    }

    void setVideoQuality(int videoQuality) {
        this.videoQuality = videoQuality;
    }

    int getAudioCodec() {
        return audioCodec;
    }

    int getAudioBitrate() {
        return audioBitrate;
    }

    void setAudioBitrate(int audioBitrate) {
        this.audioBitrate = audioBitrate;
    }

    int getVideoBitrate() {
        return 1000000;
    }

    int getAudioChannel() {
        return 1;
    }

}
