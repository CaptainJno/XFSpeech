# XFSpeech
讯飞在线语音合成 优化

优化策略：将讯飞在线语音生成的语音，缓存在指定路径，以后每回播放语音时，先播放缓存。无缓存才网络合成。

优化结果：1、省流量：首次播放时才网络合成。2、响应快：播放缓存时，速度明显快于网络合成。

知识点：缓存的语音格式是.pcm，播放缓存采用AudioTrack。

使用步骤：

 1、初始化语音合成功能：
 
 SpeechSynthesizerUtil.initSpeech(getApplicationContext());
 
 
 2、播放语音：
 
 SpeechSynthesizerUtil.getInstance().makeSpeech(getApplicationContext(), "Nice to meet U", "12345", null);  //默认缓存路径
 
 String str = SpeechSynthesizerUtil.getInstance().makeSpeech(getApplicationContext(), "Nice to meet U", "12345", 指定缓存路径);  //指定缓存路径
 
使用中有任何问题、优化建议，请提Issues。我会尽快fix 或 update。
