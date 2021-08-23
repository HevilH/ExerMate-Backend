package ExerMate.ExerMate.Biz.Processor;

import ExerMate.ExerMate.Base.Constant.KeyConstant;
import ExerMate.ExerMate.Base.Model.ExerPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExerPostProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    public ExerPost getExerPostByID(String exerPostID) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.EXERPOSTID).is(exerPostID));
        return mongoTemplate.findOne(query, ExerPost.class);
    }

    public void createExerPost(String useremail,  String exerPostID, String exerName, Long uploadTime, String exerPlace, Long exerTime, int maxNum, String contents){
        ExerPost newExerPost = new ExerPost();
        newExerPost.setExerPostID(exerPostID);
        newExerPost.setUserEmail(useremail);
        newExerPost.setExerName(exerName);
        newExerPost.setUploadTime(uploadTime);
        newExerPost.setExerPlace(exerPlace);
        newExerPost.setExerTime(exerTime);
        newExerPost.setMaxNum(maxNum);
        newExerPost.setContents(contents);
        mongoTemplate.insert(newExerPost, "ExerPost");
    }

    public void modifyExerPost(String exerPostID, String exerName, String exerPlace, Long exerTime, int maxNum, String contents){
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.EXERPOSTID).is(exerPostID));
        Update update = new Update();
        update.set("exerName", exerName);
        update.set("exerPlace", exerPlace);
        update.set("exerTime", exerTime);
        update.set("maxNum", maxNum);
        update.set("contents", contents);
        mongoTemplate.updateFirst(query, update, ExerPost.class);
    }

    public List<ExerPost> getAllExerPost(){
        return mongoTemplate.findAll(ExerPost.class);
    }
}
