# ***********************字段配置部分******************************

# 库存模块预减
ALTER TABLE cnoa_jxc_stock_goods_detail ADD tempId INT;

# 收发存汇总
ALTER TABLE cnoa_jxc_goods ADD UNIQUE (`goodsCode`);
ALTER TABLE cnoa_jxc_stock_goods_detail ADD chongkuCount INT;
ALTER TABLE cnoa_jxc_stock_goods_detail ADD openingInventoryQuantity INT;
ALTER TABLE cnoa_jxc_stock_goods_detail ADD openingBalance INT;
ALTER TABLE cnoa_jxc_stock_goods_detail ADD initCount INT;
ALTER TABLE cnoa_z_wf_d_93_1276 ADD lastStock INT;


# ***********************删除垃圾触发器******************************

# 批量删除触发器
DROP TRIGGER IF EXISTS cnoa_afterinsert_on_cnoa_wf_u_step;
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_wf_u_step;
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_jxc_goods_detail;
DROP TRIGGER IF EXISTS cnoa_beforeinsert_on_cnoa_jxc_goods_detail;
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_z_wf_d_93_1276;
DROP TRIGGER IF EXISTS cnoa_beforeinsert_on_cnoa_z_wf_d_93_1276;
DROP TRIGGER IF EXISTS cnoa_beforeupdate_on_cnoa_z_wf_d_93_1276;

SHOW TRIGGERS;


# ***********************触发器******************************
# step——detail——93

DELIMITER //

# 流程发起预减 （AND NEW.stepname <> '出库确认'）。流程完成不计算价格，因为单价会变，所以这里总价得重新计算，D_256单价 D_255数量
DROP TRIGGER IF EXISTS cnoa_afterinsert_on_cnoa_wf_u_step;
CREATE TRIGGER cnoa_afterinsert_on_cnoa_wf_u_step AFTER INSERT
  ON cnoa_wf_u_step FOR EACH ROW
  BEGIN
    IF(NEW.stepType = 1) THEN
      SELECT SUM(quantity) INTO @ct FROM `cnoa_jxc_stock_goods_detail`;
    ELSEIF (NEW.stepType = 2) THEN
      IF((SELECT COUNT(1) FROM `cnoa_jxc_stock_goods_detail`WHERE tempId = NEW.uFlowId)<=0) THEN
        INSERT INTO cnoa_jxc_stock_goods_detail(goodsId,price,quantity,sum,storageId,uFlowId,tempId) SELECT c.bindid,c.D_256,-c.D_255,-c.D_257,ck.storageId,c.uFlowId,c.uFlowId FROM cnoa_z_wf_d_93_1276 AS c,cnoa_jxc_stock_chuku AS ck WHERE c.uFlowId = ck.uFlowId AND c.uFlowId = new.uFlowId;
      END IF;
    ELSEIF (NEW.stepType = 3) THEN
      DELETE FROM cnoa_jxc_stock_goods_detail WHERE tempId = NEW.uFlowId;
    END IF ;
  END;

# 流程拒绝后回滚预减数量,重新计算价格
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_wf_u_step;
CREATE TRIGGER cnoa_afterdelete_on_cnoa_wf_u_step AFTER DELETE
  ON cnoa_wf_u_step FOR EACH ROW
  BEGIN
    SELECT COUNT(stepType) INTO @st FROM cnoa_wf_u_step WHERE uFlowId = OLD.uFlowId AND stepType = 2;
    IF (@st = 0) THEN
      DELETE FROM cnoa_jxc_stock_goods_detail WHERE tempId = OLD.uFlowId;
    END IF ;
  END;

# 出库隐藏单价后自动计算单价
DROP TRIGGER IF EXISTS cnoa_beforeinsert_on_cnoa_z_wf_d_93_1276;
CREATE TRIGGER cnoa_beforeinsert_on_cnoa_z_wf_d_93_1276 BEFORE INSERT
  ON cnoa_z_wf_d_93_1276 FOR EACH ROW
  BEGIN
    SELECT storageid INTO @storageId FROM cnoa_jxc_stock_chuku WHERE uFlowId = NEW.uFlowId;
    SELECT SUM(sum) INTO @sum FROM cnoa_jxc_stock_goods_detail WHERE storageId = @storageId AND goodsId = NEW.bindid;
    SELECT SUM(quantity) INTO @quantity FROM cnoa_jxc_stock_goods_detail WHERE storageId = @storageId AND goodsId = NEW.bindid;

    IF(NEW.D_255 = @quantity) THEN
      SET NEW.D_256 = CONVERT(@sum/@quantity,decimal(12,2));
      SET NEW.D_257 = @sum;
    ELSEIF (NEW.D_255 < @quantity) THEN
      SET NEW.D_256 = CONVERT(@sum/@quantity,decimal(12,2));
      SET NEW.D_257 = NEW.D_255*CONVERT(@sum/@quantity,decimal(12,2));
    END IF ;

  END;



# 收发存汇总
DROP TRIGGER IF EXISTS cnoa_beforeinsert_on_cnoa_jxc_goods_detail;
CREATE TRIGGER cnoa_beforeinsert_on_cnoa_jxc_goods_detail BEFORE INSERT
  on cnoa_jxc_stock_goods_detail FOR EACH ROW
  BEGIN
    IF(NEW.tempId IS NULL ) THEN
      IF (NEW.quantity>0) THEN
        INSERT INTO cnoa_jxc_collect SET
          storagename = (SELECT storagename FROM cnoa_jxc_storage WHERE id = new.storageId),
          goodsCode = (SELECT goodsCode FROM cnoa_jxc_goods WHERE id = new.goodsId),
          storageCode = (SELECT field6 FROM cnoa_jxc_goods WHERE id = new.goodsId),
          goodsname = (SELECT goodsname FROM cnoa_jxc_goods WHERE id = new.goodsId),
          standard = (SELECT standard FROM cnoa_jxc_goods WHERE id = new.goodsId),
          unit = (SELECT unit FROM cnoa_jxc_goods WHERE id = new.goodsId),
          goodsSubCode = (SELECT c.code FROM cnoa_jxc_category AS c LEFT JOIN cnoa_jxc_goods AS g ON c.code = g.field7 WHERE g.id = new.goodsId),
          inventoryClassificationName = (SELECT c.name FROM cnoa_jxc_category AS c LEFT JOIN cnoa_jxc_goods AS g ON c.code = g.field7 WHERE g.id = new.goodsId),
          openingInventoryQuantity = (new.openingInventoryQuantity),
          openingBalance = (new.openingBalance),
          stockInCount = (new.quantity),
          stockInBalance = (new.sum),
          stockOutCount = 0,
          stockOutBalance = 0,
          endingInventoryQuantity = (new.quantity),
          endingBalance = (new.sum),
          storageId = (NEW.storageId),
          goodsId = (new.goodsId),
          chongkuCount = (new.chongkuCount),
          uFlowId = (new.uFlowId);
      ELSEIF(NEW.quantity<0) THEN

        SET NEW.sum = -(SELECT D_257 FROM cnoa_z_wf_d_93_1276 WHERE bindid = NEW.goodsId AND uFlowId = NEW.uFlowId);
        INSERT INTO cnoa_jxc_collect SET
          storagename = (SELECT storagename FROM cnoa_jxc_storage WHERE id = new.storageId),
          goodsCode = (SELECT goodsCode FROM cnoa_jxc_goods WHERE id = new.goodsId),
          storageCode = (SELECT field6 FROM cnoa_jxc_goods WHERE id = new.goodsId),
          goodsname = (SELECT goodsname FROM cnoa_jxc_goods WHERE id = new.goodsId),
          standard = (SELECT standard FROM cnoa_jxc_goods WHERE id = new.goodsId),
          unit = (SELECT unit FROM cnoa_jxc_goods WHERE id = new.goodsId),
          goodsSubCode = (SELECT c.code FROM cnoa_jxc_category AS c LEFT JOIN cnoa_jxc_goods AS g ON c.code = g.field7 WHERE g.id = new.goodsId),
          inventoryClassificationName = (SELECT c.name FROM cnoa_jxc_category AS c LEFT JOIN cnoa_jxc_goods AS g ON c.code = g.field7 WHERE g.id = new.goodsId),
          openingInventoryQuantity = (new.openingInventoryQuantity),
          openingBalance = (new.openingBalance),
          stockInCount = 0,
          stockInBalance = 0,
          stockOutCount = (new.quantity),
          stockOutBalance = (NEW.sum),
          endingInventoryQuantity = (new.quantity),
          endingBalance = (NEW.sum),
          storageId = (NEW.storageId),
          goodsId = (new.goodsId),
          chongkuCount = (new.chongkuCount),
          uFlowId = (new.uFlowId);

      END IF;

    END IF ;

  END;

# 退库后收发存数量回滚。重新计算货品价格.退库先删除表单，再删除记录。
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_z_wf_d_93_1276;
CREATE TRIGGER cnoa_afterdelete_on_cnoa_z_wf_d_93_1276 AFTER DELETE
  ON cnoa_z_wf_d_93_1276 FOR EACH ROW
  BEGIN
    DELETE FROM cnoa_jxc_collect WHERE goodsId = OLD.bindid AND uFlowId = OLD.uFlowId;
  END;

# 流程完成删除预减，价格不处理。拒绝回滚后计算价格并更新原表单价格.
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_jxc_goods_detail;
CREATE TRIGGER cnoa_afterdelete_on_cnoa_jxc_goods_detail AFTER DELETE
  ON cnoa_jxc_stock_goods_detail FOR EACH ROW
  BEGIN
    IF((SELECT COUNT(*) FROM cnoa_wf_u_step WHERE uFlowId = OLD.uFlowId AND stepType = 3)=0) THEN
      SELECT SUM(sum) INTO @ds FROM cnoa_jxc_stock_goods_detail WHERE goodsId = OLD.goodsId AND storageId = OLD.storageId;
      SELECT SUM(quantity) INTO @dq FROM cnoa_jxc_stock_goods_detail WHERE goodsId = OLD.goodsId AND storageId = OLD.storageId;
      UPDATE cnoa_z_wf_d_93_1276 SET D_256 = CONVERT(@ds/@dq,decimal(12,2)) WHERE uFlowId = OLD.uFlowId AND bindid = OLD.goodsId;
    END IF;
  END;

# 拒绝后重新计算总价，单价和数量会改变
DROP TRIGGER IF EXISTS cnoa_beforeupdate_on_cnoa_z_wf_d_93_1276;
CREATE TRIGGER cnoa_beforeupdate_on_cnoa_z_wf_d_93_1276 BEFORE UPDATE
  ON cnoa_z_wf_d_93_1276 FOR EACH ROW
  BEGIN
    SELECT storageid INTO @storageId FROM cnoa_jxc_stock_chuku WHERE uFlowId = NEW.uFlowId;
    SELECT SUM(sum) INTO @sum FROM cnoa_jxc_stock_goods_detail WHERE storageId = @storageId AND goodsId = NEW.bindid;
    SELECT SUM(quantity) INTO @quantity FROM cnoa_jxc_stock_goods_detail WHERE storageId = @storageId AND goodsId = NEW.bindid;

    IF(NEW.D_255 = @quantity) THEN
      SET NEW.D_256 = CONVERT(@sum/@quantity,decimal(12,2));
      SET NEW.D_257 = @sum;
    ELSEIF (NEW.D_255 < @quantity) THEN
      SET NEW.D_256 = CONVERT(@sum/@quantity,decimal(12,2));
      SET NEW.D_257 = NEW.D_255*CONVERT(@sum/@quantity,decimal(12,2));
    END IF ;
  END;

//
SHOW TRIGGERS;


# ***********************初始化table******************************

# 删除历史数据
delete from cnoa_jxc_goods;
delete from cnoa_jxc_collect;
delete from cnoa_jxc_stock_goods_detail;
DELETE FROM cnoa_jxc_stock_chuku;
DELETE FROM cnoa_jxc_stock_ruku;
DELETE FROM cnoa_z_wf_t_93;
DELETE FROM cnoa_z_wf_d_93_1276;
DELETE FROM cnoa_z_wf_t_92;
DELETE FROM cnoa_z_wf_d_92_1165;