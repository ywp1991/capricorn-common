package com.caishi.capricorn.common.push.constants;

import com.caishi.capricorn.common.push.protocol.MessageProtocol;
import com.caishi.capricorn.common.push.protocol.SceneProtocol;

import java.lang.reflect.Type;

public enum PushType {

    /**
     * news
     */
    NEWS(1, "newsDetail", MessageProtocol.class, "新闻推送"),

    /**
     * scene
     */
    SCENE(2, "scene", SceneProtocol.class, "场景推送");

    /**
     * constructors
     *
     * @param id          push type id
     * @param name        push type name
     * @param description push type description
     */
    PushType(int id, String name, Type type, String description) {
        setId(id);
        setName(name);
        setType(type);
        setDescription(description);
    }

    /**
     * push type id
     */
    private int id;

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    /**
     * push type name
     */
    private String name;

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private Type type;

    public Type getType() {
        return type;
    }

    private void setType(Type type) {
        this.type = type;
    }

    /**
     * push type description
     */
    private String description;

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    /**
     * get push type info by push type id
     *
     * @param id push type id
     * @return push type info
     */
    public final static PushType getPushType(int id) {
        if (id >= 1 && id <= 2) {
            for (PushType pushType : PushType.values()) {
                if (pushType.getId() == id) {
                    return pushType;
                }
            }
        }
        return null;
    }

    /**
     * get push type info by push type name
     *
     * @param name push type name
     * @return push type info
     */
    public final static PushType getPushType(String name) {
        if (name != null && (!(name = name.replace(" ", "")).isEmpty())) {
            for (PushType pushType : PushType.values()) {
                if (pushType.getName().equalsIgnoreCase(name)) {
                    return pushType;
                }
            }
        }
        return null;
    }

    /**
     * make validation by push type id
     *
     * @param id push type id
     * @return valdiation status
     */
    public final static Boolean validation(int id) {
        return getPushType(id) != null;
    }

    /**
     * make validation by push type name
     *
     * @param name push type name
     * @return validation status
     */
    public final static Boolean validation(String name) {
        return getPushType(name) != null;
    }
}