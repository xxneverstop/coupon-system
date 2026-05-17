-- 1. 检查 user_coupon 是否存在重复 user_id + activity_id
select user_id, activity_id, count(*) as cnt
from user_coupon
group by user_id, activity_id
having count(*) > 1;

-- 2. 删除重复记录，仅保留 id 最小的一条
delete uc
from user_coupon uc
join (
    select user_id, activity_id, min(id) as keep_id
    from user_coupon
    group by user_id, activity_id
    having count(*) > 1
) d
  on uc.user_id = d.user_id
 and uc.activity_id = d.activity_id
 and uc.id <> d.keep_id;

-- 3. 增加唯一索引
alter table user_coupon
add unique key uk_user_activity (user_id, activity_id);
