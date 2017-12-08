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
    
### 5.加数据库初始化接口，部分配置表数据自动预设数据 


#视频监控

###1、方案

    1.搭建好IPC和NVR环境后，可以通过NVR的IP地址（内网）和rtsp协议访问到关联此NVR的IPC视频
    2.通过路由器的端口映射功能，将NVR的端口映射到外网上，这样我们就能通过外网IP和映射的外网端口访问到与承此NVR关联的所有IPC视频,
    3.在远程服务器上使用ffmpeg将rtsp视频转换成hls视频
    4.通过nginx将hls视频进行转发
    注：ip为NVR的ip，通过通道号来区分具体的摄像头，重复调用转码指令。
    
    http://10.38.8.20/   admin/admin
    http://10.38.5.10:85/   admin/12345
    rtsp://admin:admin12345@10.38.8.28:554/h264/ch1/main/av_stream      铺龙湾海康摄像机
    
     测试：   ffmpeg -i "rtsp://admin:admin12345@10.38.8.28:554/h264/ch1/main/av_stream" -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8



###2、 拉流指令

    ffmpeg -i "rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream" -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 D:/app/nginx-1.12.2/html/hls/slkj.m3u8
    ffmpeg -i "rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream" -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8
    
    
###3、具体步骤：

    1、安装ffmpeg
    2、安装nginx并配置
    3、测试NVR中rtsp流
    4、ffmpeg转码hls推流
    5、nginx发布
    6、java程序通过nginx拉流预览（通道配置+视频管理）

###4、mac nginx启动

    二、启动
    
    在终端中输入
    
    ps -ef|grep nginx
    如果执行的结果是
    
      501 15800     1   0 12:17上午 ??         0:00.00 nginx: master process /usr/local/Cellar/nginx/1.8.0/bin/nginx -c /usr/local/etc/nginx/nginx.conf  
      501 15801 15800   0 12:17上午 ??         0:00.00 nginx: worker process  
      501 15848 15716   0 12:21上午 ttys000    0:00.00 grep nginx
    
    表示已启动成功，如果不是上图结果，在终端中执行
    
    /usr/local/Cellar/nginx/1.12.2_1/bin/nginx -c /usr/local/etc/nginx/nginx.conf 
    命令即可启动nginx。
    这时候如果成功访问localhost:8080，说明成功安装和启动好了。
    
    三、停止
    
    在终端中输入 ps -ef|grep nginx  获取到nginx的进程号，注意是找到“nginx:master”的那个进程号，如下面的进程好是 15800
    
    
      501 15800     1   0 12:17上午 ??         0:00.00 nginx: master process /usr/local/Cellar/nginx/1.8.0/bin/nginx -c /usr/local/etc/nginx/nginx.conf  
      501 15801 15800   0 12:17上午 ??         0:00.00 nginx: worker process  
      501 15848 15716   0 12:21上午 ttys000    0:00.00 grep nginx
    
    在终端中输入以下几种命令都可以停止
    
    kill -QUIT  15800 (从容的停止，即不会立刻停止)
    
    Kill -TERM  15800 （立刻停止）
    
    Kill -INT  15800  （和上面一样，也是立刻停止）
    
    四、重启
    
    如果配置文件错误，则将启动失败，所以在启动nginx之前，需要先验证在配置文件的正确性，如下表示配置文件正确
    
    promote:bin yangqianhua$ /usr/local/Cellar/nginx/1.8.0/bin/nginx -t -c /usr/local/etc/nginx/nginx.conf
    nginx: the configuration file /usr/local/etc/nginx/nginx.conf syntax is ok
    nginx: configuration file /usr/local/etc/nginx/nginx.conf test is successful
    重启有两种方法
    1）在终端输入输入如下命令即可重启
    
    
    promote:~ yangqianhua$ cd /usr/local/Cellar/nginx/1.8.0/bin/
    promote:bin yangqianhua$ ./nginx -s reload
    promote:bin yangqianhua$ 
    
    2）根据进程号重启，执行命令 kill -HUP 进程号
    
    
    VIDEOJS: ERROR: TypeError: Cannot read property 'duration' of undefined




