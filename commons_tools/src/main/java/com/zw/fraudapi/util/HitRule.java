package com.zw.fraudapi.util;

import java.io.Serializable;

class HitRule implements Serializable {

    private static final long serialVersionUID = 6297666052880082771L;
    private String            uuid;                                   // 规则uuid
    private String            id;                                     // 规则编号
    private String            name;                                   // 规则名称
    private String            decision;                               // 该条规则决策结果
    private int               score;                                  // 规则分数

    //    ...省略若干Getter与Setter

    @Override
    public String toString() {
        return "rule_name:" + this.name + "\nscore:" + this.score + "\ndescision:" + this.decision + "\n";
    }
}