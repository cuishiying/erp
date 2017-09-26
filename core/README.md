收发存汇总：

1、自己实现excel导入；

2、导入的货品信息存储在cnoa_jxc_goods中，其他数据存储在cnoa_jxc_stock_goods_detail中，作为期初结存（可以加标记，或者加字段代表期初数量）

3、不通过工作流导入的数据在库存模块的报表依然会显示，在报表模块不会显示。所以报表部分或者使用报表模块或者自己实现，预订使用报表模块。

4、收发存汇总包括第一次导入的数据和其他方式录入的数据（期初=null）,未处理同一货品不同库问题，所有数量-期初=入库

alter table id_name add age int,add address varchar(11);





脚本：

1、按仓库和货品分组求和统计：(必须先导入收发存汇总表才能统计出期初数量)

SELECT d.goodsId,d.storageId,SUM(d.quantity) FROM cnoa_jxc_stock_goods_detail AS d WHERE 1 = 1 GROUP BY d.goodsId,d.storageId ORDER BY d.goodsId;
String sql = "SELECT d.storageId,g.goodsCode,g."+dm+",g.goodsname,g.standard,g.unit,g."+subcode+",d.openingInventoryQuantity,SUM(d.quantity) AS s,g.goodsname,st.storagename FROM (cnoa_jxc_stock_goods_detail AS d LEFT JOIN cnoa_jxc_storage AS st ON d.storageId = st.id) LEFT JOIN  cnoa_jxc_goods AS g ON d.goodsId = g.id WHERE 1 = 1 GROUP BY d.goodsId,d.storageId ORDER BY d.goodsId;";
String sql = "SELECT c.storageName,c.goodsCode,c.storageCode,c.goodsname,c.standard,c.unit,c.goodsSubCode,c.inventoryClassificationName,c.openingInventoryQuantity,c.openingBalance,SUM(c.stockInCount) AS sic,SUM(c.stockInBalance) AS sib,SUM(c.stockOutCount) AS soc,SUM(c.stockOutBalance) AS sob,SUM(c.endingInventoryQuantity) AS eiq,SUM(c.endingBalance) AS eb FROM collect c GROUP BY c.storageName,c.goodsCode ORDER BY c.goodsname";



改配置文件
改脚本
改映射
运行生成table
导入数据
出入库流程实际设置
考勤排班设置

