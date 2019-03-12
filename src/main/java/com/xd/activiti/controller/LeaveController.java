package com.xd.activiti.controller;

import com.xd.activiti.entity.Application;
import com.xd.activiti.entity.LeaveInfo;
import com.xd.activiti.entity.Msg;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @PostMapping("/apply")
    public Msg apply(@RequestBody LeaveInfo leaveInfo){
        String processDefinitionKey = "leave";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println(task.getId());
        System.out.println(leaveInfo.getApplicant()+leaveInfo.getDay()+leaveInfo.getReason());
        taskService.setAssignee(task.getId(),leaveInfo.getApplicant());//设置申请人

        Map map = new HashMap();
        map.put("leaveInfo",leaveInfo);
        taskService.complete(task.getId(),map);//完成申请
        return Msg.success().add("instanceId",processInstance.getId());
    }

    @GetMapping("/task")
    public List<LeaveInfo> getTask(){
        List<Task> list = taskService.createTaskQuery().taskAssignee("李四").list();
        List<LeaveInfo> leaveInfos = new ArrayList<>();
        if (list != null && list.size()>0){
            for (Task t : list){
                LeaveInfo leaveInfo = (LeaveInfo) taskService.getVariable(t.getId(),"leaveInfo");
                leaveInfos.add(leaveInfo);
            }
        }
        return leaveInfos;
    }

    @PutMapping("/finish{taskId}")
    public Msg finish(@PathVariable String taskId){
        taskService.complete(taskId);
        return Msg.success();
    }
}
