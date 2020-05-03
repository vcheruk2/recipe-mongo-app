#Start the container
#docker run --name recipe_mysql -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql

#Create Databases
CREATE DATABASE recipe_dev;
CREATE DATABASE recipe_prod;

#Create database service accounts
CREATE USER 'dev_user'@'%' IDENTIFIED BY 'dev_pass';
CREATE USER 'prod_user'@'%' IDENTIFIED BY 'prod_pass';

#Database grants
GRANT SELECT ON recipe_dev.* to 'dev_user'@'%';
GRANT INSERT ON recipe_dev.* to 'dev_user'@'%';
GRANT UPDATE ON recipe_dev.* to 'dev_user'@'%';
GRANT DELETE ON recipe_dev.* to 'dev_user'@'%';
GRANT SELECT ON recipe_prod.* to 'prod_user'@'%';
GRANT INSERT ON recipe_prod.* to 'prod_user'@'%';
GRANT UPDATE ON recipe_prod.* to 'prod_user'@'%';
GRANT DELETE ON recipe_prod.* to 'prod_user'@'%';
