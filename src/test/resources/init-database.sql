set foreign_key_checks = 0;

truncate table balance;
truncate table category;
truncate table daily_report;
truncate table expense;
truncate table monthly_report;
truncate table order_item;
truncate table orders;
truncate table organization;
truncate table payment;
truncate table product;
truncate table product_variant;
truncate table queue_info;
truncate table review;
truncate table role;
truncate table shipping;
truncate table user;
truncate table wish_list;
truncate table yearly_report;

set foreign_key_checks = 1;