package com.xd.activiti;

import com.xd.entity.Person;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessVariableTest {

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

    /** 设置流程变量*/
    @Test
    public void setVariables(){
        String taskId = "267eaebf-43a2-11e9-bc0c-8cec4b862f6e";
        // 1.设置流程变量，使用基本数据类型
//        taskService.setVariable(taskId,"请假天数", 3);
//        taskService.setVariable(taskId, "请假日期", new Date());
//        taskService.setVariable(taskId, "请假原因", "回家探亲");
        // 2.设置流程变量，使用javabean类型
        /**
         * 当一个javabean（实现序列化）放置到流程变量中，要求javabean的属性不能再发生变化
         * 如果发生变化，再获取的时候，抛出异常
         *
         * 解决方案：再person对象中实现Serializable接口，并添加序列化ID
         */
        Person p = new Person();
        p.setId(10);
        p.setName("翠花");
        taskService.setVariable(taskId,"人员信息",p);
    }

    /** 获取流程变量*/
    @Test
    public void getVariables(){
        String taskId = "267eaebf-43a2-11e9-bc0c-8cec4b862f6e";
        // 1.获取流程变量，使用基本数据类型
//        Integer days = (Integer) taskService.getVariable(taskId,"请假天数");
//        Date date = (Date) taskService.getVariable(taskId,"请假日期");
//        String reason = (String) taskService.getVariable(taskId,"请假原因");
//        System.out.println("请假天数："+ days+" 请假日期："+ date+" 请假原因："+reason);
        // 2.设置流程变量，使用javabean类型
        Person p = (Person) taskService.getVariable(taskId,"人员信息");
        System.out.println(p.getId()+"     "+p.getName());
    }

    /** 模拟设置和获取流程变量的场景*/
    public void setAndGetVariables(){
        //设置流程变量
//        runtimeService.setVariable();// 表示使用执行ID，和流程变量的名称，设置流程变量的值
//        runtimeService.setVariables();//表示使用执行ID，和Map集合设置流程变量

//        taskService.setVariable();// 表示使用任务ID，和流程变量的名称，设置流程变量的值
//        taskService.setVariables();//表示使用任务ID，和Map集合设置流程变量

//        runtimeService.startProcessInstanceByKey();//启动流程实例的同时，可以设置流程变量
//        taskService.complete();//完成任务的同时，设置流程变量

        //获取流程变量
//        runtimeService.getVariable();// 使用执行对象ID，和流程变量的名称，获取流程变量的值
//        runtimeService.getVariables();// 使用执行对象ID，获取所有或部分的流程变量，将流程变量放至Map集合中

//        taskService.getVariable();// 使用任务ID，和流程变量的名称，获取流程变量的值
//        taskService.getVariables();// 使用任务ID，获取所有或部分的流程变量，将流程变量放至Map集合中
    }

    /** 完成我的任务 */
    @Test
    public void completeMyPersonalTask(){
        String taskId = "267eaebf-43a2-11e9-bc0c-8cec4b862f6e";
        taskService.complete(taskId);
        System.out.println("完成任务：任务ID"+taskId);
    }

    /** 查询流程变量的历史表*/
    @Test
    public void findHistoryProcessVariables(){
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()// 创建一个历史的流程变量查询对象
                .variableName("请假天数")
                .list();
        if(list != null && list.size()>0){
            for (HistoricVariableInstance hvi : list){
                System.out.println(hvi.getId()+"  "+hvi.getProcessInstanceId()
                        +"  "+hvi.getVariableName()
                        +"  "+hvi.getValue());
                System.out.println("##########################");
            }
        }
    }

}
