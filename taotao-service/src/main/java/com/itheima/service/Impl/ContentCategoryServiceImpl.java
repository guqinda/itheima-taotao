package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.ContentCategoryMapper;
import com.itheima.pojo.ContentCategory;
import com.itheima.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 85074 on 2018/10/25.
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    @Override
    public List<ContentCategory> getCategoryByParentId(Long id) {

        ContentCategory category =new ContentCategory();
        category.setParentId(id);

        return   contentCategoryMapper.select(category);
    }

    @Override
    public ContentCategory add(ContentCategory contentCategory) {
        //1 直接添加这个分类到表  contentCategory:ParentId name
        //把没有传过来的其他属性值设置一下
        contentCategory.setStatus(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());

        contentCategoryMapper.insertSelective(contentCategory);//有选择的存入数据

        // 上面的代码针对的场景是：在父级分类下创建子分类，如果我们是在子分类AA下创建子分类BB
        //那么上面的代码仅仅只能添加子分类BB，并不会把子分类AA  变成父级分类

        //2判断当前这个分类的父亲是不是子分类。如果是子分类，那么要将其变成父级分类

        Long parentId= contentCategory.getParentId();
        ContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);

        //判断当前目录是否为父亲目录
        if(!contentCategory.getIsParent()){
            //如果不是，让当前目录为父级目录
            parentCategory.setIsParent(true);
        }

        //为什么要返回一个对象？
        //1  如果不返回的话，分类的条目无法绑定对象。也就是添加好的分类，根本就没有数据绑定
        //以后要对这个分类进行增删改的话，服务器是不知道的。
        //2如果不返回数据，那么在页面上再进行其他的操作，那么光标会乱跑

        //结论：要返回对象。返回的对象还是当前操作的添加对象

        contentCategoryMapper.updateByPrimaryKeySelective(parentCategory);

        //问题：当前操作的目录返回的对象没有id值     @GeneratedValue(generator = "JDBC"所以在ContentCategory类里的id前面加上这个注解

        return  contentCategory;
    }

    @Override
    public int update(ContentCategory contentCategory) {

     int rows=   contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);

      System.out.println("rows="+rows);
        return rows;
    }

    @Override
    public int delete(ContentCategory contentCategory) {
        //现在只删除子级分类
        //int result = contentCategoryMapper.deleteByPrimaryKey(contentCategory);

        //2 上面针对的是删除一条子级分类。现在要考虑的是直接删除父级分类

        //先定义出来集合，装要删除的对象
        List<ContentCategory> list=new ArrayList<>();
        //得根据当前的删除的节点id，找到它的所有孩子。
        //先往集合里面存当前本来应该删除的分类
        list.add(contentCategory);

        //还要去查询它的子级分类，用递归
        findAllChild(list,contentCategory.getId());

        //删除
       int result= deleteAll(list);

       /*
       这里还要考虑最后一个问题，删除的是BBB，那么AAA的下面就没有子级分类了
       所以它应该变成一个子级的分类，而不是原来的父级分类。
         AAA
           BBB  --->删除的是BBB
             CCC
             DDD
             删除操作的时候，会传递过来当前操作的节点id和父节点的id
             id & parentID  ===》封装在contentCategory了
        */

       //这里是按照parentID去查询总数，看当前操作的目录的父亲目录下还有没有其他字目录
       ContentCategory c=new ContentCategory();
       c.setParentId(contentCategory.getParentId());
       int count=contentCategoryMapper.selectCount(c);

       //表示当前操作的这个节点的父亲，下面已经没有子节点了
        if(count<1){
            //由于这里要更新ParentID ，所以还需要再创建一次对象
            c=new ContentCategory();
            c.setId(contentCategory.getParentId());
            c.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKeySelective(c);
        }

        return result;
    }

    //删除一个集合
    private int deleteAll(List<ContentCategory> list) {
        int result=0;
        for (ContentCategory category : list) {
            result +=contentCategoryMapper.delete(category);
        }
        return result;
    }

    /***
     查询给定的分类id的所有子分类，包含多重的子级分类
     * @param list  存储的集合
     * @param id   当前要查询的id
     */
    private void findAllChild(List<ContentCategory> list, Long id) {

        //找到当前的节点孩子
        List<ContentCategory> childList = getCategoryByParentId(id);

        //如果下级还有子目录就继续找，没有就停
        if(childList!=null&&childList.size()>0){

            //遍历这些子级分类
            for (ContentCategory category : childList) {
                //先往list集合里面添加这个分类
                list.add(category);
                //执行递归，再调用自己本身的方法查找下一级的子目录
                findAllChild(list,category.getId());
            }
        }

    }
}
