package demo.captain.xfspeech.xfutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by captainjno on 2017/5/25.
 * 科大讯飞语音合成--工具类
 */

public class SpeechSynthesizerUtil {
    AudioPlayer mAudioPlayer = null;
    private static SpeechSynthesizerUtil instance;

    public synchronized static SpeechSynthesizerUtil getInstance() {
        if (instance == null) {
            instance = new SpeechSynthesizerUtil();
        }
        return instance;
    }

    public static void initSpeech(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String msg = appInfo.metaData.getString("SUNFLOWER_APPID");
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + msg);
    }

    public void makeSpeech(Context context, String content, String speechId) {
        makeSpeech(context, content, speechId, null);
    }

    /**
     * @param speechContent:语音内容
     * @param speechId:唯一语音Id(字符串，切记不可与其他语音id重复)
     * @param speechSaveParentPath:语音存储的文件夹完整路径(如Environment.getExternalStorageDirectory().getAbsolutePath() )，结尾不能含"/"
     * @注意：当speechId和speechSaveParentPath任一为空时，语音存储使用默认路径(Configs.xfSpeechSavePath + speechId + ".pcm")
     */
    public String makeSpeech(Context context, String speechContent, String speechId, String speechSaveParentPath) {
        String speechSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "XFSpeech" + File.separator + speechId + ".pcm";                   //语音文件默认路径
        if (speechSaveParentPath != null && speechSaveParentPath.trim().length() != 0) {
            speechSavePath = speechSaveParentPath + File.separator + speechId + ".pcm";
        }
        if (speechContent == null || speechContent.trim().length() == 0) {
            return "";
        }

 /*--------------------------下面这段是播放本地缓存的关键代码-------------------------*/
        File file = new File(speechSavePath);
        if (file.exists()) {
            Log.e(">>>", "有缓存的语音，播放");
            if (mAudioPlayer == null) {
                mAudioPlayer = new AudioPlayer(null);

                mAudioPlayer.setAudioParam(getAudioParam());    // 获取音频参数
            }
            byte[] data = getPCMData(file);                     // 获取音频数据
            mAudioPlayer.setDataSource(data);
            mAudioPlayer.prepare();                             // 音频源就绪
            mAudioPlayer.play();
            return "播放缓存语音";
        }

/*--------------------------下面这段是科大讯飞官方代码-------------------------*/
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null/*new InitListener() {
            @Override
            public void onInit(int i) {

            }
        }*/);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "vils");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "100");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置）
        //如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, speechSavePath);
        //3.开始合成,第二个参数：合成监听器（可单独摘出）
        mTts.startSpeaking(speechContent, new SynthesizerListener() {
            //会话结束回调接口，没有错误时，error为null
            public void onCompleted(SpeechError error) {
                error.printStackTrace();
            }

            public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            }

            public void onSpeakBegin() {
            }

            public void onSpeakPaused() {
            }

            public void onSpeakProgress(int percent, int beginPos, int endPos) {
            }

            public void onSpeakResumed() {
            }

            public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            }
        });
        return "网络合成";
    }

    /*--------------------------以下代码与讯飞无关-------------------------*/

    /*
    * 获得PCM音频数据参数
    */

    AudioParam audioParam = null;

    public AudioParam getAudioParam() {
        if (audioParam == null) {
            audioParam = new AudioParam();
            audioParam.mFrequency = 8000;            //44100是谷歌建议的采样率（适配效果最好的），8000是针对科大讯飞语音的采样率
//        audioParam.mFrequency = 44100;            //44100是谷歌建议的采样率（适配效果最好的），8000是针对科大讯飞语音的采样率
            audioParam.mChannel = AudioFormat.CHANNEL_CONFIGURATION_STEREO;  //值是 3
            audioParam.mSampBit = AudioFormat.ENCODING_PCM_16BIT;   //值是 2
        }
        return audioParam;
    }

    /*
   * 获得PCM音频数据
   */
    public byte[] getPCMData(File file) {
        if (file == null) {
            return null;
        }
        FileInputStream inStream;
        try {
            inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        byte[] data_pack = null;
        if (inStream != null) {
            long size = file.length();
            data_pack = new byte[(int) size];
            try {
                inStream.read(data_pack);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
        return data_pack;
    }
}
