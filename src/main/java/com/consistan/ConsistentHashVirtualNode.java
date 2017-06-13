package com.consistan;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Demon on 2017/6/13.
 * 带虚拟节点的一致性Hash算法
 */
public class ConsistentHashVirtualNode {
    /**
     * 待添加入Hash环的服务器列表
     */
    private static String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111",
            "192.168.0.3:111", "192.168.0.4:111"};

    //真实结点列表,考虑到服务器上线、下线的场景，即添加、删除的场景会比较频繁，这里使用LinkedList会更好
    private static List<String> readlNodes = new LinkedList<String>();

    /**
     * 虚拟节点，key表示虚拟节点的hash值，value表示虚拟节点的名称
     */
    private static SortedMap<Integer, String> virtualNodes = new TreeMap<Integer, String>();

    /**
     * 虚拟节点的数目，这里写死，为了演示需要，一个真实结点对应5个虚拟节点
     */
    private static final int VIRTUAL_NODES = 5;

    static {
        // 先把原始的服务器添加到真实结点列表中
        for (int i = 0; i < servers.length; i++) {
            readlNodes.add(servers[i]);
        }
        // 再添加虚拟节点
        for (String readlNode : readlNodes) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNodeName = readlNode + "&&VN" + String.valueOf(i);
                int hash = HashAlgorithms.FNVHash1(virtualNodeName);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
    }

    public static String getServer(String node) {
        int hash = HashAlgorithms.FNVHash1(node);
        // 得到大于该Hash值的所有Map
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        String virtualNode = "";
        if (subMap.isEmpty()) {
            // 第一个Key就是顺时针过去离node最近的那个结点
            Integer i = subMap.firstKey();
            // 返回对应的虚拟节点名称，这里字符串稍微截取一下
            virtualNode = subMap.get(i);
        }
        // 得到带路由的结点的Hash值
        return virtualNode.substring(0, virtualNode.indexOf("&&"));
    }
}
