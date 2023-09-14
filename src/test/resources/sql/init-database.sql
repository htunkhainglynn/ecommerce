set foreign_key_checks = 0;

truncate table user;
truncate table role;
truncate table address;
truncate table product;
truncate table organization;
truncate table review;
truncate table wish_list;
truncate table wish_list_product;
truncate table product_variant;
truncate table orders;
truncate table order_item;
truncate table balance;
truncate table daily_report;
truncate table expense;
truncate table monthly_report;
truncate table queue_info;
truncate table yearly_report;

set foreign_key_checks = 1;