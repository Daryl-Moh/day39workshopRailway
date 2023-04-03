package day39workshopRailway.server.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import day39workshopRailway.server.models.Comment;

@Repository
public class CharacterRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    // follows your MongoDB collection name
    private static final String COMMENTS_COL = "comments";

    public Comment insertComment(Comment comment) {
        System.out.println("Inserting into MongoDB >>> " + comment.toString());
        return mongoTemplate.insert(comment, COMMENTS_COL);
    }

    public List<Comment> getAllComments(String charId) {
        Pageable pageable = PageRequest.of(0, 10);

        Query patientsDynamicQuery = new Query()
            .addCriteria(Criteria.where("charId").is(charId))
            .with(pageable);

        List<Comment> filteredPatients =
            mongoTemplate.find(patientsDynamicQuery, Comment.class, COMMENTS_COL);
            Page<Comment> patientPage = PageableExecutionUtils.getPage(
                filteredPatients,
                pageable,
                () -> mongoTemplate.count(patientsDynamicQuery, Comment.class));
                System.out.println("patientPage >>> " + patientPage.toList().toString());
                return patientPage.toList();

    }
}
