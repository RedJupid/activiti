package com.xd.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessInstanceTest {

    /** 流程定义和部署相关的存储服务 */
    @Autowired
    private RepositoryService repositoryService;

    /** 流程运行时相关的服务 */
    @Autowired
    private RuntimeService runtimeService;

    /** 节点任务相关操作接口 */
    @Autowired
    private TaskService taskService;

    /** 流程图生成器 */
    @Autowired
    private ProcessDiagramGenerator processDiagramGenerator;


    /** 历史记录相关服务接口 */
    @Autowired
    private HistoryService historyService;

    @Test
    public void contextLoads() {
    }


    /** 部署流程定义 */
    @Test
    public void deploymentProcessDefinition(){
        Deployment deployment = repositoryService.createDeployment()//创建一个部署对象
                .name("helloworld入门程序")//添加部署名称
                .addClasspathResource("processes/helloworld.bpmn")
                .addClasspathResource("processes/helloworld.png")
                .deploy();//完成部署
        System.out.println("部署ID:"+deployment.getId());
        System.out.println("部署名称："+deployment.getName());
    }

    /** 启动流程实例 */
    @Test
    public void startProcessInstance(){
        String processDefinitionKey = "helloworld";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID:"+processInstance.getId());//流程实例ID
        System.out.println("流程定义ID:"+processInstance.getProcessDefinitionId());//流程定义ID
    }


}
