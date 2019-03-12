package com.xd.activiti;


import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HistoryQueryTest {

    @Autowired
    HistoryService historyService;

    /** 查询历史流程实例*/
    @Test
    public void findHistoryProcessInstance(){
        String processIntanceId = "2679ccbb-43a2-11e9-bc0c-8cec4b862f6e";
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processIntanceId)
                .orderByProcessInstanceStartTime().asc()
                .singleResult();
        System.out.println(hpi.getId()
                +" "+hpi.getProcessDefinitionId()
                +" "+hpi.getStartTime()
                +" "+hpi.getEndTime()
                +" "+hpi.getDurationInMillis());
    }

    /** 查询历史活动*/
    @Test
    public void findHistoryActiiti(){
        String processIntanceId = "2679ccbb-43a2-11e9-bc0c-8cec4b862f6e";
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processIntanceId)
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();
        if(list != null && list.size()>0){
            for (HistoricActivityInstance hai : list){
                System.out.println(hai.getId()+"  "+hai.getProcessInstanceId()
                        +"  "+hai.getActivityType()
                        +"  "+hai.getStartTime()
                        +"  "+hai.getEndTime());
                System.out.println("##########################");
            }
        }
    }

    /** 查询历史任务*/
    @Test
    public void findHistoryTask(){
        String processIntanceId = "2679ccbb-43a2-11e9-bc0c-8cec4b862f6e";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()// 创建任务查询对象
                .processInstanceId(processIntanceId)// 指定个人任务查询，指定办理人
                /** 排序*/
                .orderByHistoricTaskInstanceStartTime().asc()// 使用创建时间的升序排序
                /** 返回结果集*/
                .list();
//                .singleResult();// 返回唯一结果集
//                .count();// 返回结果集数量
//                .listPage(firstResult, maxResult);// 分页查询
        if(list != null && list.size()>0){
            for (HistoricTaskInstance hti : list){
                System.out.println(hti.getId()+"  "+hti.getProcessInstanceId()
                        +"  "+hti.getName()
                        +"  "+hti.getStartTime()
                        +"  "+hti.getEndTime());
                System.out.println("##########################");
            }
        }
    }

    /** 查询历史流程变量*/
    @Test
    public void findHistoryProcessVariables(){

        String processIntanceId = "2679ccbb-43a2-11e9-bc0c-8cec4b862f6e";
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()// 创建一个历史的流程变量查询对象
                .processInstanceId(processIntanceId)
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
