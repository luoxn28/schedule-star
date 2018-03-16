package com.schedule.star.admin.bean.convert;

import com.schedule.star.admin.bean.ExecutorBean;
import com.schedule.star.admin.entity.ExecutorEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangnan
 * @date 2018/1/31 22:14
 */
public class ExecutorConvert extends BaseConvert {

    public static ExecutorEntity toEntity(ExecutorBean bean) {
        ExecutorEntity entity = null;

        if (bean != null) {
            entity = mapper.map(bean, ExecutorEntity.class);
        }

        return entity;
    }

    public static ExecutorBean toBean(ExecutorEntity entity) {
        ExecutorBean bean = null;

        if (entity != null) {
            bean = mapper.map(entity, ExecutorBean.class);
        }

        return bean;
    }

    public static List<ExecutorBean> toBeanList(List<ExecutorEntity> entityList) {
        List<ExecutorBean> beanList = new ArrayList<>();

        if (entityList != null) {
            for (ExecutorEntity entity : entityList) {
                beanList.add(toBean(entity));
            }
        }

        return beanList;
    }

}
