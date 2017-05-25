package demo.captain.xfspeech;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import demo.captain.xfspeech.xfutils.SpeechSynthesizerUtil;

/**
 * Created by captainjno on 2017/5/25.
 */
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
            case R.id.play_btn:
                cache_tv.setText(SpeechSynthesizerUtil.getInstance().makeSpeech(getApplicationContext(), "Nice to meet U", "12345", null));
                break;
        }
    }
}
