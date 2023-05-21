package com.boheng.wiki.controller;

import com.boheng.wiki.domain.Demo;
import com.boheng.wiki.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DemoController {

    @Resource
    private DemoService demoService;

    @GetMapping("/demo/list")
    public List<Demo> list() {
        return demoService.list();
    }
}
