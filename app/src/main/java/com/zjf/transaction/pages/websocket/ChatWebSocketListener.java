package com.zjf.transaction.pages.websocket;

import com.google.gson.Gson;
import com.zjf.transaction.database.entity.Msg;
import com.zjf.transaction.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatWebSocketListener extends WebSocketListener {

    private boolean isConnect;

    public interface OnWebSocketListener {
        void onOpen(WebSocket webSocket, Response response);

        void onMessage(String text);

        void onClosed(WebSocket webSocket, int code, String reason);
    }

    private List<OnWebSocketListener> listenerList = new ArrayList<>();


    public void addOnWebSocketListener(OnWebSocketListener listener) {
        if (listener != null) {
            listenerList.add(listener);
        }
    }

    public void removeWebSocketListener(OnWebSocketListener listener) {
        if (listener != null) {
            listenerList.remove(listener);
        }
    }

    @Override
    public void onOpen(final WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        LogUtil.d("web socket onOpen");
        isConnect = true;
        if (listenerList != null) {
            for (OnWebSocketListener listener : listenerList) {
                listener.onOpen(webSocket, response);
            }
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        LogUtil.d("web socket onMessage, text -> %s", text);
//        Msg item = new Gson().fromJson(text, Msg.class);
        if (listenerList != null) {
            for (OnWebSocketListener listener : listenerList) {
                listener.onMessage(text);
            }
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
//        WebSocketConnectUtil.getInstance().connectWebSocket();
        LogUtil.d("web socket onClosing");
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        LogUtil.d("web socket onClosed");
        isConnect = false;
        if (listenerList != null) {
            for (OnWebSocketListener listener : listenerList) {
                listener.onClosed(webSocket, code, reason);
            }
        }

    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        LogUtil.e("web socket onFailure, throwable -> %s", t.getMessage());
        isConnect = false;
        while (!isConnect) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WebSocketConnectUtil.getInstance().connectWebSocket();
        }
    }
}
