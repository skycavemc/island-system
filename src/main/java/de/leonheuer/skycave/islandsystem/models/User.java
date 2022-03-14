package de.leonheuer.skycave.islandsystem.models;

import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

public class User {

    @BsonId
    private ObjectId id;
    private String uuid;
    @BsonProperty(value = "ignored_entity_limits")
    private HashMap<Integer, List<String>> ignoredEntityLimits;

    public User(String uuid) {
        this.uuid = uuid;
        this.ignoredEntityLimits = new HashMap<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public HashMap<Integer, List<String>> getIgnoredEntityLimits() {
        return ignoredEntityLimits;
    }

    public void setIgnoredEntityLimits(HashMap<Integer, List<String>> ignoredEntityLimitTypes) {
        this.ignoredEntityLimits = ignoredEntityLimitTypes;
    }
}
