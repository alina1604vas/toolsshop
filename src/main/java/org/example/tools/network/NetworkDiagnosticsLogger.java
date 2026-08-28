package org.example.tools.network;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v151.network.Network;

import java.util.Optional;

/**
 * TEMP diagnostic: passively logs the browser's real network traffic over CDP so we can see,
 * in the CI console, exactly what the page requested and what came back (status codes,
 * blocked/failed requests). Useful for figuring out why a page renders empty on the runner.
 *
 * This only LISTENS - it does not pause, replay or modify any request, and it does NOT
 * disable the browser cache. Remove the wiring in BaseTest once the investigation is done.
 */
public class NetworkDiagnosticsLogger {

    private final DevTools devTools;
    private boolean started = false;

    public NetworkDiagnosticsLogger(DevTools devTools) {
        this.devTools = devTools;
    }

    public void start() {
        devTools.createSession();
        devTools.send(Network.enable(
                Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty()));
        started = true;

        // Every response the browser received: HTTP status + resource type + URL.
        devTools.addListener(Network.responseReceived(), event -> {
            try {
                System.out.println("[NET] " + event.getResponse().getStatus()
                        + " " + event.getType()
                        + " " + event.getResponse().getUrl());
            } catch (Exception e) {
                System.out.println("[NET] <failed to read responseReceived: " + e + ">");
            }
        });

        // Requests that failed or were blocked (CORS, connection refused, blocked by client, etc.).
        // errorText carries the reason, e.g. "net::ERR_BLOCKED_BY_CLIENT" / "net::ERR_FAILED".
        devTools.addListener(Network.loadingFailed(), event -> {
            try {
                System.out.println("[NET-FAIL] " + event.getType()
                        + " errorText=" + event.getErrorText());
            } catch (Exception e) {
                System.out.println("[NET-FAIL] <failed to read loadingFailed: " + e + ">");
            }
        });
    }

    public void stop() {
        if (!started) {
            return;
        }
        try {
            devTools.clearListeners();
        } catch (Exception ignored) {
        }
        started = false;
    }
}
