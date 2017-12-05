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
     * @return map 変換後マップ
     */
    @SuppressWarnings("unchecked")
    static Map<String, Object> fromJsonToMap(String json) {
        try {
            Map<String, Object> map = mapper.readValue(json, HashMap.class);
            Map<String, Object> decomposeMap = new HashMap<>();
            return mapToDecompose(decomposeMap, map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 標準入力から読み込みMapに変換したデータを解析し更に分解する
     *
     * @param decomposeMap 解析済みマップの格納用
     * @param map 解析前マップ
     * @return 解析済みマップ
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> mapToDecompose(Map<String, Object> decomposeMap, Map<String, Object> map) {
        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                mapToDecompose(decomposeMap, (Map<String, Object>) value);
            } else {
                decomposeMap.put(key, value);
            }
        }
        return decomposeMap;
    }
}
