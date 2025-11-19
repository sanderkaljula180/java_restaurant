package org.example.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class JsonResponseConverter {

    public <T> byte[] convertArrayIntoJsonByte(List<T> listToConvert) {
        JSONArray jsonArray = new JSONArray(listToConvert);
        String jsonString = jsonArray.toString();
        byte[] jsonBytes = jsonString.getBytes();
        return jsonBytes;
    }

    public <T> byte[] convertDTOIntoJsonByte(T dtoToConvert) {
        JSONObject jsonObject = new JSONObject(dtoToConvert);
        String jsonString = jsonObject.toString();
        byte[] jsonBytes = jsonString.getBytes();
        return jsonBytes;
    }
}
