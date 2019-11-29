package com.bingo.qa.controller;

import com.bingo.qa.dao.QuestionDAO;
import com.bingo.qa.dao.TopUserDAO;
import com.bingo.qa.model.*;
import com.bingo.qa.service.CommentService;
import com.bingo.qa.service.FollowService;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.service.UserService;
//import io.lettuce.core.ScriptOutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

/**
 */
@Controller
public class IndexController {

    private final UserService userService;

    private final QuestionService questionService;

    private final HostHolder hostHolder;

    private final FollowService followService;

    private final CommentService commentService;

    QuestionDAO questionDAO;
    TopUserDAO topUserDAO;

    @Autowired
    public IndexController(UserService userService, QuestionService questionService, HostHolder hostHolder, FollowService followService, CommentService commentService, QuestionDAO questionDAO,TopUserDAO topUserDAO) {
        this.userService = userService;
        this.questionService = questionService;
        this.hostHolder = hostHolder;
        this.followService = followService;
        this.commentService = commentService;
        this.questionDAO = questionDAO;
        this.topUserDAO = topUserDAO;
    }

    @GetMapping(value = {"/", "/index","/search"})
    public String index(Model model) {
        //页面改动 显示数据库中所有问题
        int quencount = questionDAO.getQuestionCount();//获取数据库中的问题数目 li

        List<ViewObject> vos = getQuestions(0, 0, quencount);
        model.addAttribute("vos", vos);


        //这里的bos是看用户是否属于top前20的用户，在top前20的用户界面前有五条推荐求解。如果不在top20里面则不会显示。
        List<Question> questionList = questionService.selectLatestQuestions(0, 0, quencount);
        PriorityQueue<Question> MinProfitQ = new PriorityQueue<>(new RecommendController.MinProfitComparator());
        for (Question question : questionList) {
            MinProfitQ.add(question);
        }
        List<ViewObject> bos = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        for (int j = 1; j < 21; j++) {
            set.add(topUserDAO.selectById(j).getUserid());
        }
        User user = hostHolder.getUser();
//        System.out.println(user);
//        System.out.println(set);
        if(user!=null){
            if (set.contains(user.getId())) {
                int i  =0;
                while(i<5){
                    Question question = MinProfitQ.poll();
//                    System.out.println(question);
                    ViewObject bo = new ViewObject();
                    bo.set("question", question);
                    bo.set("user", userService.selectById(question.getUserId()));
                    bo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                    bos.add(bo);
                    i++;
                }
            }

        }
        model.addAttribute("bos", bos);





        return "index";
}


    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.selectLatestQuestions(userId, offset, limit);


        List<ViewObject> vos = new ArrayList<>();

        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
//            System.out.println(followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vo.set("question", question);
            vo.set("user", userService.selectById(question.getUserId()));
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vos.add(vo);
        }
        return vos;
    }

    @GetMapping(value = "/user/{userId}")
    public String userIndex(Model model,
                            @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));

        User user = userService.selectById(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }


    public static class MinProfitComparator implements Comparator<Question> {
        @Override
        public int compare(Question o1, Question o2) {
            return o1.getCommentCount() - o2.getCommentCount();
        }
    }


}
