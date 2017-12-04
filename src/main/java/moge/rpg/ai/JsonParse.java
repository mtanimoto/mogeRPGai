package moge.rpg.ai;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParse {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * JsonからMapに変換
     * 
     * @param json
     *            標準入力で読み込んだJson
     * @return map
     */
    @SuppressWarnings("unchecked")
    static Map<String, Object> fromJsonToMap(String json) {
        try {
            Map<String, Object> map = mapper.readValue(json, HashMap.class);
            return mapToDecompose(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 標準入力から読み込みMapに変換したデータを解析し更に分解する
     * 
     * @param map
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> mapToDecompose(Map<String, Object> map) {
        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                mapToDecompose((Map<String, Object>) value);
            } else {
                mapmap.put(key, value);
            }
        }
        return mapmap;
    }

    private static final Map<String, Object> mapmap = new HashMap<>();
}
