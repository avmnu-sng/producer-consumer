drop table if exists `analytics`;

create table `analytics` (
    `id` bigint not null auto_increment primary key,
    `worker_type` enum('producer', 'consumer') not null,
    `worker_id` varchar(36) not null,
    `value` int not null,
    `created_at` datetime default null
);

create index `index_on_worker_id` on `analytics` (`worker_id`);
create index `index_on_value` on `analytics` (`value`) using hash;
create index `index_on_created_at` on `analytics` (`created_at`);
