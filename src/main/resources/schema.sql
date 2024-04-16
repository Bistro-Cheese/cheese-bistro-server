create table address
(
    id        binary(16)   not null
        primary key,
    addr_line varchar(255) null,
    city      varchar(255) null,
    region    varchar(255) null
);

create table category
(
    id         int auto_increment
        primary key,
    created_at timestamp  default CURRENT_TIMESTAMP not null,
    created_by binary(16) default 0x30              null,
    updated_at timestamp  default CURRENT_TIMESTAMP not null,
    updated_by binary(16) default 0x30              null,
    name       varchar(255)                         null
);

create table customer
(
    id          binary(16)                           not null
        primary key,
    created_at  timestamp  default CURRENT_TIMESTAMP not null,
    created_by  binary(16) default 0x30              null,
    updated_at  timestamp  default CURRENT_TIMESTAMP not null,
    updated_by  binary(16) default 0x30              null,
    cus_nme     varchar(255)                         not null,
    cus_type    tinyint                              not null,
    phone_num   varchar(255)                         not null,
    total_spent decimal(38, 2)                       not null,
    visit       int                                  not null
);

create table daily_revenue
(
    id            bigint auto_increment
        primary key,
    date          datetime(6)                 not null,
    revenue       decimal(38, 2) default 0.00 null,
    num_of_orders bigint         default 0    null,
    num_of_cus    bigint         default 0    null,
    constraint date_UNIQUE
        unique (date)
);

create table discount
(
    id         int auto_increment
        primary key,
    end_date   datetime(6)                          null,
    start_date datetime(6)                          null,
    type       tinyint                              not null,
    uses_cnt   int                                  null,
    uses_max   int                                  null,
    value      decimal(38, 2)                       not null,
    is_active  bit                                  not null,
    name       varchar(255)                         not null,
    created_at timestamp  default CURRENT_TIMESTAMP not null,
    created_by binary(16) default 0x30              null,
    updated_at timestamp  default CURRENT_TIMESTAMP not null,
    updated_by binary(16) default 0x30              null
);

create table food
(
    id                 binary(16)                           not null
        primary key,
    created_date       datetime(6)                          null,
    description        varchar(255)                         not null,
    last_modified_date datetime(6)                          null,
    name               varchar(255)                         not null,
    price              decimal(38, 2)                       not null,
    image              varchar(255)                         not null,
    status             int                                  not null,
    category_id        int                                  not null,
    created_at         timestamp  default CURRENT_TIMESTAMP not null,
    created_by         binary(16) default 0x30              null,
    updated_at         timestamp  default CURRENT_TIMESTAMP not null,
    updated_by         binary(16) default 0x30              null,
    constraint key_food_name
        unique (name),
    constraint food_category_fk
        foreign key (category_id) references category (id)
);

create index food_category_id
    on food (category_id);

create table ingredient
(
    id              bigint auto_increment
        primary key,
    created_at      timestamp  default CURRENT_TIMESTAMP not null,
    created_by      binary(16) default 0x30              null,
    updated_at      timestamp  default CURRENT_TIMESTAMP not null,
    updated_by      binary(16) default 0x30              null,
    ingredient_type tinyint                              not null,
    name            varchar(45)                          not null,
    supplier        varchar(255)                         not null,
    unit            varchar(45)                          not null
);

create table inventory
(
    id            binary(16)                           not null
        primary key,
    ingredient_id bigint                               not null,
    total_quan    float                                not null,
    created_at    timestamp  default CURRENT_TIMESTAMP not null,
    created_by    binary(16) default 0x30              null,
    updated_at    timestamp  default CURRENT_TIMESTAMP not null,
    updated_by    binary(16) default 0x30              null,
    constraint inventory_ingredient_id
        foreign key (ingredient_id) references ingredient (id)
);

create index inventory_ingredient_id_idx
    on inventory (ingredient_id);

create definer = root@localhost trigger trg_after_inventory_insert
    after insert
    on inventory
    for each row
BEGIN

    DECLARE ingre_name VARCHAR(255);
    DECLARE ingre_supplier VARCHAR(255);
    DECLARE ingre_unit VARCHAR(45);

    SELECT name, supplier, unit into ingre_name, ingre_supplier, ingre_unit FROM ingredient WHERE id = NEW.ingredient_id;

    INSERT INTO inventory_report(`ingredient_name`, `supplier`, `unit`, `quantity`, `import_quantity`, `export_quantity`, `operation_date`)
    VALUES (ingre_name, ingre_supplier, ingre_unit, 0, 0, 0, NEW.created_at);
END;

create table inventory_report
(
    id              int auto_increment
        primary key,
    ingredient_name varchar(255) not null,
    supplier        varchar(255) not null,
    unit            varchar(45)  not null,
    quantity        float        not null,
    import_quantity double       not null,
    export_quantity double       not null,
    operation_date  date         null,
    constraint unique_inventory_report_key
        unique (ingredient_name, supplier, operation_date)
);

create table monthly_revenue
(
    id            int auto_increment
        primary key,
    year          int                         not null,
    month         int                         not null,
    revenue       decimal(38, 2) default 0.00 null,
    num_of_orders bigint         default 0    null,
    num_of_cus    bigint         default 0    null,
    constraint unique_month
        unique (year, month)
);

create table operation
(
    id           binary(16)                           not null
        primary key,
    created_at   timestamp  default CURRENT_TIMESTAMP not null,
    created_by   binary(16) default 0x30              null,
    updated_at   timestamp  default CURRENT_TIMESTAMP not null,
    updated_by   binary(16) default 0x30              null,
    quantity     double                               not null,
    type         tinyint                              not null,
    inventory_id binary(16)                           not null,
    constraint operation_inven_fk
        foreign key (inventory_id) references inventory (id),
    check (`type` between 0 and 1)
);

create index operation_inven_key
    on operation (inventory_id);

create definer = root@localhost trigger trg_after_export_operation
    after insert
    on operation
    for each row
BEGIN
    DECLARE ingre_name VARCHAR(255);
    DECLARE ingre_supplier VARCHAR(255);
    DECLARE ingre_unit VARCHAR(45);

    select name, supplier, unit into ingre_name, ingre_supplier, ingre_unit FROM ingredient
    WHERE id = (select ingredient_id from inventory where id = new.inventory_id  );

    -- Update inventory_report for import operation
    IF NEW.type = 1 THEN
        INSERT INTO inventory_report(`ingredient_name`, `supplier`, `unit`, `quantity`, `import_quantity`, `export_quantity`, `operation_date`)
        VALUES (ingre_name, ingre_supplier, ingre_unit, quantity = quantity - NEW.quantity, 0, NEW.quantity, new.created_at)
        ON DUPLICATE KEY UPDATE
                             quantity = quantity - NEW.quantity, export_quantity = export_quantity + NEW.quantity;
    END IF;
END;

create definer = root@localhost trigger trg_after_import_operation
    after insert
    on operation
    for each row
BEGIN
    DECLARE ingre_name VARCHAR(255);
    DECLARE ingre_supplier VARCHAR(255);
    DECLARE ingre_unit VARCHAR(45);

    select name, supplier, unit into ingre_name, ingre_supplier, ingre_unit FROM ingredient
    WHERE id = (select ingredient_id from inventory where id = new.inventory_id  );

    -- Update inventory_report for import operation
    IF NEW.type = 0 THEN
        INSERT INTO inventory_report(`ingredient_name`, `supplier`, `unit`, `quantity`, `import_quantity`, `export_quantity`, `operation_date`)
        VALUES (ingre_name, ingre_supplier, ingre_unit, quantity = quantity + NEW.quantity, NEW.quantity, 0, new.created_at)
        ON DUPLICATE KEY UPDATE
                             quantity = quantity + NEW.quantity, import_quantity = import_quantity + NEW.quantity;
    END IF;
END;

create table res_table
(
    id       int auto_increment
        primary key,
    seat_num int     not null,
    tab_num  int     null,
    status   tinyint not null,
    check (`status` between 0 and 2)
);

create table template
(
    id      int auto_increment
        primary key,
    code    varchar(255) not null,
    content varchar(255) not null,
    name    varchar(255) not null,
    title   varchar(255) not null,
    type    int          not null
);

create table transfer_method
(
    id              int auto_increment
        primary key,
    acc_holder_name varchar(255)                         not null,
    acc_num         varchar(255)                         not null,
    is_active       bit                                  not null,
    method_name     varchar(255)                         not null,
    method_type     tinyint                              not null,
    created_at      timestamp  default CURRENT_TIMESTAMP not null,
    created_by      binary(16) default 0x30              null,
    updated_at      timestamp  default CURRENT_TIMESTAMP not null,
    updated_by      binary(16) default 0x30              null,
    check (`method_type` between 0 and 1)
);

create table payment
(
    id         binary(16)                           not null
        primary key,
    type       tinyint                              not null,
    created_at timestamp  default CURRENT_TIMESTAMP not null,
    created_by binary(16) default 0x30              null,
    updated_at timestamp  default CURRENT_TIMESTAMP not null,
    updated_by binary(16) default 0x30              null,
    method_id  int                                  null,
    constraint payment_method_fk
        foreign key (method_id) references transfer_method (id)
);

create index key_payment_method
    on payment (method_id);

create table user
(
    id         binary(16)                           not null
        primary key,
    created_at timestamp  default CURRENT_TIMESTAMP not null,
    created_by binary(16) default 0x30              null,
    updated_at timestamp  default CURRENT_TIMESTAMP not null,
    updated_by binary(16) default 0x30              null,
    avt        varchar(255)                         null,
    dob        datetime(6)                          null,
    email      varchar(255)                         null,
    enabled    bit                                  not null,
    fst_name   varchar(255)                         null,
    lst_name   varchar(255)                         not null,
    password   varchar(255)                         null,
    phone_num  varchar(255)                         null,
    role       int                                  not null,
    status     int                                  null,
    username   varchar(255)                         null,
    addr_id    binary(16)                           null,
    constraint user_addr_fk
        foreign key (addr_id) references address (id),
    check (`role` between 0 and 2)
);

create table manager
(
    certi_management varchar(255) not null,
    ex_y             varchar(255) not null,
    frg_lg           varchar(255) not null,
    id               binary(16)   not null
        primary key,
    constraint manager_user_fk
        foreign key (id) references user (id)
);

create table owner
(
    branch         varchar(255) not null,
    licen_business varchar(255) not null,
    id             binary(16)   not null
        primary key,
    constraint owner_user_fk
        foreign key (id) references user (id)
);

create table staff
(
    acdmic_lv varchar(255) null,
    frg_lg    varchar(255) null,
    id        binary(16)   not null
        primary key,
    constraint staff_user_fk
        foreign key (id) references user (id)
);

create table res_order
(
    id                 binary(16)                           not null
        primary key,
    status             tinyint                              null,
    table_id           int                                  null,
    staff_id           binary(16)                           null,
    created_at         timestamp  default CURRENT_TIMESTAMP not null,
    created_by         binary(16) default 0x30              null,
    updated_at         timestamp  default CURRENT_TIMESTAMP not null,
    updated_by         binary(16) default 0x30              null,
    number_of_customer int                                  not null,
    deposit            decimal(38, 2)                       null,
    total              decimal(38, 2)                       null,
    cus_in             datetime(6)                          null,
    sub_total          decimal(38, 2)                       null,
    cus_id             binary(16)                           null,
    discount_id        int                                  null,
    constraint order_cus_fk
        foreign key (cus_id) references customer (id),
    constraint order_discount_fk
        foreign key (discount_id) references discount (id),
    constraint orders_staff_fk
        foreign key (staff_id) references staff (id),
    constraint orders_table_fk
        foreign key (table_id) references res_table (id)
);

create table bill
(
    id          binary(16)     not null
        primary key,
    change_paid decimal(38, 2) null,
    cus_in      datetime(6)    null,
    cus_out     datetime(6)    null,
    paid        decimal(38, 2) null,
    total       decimal(38, 2) null,
    order_id    binary(16)     null,
    pay_id      binary(16)     null,
    constraint bill_orders_key
        unique (order_id),
    constraint bill_orders_fk
        foreign key (pay_id) references payment (id),
    constraint bill_pay_fk
        foreign key (order_id) references res_order (id)
);

create index bill_pay_key
    on bill (pay_id);

create definer = root@localhost trigger trg_after_bill_insert
    after insert
    on bill
    for each row
BEGIN
    DECLARE order_visit BIGINT;

    select number_of_customer into order_visit from restaurant_management.res_order where res_order.id = NEW.order_id;

    INSERT INTO daily_revenue (`date`, `revenue`, `num_of_orders`, `num_of_cus`)
    VALUES (DATE(NEW.cus_out), NEW.total, 1, COALESCE(order_visit, 1))
    ON DUPLICATE KEY UPDATE revenue = revenue + NEW.total, num_of_orders = num_of_orders + 1, num_of_cus = num_of_cus + COALESCE(order_visit, 1);

    INSERT INTO monthly_revenue (year, month, revenue, num_of_orders, num_of_cus)
    VALUES (YEAR(NEW.cus_in), MONTH(NEW.cus_in), NEW.total, 1, COALESCE(order_visit, 1))
    ON DUPLICATE KEY UPDATE revenue = revenue + NEW.total, num_of_orders = num_of_orders + 1, num_of_cus = num_of_cus + COALESCE(order_visit, 1);

    INSERT INTO yearly_revenue (year, revenue, num_of_orders, num_of_cus)
    VALUES (YEAR(NEW.cus_in), NEW.total, 1, COALESCE(order_visit, 1))
    ON DUPLICATE KEY UPDATE revenue = revenue + NEW.total, num_of_orders = num_of_orders + 1, num_of_cus = num_of_cus + COALESCE(order_visit, 1);
END;

create table order_line
(
    id       binary(16) not null
        primary key,
    quantity int        null,
    food_id  binary(16) null,
    order_id binary(16) null,
    constraint o_line_food_fk
        foreign key (food_id) references food (id),
    constraint o_line_orders_fk
        foreign key (order_id) references res_order (id)
);

create index o_line_food_key
    on order_line (food_id);

create index o_line_orders_key
    on order_line (order_id);

create index orders_staff_id
    on res_order (staff_id);

create index orders_table_id
    on res_order (table_id);

create table schedule
(
    id         bigint                               not null
        primary key,
    created_at timestamp  default CURRENT_TIMESTAMP not null,
    created_by binary(16) default 0x30              null,
    updated_at timestamp  default CURRENT_TIMESTAMP not null,
    updated_by binary(16) default 0x30              null,
    staff_id   binary(16)                           not null,
    constraint schedule_staff_fk
        foreign key (staff_id) references staff (id)
);

create index schedule_staff_id
    on schedule (staff_id);

create table schedule_line
(
    id             bigint                               not null
        primary key,
    created_at     timestamp  default CURRENT_TIMESTAMP not null,
    created_by     binary(16) default 0x30              null,
    updated_at     timestamp  default CURRENT_TIMESTAMP not null,
    updated_by     binary(16) default 0x30              null,
    shift          tinyint                              not null,
    status         tinyint                              null,
    timekeeping_by binary(16)                           null,
    work_date      datetime(6)                          not null,
    schedule_id    bigint                               not null,
    constraint line_schedule_fk
        foreign key (schedule_id) references schedule (id),
    check (`shift` between 0 and 2)
);

create index line_schedule_id
    on schedule_line (schedule_id);

create table token
(
    id         binary(16)      not null
        primary key,
    expired    bit             null,
    revoked    bit             null,
    token      varchar(255)    null,
    token_type enum ('BEARER') null,
    usr_id     binary(16)      null,
    constraint token_user_fk
        foreign key (usr_id) references user (id)
);

create index token_user_id
    on token (usr_id);

create table yearly_revenue
(
    id            int auto_increment
        primary key,
    year          int                         not null,
    revenue       decimal(38, 2) default 0.00 null,
    num_of_orders bigint         default 0    null,
    num_of_cus    bigint         default 0    null,
    constraint unique_month
        unique (year)
);

