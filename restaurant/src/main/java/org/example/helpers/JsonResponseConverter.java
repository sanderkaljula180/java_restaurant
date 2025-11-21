package org.example.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class JsonResponseConverter {

    public <T> byte[] convertArrayIntoJsonByte(List<T> listToConvert) {
        JSONArray jsonArray = new JSONArray(listToConvert);
        String jsonString = jsonArray.toString();
        return jsonString.getBytes();
    }

    public <T> byte[] convertDTOIntoJsonByte(T dtoToConvert) {
        JSONObject jsonObject = new JSONObject(dtoToConvert);
        String jsonString = jsonObject.toString();
        return jsonString.getBytes();
    }
}
