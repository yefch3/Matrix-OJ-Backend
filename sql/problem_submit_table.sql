create table if not exists problem_submit
(
    id            bigint auto_increment comment 'id' primary key,
    problemId     bigint                             not null comment '题目 id',
    userId        bigint                             not null comment '提交用户 id',
    language      varchar(128)                       not null comment '语言',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '提交时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除',
    index idx_postId (problemId),
    index idx_userId (userId)
) comment '帖子点赞';