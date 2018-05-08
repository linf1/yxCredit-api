package com.zw.service.task.abs;

import com.base.util.ThreadLocalHelper;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.zw.service.base.AbsServiceBase;
import com.zw.service.dto.UserInfoDTO;
import com.zw.service.task.ITask;
import org.apache.log4j.Logger;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年03月08日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) zw.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:zh-pc <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public abstract class AbsTask extends AbsServiceBase implements ITask, SimpleJob {
    protected static Logger logger = Logger.getLogger("task");

    private String taskName;

    private String cron;

    private int shardingTotalCount = 1;

    private boolean elasticJob = true;

    public final void execute(ShardingContext shardingContext) {
        try {
            setContext();
            logger.info("开始执行任务：" + this.getTaskName());
            this.doWork();
            logger.info("执行任务结束：" + this.getTaskName());
        } catch (Exception ex) {
            logger.error("执行任务出现异常：" + this.getTaskName(), ex);
        }
    }

    private void setContext() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setHttpReqId("task:" + System.currentTimeMillis());
        userInfoDTO.setUserId("task");
        ThreadLocalHelper.getMap().put(ThreadLocalHelper.USER_INFO_DTO, userInfoDTO);
    }

    @Override
    public String getTaskName() {
        return taskName;
    }

    @Override
    public String getCron() {
        return cron;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(int shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }

    @Override
    public boolean isElasticJob() {
        return elasticJob;
    }

    public void setElasticJob(boolean elasticJob) {
        this.elasticJob = elasticJob;
    }
}
