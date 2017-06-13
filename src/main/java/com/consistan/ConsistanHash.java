package com.consistan;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Demon on 2017/6/13.
 */
public class ConsistanHash {
    private static SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();

    static {
        String[] dbs = {"", ""};
        for (int i = 0; i < dbs.length; i++) {
            int hash = HashAlgorithms.FNVHash1(dbs[i]);
            sortedMap.put(hash, dbs[i]);
        }
    }

    //不带虚拟节点
    public static  String getDB(String node){
        // 得到该路由的hash 值
        int hash  =HashAlgorithms.FNVHash1(node);
        // 得到大于该hash值得所有map
        SortedMap<Integer,String> subMap=sortedMap.tailMap(hash);
        String dbNode="";
        // 第一个key就是顺时针node最近的节点
        // 没命中则选择第一个
        if(subMap.isEmpty()){
            Integer i=sortedMap.firstKey();
            dbNode=sortedMap.get(i);
        }else{
            Integer i = subMap.firstKey();
            dbNode = subMap.get(i);
        }
        return dbNode;
    }
}
