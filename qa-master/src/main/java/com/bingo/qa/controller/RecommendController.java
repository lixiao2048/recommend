package com.bingo.qa.controller;

import com.bingo.qa.dao.QuestionDAO;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 */
@Controller
public class RecommendController {

    private final UserService userService;

    private final QuestionService questionService;

    private final HostHolder hostHolder;

    private final FollowService followService;

    private final CommentService commentService;

    QuestionDAO questionDAO;

    @Autowired
    public RecommendController(UserService userService, QuestionService questionService, HostHolder hostHolder, FollowService followService, CommentService commentService, QuestionDAO questionDAO) {
        this.userService = userService;
        this.questionService = questionService;
        this.hostHolder = hostHolder;
        this.followService = followService;
        this.commentService = commentService;
        this.questionDAO = questionDAO;
    }

    @GetMapping(value = {"/recommend"})
    public String index(Model model) {
        //页面改动 显示数据库中所有问题
        int quencount = questionDAO.getQuestionCount();

        List<ViewObject> vos = getQuestions(0, 0, quencount);
        model.addAttribute("vos", vos);
        return "index1";
    }

    public static class MinProfitComparator implements Comparator<Question> {
        @Override
        public int compare(Question o1, Question o2) {
            return o1.getCommentCount() - o2.getCommentCount();
        }
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.selectLatestQuestions(userId, offset, limit);
        PriorityQueue<Question> MinProfitQ = new PriorityQueue<>(new MinProfitComparator());
        for (Question question : questionList) {
            MinProfitQ.add(question);
        }

        List<ViewObject> vos = new ArrayList<>();

     while(MinProfitQ.size()>0){
         Question question = MinProfitQ.poll();
//            System.out.println(question.getCommentCount());
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.selectById(question.getUserId()));
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vos.add(vo);
        }
        return vos;
    }


}
