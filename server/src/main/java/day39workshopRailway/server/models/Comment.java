package day39workshopRailway.server.models;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Comment {
    private String charId;
    private String comment;

    public String getCharId() {
        return charId;
    }
    public void setCharId(String charId) {
        this.charId = charId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public static Comment create(Document doc) {
        Comment comment = new Comment();
        comment.setCharId(doc.getString("charId").toString());
        comment.setComment(doc.getString("comment"));
        return comment;
    }

    public JsonObject toJSON() {
        JsonObject jobj = Json.createObjectBuilder()
                .add("charId", getCharId())
                .add("comment", getComment())
                .build();
        return jobj;
    }
}
