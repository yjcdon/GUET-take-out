package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId (Long categoryId);


    // 新增菜品
    @AutoFill(value = OperationType.INSERT)
    void insert (Dish dish);

    // 菜品分页查询
    Page<DishVO> pageQuery (DishPageQueryDTO dishPageQueryDTO);

    // 根据主键查询菜品
    @Select("select *from dish where id = #{id}")
    Dish getById (Long id);

    // 根据主键删除菜品数据
    @Delete("delete from dish where id=#{id}")
    void deleteById (Long id);

    // 根据菜品id集合批量删除菜品数据
    void deleteByIds (List<Long> ids);

    // 根据id修改菜品基本信息和口味信息
    @AutoFill(value = OperationType.UPDATE)
    void update (Dish dish);

    /**
     * 动态条件查询菜品
     *
     * @param dish
     * @return
     */
    List<Dish> list (Dish dish);

    /**
     * 根据套餐id查询菜品
     *
     * @param
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId (Long id);

    /**
     * 根据条件统计菜品数量
     *
     * @param map
     * @return
     */
    Integer countByMap (Map map);
}
