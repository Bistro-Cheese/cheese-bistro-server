create trigger trg_after_bill_delete
    after delete
    on bill
    for each row
BEGIN
    DECLARE revenue_exists INT;
    DECLARE total_amount DECIMAL(38,2);

    -- Check if a record already exists for the current date in daily_revenue
    SELECT COUNT(*) INTO revenue_exists FROM daily_revenue WHERE `date` = DATE(OLD.cus_out);

    -- If a record exists, update it, otherwise do nothing (as it shouldn't happen)
    IF revenue_exists > 0 THEN
        -- Update existing record
        UPDATE daily_revenue
        SET `revenue` = `revenue` - OLD.total,
            `num_of_orders` = `num_of_orders` - 1
        WHERE `date` = DATE(OLD.cus_out);
    END IF;
END;

create trigger trg_after_bill_insert
    after insert
    on bill
    for each row
BEGIN
    DECLARE revenue_exists INT;
    DECLARE total_amount DECIMAL(38,2);

    -- Check if a record already exists for the current date in daily_revenue
    SELECT COUNT(*) INTO revenue_exists FROM daily_revenue WHERE `date` = DATE(NEW.cus_out);

    -- If a record exists, update it, otherwise insert a new record
    IF revenue_exists > 0 THEN
        -- Update existing record
        UPDATE daily_revenue
        SET `revenue` = `revenue` + NEW.total,
            `num_of_orders` = `num_of_orders` + 1
        WHERE `date` = DATE(NEW.cus_out);
    ELSE
        -- Insert a new record
        INSERT INTO daily_revenue (`date`, `revenue`, `num_of_orders`)
        VALUES (DATE(NEW.cus_out), NEW.total, 1);
    END IF;
END;

create trigger trg_after_bill_update
    after update
    on bill
    for each row
BEGIN
    DECLARE revenue_exists INT;
    DECLARE total_amount DECIMAL(38,2);

    -- Check if a record already exists for the current date in daily_revenue
    SELECT COUNT(*) INTO revenue_exists FROM daily_revenue WHERE `date` = DATE(OLD.cus_out);

    -- If a record exists, update it, otherwise do nothing (as it shouldn't happen)
    IF revenue_exists > 0 THEN
        -- Update existing record
        UPDATE daily_revenue
        SET `revenue` = `revenue` - OLD.total
        WHERE `date` = DATE(OLD.cus_out);

        UPDATE daily_revenue
        SET `revenue` = `revenue` + NEW.total
        WHERE `date` = DATE(OLD.cus_out);

    END IF;
END;