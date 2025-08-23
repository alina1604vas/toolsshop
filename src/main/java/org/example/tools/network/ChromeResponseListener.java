package org.example.tools.utils.network;

import com.google.gson.Gson;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.network.Network;
import org.openqa.selenium.devtools.v137.network.model.RequestId;
import org.openqa.selenium.devtools.v137.network.model.Response;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.Optional;
import java.util.function.Consumer;

public class ChromeResponseListener {

    private final DevTools devTools;

    public ChromeResponseListener(DevTools devTools) {
        this.devTools = devTools;

        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
    }

    public <T> void subscribe(String endpointPattern, Type type, Consumer<T> onResponse) {
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response response = responseReceived.getResponse();

            if (response.getUrl().matches(endpointPattern) && response.getMimeType().contains("application/json") &&
                    response.getStatus() == HttpURLConnection.HTTP_OK) {
                RequestId requestId = responseReceived.getRequestId();
                Network.GetResponseBodyResponse bodyResponse = devTools.send(Network.getResponseBody(requestId));
                String body = bodyResponse.getBody();

                T parsed = new Gson().fromJson(body, type);

                onResponse.accept(parsed);
            }
        });
    }

    public void unSubscribe() {
        devTools.clearListeners();
    }

}
