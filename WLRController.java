package com.bingo.qa.controller;

import com.bingo.qa.dao.CommentDAO;
import com.bingo.qa.dao.QuestionDAO;
import com.bingo.qa.dao.TopUserDAO;
import com.bingo.qa.dao.UserDAO;
import com.bingo.qa.model.*;
import com.bingo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.text.DecimalFormat;
import org.springframework.ui.Model;
import java.util.*;

@Controller
//如果你不想使用模板,请使用ResControllre 这个是不渲染模板的
public class WLRController {

    public static class Node {
        public String p;
        public float c;
        public Node(String p, float c){
            this.p = p;
            this.c = c;
        }
    }

    public static class MaxProfitComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            if(o2.c - o1.c>0){
                return 1;
            }else if(o2.c - o1.c==0){
                return 0;
            }else{
                return -1;
            }
        }
    }


    private final HostHolder hostHolder;

    private final UserService userService;

    private final QuestionService questionService;

    private final CommentService commentService;

    QuestionDAO questionDAO;

    UserDAO userDAO;

    CommentDAO commentDao;

    TopUserDAO topUserDAO;
    @Autowired
    public WLRController(HostHolder hostHolder, UserService userService, QuestionService questionService,CommentService commentService,CommentDAO commentDao,UserDAO userDAO, QuestionDAO questionDAO,TopUserDAO topUserDAO) {
        this.hostHolder = hostHolder;
        this.userService = userService;
        this.questionService = questionService;
        this.commentService = commentService;
        this.commentDao = commentDao;
        this.userDAO = userDAO;
        this.questionDAO = questionDAO;
        this.topUserDAO = topUserDAO;
    }

    @RequestMapping(value = "/sort")
        public String WRL(Model model){
        // 从hostHolder中取出用户
        User user = hostHolder.getUser();
        if (user == null) {
            // 未登录，直接重定向到登录页面
            return "redirect:/reglogin";
        }
        System.out.println("评论数"+commentDao.getbigCommentCount());
        int commentnum = commentDao.getbigCommentCount();

        Vector<Vector> suers=new Vector<>();
        Map<String, Integer> edges = new HashMap<>();
        Map<Object, Object> alluser = new HashMap<>();//(String,1f)

        for (int i = 1; i <= commentnum; i++) {
            String ansid = commentDao.retuserid(i);
            String askid = questionDAO.retuserid(commentDao.retentityid(i));
            alluser.put(String.valueOf(ansid),1f);
            alluser.put(String.valueOf(askid),1f);
            String edg = ansid+","+askid;
            if(edges.containsKey(edg)){
                edges.put(edg,edges.get(edg)+1);
            }else{
                edges.put(edg,1);
            }
        }
        System.out.println("参与用户"+alluser.size());
        System.out.println("构成的边"+edges.size());
        Iterator<Map.Entry<Object, Object>> entries = alluser.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Object, Object> entry = entries.next();
            Vector v1 = new Vector();
            v1.addElement(String.valueOf(entry.getKey()));
            suers.add(v1);
        }

        Vector<Object> gRoot = new Vector<>();
        gRoot.add("gRoot");
        suers.add(gRoot);
        System.out.println(suers.size());
        alluser.put("gRoot", 1f);
        for (int i = 0; i < suers.size(); i++) {
            suers.get(i).add(1f);
        }

        for (int i = 0; i < suers.size(); i++) {
            if (suers.get(i).get(0) != "gRoot") {
                edges.put(suers.get(i).get(0) + "," + "gRoot", 1);
            }
        }
        System.out.println("Step1  Finished");

        // 构建入度方法
        Map<String, Integer> InDegree = new TreeMap<String, Integer>();
        Iterator<Map.Entry<String, Integer>> entriesedges0 = edges.entrySet().iterator();
        while (entriesedges0.hasNext()) {
            Map.Entry<String, Integer> entryedge = entriesedges0.next();
            String jname = entryedge.getKey().substring(entryedge.getKey().indexOf(",") + 1);

            if (!InDegree.containsKey("gRoot," + jname)) {
                InDegree.put("gRoot," + jname, 1);
            } else {
                InDegree.put("gRoot," + jname, InDegree.get("gRoot," + jname) + 1);
            }
        }

        Iterator<Map.Entry<String, Integer>> entriesIndegree = InDegree.entrySet().iterator();
        while (entriesIndegree.hasNext()) {
            Map.Entry<String, Integer> entry = entriesIndegree.next();
            edges.put(entry.getKey(), entry.getValue());
            edges.remove("gRoot,gRoot");
        }
        System.out.println("Step2  Finished");


        Map<String,Integer> isum= new HashMap<String,Integer>();
        Iterator<Map.Entry<String, Integer>> entriesSum=edges.entrySet().iterator();
        while(entriesSum.hasNext()){
            Map.Entry<String,Integer> entry= entriesSum.next();
            String iname=entry.getKey().substring(0, entry.getKey().indexOf(","));
            if(!isum.containsKey(iname)){
                isum.put(iname, entry.getValue());
            }else{
                isum.put(iname, isum.get(iname)+entry.getValue());
            }
            //System.out.println("边  "+entry.getKey()+" 权重 "+entry.getValue());
        }


        Iterator<Map.Entry<String, Integer>> entriesSum1=isum.entrySet().iterator();
        while(entriesSum1.hasNext()){
            Map.Entry<String,Integer> entry= entriesSum1.next();
            //System.out.println(" 出度 " + entry.getKey()+"   "+entry.getValue());
        }
        System.out.println("Step3  Finished");

//        System.out.println("user"+user);
//        System.out.println("alluser"+alluser);
//        System.out.println("edges"+edges);
//        System.out.println(isum);

        //算法
        for(int t=0;t<1000;t++) {
            int num=0;
            for (int i = 0; i < suers.size(); i++) {
                float sorce = 0f;
                Iterator<Map.Entry<String, Integer>> entriesedges = edges.entrySet().iterator();
                while (entriesedges.hasNext()) {
                    Map.Entry<String, Integer> entry = entriesedges.next();
                    String calname = entry.getKey().substring(entry.getKey().indexOf(",") + 1);
                    String fanname = entry.getKey().substring(0, entry.getKey().indexOf(","));
                    if (calname.equals(suers.get(i).get(0))) {
                        float Sj = (float) alluser.get(fanname);
                        float jSum=isum.get(fanname);
                        sorce=sorce+entry.getValue()/jSum*Sj;
                        if(suers.get(i).get(0)==gRoot){
                            System.out.println("根节点得分"+sorce);
                        }
                    }
                }
                DecimalFormat decimalFormat = new DecimalFormat(".0000000");
                sorce = (float) Double.parseDouble(decimalFormat.format(sorce));

                suers.get(i).add(sorce);

                Object adaa = alluser.get(suers.get(i).get(0));

                if(alluser.get(suers.get(i).get(0)).equals(sorce)){
                    num++;
                }
            }
            for(int i=0;i<suers.size();i++){
                alluser.put(suers.get(i).get(0), suers.get(i).lastElement());
            }
            if(num==suers.size()){
                System.out.println(t+"计算完成  ，开始写入");
                break;
            }else{
                System.out.println("第"+t+"次完成，已收敛"+num);
            }
        }
        PriorityQueue<Node> maxProfitQ = new PriorityQueue<>(new MaxProfitComparator());
        Iterator<Map.Entry<Object, Object>> entriesl = alluser.entrySet().iterator();
        while (entriesl.hasNext()) {
            Map.Entry<Object, Object> entry = entriesl.next();//Map.Entry是Map声明的一个内部接口，此接口为泛型，定义为Entry<K,V>。它表示Map中的一个实体（一个key-value对）。接口中有getKey(),getValue方法。
            if(entry.getKey().equals("gRoot")){
                entriesl.remove();
            }else{
//                System.out.println("Key = " + entry.getKey() + ", Sorce = " + entry.getValue());
                float tempf= Float.parseFloat(String.valueOf(entry.getValue()));
                maxProfitQ.add(new Node(String.valueOf(entry.getKey()),tempf));
            }
        }
        List<Integer> sortlist = new ArrayList<>();
        List<String> userList = new ArrayList<>();
        List<String> ScoreList = new ArrayList<>();
        int index= 1;
        while(maxProfitQ.size()!=0){
            Node temp = maxProfitQ.poll();
            if(temp==null||temp.p==null||temp.p.equals("null")){continue;}
            if(index<=20){
                TopUser topUser = new TopUser();
                topUser.setId(index);
                topUser.setUserid(Integer.parseInt(temp.p));
//                System.out.println(topUser.getId()+"  "+topUser.getUserid());
                topUserDAO.addUser(topUser);//这里加入 排名前20的用户
            }
            System.out.println("id是"+temp.p+"姓名是"+userDAO.selectById(Integer.parseInt(temp.p)).getName()+"得分是："+temp.c);
            userList.add(temp.p);
            ScoreList.add(String.valueOf(temp.c));
            sortlist.add(index++);
        }

        List<ViewObject> vos = getuser(userList,ScoreList,sortlist);
        model.addAttribute("vos", vos);
        return "sort";
    }

    private List<ViewObject> getuser(List<String> userList,List<String> ScoreList,List<Integer> sortlist) {
        List<ViewObject> vos = new ArrayList<>();
        for (String user :userList) {
            ViewObject vo = new ViewObject();
            vo.set("userid", user);
            vo.set("username",userDAO.selectById(Integer.parseInt(user)).getName());
            vo.set("headurl",userDAO.selectById(Integer.parseInt(user)).getHeadUrl());
            vo.set("userscore", ScoreList.get(userList.indexOf(user)));
            vo.set("paiming", sortlist.get(userList.indexOf(user)));
            vos.add(vo);
        }
        return vos;
    }


}