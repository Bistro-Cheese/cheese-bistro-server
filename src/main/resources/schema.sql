-- user domain --
CREATE TABLE if not exists `template` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `code` varchar(255) NOT NULL,
                            `content` varchar(255) NOT NULL,
                            `name` varchar(255) NOT NULL,
                            `title` varchar(255) NOT NULL,
                            `type` int NOT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- address --
CREATE TABLE if not exists `address`  (
                                          `id` binary(16) NOT NULL,
                                          `addr_line` varchar(255) DEFAULT NULL,
                                          `city` varchar(255) DEFAULT NULL,
                                          `region` varchar(255) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- user--
CREATE TABLE if not exists `user` (
                                      `id` binary(16) NOT NULL,
                                      `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                      `avt` varchar(255) DEFAULT NULL,
                                      `dob` datetime(6) DEFAULT NULL,
                                      `email` varchar(255) DEFAULT NULL,
                                      `enabled` bit(1) NOT NULL,
                                      `fst_name` varchar(255) DEFAULT NULL,
                                      `lst_name` varchar(255) NOT NULL,
                                      `password` varchar(255) DEFAULT NULL,
                                      `phone_num` varchar(255) DEFAULT NULL,
                                      `role` int NOT NULL,
                                      `status` int DEFAULT NULL,
                                      `username` varchar(255) DEFAULT NULL,
                                      `addr_id` binary(16) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      CONSTRAINT `user_addr_fk` FOREIGN KEY (`addr_id`) REFERENCES `address` (`id`),
                                      CONSTRAINT `user_chk_1` CHECK ((`role` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- token --
CREATE TABLE if not exists `token` (
                                       `id` binary(16) NOT NULL,
                                       `expired` bit(1) DEFAULT NULL,
                                       `revoked` bit(1) DEFAULT NULL,
                                       `token` varchar(255) DEFAULT NULL,
                                       `token_type` enum('BEARER') DEFAULT NULL,
                                       `usr_id` binary(16) DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       KEY `token_user_id` (`usr_id`),
                                       CONSTRAINT `token_user_fk` FOREIGN KEY (`usr_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- staff --
CREATE TABLE if not exists `staff` (
                                       `acdmic_lv` varchar(255) DEFAULT NULL,
                                       `frg_lg` varchar(255) DEFAULT NULL,
                                       `id` binary(16) NOT NULL,
                                       PRIMARY KEY (`id`),
                                       CONSTRAINT `staff_user_fk` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- owner --
CREATE TABLE if not exists `owner` (
                                       `branch` varchar(255) NOT NULL,
                                       `licen_business` varchar(255) NOT NULL,
                                       `id` binary(16) NOT NULL,
                                       PRIMARY KEY (`id`),
                                       CONSTRAINT `owner_user_fk` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- manager --
CREATE TABLE if not exists `manager` (
                                         `certi_managment` varchar(255) NOT NULL,
                                         `ex_y` varchar(255) NOT NULL,
                                         `frg_lg` varchar(255) NOT NULL,
                                         `id` binary(16) NOT NULL,
                                         PRIMARY KEY (`id`),
                                         CONSTRAINT `manager_user_fk` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE if not exists `schedule` (
                                          `id` bigint NOT NULL,
                                          `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                          `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                          `staff_id` binary(16) NOT NULL,
                                          PRIMARY KEY (`id`),
                                          KEY `schedule_staff_id` (`staff_id`),
                                          CONSTRAINT `schedule_staff_fk` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- schedule_line --
CREATE TABLE if not exists `schedule_line` (
                                               `id` bigint NOT NULL,
                                               `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                               `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                               `shift` tinyint NOT NULL,
                                               `status` tinyint DEFAULT NULL,
                                               `timekeeping_by` binary(16) DEFAULT NULL,
                                               `work_date` datetime(6) NOT NULL,
                                               `schedule_id` bigint NOT NULL,
                                               PRIMARY KEY (`id`),
                                               KEY `line_schedule_id` (`schedule_id`),
                                               CONSTRAINT `line_schedule_fk` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`),
                                               CONSTRAINT `schedule_line_chk_1` CHECK ((`shift` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- user domain --

-- food domain --
-- category --
CREATE TABLE if not exists `category` (
                                          `id` int NOT NULL AUTO_INCREMENT,
                                          `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                          `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                          `name` varchar(255) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- food --
CREATE TABLE if not exists `food` (
                                      `id` binary(16) NOT NULL,
                                      `created_date` datetime(6) DEFAULT NULL,
                                      `description` varchar(255) NOT NULL,
                                      `last_modified_date` datetime(6) DEFAULT NULL,
                                      `name` varchar(255) NOT NULL,
                                      `price` decimal(38,2) NOT NULL,
                                      `image` varchar(255) NOT NULL,
                                      `status` int NOT NULL,
                                      `category_id` int NOT NULL,
                                      `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `key_food_name` (`name`),
                                      KEY `food_category_id` (`category_id`),
                                      CONSTRAINT `food_category_fk` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- food domain --

-- order domain --

-- table --
CREATE TABLE if not exists `res_table` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `seat_num` int NOT NULL,
                                           `tab_num` int DEFAULT NULL,
                                           `status` tinyint NOT NULL,
                                           PRIMARY KEY (`id`),
                                           CONSTRAINT `res_table_chk_1` CHECK ((`status` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




-- order --
CREATE TABLE if not exists `res_order` (
                                           `id` binary(16) NOT NULL,
                                           `phone_cus` varchar(255) DEFAULT NULL,
                                           `status` tinyint DEFAULT NULL,
                                           `table_id` int DEFAULT NULL,
                                           `staff_id` binary(16) DEFAULT NULL,
                                           `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                           `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                           PRIMARY KEY (`id`),
                                           KEY `orders_table_id` (`table_id`),
                                           KEY `orders_staff_id` (`staff_id`),
                                           CONSTRAINT `orders_staff_fk` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`),
                                           CONSTRAINT `orders_table_fk` FOREIGN KEY (`table_id`) REFERENCES `res_table` (`id`),
                                           CONSTRAINT `orders_chk_1` CHECK ((`status` between 0 and 3))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- order line --
CREATE TABLE if not exists `order_line` (
                                            `id` binary(16) NOT NULL,
                                            `quantity` int DEFAULT NULL,
                                            `food_id` binary(16) DEFAULT NULL,
                                            `order_id` binary(16) DEFAULT NULL,
                                            PRIMARY KEY (`id`),
                                            KEY `o_line_food_key` (`food_id`),
                                            KEY `o_line_orders_key` (`order_id`),
                                            CONSTRAINT `o_line_food_fk` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`),
                                            CONSTRAINT `o_line_orders_fk` FOREIGN KEY (`order_id`) REFERENCES `res_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE if not exists `transfer_method` (
                                                 `id` int NOT NULL AUTO_INCREMENT,
                                                 `acc_holder_name` varchar(255) NOT NULL,
                                                 `acc_num` varchar(255) NOT NULL,
                                                 `is_active` bit(1) NOT NULL,
                                                 `method_name` varchar(255) NOT NULL,
                                                 `method_type` tinyint NOT NULL,
                                                 `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                                 `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                                 PRIMARY KEY (`id`),
                                                 CONSTRAINT `transfer_method_chk_1` CHECK ((`method_type` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE if not exists `payment` (
                                         `id` binary(16) NOT NULL,
                                         `type` tinyint NOT NULL,
                                         `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                         `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                         `cus_nme` varchar(255) NOT NULL,
                                         `phone_num` varchar(255) NOT NULL,
                                         `method_id` int DEFAULT NULL,
                                         PRIMARY KEY (`id`),
                                         KEY `key_payment_method` (`method_id`),
                                         CONSTRAINT `payment_method_fk` FOREIGN KEY (`method_id`) REFERENCES `transfer_method` (`id`),
                                         CONSTRAINT `payment_chk_1` CHECK ((`type` between 0 and 7))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE if not exists `discount` (
                                          `id` int NOT NULL AUTO_INCREMENT,
                                          `code` varchar(45) NOT NULL,
                                          `end_date` datetime(6) DEFAULT NULL,
                                          `start_date` datetime(6) DEFAULT NULL,
                                          `type` tinyint NOT NULL,
                                          `uses_cnt` int DEFAULT NULL,
                                          `uses_max` int DEFAULT NULL,
                                          `value` decimal(38,2) NOT NULL,
                                          `is_active` bit(1) NOT NULL,
                                          `name` varchar(255) NOT NULL,
                                          `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                          `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE if not exists `bill` (
                                      `id` binary(16) NOT NULL,
                                      `change_paid` decimal(38,2) DEFAULT NULL,
                                      `cus_in` datetime(6) DEFAULT NULL,
                                      `cus_out` datetime(6) DEFAULT NULL,
                                      `paid` decimal(38,2) DEFAULT NULL,
                                      `sub_total` decimal(38,2) DEFAULT NULL,
                                      `total` decimal(38,2) DEFAULT NULL,
                                      `discount_id` int DEFAULT NULL,
                                      `order_id` binary(16) DEFAULT NULL,
                                      `pay_id` binary(16) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `bill_orders_key` (`order_id`),
                                      KEY `bill_discount_key` (`discount_id`),
                                      KEY `bill_pay_key` (`pay_id`),
                                      CONSTRAINT `bill_orders_fk` FOREIGN KEY (`pay_id`) REFERENCES `payment` (`id`),
                                      CONSTRAINT `bill_discount_fk` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`id`),
                                      CONSTRAINT `bill_pay_fk` FOREIGN KEY (`order_id`) REFERENCES `res_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- order domain --

-- inventory domain --
CREATE TABLE if not exists `ingredient` (
                                            `id` bigint NOT NULL auto_increment,
                                            `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                            `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                            `ingredient_type` tinyint NOT NULL,
                                            `name` varchar(45) NOT NULL,
                                            `supplier` varchar(255) NOT NULL,
                                            `unit` varchar(45) NOT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE if not exists `inventory` (
                                           `id` binary(16) NOT NULL,
                                           `ingredient_id` bigint NOT NULL,
                                           `total_quan` float NOT NULL,
                                           `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                           `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                           PRIMARY KEY (`id`),
                                           KEY `inventory_ingredient_id_idx` (`ingredient_id`),
                                           CONSTRAINT `inventory_ingredient_id` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredient` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE if not exists `operation` (
                                           `id` binary(16) NOT NULL,
                                           `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           `created_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                           `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           `updated_by` binary(16) DEFAULT '0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',
                                           `quantity` double NOT NULL,
                                           `type` tinyint NOT NULL,
                                           `inventory_id` binary(16) NOT NULL,
                                           PRIMARY KEY (`id`),
                                           KEY `operation_inven_key` (`inventory_id`),
                                           CONSTRAINT `operation_inven_fk` FOREIGN KEY (`inventory_id`) REFERENCES `inventory` (`id`),
                                           CONSTRAINT `operation_chk_1` CHECK ((`type` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- inventory domain --

-- daily revenue --

CREATE TABLE if not exists `daily_revenue`
(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `date` datetime(6) NOT NULL,
    `revenue` decimal(38,2) DEFAULT NULL,
    `num_of_orders` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `daily_key` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create trigger report daily revenue
-- Insert Bill Trigger --
# DROP TRIGGER IF EXISTS trg_after_bill_insert;
# CREATE TRIGGER trg_after_bill_insert
#     AFTER INSERT ON bill
#     FOR EACH ROW
# BEGIN
#     DECLARE revenue_exists INT;
#     DECLARE total_amount DECIMAL(38,2);
#
#     -- Check if a record already exists for the current date in daily_revenue
#     SELECT COUNT(*) INTO revenue_exists FROM daily_revenue WHERE `date` = DATE(NEW.cus_out);
#
#     -- If a record exists, update it, otherwise insert a new record
#     IF revenue_exists > 0 THEN
#         -- Update existing record
#         UPDATE daily_revenue
#         SET `revenue` = `revenue` + NEW.total,
#             `num_of_orders` = `num_of_orders` + 1
#         WHERE `date` = DATE(NEW.cus_out);
#     ELSE
#         -- Insert a new record
#         INSERT INTO daily_revenue (`date`, `revenue`, `num_of_orders`)
#         VALUES (DATE(NEW.cus_out), NEW.total, 1);
#     END IF;
# END;
#
#
# -- Delete Bill Trigger --
# DROP TRIGGER IF EXISTS trg_after_bill_delete;
# CREATE TRIGGER trg_after_bill_delete
#     AFTER DELETE ON bill
#     FOR EACH ROW
# BEGIN
#     DECLARE revenue_exists INT;
#     DECLARE total_amount DECIMAL(38,2);
#
#     -- Check if a record already exists for the current date in daily_revenue
#     SELECT COUNT(*) INTO revenue_exists FROM daily_revenue WHERE `date` = DATE(OLD.cus_out);
#
#     -- If a record exists, update it, otherwise do nothing (as it shouldn't happen)
#     IF revenue_exists > 0 THEN
#         -- Update existing record
#         UPDATE daily_revenue
#         SET `revenue` = `revenue` - OLD.total,
#             `num_of_orders` = `num_of_orders` - 1
#         WHERE `date` = DATE(OLD.cus_out);
#     END IF;
# END;
#
#
#
# -- UPDATE BILL TRIGGER
# DROP TRIGGER IF EXISTS trg_after_bill_update;
# CREATE TRIGGER trg_after_bill_update
#     AFTER UPDATE ON bill
#     FOR EACH ROW
# BEGIN
#     DECLARE revenue_exists INT;
#     DECLARE total_amount DECIMAL(38,2);
#
#     -- Check if a record already exists for the current date in daily_revenue
#     SELECT COUNT(*) INTO revenue_exists FROM daily_revenue WHERE `date` = DATE(OLD.cus_out);
#
#     -- If a record exists, update it, otherwise do nothing (as it shouldn't happen)
#     IF revenue_exists > 0 THEN
#         -- Update existing record
#         UPDATE daily_revenue
#         SET `revenue` = `revenue` - OLD.total
#         WHERE `date` = DATE(OLD.cus_out);
#
#         UPDATE daily_revenue
#         SET `revenue` = `revenue` + NEW.total
#         WHERE `date` = DATE(OLD.cus_out);
#
#     END IF;
# END;

