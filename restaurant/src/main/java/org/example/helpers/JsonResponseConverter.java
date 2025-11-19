package org.example.helpers;

import org.json.JSONArray;

import java.util.List;

public class JsonResponseConverter {

    public <T> byte[] convertArrayIntoJsonByte(List<T> listToConvert) {
        JSONArray jsonArray = new JSONArray(listToConvert);
        String jsonString = jsonArray.toString();
        byte[] jsonBytes = jsonString.getBytes();
        return jsonBytes;
    }

}
