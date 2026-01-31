package com.xyz.ordersvc.client.logger;

import feign.Logger;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class FeignClientLogger extends Logger {

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.debug(String.format(methodTag(configKey) + format, args));
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
            super.logRequest(configKey, logLevel, request);
            String body = null;
            if (request.body() != null && logLevel.ordinal() >= Level.FULL.ordinal()) {
                body = new String(request.body(), request.charset());
            }
            log.info("Sending Api ➡️ [Method: {}] [URL: {}] [Header: {}] [Request body: {}]",
                    request.httpMethod(),
                    request.url(),
                    request.headers(),
                    body
            );
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel,
                                              Response response, long elapsedTime) throws IOException {
        log.info("Receiving API ➡️ [Status: {}] [Header: {}] [Request body: {}]",
                response.status(),
                response.headers(),
                response.body()
        );
        return response;
    }

}
