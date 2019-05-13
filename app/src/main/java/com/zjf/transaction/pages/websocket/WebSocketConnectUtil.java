package com.zjf.transaction.pages.websocket;

import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.user.UserConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class WebSocketConnectUtil {
    private final static int CONNECT_TIMEOUT = 60;
    private final static int READ_TIMEOUT = 60;
    private final static int WRITE_TIMEOUT = 60;
    private static OkHttpClient client;
    private static volatile WebSocketConnectUtil webSocketConnectUtil;
    private WebSocket webSocket;
    private ChatWebSocketListener listener;

    private WebSocketConnectUtil() {
        client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        listener = new ChatWebSocketListener();
    }

    public static WebSocketConnectUtil getInstance() {
        if (webSocketConnectUtil == null) {
            synchronized (WebSocketConnectUtil.class) {
                if (webSocketConnectUtil == null) {
                    webSocketConnectUtil = new WebSocketConnectUtil();
                }
            }
        }
        return webSocketConnectUtil;
    }

    public void connectWebSocket() {
        final String url = BaseConstant.WEBSOCKET_URL + UserConfig.inst().getUserId();
        Request request = new Request.Builder().url(url).build();
        client.newWebSocket(request, listener);
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public void addOnWebSocketListener(ChatWebSocketListener.OnWebSocketListener onWebSocketListener) {
        listener.addOnWebSocketListener(onWebSocketListener);
    }

    public void removeOnWebSocketListener(ChatWebSocketListener.OnWebSocketListener onWebSocketListener) {
        listener.removeWebSocketListener(onWebSocketListener);
    }
}
