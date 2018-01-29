# ***********************字段配置部分******************************

# 库存模块预减
ALTER TABLE cnoa_jxc_stock_goods_detail ADD tempId INT;

# 收发存汇总
ALTER TABLE cnoa_jxc_goods ADD UNIQUE (`goodsCode`);
ALTER TABLE cnoa_jxc_stock_goods_detail ADD chongkuCount INT;
ALTER TABLE cnoa_jxc_stock_goods_detail ADD openingInventoryQuantity INT;
ALTER TABLE cnoa_jxc_stock_goods_detail ADD openingBalance INT;
ALTER TABLE cnoa_jxc_stock_goods_detail ADD initCount INT;


# ***********************删除垃圾触发器******************************

# 批量删除触发器
DROP TRIGGER IF EXISTS cnoa_afterinsert_on_cnoa_jxc_stock_chuku;
DROP TRIGGER IF EXISTS cnoa_afterinsert_on_cnoa_wf_u_step;
DROP TRIGGER IF EXISTS cnoa_afterinsert_on_cnoa_wf_u_step;
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_wf_u_step;
DROP TRIGGER IF EXISTS cnoa_afterinsert_on_cnoa_jxc_goods_detail;
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_jxc_goods_detail;
DROP TRIGGER IF EXISTS cnoa_beforeinsert_on_cnoa_jxc_goods_detail;
DROP TRIGGER IF EXISTS cnoa_afterupdate_on_cnoa_z_wf_d_c;
SHOW TRIGGERS;


# ***********************触发器******************************

DELIMITER //

# 流程发起预减 AND NEW.stepname <> '出库确认'
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

# 流程拒绝后回滚预减数量
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_wf_u_step;
CREATE TRIGGER cnoa_afterdelete_on_cnoa_wf_u_step AFTER DELETE
  ON cnoa_wf_u_step FOR EACH ROW
  BEGIN
    SELECT COUNT(stepType) INTO @st FROM cnoa_wf_u_step WHERE uFlowId = OLD.uFlowId AND stepType = 2;
    IF (@st = 0) THEN
      DELETE FROM cnoa_jxc_stock_goods_detail WHERE tempId = OLD.uFlowId;
    END IF ;
  END;

# 隐藏单价后自动计算单价
DROP TRIGGER IF EXISTS cnoa_beforeinsert_on_cnoa_z_wf_d_93_1276;
CREATE TRIGGER cnoa_beforeinsert_on_cnoa_z_wf_d_93_1276 BEFORE INSERT
  ON cnoa_z_wf_d_93_1276 FOR EACH ROW
  BEGIN
    SELECT price INTO @price FROM cnoa_jxc_goods WHERE goodsCode = NEW.D_251;
    SET NEW.D_256 = @price;
    SET NEW.D_257 = NEW.D_255*@price;
  END;

# 流程中审批者更新数据（数量）
# DROP TRIGGER  IF EXISTS cnoa_afterupdate_on_cnoa_z_wf_d_93_1276;
# CREATE TRIGGER cnoa_afterupdate_on_cnoa_z_wf_d_93_1276 AFTER UPDATE
#   ON cnoa_z_wf_d_93_1276 FOR EACH ROW
#   BEGIN
#     UPDATE cnoa_jxc_stock_goods_detail  SET
#       quantity = -NEW.D_255 WHERE uFlowId = NEW.uFlowId AND tempId = NEW.uFlowId;
#   END;


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
        SELECT SUM(quantity) INTO @qt FROM cnoa_jxc_stock_goods_detail WHERE goodsId = NEW.goodsId;
        IF (@qt=-NEW.quantity) THEN

          SELECT SUM(sum) INTO @sm FROM cnoa_jxc_stock_goods_detail WHERE goodsId = NEW.goodsId;
          SET NEW.sum = -@sm;

          UPDATE cnoa_z_wf_d_93_1276 SET D_257 = @sm WHERE uFlowId = NEW.uFlowId AND bindid = NEW.goodsId;

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
        ELSEIF (@qt>-NEW.quantity) THEN
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
            stockOutBalance = (new.sum),
            endingInventoryQuantity = (new.quantity),
            endingBalance = (new.sum),
            storageId = (NEW.storageId),
            goodsId = (new.goodsId),
            chongkuCount = (new.chongkuCount),
            uFlowId = (new.uFlowId);
        END IF ;
      END IF;

    END IF ;

    IF ((SELECT COUNT(*) FROM cnoa_jxc_stock_goods_detail WHERE goodsId = NEW.goodsId)>0) THEN
      SELECT SUM(sum) INTO @tempSum FROM cnoa_jxc_stock_goods_detail WHERE goodsId = NEW.goodsId;
      SELECT SUM(quantity) INTO @tempCount FROM cnoa_jxc_stock_goods_detail WHERE goodsId = NEW.goodsId;
    ELSEIF((SELECT COUNT(*) FROM cnoa_jxc_stock_goods_detail WHERE goodsId = NEW.goodsId)=0) THEN
      SET @tempSum = 0;
      SET @tempCount = 0;
    END IF ;

    UPDATE cnoa_jxc_goods SET price = CONVERT((@tempSum+NEW.sum)/(@tempCount+NEW.quantity),decimal(12,2)) WHERE id = NEW.goodsId;

  END;

# 退库后收发存数量回滚
DROP TRIGGER IF EXISTS cnoa_afterdelete_on_cnoa_jxc_goods_detail;
CREATE TRIGGER cnoa_afterdelete_on_cnoa_jxc_goods_detail AFTER DELETE
  ON cnoa_jxc_stock_goods_detail FOR EACH ROW
  BEGIN
    DELETE FROM cnoa_jxc_collect WHERE goodsId = OLD.goodsId AND storageId = OLD.storageId AND uFlowId = OLD.uFlowId;
    SELECT SUM(sum) INTO @ds FROM cnoa_jxc_stock_goods_detail WHERE tempId IS NULL AND goodsId = OLD.goodsId AND storageId = OLD.storageId;
    SELECT SUM(quantity) INTO @dq FROM cnoa_jxc_stock_goods_detail WHERE tempId IS NULL AND goodsId = OLD.goodsId AND storageId = OLD.storageId;
    UPDATE cnoa_jxc_goods SET price = CONVERT(@ds/@dq,decimal(12,2)) WHERE id = OLD.goodsId;
    UPDATE cnoa_z_wf_d_93_1276 SET D_256 = CONVERT(@ds/@dq,decimal(12,2)) WHERE uFlowId = OLD.uFlowId AND bindid = OLD.goodsId;
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