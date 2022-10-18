package cn.iyunbei.handself.service;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

public class Audio {
    private Thread newThread; //声明一个子线程

    //variables
    private int audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
    private int samplingRate = 44100; /* in Hz*/
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize = AudioRecord.getMinBufferSize(samplingRate, channelConfig, audioFormat) + 2048;
    private int sampleNumBits = 16;
    private int numChannels = 1;
    private boolean isLink = false;
    private int readBytes=0, writtenBytes=0;

    public Audio() {

        newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AudioRecord recorder = new AudioRecord(audioSource, samplingRate, channelConfig, audioFormat, bufferSize);
                recorder.startRecording();

                AudioTrack audioPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

                if(audioPlayer.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
                    audioPlayer.play();

//capture data and record to file
                byte[] data = new byte[bufferSize];

                do{
                    readBytes = recorder.read(data, 0, bufferSize);

                    if(AudioRecord.ERROR_INVALID_OPERATION != readBytes){
                        writtenBytes += audioPlayer.write(data, 0, readBytes);
                    }

                }
                while(true);
            }
        });
//        newThread.start(); //启动线程

    }


}
