# 收发存汇总：

    1. 自己实现excel导入；
    
    2. 导入的货品信息存储在cnoa_jxc_goods中，其他数据存储在cnoa_jxc_stock_goods_detail中，作为期初结存（可以加标记，或者加字段代表期初数量）
    
    3. 不通过工作流导入的数据在库存模块的报表依然会显示，在报表模块不会显示。所以报表部分或者使用报表模块或者自己实现，预订使用报表模块。
    
    4. 收发存汇总包括第一次导入的数据和其他方式录入的数据（期初=null）,未处理同一货品不同库问题，所有数量-期初=入库
    
    alter table id_name add age int,add address varchar(11);





## 脚本：

1. 按仓库和货品分组求和统计：(必须先导入收发存汇总表才能统计出期初数量)


    SELECT d.goodsId,d.storageId,SUM(d.quantity) FROM cnoa_jxc_stock_goods_detail AS d WHERE 1 = 1 GROUP BY d.goodsId,d.storageId ORDER BY d.goodsId;
    String sql = "SELECT d.storageId,g.goodsCode,g."+dm+",g.goodsname,g.standard,g.unit,g."+subcode+",d.openingInventoryQuantity,SUM(d.quantity) AS s,g.goodsname,st.storagename FROM (cnoa_jxc_stock_goods_detail AS d LEFT JOIN cnoa_jxc_storage AS st ON d.storageId = st.id) LEFT JOIN  cnoa_jxc_goods AS g ON d.goodsId = g.id WHERE 1 = 1 GROUP BY d.goodsId,d.storageId ORDER BY d.goodsId;";
    String sql = "SELECT c.storageName,c.goodsCode,c.storageCode,c.goodsname,c.standard,c.unit,c.goodsSubCode,c.inventoryClassificationName,c.openingInventoryQuantity,c.openingBalance,SUM(c.stockInCount) AS sic,SUM(c.stockInBalance) AS sib,SUM(c.stockOutCount) AS soc,SUM(c.stockOutBalance) AS sob,SUM(c.endingInventoryQuantity) AS eiq,SUM(c.endingBalance) AS eb FROM collect c GROUP BY c.storageName,c.goodsCode ORDER BY c.goodsname";


2. 部署操作步骤


    * 改配置文件
    * 改脚本
    * 改映射
    * 运行生成table
    * 导入数据
    * 出入库流程实际设置
    * 考勤排班设置


## 打包：

    mvn clean package -DskipTests -Pproduct 生产环境打包
    mvn clean package -DskipTests -Psit 测试环境打包




# 脚本重构

### 1. 数据库分析

    cnoa_jxc_goods    货品表
    cnoa_jxc_stock_goods_detail    出入库明细表
    cnoa_jxc_stock_chuku  出库信息
    
    cnoa_wf_u_step    流程步骤信息
    cnoa_z_wf_t_93    出库流程信息
    cnoa_z_wf_d_93_1174   出库流程货品明细


### 2. 旧方案（status=2完成；status=1未完成;）

    流程发起商品预减（在cnoa_jxc_stock_goods_detail插入临时数据）
    流程结束回滚预减数量 (在cnoa_jxc_stock_goods_detail删除临时数据）
    流程拒绝回滚预减数量（在cnoa_jxc_stock_goods_detail删除临时数据）
    收发存汇总（在cnoa_jxc_stock_goods_detail有正式数据插入时，往cnoa_jxc_collect同步数据，退库后cnoa_jxc_collect中相关数据同步删除）

### 3. 新方案

    监听cnoa_wf_u_step插入，stepType=1代表发起流程，预减；stepType=3代表流程结束，回滚预减；stepType=2,判断是否已经存在预减信息，不存在则预减
    监听cnoa_wf_u_step删除，删除代表拒绝，回滚预减。
    收发存汇总（在cnoa_jxc_stock_goods_detail有正式数据插入时，往cnoa_jxc_collect同步数据，退库后cnoa_jxc_collect中相关数据同步删除，退库只发生在流程完成时）

### 4. 初始化库存数据

    delete from cnoa_jxc_goods;
    delete from cnoa_jxc_collect;
    delete from cnoa_jxc_stock_goods_detail;
    DELETE FROM cnoa_jxc_stock_chuku;
    DELETE FROM cnoa_jxc_stock_ruku;
    DELETE FROM cnoa_z_wf_t_93;
    DELETE FROM cnoa_z_wf_d_93_1174;
    
### 5. 删除触发器

    
    DROP TRIGGER IF EXISTS cnoa_afterinsert_on_cnoa_jxc_stock_chuku;
    DROP TRIGGER IF EXISTS cnoa_afterinsert_on_cnoa_wf_u_step;
    DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_wf_u_step;
    DROP TRIGGER IF EXISTS cnoa_afterinsert_on_cnoa_jxc_goods_detail;
    DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_jxc_goods_detai;




