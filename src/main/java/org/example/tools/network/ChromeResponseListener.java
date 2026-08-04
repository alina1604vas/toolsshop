package org.example.tools.network;

import com.google.gson.Gson;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v143.network.Network;
import org.openqa.selenium.devtools.v143.network.model.RequestId;
import org.openqa.selenium.devtools.v143.network.model.Response;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.function.Consumer;

public class ChromeResponseListener {

    private static final int MAX_BODY_READ_ATTEMPTS = 6;
    private static final long BODY_READ_RETRY_DELAY_MS = 100;

    private final DevTools devTools;
    private final Map<String, ObserverData<?>> observers = new LinkedHashMap<>();

    public ChromeResponseListener(DevTools devTools) {
        this.devTools = devTools;

        devTools.createSession();
        devTools.send(Network.enable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty())
        );
        devTools.send(Network.setCacheDisabled(true));

        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response response = responseReceived.getResponse();
            String url = response.getUrl();

            if (response.getMimeType().contains("application/json") && response.getStatus() == HttpURLConnection.HTTP_OK) {
                for (Map.Entry<String, ObserverData<?>> entry : observers.entrySet()) {
                    String pattern = entry.getKey();
                    ObserverData<?> observerData = entry.getValue();

                    if (url.matches(pattern)) {
                        RequestId requestId = responseReceived.getRequestId();
                        // responseReceived only guarantees headers have arrived; the body
                        // may not be fully buffered yet (more likely for larger payloads),
                        // so retry a few times with a short delay instead of failing once.
                        String body = readResponseBodyWithRetry(requestId, url);
                        if (body != null) {
                            Object parsed = new Gson().fromJson(body, observerData.type);
                            observerData.call(parsed);
                        }
                    }
                }
            }
        });
    }

    private String readResponseBodyWithRetry(RequestId requestId, String url) {
        for (int attempt = 1; attempt <= MAX_BODY_READ_ATTEMPTS; attempt++) {
            try {
                Network.GetResponseBodyResponse bodyResponse = devTools.send(Network.getResponseBody(requestId));
                return bodyResponse.getBody();
            } catch (Exception e) {
                if (attempt == MAX_BODY_READ_ATTEMPTS) {
                    System.err.println("[ChromeResponseListener] Failed to read response body for " + url
                            + " after " + attempt + " attempts: " + e);
                    return null;
                }
                try {
                    Thread.sleep(BODY_READ_RETRY_DELAY_MS);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        }
        return null;
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
