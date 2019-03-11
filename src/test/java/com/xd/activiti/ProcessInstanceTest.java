package com.xd.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
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

    /** 查询当前人的个人任务*/
    @Test
    public void findMyPersonalTask(){
        String assignee = "张三";
        List<Task> list = taskService.createTaskQuery()// 创建任务查询对象
                .taskAssignee(assignee)// 指定个人任务查询，指定办理人
//                .taskCandidateUser()// 组任务的办理人查询
//                .processDefinitionId()// 使用流程定义ID查询
//                .processInstanceId()// 使用执行对象ID查询
//                .executionId()// 使用执行对象ID查询
                /** 排序*/
                .orderByTaskCreateTime().asc()// 使用创建时间的升序排序
                /** 返回结果集*/
                .list();
//                .singleResult();// 返回唯一结果集
//                .count();// 返回结果集数量
//                .listPage(firstResult, maxResult);// 分页查询
        if(list != null && list.size()>0){
            for (Task task : list){
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID:"+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
                System.out.println("###########################################");
            }
        }
    }
    /** 完成我的任务 */
    @Test
    public void completeMyPersonalTask(){
        String taskId = "c4abe724-4175-11e9-9efe-8cec4b862f6e";
        taskService.complete(taskId);
        System.out.println("完成任务：任务ID"+taskId);
    }

    /** 查询流程状态（判断流程正在执行，还是结束）*/
    @Test
    public void isProcessEnd(){
        String processInstanceId = "a70f1f4b-413c-11e9-ac90-8cec4b862f6e";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance == null){
            System.out.println("流程已经结束");
        }else{
            System.out.println("流程没有结束");
        }
    }

    /** 查询历史任务*/
    @Test
    public void findHistoryTask(){
        String taskAssignee = "张三";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()// 创建历史任务实例查询
                .taskAssignee(taskAssignee)// 指定历史任务的办理人
                .list();
        if (list != null && list.size()>0){
            for (HistoricTaskInstance hti : list){
                System.out.println("任务ID："+hti.getId()+"名称："+hti.getName()
                        +"流程实例ID:"+hti.getProcessInstanceId()
                        +"开始时间："+hti.getStartTime()
                        +"结束时间："+hti.getEndTime()
                        +"持续时间："+hti.getDurationInMillis());
                System.out.println("##################################");
            }
        }
    }

    /** 查询历史流程实例*/
    @Test
    public void findHistoryProcessInstance(){
        String processInstanceId = "a70f1f4b-413c-11e9-ac90-8cec4b862f6e";
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()//创建历史流程实例查询
                .processInstanceId(processInstanceId)
                .singleResult();
        System.out.println(hpi.getId()
                +"  "+hpi.getProcessDefinitionId()
                +"  "+hpi.getStartTime()
                +"  "+hpi.getEndTime()
                +"  "+hpi.getDurationInMillis());
    }

}
