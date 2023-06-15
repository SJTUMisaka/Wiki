package com.boheng.wiki.service;

import com.boheng.wiki.domain.Category;
import com.boheng.wiki.domain.CategoryExample;
import com.boheng.wiki.mapper.CategoryMapper;
import com.boheng.wiki.req.CategoryQueryReq;
import com.boheng.wiki.req.CategorySaveReq;
import com.boheng.wiki.resp.CategoryQueryResp;
import com.boheng.wiki.resp.PageResp;
import com.boheng.wiki.util.CopyUtil;
import com.boheng.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categories = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categories);
        LOG.info("Total Row Number:{}", pageInfo.getTotal());
        LOG.info("Total Page Number:{}", pageInfo.getPages());

        List<CategoryQueryResp> list = CopyUtil.copyList(categories, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
