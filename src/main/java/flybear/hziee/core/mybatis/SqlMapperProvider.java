package flybear.hziee.core.mybatis;

/**
 * 运行自定义sql的Provider实现类
 *
 * @author Simon
 */
public class SqlMapperProvider {

    public String select(SOMap somap) {
        return somap.get("sql").toString();
    }

    public String update(SOMap somap) {
        return somap.get("sql").toString();
    }

    public String insert(SOMap somap) {
        return somap.get("sql").toString();
    }

    public String delete(SOMap somap) {
        return somap.get("sql").toString();
    }

}
