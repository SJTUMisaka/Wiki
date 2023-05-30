package com.boheng.wiki.service;

import com.boheng.wiki.domain.Ebook;
import com.boheng.wiki.domain.EbookExample;
import com.boheng.wiki.mapper.EbookMapper;
import com.boheng.wiki.req.EbookReq;
import com.boheng.wiki.resp.EbookResp;
import com.boheng.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);

        return CopyUtil.copyList(ebooks, EbookResp.class);
    }

}
