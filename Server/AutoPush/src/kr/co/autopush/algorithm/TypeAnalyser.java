// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TypeAnalyser.java
package kr.co.autopush.algorithm;

import java.util.ArrayList;


// Referenced classes of package org.ssm.algorithm.tabledetector:
//            ChildNode, NodeFreq

public class TypeAnalyser {
	private int total;
	private int max;
	private ArrayList<NodeFreq> list;
    public TypeAnalyser() {
        list = new ArrayList<NodeFreq>();
        total = 0;
        max = 0;
    }

    public void add(ChildNode node) {
        if(node.size() == 0) return;
        total++;
        for(NodeFreq type : list) {            
            if(type.equals(node)) {
                type.countUp();
                max = max >= type.getCount() ? max : type.getCount();
                return;
            }
        }
        list.add(new NodeFreq(node));
    }

    public float eval() {
        if(list.size() < 1)
            return 0.0F;
        else
            return (float)max / total;
    }

    public int getChildSize() {
        return total;
    }

   
}

class NodeFreq {
	private ChildNode node;
    private int count;
	public NodeFreq() {
        count = 0;
    }

    public NodeFreq(ChildNode bean) {
        node = bean;
        count = 1;
    }

    public ChildNode getBean() {
        return node;
    }

    public void countUp() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public boolean equals(Object obj) {
        return node.equals(obj);
    }   
}

