package org.example.tools.network;

import com.google.gson.Gson;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.network.Network;
import org.openqa.selenium.devtools.v137.network.model.RequestId;
import org.openqa.selenium.devtools.v137.network.model.Response;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.function.Consumer;

public class ChromeResponseListener {

    private final DevTools devTools;
    private final Map<String, ObserverData<?>> observers = new LinkedHashMap<>();

    public ChromeResponseListener(DevTools devTools) {
        this.devTools = devTools;

        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response response = responseReceived.getResponse();
            if (response.getMimeType().contains("application/json") && response.getStatus() == HttpURLConnection.HTTP_OK) {
                String url = response.getUrl();

                for (Map.Entry<String, ObserverData<?>> entry : observers.entrySet()) {
                    String pattern = entry.getKey();
                    ObserverData<?> observerData = entry.getValue();

                    if (url.matches(pattern)) {
                        RequestId requestId = responseReceived.getRequestId();
                        Network.GetResponseBodyResponse bodyResponse = devTools.send(Network.getResponseBody(requestId));
                        String body = bodyResponse.getBody();

                        Object parsed = new Gson().fromJson(body, observerData.type);
                        observerData.call(parsed);
                    }
                }
            }
        });
    }

    public <T> void addObserver(String endpointPattern, Type type, Consumer<T> onResponse) {
        observers.put(endpointPattern, new ObserverData<>(type, onResponse));
    }

    public void removeObserver(String endpointPattern) {
        observers.remove(endpointPattern);
    }

    public void destroy() {
        devTools.clearListeners();
        observers.clear();
    }

    private static class ObserverData<T> {

        private final Type type;
        private final Consumer<T> callback;

        public ObserverData(Type type, Consumer<T> callback) {
            this.type = type;
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        public void call(Object parsed) {
            callback.accept((T) parsed);
        }
    }

}
