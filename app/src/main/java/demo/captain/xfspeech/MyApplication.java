package demo.captain.xfspeech;

import android.app.Application;

import demo.captain.xfspeech.xfutils.SpeechSynthesizerUtil;

/**
 * Created by captainjno on 2017/5/25.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechSynthesizerUtil.initSpeech(getApplicationContext());            //科大讯飞语音合成功能，初始化
    }
}
