package day39workshopRailway.server.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

public class MarvelCharacter implements Serializable{
    private Integer id;
    private String name;
    private String desc;
    private String photo;
    private List<Comment> comments;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // Creates a MarvelChacter Model Object
    public static MarvelCharacter create(JsonObject obj) {
        MarvelCharacter mc = new MarvelCharacter();
        JsonObject tempObj = obj.getJsonObject("thumbnail");
        String path = tempObj.getString("path");
        String ext = tempObj.getString("extension");

        mc.id = obj.getJsonNumber("id").intValue();
        mc.name = obj.getString("name");
        mc.desc = obj.getString("description");
        mc.photo = path + '.' + ext;

        System.out.println("mc.id >>> " + mc.id);
        System.out.println("mc.name >>> " + mc.name);
        System.out.println("mc.desc >>> " + mc.id);
        System.out.println("mc.photo >>> " + mc.photo);

        return mc;
    }

    // Creates a list of MarvelCharacter Model Objects
    public static List<MarvelCharacter> createListOfMarvChar(String json) throws IOException {
        List<MarvelCharacter> marvCharList = new LinkedList<>();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject obj = r.readObject();
            JsonObject tempObj = obj.getJsonObject("data");
            
            if (tempObj.getJsonArray("results") != null) {
                marvCharList = tempObj.getJsonArray("results").stream()
                    //.map(v -> (JsonObject) v)
                    .map(JsonValue::asJsonObject)
                    // .map(v -> MarvelCharacter.create(v))
                    .map(MarvelCharacter::create)
                    .toList();
            }
        }
        System.out.println("marvCharList >>> " + marvCharList.toString());
        return marvCharList;
    }

    // Function to convert MarvelCharacter Model Object to JSON
    public JsonObject toJSON() {
        JsonObject JsonObj = Json.createObjectBuilder()
            .add("id", getId())
            .add("name", getName())
            .add("description", getDesc())
            .add("photo", getPhoto())
            .build();
        return JsonObj;
    }

    // Creates MarvelCharacter Object for Redis Cache
    public static MarvelCharacter createForCache(String json) throws IOException {
        MarvelCharacter mc = new MarvelCharacter();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject obj = r.readObject();
            mc.setId(obj.getJsonNumber("id").intValue());
            mc.setName(obj.getString("name"));
            mc.setDesc(obj.getString("description"));
            mc.setPhoto(obj.getString("photo"));
        }
        return mc;
    }
}
