package com.crawler.service.impl;

import com.crawler.dao.JobInfoDao;
import com.crawler.pojo.JobInfo;
import com.crawler.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service//业务层
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired//自动注入
    private JobInfoDao jobInfoDao;

    @Override
    @Transactional//事务
    public void save(JobInfo jobInfo) {

        //根据url和发布时间查询数据
        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());
        //执行查询
        List<JobInfo> list = this.findJobInfo(param);
        //如果查询为空，表示信息不存在或已更新，需要新增或更新数据库
        if(list.size() == 0){
            this.jobInfoDao.saveAndFlush(jobInfo);
        }
    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        //设置查询条件
        Example example = Example.of(jobInfo);
        //执行查询
        List list = this.jobInfoDao.findAll(example);
        return list;
    }
}
