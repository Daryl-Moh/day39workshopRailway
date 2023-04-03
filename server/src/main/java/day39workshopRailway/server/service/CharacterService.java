package day39workshopRailway.server.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import day39workshopRailway.server.models.Comment;
import day39workshopRailway.server.models.MarvelCharacter;
import day39workshopRailway.server.repository.CharacterRepository;

@Service
public class CharacterService {
    @Autowired
    private MarvelApiService marvelApiSvc;

    @Autowired
    private CharacterRepository charRepo;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
 
 
    public Optional<List<MarvelCharacter>> getCharacters(String characterName,
            Integer limit , Integer offset){
        return marvelApiSvc.getCharacters(characterName, limit, offset);
    }

    public MarvelCharacter getCharacterDetails(String characterId) 
            throws IOException{
        MarvelCharacter cc = null;
        String detailsJson = (String)redisTemplate.opsForValue().get(characterId);
        System.out.println("" + detailsJson);
        if(detailsJson != null){
            cc = (MarvelCharacter) MarvelCharacter.createForCache(detailsJson);
        }else{
            Optional<MarvelCharacter> c  = marvelApiSvc.getCharacterDetails(characterId);
            cc = c.get();
            cc.setComments(this.getAllComments(characterId));
            if(detailsJson == null){
                redisTemplate.opsForValue().set(characterId,cc.toJSON().toString());
                long currentTime = Instant.now().getMillis();
                // add 60 mins to the current time.
                Date afterAdding60Mins = new Date(currentTime + (60 * 60 * 1000));
                redisTemplate.expireAt(characterId,afterAdding60Mins);
            }
                
        }
        
        return cc;
    }

    public Comment insertComment(Comment r){
        return charRepo.insertComment(r);
    }

    public List<Comment> getAllComments(String charId){
        return charRepo.getAllComments(charId);
    }
}
