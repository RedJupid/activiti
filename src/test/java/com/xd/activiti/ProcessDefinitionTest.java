package com.xd.activiti;

import org.activiti.engine.*;
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
public class ProcessDefinitionTest {

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

    /** 查询流程定义 */
    @Test
    public void findProcessDefinition(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()// 创建一个流程定义查询
//                .deploymentId()// 使用部署对象ID查询
//                .processDefinitionId()// 使用流程定义ID查询
//                .processDefinitionKey()// 使用流程定义的key查询
//                .processDefinitionNameLike()// 使用流程定义的名称模糊查询
                /** 排序*/
                .orderByProcessDefinitionVersion().asc()// 按照版本的升序排列
//                .orderByProcessDefinitionName().desc()// 按照流程定义的名称降序排列
                /** 返回的结果集 */
                .list();// 返回一个集合列表，封装流程定义
//                .singleResult();// 返回唯一结果集
//                .count();// 返回结果集数量
//                .listPage(firstResult, maxResult);// 分页查询
        if (list != null && list.size()>0){
            for (ProcessDefinition pd : list){
                System.out.println("流程定义ID："+pd.getId());// 流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称："+pd.getName());// 对应helloworld.bpmn文件中的name属性值
                System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中id属性值
                System.out.println("流程定义的版本："+pd.getVersion());//当流程定义的key值相同的情况下，版本升级
                System.out.println("资源名称bpmn文件："+pd.getResourceName());
                System.out.println("资源名称png文件："+pd.getDiagramResourceName());
                System.out.println("部署对象ID:"+pd.getDeploymentId());
                System.out.println("#########################");
            }
        }
    }

    /** 删除流程定义 */
    @Test
    public void deleteProcessDefinition(){
        String deploymentId = "623e5629-4167-11e9-90ed-8cec4b862f6e";
        /**
         * 不带级联的删除
         *     只能删除没有启动的流程，如果流程启动，就会抛出异常
         */
//        repositoryService.deleteDeployment(deploymentId);
        /**
         * 级联删除
         *     不管流程是否启动，都能可以删除
         */
        repositoryService.deleteDeployment(deploymentId, true);
        System.out.println("删除成功！");
    }

    /** 查看流程图*/
    @Test
    public void viePic() throws IOException {
        // 将生成的图片放到文件夹下
        String deploymentId = "fe7119be-4168-11e9-aa2f-8cec4b862f6e";
        //获取图片资源名称
        List<String> list = repositoryService.getDeploymentResourceNames(deploymentId);
        //定义图片资源的名称
        String resourceName = "";
        if (list != null && list.size()>0){
            for (String name : list){
                if (name.endsWith(".png")){
                    resourceName = name;
                }
            }
        }
        InputStream in = repositoryService.getResourceAsStream(deploymentId, resourceName);
        //将图片生成到c盘目录下
        File file = new File("C:/Users/xd/IdeaProjects/activiti/src/main/resources/static/img/"+resourceName);
        //将输入流的图片写到c盘目录下
        FileUtils.copyInputStreamToFile(in, file);
    }

    /** 附加功能：查询最新版本的流程定义 */
    @Test
    public void findLastVersionProcessDefinition(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()// 使用流程定义的版本号升序排序
                .list();
        Map<String, ProcessDefinition> map = new LinkedHashMap<>();
        if (list != null && list.size()>0){
            for (ProcessDefinition pd : list){
                map.put(pd.getKey(), pd);
            }
        }
        List<ProcessDefinition> pdList = new ArrayList<>(map.values());
        if (pdList != null && pdList.size()>0){
            for (ProcessDefinition pd : pdList){
                System.out.println("流程定义ID："+pd.getId());// 流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称："+pd.getName());// 对应helloworld.bpmn文件中的name属性值
                System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中id属性值
                System.out.println("流程定义的版本："+pd.getVersion());//当流程定义的key值相同的情况下，版本升级
                System.out.println("资源名称bpmn文件："+pd.getResourceName());
                System.out.println("资源名称png文件："+pd.getDiagramResourceName());
                System.out.println("部署对象ID:"+pd.getDeploymentId());
                System.out.println("#########################");
            }
        }
    }

    /** 附加功能：删除流程定义（删除key相同的所有不同版本的流程定义）*/
    @Test
    public void deleteProcessDefinitionByKey(){
        String processDefinitionKey = "helloworld";
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        if (list != null && list.size()>0){
            for (ProcessDefinition pd : list){
                String deploymentId = pd.getDeploymentId();
                repositoryService.deleteDeployment(deploymentId, true);
            }
        }
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
}
