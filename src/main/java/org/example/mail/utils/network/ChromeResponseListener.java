package org.example.mail.utils.network;

import com.google.gson.Gson;
import org.example.mail.utils.JsonUtils;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.network.Network;
import org.openqa.selenium.devtools.v137.network.model.RequestId;
import org.openqa.selenium.devtools.v137.network.model.Response;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ChromeResponseListener {

    private final DevTools devTools;

    public ChromeResponseListener(DevTools devTools) {
        this.devTools = devTools;

        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
    }

    public <T> void subscribe(String endpoint, Class<T> clazz, Consumer<T> onResponse, String str) {
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response response = responseReceived.getResponse();

            if (response.getUrl().contains(endpoint) && response.getStatus() == 200) {
                RequestId requestId = responseReceived.getRequestId();
                Network.GetResponseBodyResponse bodyResponse = devTools.send(Network.getResponseBody(requestId));
                String body = bodyResponse.getBody();

                T sss = new Gson().fromJson(body, clazz);

                onResponse.accept(sss);
            }
        });
    }

    public <T> void subscribe(String endpoint, Class<T> clazz, Consumer<List<T>> onResponse) {
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response response = responseReceived.getResponse();

            if (response.getUrl().contains(endpoint) && response.getStatus() == 200) {
                RequestId requestId = responseReceived.getRequestId();
                Network.GetResponseBodyResponse bodyResponse = devTools.send(Network.getResponseBody(requestId));
                String body = bodyResponse.getBody();

                // TODO: not all responses are collected as lists
                List<T> items = JsonUtils.fromJsonList(body, clazz);

                onResponse.accept(items);
            }
        });
    }

    public void unSubscribe() {
        devTools.clearListeners();
    }

}
