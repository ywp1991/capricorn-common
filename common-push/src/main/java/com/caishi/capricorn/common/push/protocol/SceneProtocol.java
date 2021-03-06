package com.caishi.capricorn.common.push.protocol;

import java.io.Serializable;

public class SceneProtocol extends BasicProtocol implements Serializable {

    /**
     * scene id
     */
    private String sceneId;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    /**
     * scene identified
     */
    private String identified;

    public String getIdentified() {
        return identified;
    }

    public void setIdentified(String identified) {
        this.identified = identified;
    }
}