drop table if exists `requests`;

create table `requests` (
    `id` bigint not null auto_increment primary key,
    `worker_type` enum('producer', 'consumer') not null,
    `worker_id` varchar(36) not null,
    `created_at` datetime default null
);

create index `index_on_worker_id` on `requests` (`worker_id`);
create index `index_on_created_at` on `requests` (`created_at`);
