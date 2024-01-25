package dev.com.kupreychik.handlres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.com.kupreychik.model.ErrorMessage;
import dev.com.kupreychik.model.Port;
import dev.com.kupreychik.repository.Repository;
import dev.com.kupreychik.model.Device;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class DevicesHandler implements HttpHandler {
    private static Logger LOGGER = LogManager.getLogger(DevicesHandler.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                processGet(exchange);
                break;
            case "POST":
                processPost(exchange);
                break;
            case "DELETE":
                processDelete(exchange);
                break;
        }
    }

    private void processGet(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        LOGGER.trace("GET url = {}", url);

        if ("/devices".equals(url)) {
            try {
                List<Device> devices = Repository.fetchAllDevices();
                String resultJson = mapper.writeValueAsString(devices);

                ok(exchange, resultJson);
            } catch (Exception ex) {
                LOGGER.error("Exception during getting all devices", ex);
                String resultJson = mapper.writeValueAsString(new ErrorMessage(ex.getMessage()));
                nok(exchange, resultJson);
            }
        } else
            if (url.endsWith("/ports")) {
            //devices/1/port
            String deviceIdValue = url.replaceAll("\\D+", "");
            LOGGER.trace("deviceIdValue: {}", deviceIdValue);
            if (deviceIdValue.isEmpty()) {
                nok(exchange, "URL doesn't contain deviceId. URL: " + url);
            }

            try {
                long deviceId = Long.parseLong(deviceIdValue);
                List<Port> ports = Repository.fetchAllPortsByDevice(deviceId);
                String resultJson = mapper.writeValueAsString(ports);
                ok(exchange, resultJson);
            } catch (Exception ex) {
                nok(exchange, ex.getMessage());
            }
        } else {
                //device/1
            String deviceIdValue = url.substring(url.lastIndexOf('/') + 1);
            LOGGER.trace("deviceIdValue: {}", deviceIdValue);
            if (deviceIdValue.isEmpty()) {
                nok(exchange, "URL doesn't contain deviceId. URL: " + url);
                return;
            }

            try {
                long deviceId = Long.parseLong(deviceIdValue);
                Device device = Repository.fetchDevice(deviceId);
                String resultJson = mapper.writeValueAsString(device);
                ok(exchange, resultJson);
            } catch (Exception ex) {
                nok(exchange, mapper.writeValueAsString(new ErrorMessage(ex.getMessage())));
            }
        }
    }

    private void processPost(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        LOGGER.trace("POST url = {}", url);

        if (url.endsWith("/ports")) {
            String deviceIdValue = url.replaceAll("\\D+", "");
            LOGGER.trace("deviceIdValue: {}", deviceIdValue);
            if (deviceIdValue.isEmpty()) {
                nok(exchange, "URL doesn't contain deviceId. URL: " + url);
            }

            try {
                long deviceId = Long.parseLong(deviceIdValue);

                String portJson = new String(exchange.getRequestBody().readAllBytes());
                Port port = mapper.readValue(portJson, Port.class);

                Repository.addDevicePort(deviceId, port);

                String resultJson = mapper.writeValueAsString(port);
                ok(exchange, resultJson);
            } catch (Exception ex) {
                nok(exchange, ex.getMessage());
            }
        } else if (url.equals("/devices")) {
            LOGGER.trace("try add new device");
            try {
                String deviceJson = new String(exchange.getRequestBody().readAllBytes());
                LOGGER.trace("Device data: {}", deviceJson);
                Device device = mapper.readValue(deviceJson, Device.class);

                Repository.addDevice(device);

                LOGGER.trace("Device added: {}", device);
                String resultJson = mapper.writeValueAsString(device);
                ok(exchange, resultJson);
            } catch (Exception ex) {
                LOGGER.error(ex);
                nok(exchange, mapper.writeValueAsString(new ErrorMessage(ex.getMessage())));
            }
        } else {
            nok(exchange, mapper.writeValueAsString(new ErrorMessage("Can't map URL")));
        }
    }

    private void processDelete(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        LOGGER.trace("DELETE url = {}", url);

        String deviceIdValue = url.substring(url.lastIndexOf('/') + 1);
        LOGGER.trace("deviceIdValue: {}", deviceIdValue);
        if (deviceIdValue.isEmpty()) {
            nok(exchange, "URL doesn't contain deviceId. URL: " + url);
        }

        try {
            long deviceId = Long.parseLong(deviceIdValue);
            Repository.removeDevice(deviceId);
            ok(exchange, "");
        } catch (Exception ex) {
            nok(exchange, ex.getMessage());
        }
    }

    private void ok(HttpExchange exchange, String resultJson) throws IOException {
        exchange.sendResponseHeaders(200, resultJson.getBytes().length);
        exchange.getResponseBody().write(resultJson.getBytes());
        exchange.getResponseBody().flush();
    }

    private void nok(HttpExchange exchange, String resultJson) throws IOException {
        exchange.sendResponseHeaders(503, resultJson.getBytes().length);
        exchange.getResponseBody().write(resultJson.getBytes());
        exchange.getResponseBody().flush();
    }


}