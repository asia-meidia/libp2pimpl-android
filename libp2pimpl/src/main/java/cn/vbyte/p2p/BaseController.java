package cn.vbyte.p2p;

import com.vbyte.p2p.IController;
import com.vbyte.p2p.OnLoadedListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by passion on 16-1-14.
 */
public abstract class BaseController implements IController {

    // load => unload => load 操作的异步转同步
    public static final int VIDEO_LIVE = 0;
    public static final int VIDEO_VOD = 1;
    public static class LoadEvent {
        public int videoType;
        public String channel;
        public String resolution;
        public double startTime;
        public OnLoadedListener listener;

        public LoadEvent(int videoType, String channel, String resolution, OnLoadedListener listener) {
            this(videoType, channel, resolution, 0, listener);
        }

        public LoadEvent(int videoType, String channel, String resolution, double startTime, OnLoadedListener listener) {
            this.videoType = videoType;
            this.channel = channel;
            this.resolution = resolution;
            this.startTime = startTime;
            this.listener = listener;
        }
    }
    protected static List<LoadEvent> loadQueue = Collections.synchronizedList(new LinkedList<LoadEvent>());

    /**
     * 这2个工具函数能让LiveController和VodController能事先对P2P线程反应的事件进行预处理
     * @param code 事件码
     * @param msg 事件说明
     */
    protected void onEvent(int code, String msg) {}
    protected void onError(int code, String msg) {}

    /**
     * 为方便子类实现
     * @param channel 对直播是频道ID，对点播是资源链接
     * @param resolution 统一为 "UHD"
     * @param listener 当成功load时的回调函数
     * @throws Exception 当load/unload没有成对调用时，会抛出异常提示
     */
    @Override
    public void load(String channel, String resolution, OnLoadedListener listener) throws Exception {
        load(channel, resolution, 0, listener);
    }

    /**
     * 直接调用native层的load
     * @param channel 对直播是频道ID，对点播是资源链接
     * @param resolution 统一为 "UHD"
     * @param startTime 起始时间，直播是时移，点播即开始时间偏移
     */
    protected void loadDirectly(String channel, String resolution, double startTime) {}

    /**
     * 对直播而言，seek函数并无意义
     * @param startTime 随机点播时的起始时间点
     */
    @Override
    public void seek(double startTime) {
        return;
    }

    @Override
    public void pause() {
        return;
    }

    @Override
    public void resume() { return; }
}
