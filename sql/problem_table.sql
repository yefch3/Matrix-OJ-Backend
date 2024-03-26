create table if not exists problem
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    difficulty  enum ('Easy', 'Medium', 'Hard')    null comment '难度',
    answer      text                               null comment '答案',
    submitNum   int      default 0                 not null comment '提交数',
    acceptNum   int      default 0                 not null comment '通过数',
    judgeCase   text                               null comment '评测用例 (json 数组)',
    judgeConfig text                              null comment '评测配置 (json 对象)',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;