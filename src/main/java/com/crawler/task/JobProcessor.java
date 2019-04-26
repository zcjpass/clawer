package com.crawler.task;

import com.crawler.pojo.JobInfo;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    private String url = "https://search.51job.com/list/070200,000000,0000,01%252C37%252C38%252C40%252C32,9,99,Java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
    @Override
    public void process(Page page) {

        //解析页面，获取招聘信息详情的url地址
        List<Selectable> list = page.getHtml().css("div#resultList div.el").nodes();
        if(list.size() == 0){
            //如果为空，表示这是详情页，解析页面，获取详情信息，保存数据
            this.saveJobInfo(page);
        }else{
            for(Selectable selectable : list){
                //或取url地址
                String jobInfoUrl = selectable.links().toString();
                //把获取的url地址放到任务队列中
                page.addTargetRequest(jobInfoUrl);
            }
            //获取下一页的url
            String bkurl = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            //把url放到任务队列中
            page.addTargetRequest(bkurl);
        }
        String html = page.getHtml().toString();
    }
    //解析页面，获取招聘详情信息，保存信息
    private void saveJobInfo(Page page) {
        //创建招聘详情对象
        JobInfo jobInfo = new JobInfo();
        //解析页面
        Html html = page.getHtml();
        //获取数据，封装到对象中
        jobInfo.setId(0L);
        jobInfo.setCompanyName(html.css("div.cn p.cname a","text").toString());
        jobInfo.setCompanyAddress(Jsoup.parse(html.css("div.bmsg").nodes().get(1).toString()).text());
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text());
        jobInfo.setJobName(html.css("div.cn h1","text").toString());
        String str;
        str = html.css("div.cn p.ltype","text").toString();
//        System.out.println(str);
        jobInfo.setJobAddress(Mathslary.getString(str));
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        String salary = html.css("div.cn strong","text").toString();
        jobInfo.setSalaryMin(Mathslary.getSalary(salary)[0]);
        jobInfo.setSalaryMax(Mathslary.getSalary(salary)[1]);
        jobInfo.setUrl(page.getUrl().toString());
        jobInfo.setTime(Mathslary.getTime(str));

        //内存中保存结果
        page.putField("jobInfo",jobInfo);
    }

    private Site site = Site.me()
            .setCharset("gbk")//设制编码
            .setTimeOut(10*1000)//设置超时时间
            .setRetrySleepTime(3000)//设置重试间隔时间
            .setRetryTimes(3);//设置重试次数
    @Override
    public Site getSite() {
        return site;
    }

    @Autowired
    private SavePipeline savePipeline;
    //initialDelay当前任务执行后，等等多久执行方法
    //fixedDelay每个多久执行方法
    @Scheduled(initialDelay = 1000,fixedDelay = 10000)//定时启动
    public void process(){
        Spider.create(new JobProcessor())
                .addUrl(url)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(10)
                .addPipeline(this.savePipeline)
                .run();
    }
}
