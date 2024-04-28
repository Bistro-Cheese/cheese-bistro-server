#!/bin/bash

#install plugin mysql_clone
install_plugin_mysql_clone='INSTALL PLUGIN clone SONAME "mysql_clone.so";';
#master config script
#Create account with granting privileges required for replication
user_master_with_the_backup_admin_privilege='CREATE USER "donor_clone_user"@"%" IDENTIFIED BY "12345678";GRANT BACKUP_ADMIN on *.* to  "donor_clone_user"@"%";'
sql_slave_user='CREATE USER "repl"@"%" IDENTIFIED WITH mysql_native_password  BY "12345678"; GRANT REPLICATION SLAVE ON *.* TO "repl"@"%"; FLUSH PRIVILEGES;'

echo "Combine command in mysqldb_master: $user_master_with_the_backup_admin_privilege $sql_slave_user $install_plugin_mysql_clone"


docker exec -it mysqldb_master sh -c "mysql -u root -p12345678 -e '$user_master_with_the_backup_admin_privilege $sql_slave_user $install_plugin_mysql_clone'"

#slave config script
#Get the master server's IP address
MASTER_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysqldb_master)
echo "$MASTER_IP"
user_slave_with_the_CLONE_ADMIN_privilege='CREATE USER "recipient_clone_user"@"%" IDENTIFIED BY "12345678";GRANT CLONE_ADMIN on *.* to  "recipient_clone_user"@"%";FLUSH PRIVILEGES;'
global_clone_valid_donor_list='SET GLOBAL clone_valid_donor_list = "mysqldb_master:3306";'

docker exec -it mysqldb_slave sh -c "mysql -u root -p12345678 -e '$user_slave_with_the_CLONE_ADMIN_privilege $install_plugin_mysql_clone $global_clone_valid_donor_list'"

clone_master_instance='CLONE INSTANCE FROM "donor_clone_user"@"mysqldb_master":3306 IDENTIFIED BY "12345678";'
docker exec -it mysqldb_slave sh -c "mysql -u recipient_clone_user -p12345678 -e '$clone_master_instance'"

# Wait for the clone operation to complete
echo "Waiting for clone operation to complete..."
while true; do
    CLONE_STATUS=$(docker exec -it mysqldb_slave sh -c 'mysql -u root -p12345678 -e "SELECT STATE FROM performance_schema.clone_status;"')
    if [[ $CLONE_STATUS == *"Completed"* ]]; then
        echo "Clone operation completed."
        break
    else
        echo "Clone operation in progress..."
        sleep 5 # Wait for 10 seconds before checking the status again
    fi
done

# Ensure MySQL server is running in the slave container
echo "Ensuring MySQL server is running..."
docker exec mysqldb_slave sh -c "mysqladmin ping -uroot -p12345678"

MS_STATUS=`docker exec mysqldb_master sh -c 'mysql -u root -p12345678 -e "SHOW MASTER STATUS"'`
echo "$MS_STATUS"
CURRENT_LOG=`echo $MS_STATUS | awk '{print $6}'`
CURRENT_POS=`echo $MS_STATUS | awk '{print $7}'`

echo "SOURCE_LOG_FILE: $CURRENT_LOG"
echo "SOURCE_LOG_POS: $CURRENT_POS"
sql_set_master='CHANGE REPLICATION SOURCE TO SOURCE_HOST="mysqldb_master",SOURCE_USER="repl",SOURCE_PASSWORD="12345678",SOURCE_LOG_FILE="'$CURRENT_LOG'",SOURCE_LOG_POS='$CURRENT_POS'; START REPLICA;'
echo "start config replica source"
echo "changing replication: $sql_set_master"
#docker exec mysqldb_slave sh -c "$start_slave_cmd"
docker exec -it mysqldb_slave sh -c "mysql -u root -p12345678 -e '$sql_set_master'"

echo "replica completed"
SL_STATUS=$(docker exec -it mysqldb_slave sh -c 'mysql -u root -p12345678  -e "SHOW REPLICA STATUS\G"')

echo "$SL_STATUS"