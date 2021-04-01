package flybear.hziee.core.mybatis;

import flybear.hziee.core.sql.Row;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * 通用mapper
 *
 * @author Simon
 */
interface SqlMapper {

    /**
     * 返回二维结果集
     *
     * @param somap SOMap
     * @return 结果集
     */
    @SelectProvider(type = SqlMapperProvider.class, method = "select")
    List<Row> select(SOMap somap);

    /**
     * 返回单行记录
     *
     * @param somap SOMap
     * @return 单行记录
     */
    @SelectProvider(type = SqlMapperProvider.class, method = "select")
    Row find(SOMap somap);

    /**
     * 返回统计数量
     *
     * @param somap SOMap
     * @return 统计数量
     */
    @SelectProvider(type = SqlMapperProvider.class, method = "select")
    Integer count(SOMap somap);

    /**
     * 返回特定字段的值
     *
     * @param somap SOMap
     * @return 特定字段的值
     */
    @SelectProvider(type = SqlMapperProvider.class, method = "select")
    Object getValue(SOMap somap);

    /**
     * insert方法
     *
     * @param somap SOMap
     * @return 影响条数
     */
    @InsertProvider(type = SqlMapperProvider.class, method = "insert")
    Integer insert(SOMap somap);

    /**
     * update方法
     *
     * @param somap SOMap
     * @return 影响条数
     */
    @UpdateProvider(type = SqlMapperProvider.class, method = "update")
    Integer update(SOMap somap);

    /**
     * delete方法
     *
     * @param somap SOMap
     * @return 影响条数
     */
    @DeleteProvider(type = SqlMapperProvider.class, method = "delete")
    Integer delete(SOMap somap);
}
