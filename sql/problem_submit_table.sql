create table if not exists problem_submit
(
    id            bigint auto_increment comment 'id' primary key,
    problemId     bigint                             not null comment '题目 id',
    userId        bigint                             not null comment '提交用户 id',
    language      varchar(128)                       not null comment '语言',
    code          text                               not null comment '提交代码',
    status        tinyint                            not null comment '状态 （0: 等待判题； 1: 判题中； 2: 成功； 3： 失败）',
    judgeResult   text                               not null comment '判题结果 （json对象）',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '提交时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除',
    index idx_postId (problemId),
    index idx_userId (userId)
) comment '题目提交表';