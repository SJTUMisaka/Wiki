package com.boheng.wiki.controller;

import com.boheng.wiki.domain.Ebook;
import com.boheng.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/ebook/list")
    public List<Ebook> list() {
        return ebookService.list();
    }
}
