package moge.rpg.ai.action;

import java.util.Map;

public interface Action {

    Action load(Map<String, Object> receiveData);

    String execute();

}
