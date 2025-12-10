package org.example.helpers;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonResponseConverter {

    public <T> byte[] convertArrayIntoJsonByte(List<T> listToConvert) {
        JSONArray jsonArray = new JSONArray(listToConvert);
        String jsonString = jsonArray.toString();
        return jsonString.getBytes();
    }

    public <T> JSONObject convertRequestBodyJsonByteIntoJsonObject(HttpExchange httpExchange) throws IOException {
        InputStream request = httpExchange.getRequestBody();
        byte[] requestBytes = request.readAllBytes();
        String jsonRequestString = new String(requestBytes, StandardCharsets.UTF_8);
        return new JSONObject(jsonRequestString);
    }

    public <T> byte[] convertDTOIntoJsonByte(T dtoToConvert) {
        JSONObject jsonObject = new JSONObject(dtoToConvert);
        String jsonString = jsonObject.toString();
        return jsonString.getBytes();
    }
}
