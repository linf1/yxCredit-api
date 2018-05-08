package com.zw.fraudapi.util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Policy implements Serializable {

    private static final long serialVersionUID = 2971731835604653516L;
    private String            policy_uuid;                            // 策略uuid
    private String            policy_decision;                        // 策略结果
    private String            policy_mode;                            // 策略模式
    private String            policy_name;                            // 策略名称
    private int               policy_score;                           // 策略分数
    private String            risk_type;                              // 风险类型

    private List<HitRule>     hit_rules        = new ArrayList<>();   // 命中规则列表

    //    ...省略若干Getter与Setter

    @Override
    public String toString() {
        return "policy_name:" + this.policy_name + "\npolicy_mode:" + this.policy_mode + "\nhit_rules:"
                + this.hit_rules;
    }
}