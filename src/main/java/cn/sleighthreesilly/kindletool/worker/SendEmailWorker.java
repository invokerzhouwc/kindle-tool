package cn.sleighthreesilly.kindletool.worker;

import cn.sleighthreesilly.kindletool.bean.EmailMessageDO;
import cn.sleighthreesilly.kindletool.service.SendEmailService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unbescape.json.JsonEscape;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author :zhouwenchao
 * @Description:
 * @date :2019/12/29
 */
@Component
public class SendEmailWorker {

    private Logger logger = LoggerFactory.getLogger(SendEmailWorker.class);
    @Autowired
    private SendEmailService sendEmailService;

    private volatile ConcurrentLinkedQueue<EmailMessageDO> queue = new ConcurrentLinkedQueue();

    private Map<String,String> statusMap = new ConcurrentHashMap<>();

    private ExecutorService executorService = new ThreadPoolExecutor(2, 2,
            0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(30),
            new ThreadPoolExecutor.DiscardPolicy());

    public boolean put(EmailMessageDO emailMessageDO){
        List<File> files = emailMessageDO.getFile();
        for(File file : files){
            statusMap.put(file.getName(),"等待发送");
        }
        return queue.offer(emailMessageDO);
    }

    public void waitSend(EmailMessageDO emailMessageDO){
        List<File> files = emailMessageDO.getFile();
        for(File file : files){
            statusMap.put(file.getName(),"等待发送");
        }
    }

    public void sendFailed(File file){
        statusMap.put(file.getName(),"推送失败");
    }

    public void sendSuccess(File file){
        statusMap.put(file.getName(),"推送成功");
    }

    public void sending(File file){
        statusMap.put(file.getName(),"推送中");
    }

    public String sendStatus(File file){
        return statusMap.get(file.getName());
    }

    public Map<String,String> allStatus(){
        return statusMap;
    }
    @PostConstruct
    private void init(){
        executorService.execute(new SendEmailWorkerRunnable());
    }

    class SendEmailWorkerRunnable implements Runnable{

        @Override
        public void run() {
            while (true){
                EmailMessageDO emailMessageDO = queue.poll();
                if (emailMessageDO == null){
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                logger.info("拉取信息:{}", emailMessageDO.toString());
                List<File> files = emailMessageDO.getFile();
                for(File file:files){
                    executorService.execute(new Worker(file,emailMessageDO));
                }
            }
        }
    }
    class Worker implements Runnable{

        private File file;

        private EmailMessageDO emailMessageDO;

        public Worker(File file,EmailMessageDO emailMessageDO) {
            this.file = file;
            this.emailMessageDO = emailMessageDO;
        }

        @Override
        public void run() {
            sending(file);
            long length = file.length();
            Boolean send = sendEmailService.send(emailMessageDO,file);
            if(send){
                logger.info("{},大小:{},推送成功", file.getName(),length);
                sendSuccess(file);
            }else{
                logger.info("{},大小:{},推送失败", file.getName(),length);
                sendFailed(file);
            }
        }
    }

}
