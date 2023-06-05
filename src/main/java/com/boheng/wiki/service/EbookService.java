package com.boheng.wiki.service;

import com.boheng.wiki.domain.Ebook;
import com.boheng.wiki.domain.EbookExample;
import com.boheng.wiki.mapper.EbookMapper;
import com.boheng.wiki.req.EbookReq;
import com.boheng.wiki.resp.EbookResp;
import com.boheng.wiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(1,3);
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooks);
        LOG.info("Total Row Number:{}", pageInfo.getTotal());
        LOG.info("Total Page Number:{}", pageInfo.getPages());

        return CopyUtil.copyList(ebooks, EbookResp.class);
    }

}
