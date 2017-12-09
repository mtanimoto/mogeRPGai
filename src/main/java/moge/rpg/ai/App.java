package moge.rpg.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        System.out.println("つばくろ");

        ActionManager am = new ActionManager();
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line = br.readLine();
            Map<String, Object> data = mapper.readValue(line, HashMap.class);
            am.load(data);
            String output = am.execute();
            System.out.println(output);
        }
    }

}
