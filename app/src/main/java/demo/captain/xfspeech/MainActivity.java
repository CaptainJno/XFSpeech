package demo.captain.xfspeech;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import demo.captain.xfspeech.xfutils.SpeechSynthesizerUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView cache_tv;
    Button play_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_btn = (Button) findViewById(R.id.play_btn);
        cache_tv = (TextView) findViewById(R.id.cache_tv);
        play_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_btn:        //播放
                String str = SpeechSynthesizerUtil.getInstance().makeSpeech(getApplicationContext(), "Nice to meet U", "12345", null);  //语音缓存采用默认路径
//                String str = SpeechSynthesizerUtil.getInstance().makeSpeech(getApplicationContext(), "Nice to meet U", "12345",
//                        Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "XFSpeech");                     //语音缓存采用指定路径
                cache_tv.setText(str);
                break;
        }
    }
}
