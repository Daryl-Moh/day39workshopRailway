package day39workshopRailway.server.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import day39workshopRailway.server.models.Comment;
import day39workshopRailway.server.models.MarvelCharacter;
import day39workshopRailway.server.service.CharacterService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping(path="/api/characters", produces = MediaType.APPLICATION_JSON_VALUE)
public class CharacterRestController {
    private Logger logger = LoggerFactory.getLogger(CharacterRestController.class);
    
    @Autowired
    private CharacterService charSvc;
    
    @GetMapping
    public ResponseEntity<String> getCharacters(
        @RequestParam(required=true) String characterName,
        @RequestParam(required=true) Integer limit,
        @RequestParam(required=true) Integer offset) {

        JsonArray result = null;
        Optional<List<MarvelCharacter>> or = this.charSvc.getCharacters(characterName, limit, offset);
        List<MarvelCharacter> results = or.get(); 
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (MarvelCharacter mc : results)
            arrBuilder.add(mc.toJSON());
        result = arrBuilder.build();
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
    }

    @GetMapping(path="/{charId}")
    public ResponseEntity<String> getCharacterDetails(
        @PathVariable(required=true) String charId) throws IOException {
        MarvelCharacter c = this.charSvc.getCharacterDetails(charId);
        JsonObjectBuilder ocjBuilder = Json.createObjectBuilder();
        ocjBuilder.add("details" , c.toJSON());
        JsonObject result = ocjBuilder.build();
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
    }

    @PostMapping(path="/{charId}")
    public ResponseEntity<String> saveCharacterComment(
        @RequestBody Comment comment, @PathVariable(required=true) String charId) {
        logger.info("save comment > : " + charId);
        Comment c= new Comment();
        c.setComment(comment.getComment());
        c.setCharId(charId);
        Comment r = this.charSvc.insertComment(c);
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(r.toJSON().toString());
    }

    @GetMapping(path="/comments/{charId}")
    public ResponseEntity<String> getCharComments(@PathVariable(required=true) String charId) {
        logger.info("Get All ...comments");
        List<Comment> aa = this.charSvc.getAllComments(charId);
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Comment c : aa)
            arrBuilder.add(c.toJSON());
        JsonArray result = arrBuilder.build();
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
    }
}
