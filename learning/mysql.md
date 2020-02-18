-- 对mysql锁还是没有理解透,需要加倍努力
-- 间隙锁-加锁规则
drop table if exists `jfinal_demo`.t;

CREATE TABLE `t` (
  `id` int(11) NOT NULL,
  `c` int(11) DEFAULT NULL,
  `d` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `c` (`c`)
) ENGINE=InnoDB;

insert into t values(0,0,0),(5,5,5),
(10,10,10),(15,15,15),(20,20,20),(25,25,25);


案例一：等值查询间隙锁
sessionA                                                    sessionB                                                 sessionC
begin;
update t set d=d+1 where id=7;

                                                            insert into t values(8,8,8);
                                                            --blocked
                                                                                                                     update t set d=d+1 where id=10
非唯一索引等值锁
sessionA                                                    sessionB                                                 sessionC
begin;
select id from t where c=5 lock in share mode;

                                                            update t set d=d+1 where id=5;

                                                                                                                     insert into t values(7,7,7);
                                                                                                                      --blocked

案例三：主键索引范围锁
sessionA                                                    sessionB                                                 sessionC
begin;
select * from t where id>=10 and id<11 for update;

                                                            insert into t values(8,8,8);
                                                            --ok
                                                            insert values(13,13,13);
                                                            --blocked

                                                                                                                    update t set d=d+1 where id=15;
                                                                                                                    --blocked
案例四：非唯一索引范围锁
sessionA                                                    sessionB                                                 sessionC
begin;
select * from t where id>=10 and id<11 for update;

                                                            insert into t values(8,8,8);
                                                            --blocked



                                                                                                                    update t set d=d+1 where id=15;
                                                                                                                    --blocked
案例五：唯一索引范围锁 bug
sessionA                                                    sessionB                                                 sessionC
begin;
select * from t where id>=10 and id<=15 for update;

                                                            update t set d=d+1 where id=20;
                                                            --blocked



                                                                                                                    insert values(16,16,16);
                                                                                                                    --blocked

案例六：非唯一索引上存在"等值"的例子
sessionA                                                    sessionB                                                 sessionC
begin;
delete from t where c=10;

                                                            insert values(12,12,12);
                                                            --blocked



                                                                                                                    update t set d=d+1 where id=15;
                                                                                                                    --ok

案例七：limit 语句加锁
sessionA                                                    sessionB
begin;
delete from t where c=10 limit 2;

                                                            insert values(12,12,12);
                                                            --ok

案例八：一个死锁的例子
sessionA                                                    sessionB
begin;
select id from t where c=10 lock in share mode;

                                                            update t set d=d+1 where c=10;
                                                            --block

insert into t values(8,8,8);

                                                            -- deadlock

-- 查询具体的锁情况
SELECT * FROM `information_schema`.INNODB_TRX;
-- SELECT * FROM `information_schema`.INNODB_LOCKS;
SELECT * FROM `information_schema`.INNODB_LOCK_WAITS;

SELECT  r.trx_id waiting_trx_id,
r.trx_mysql_thread_id waiting_thread,
r.trx_query waiting_query,
b.trx_id blocking_trx_id,
b.trx_mysql_thread_id blocking_thread,
b.trx_query blocking_query

FROM `information_schema`.INNODB_LOCK_WAITS w
INNER JOIN  `information_schema`.INNODB_TRX b
on b.trx_id=w.blocking_trx_id
INNER JOIN  `information_schema`.INNODB_TRX r
on r.trx_id=w.requesting_trx_id;

