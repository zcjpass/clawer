package com.crawler.service;

import com.crawler.pojo.JobInfo;

import java.util.List;

public interface JobInfoService {

    //保存工作信息
    public void save(JobInfo jobInfo);
    //根据条件查询工作信息
    public List<JobInfo> findJobInfo(JobInfo jobInfo);
}
