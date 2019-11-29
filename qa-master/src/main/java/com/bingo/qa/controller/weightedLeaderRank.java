package com.bingo.qa.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;
public class weightedLeaderRank {

    public static void main(String[] args) throws Throwable {



//        FileInputStream freader;
//        freader = new FileInputStream("C:\\Users\\lixiao\\Desktop\\0306.txt");
//        ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//        Map<String,Map> infor=new HashMap<String,Map>();
//        infor = (HashMap<String, Map>) objectInputStream.readObject();
//        user=(Vector<Vector>)objectInputStream.readObject();
//        Map<String, Integer> edges=infor.get("edges");
//        Map<Object, Object> alluser=infor.get("alluser");



//                System.out.println(user.getClass());  Vector<Vector>
//                System.out.println(user.get(0).getClass());    Vector
//                System.out.println(user.get(0).get(0).getClass());  String
        Vector<Vector> suers=new Vector<>();
        Map<String, Integer> edges = new HashMap<>();
        Map<Object, Object> alluser = new HashMap<>();//(String,1f)
        edges.put("1,2",1);
        edges.put("2,3",1);
        edges.put("3,1",1);
        edges.put("2,4",1);
        edges.put("4,2",1);
        for (int i = 1; i < 5; i++) {
            String str = String.valueOf(i);
            alluser.put(str,1f);
            Vector v1 = new Vector();
            v1.addElement(str);
            suers.add(v1);
        }

        System.out.println("user"+suers);

        System.out.println("alluser"+alluser);
        System.out.println("edges"+edges);

        System.out.println(suers.size());
        System.out.println(alluser.size());
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
            Entry<String, Integer> entryedge = entriesedges0.next();
            String jname = entryedge.getKey().substring(entryedge.getKey().indexOf(",") + 1);

            if (!InDegree.containsKey("gRoot," + jname)) {
                InDegree.put("gRoot," + jname, 1);
            } else {
                InDegree.put("gRoot," + jname, InDegree.get("gRoot," + jname) + 1);
            }
        }

        Iterator<Map.Entry<String, Integer>> entriesIndegree = InDegree.entrySet().iterator();
        while (entriesIndegree.hasNext()) {
            Entry<String, Integer> entry = entriesIndegree.next();
            edges.put(entry.getKey(), entry.getValue());
            edges.remove("gRoot,gRoot");
        }
        System.out.println("Step2  Finished");


        Map<String,Integer> isum= new HashMap<String,Integer>();
        Iterator<Map.Entry<String, Integer>> entriesSum=edges.entrySet().iterator();
        while(entriesSum.hasNext()){
            Entry<String,Integer> entry= entriesSum.next();
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
            Entry<String,Integer> entry= entriesSum1.next();
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
                    Entry<String, Integer> entry = entriesedges.next();
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
                //for(int j=0;j<user.size();j++) {
                //	System.out.println(user.get(j).lastElement());//每次得分
                //}
            }
        }

        // 输出
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(new File("E:\\计算结果.txt"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PrintStream p = new PrintStream(fs);
        Iterator<Map.Entry<Object, Object>> entries = alluser.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<Object, Object> entry = entries.next();//Map.Entry是Map声明的一个内部接口，此接口为泛型，定义为Entry<K,V>。它表示Map中的一个实体（一个key-value对）。接口中有getKey(),getValue方法。
            if(entry.getKey().equals("gRoot")){
                entries.remove();
            }else{
                p.println("Key = " + entry.getKey() + ", Sorce = " + entry.getValue());
            }
            System.out.println("Key = " + entry.getKey() + ", Sorce = " + entry.getValue());
        }
        p.close();

    }
}
