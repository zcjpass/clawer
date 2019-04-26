package com.crawler.task;

import com.crawler.pojo.JobInfo;
import com.crawler.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component//实例
public class SavePipeline implements Pipeline {

    @Autowired//注入
    private JobInfoService jobInfoService;
    @Override
    public void process(ResultItems resultItems, Task task) {

        //获取封装好的招聘详情对象
        JobInfo jobInfo = resultItems.get("jobInfo");
        //判断数据是否不为空
        if(jobInfo != null){
            this.jobInfoService.save(jobInfo);
        }
    }
}
