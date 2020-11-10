package com.opencloud.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.device.entity.SystemMenu;
import com.opencloud.device.exception.BaseException;
import com.opencloud.device.mapper.SystemMenuMapper;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.SystemConstants;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.service.SystemMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemMenuServiceImpl extends BaseServiceImpl<SystemMenuMapper, SystemMenu> implements SystemMenuService {
//    @Autowired
//    private SystemAuthorityService systemAuthorityService;
//
//    @Autowired
//    private SystemActionService systemActionService;

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemMenu> findPage(PageParams pageParams) {
        SystemMenu query = pageParams.mapToBean(SystemMenu.class);
        QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getMenuCode()), SystemMenu::getMenuCode, query.getMenuCode())
                .likeRight(ObjectUtils.isNotEmpty(query.getMenuName()), SystemMenu::getMenuName, query.getMenuName());
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SystemMenu> findList(PageParams pageParams) {
        SystemMenu query = pageParams.mapToBean(SystemMenu.class);
        QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper();
        System.out.println("中饭呢哦跟首发发飞机饿哦戢国鹏" + query.getParentId());
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getMenuType()), SystemMenu::getMenuType, query.getMenuType())
                .eq(ObjectUtils.isNotEmpty(query.getParentId()), SystemMenu::getParentId, query.getParentId())
                .orderByDesc(SystemMenu::getPriority);
        List<SystemMenu> list = list(queryWrapper);
        //根据优先级从大到小排序
//        list.sort((SystemMenu h1, SystemMenu h2) -> h2.getPriority().compareTo(h1.getPriority()));
        return list;
    }

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    @Override
    public Boolean isExist(String menuCode,Integer menuType) {
        QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SystemMenu::getMenuCode, menuCode)
                .eq(ObjectUtils.isNotEmpty(menuType),SystemMenu::getMenuType,menuType);
        int count = count(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加菜单资源
     *
     * @param menu
     * @return
     */
    @Override
    public SystemMenu add(SystemMenu menu) {
        if (isExist(menu.getMenuCode(),menu.getMenuType())) {
            throw new BaseException(String.format("%s编码已存在!", menu.getMenuCode()));
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getPriority() == null) {
            menu.setPriority(0);
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getIsPersist() == null) {
            menu.setIsPersist(0);
        }
        menu.setCreateTime(new Date());
        menu.setUpdateTime(menu.getCreateTime());
        save(menu);
        // 同步权限表里的信息
        return menu;
    }

    /**
     * 修改菜单资源
     *
     * @param menu
     * @return
     */
    @Override
    public SystemMenu update(SystemMenu menu) {
        SystemMenu saved = getById(menu.getMenuId());
        if (saved == null) {
            throw new BaseException(String.format("%s信息不存在!", menu.getMenuId()));
        }
        if (!saved.getMenuCode().equals(menu.getMenuCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(menu.getMenuCode(),menu.getMenuType())) {
                throw new BaseException(String.format("%s编码已存在!", menu.getMenuCode()));
            }
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getPriority() == null) {
            menu.setPriority(0);
        }
        menu.setUpdateTime(new Date());
        updateById(menu);
        // 同步权限表里的信息
//        systemAuthorityService.saveOrUpdateAuthority(menu.getMenuId(), ResourceType.menu);
        return menu;
    }


    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public void remove(Long menuId) {
        SystemMenu menu = getById(menuId);
        if (menu != null && menu.getIsPersist().equals(SystemConstants.ENABLED)) {
            throw new BaseException(String.format("默认数据,禁止删除!"));
        }
//        // 移除菜单权限
//        systemAuthorityService.removeAuthority(menuId, ResourceType.menu);
//        // 移除功能按钮和相关权限
//        systemActionService.removeByMenuId(menuId);
        // 移除菜单信息
        removeById(menuId);
    }

    @Override
    public List<SystemMenu> findMenuByUser(Map<String, Object> paramMap) {
//        Map<String,Object> paramMap = new HashMap<>(16);
//        paramMap.put("userId",userId);
//        paramMap.put("systemCode",systemCode);
        List<SystemMenu> list = baseMapper.findMenuByUser(paramMap);
//        QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper();
//        System.out.println("中饭呢哦跟首发发飞机饿哦戢国鹏"+query.getParentId());
//        queryWrapper.lambda()
//                .eq(ObjectUtils.isNotEmpty(query.getParentId()), SystemMenu::getParentId, query.getParentId());
//        List<SystemMenu> list = list(queryWrapper);
        //根据优先级从大到小排序
        list.sort((SystemMenu h1, SystemMenu h2) -> h2.getPriority().compareTo(h1.getPriority()));
        return list;
    }

    @Override
    public List<SystemMenu> findCustMenuByUser(Map<String, Object> paramMap) {
        List<SystemMenu> list = baseMapper.findCustMenuByUser(paramMap);
        list.sort((SystemMenu h1, SystemMenu h2) -> h2.getPriority().compareTo(h1.getPriority()));
        return list;
    }
}
