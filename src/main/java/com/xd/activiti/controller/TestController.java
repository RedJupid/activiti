package com.xd.activiti.controller;


import com.xd.activiti.util.FlowUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Controller
public class TestController {

    @Autowired
    FlowUtils flowUtils;

    @GetMapping(value = "/home")
    public String home(){
        return "home";
    }

    /**
     * 查看实例流程图，根据流程实例ID获取流程图
     */
    @GetMapping(value="traceprocess/{instanceId}")
    public void traceprocess(HttpServletResponse response, @PathVariable("instanceId")String instanceId) throws Exception{
        InputStream in = flowUtils.getResourceDiagramInputStream(instanceId);
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(in, output);
    }
}
