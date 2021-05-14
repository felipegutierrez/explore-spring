

 - Start the mysql server: `docker-compose up`
 - Connect to the mysql server: `mysql -h 127.0.0.1 -u user -P 3306 -p`
 - Upload data to MySQL: 
   ```
   mysql -h 127.0.0.1 -u user -P 3306 -p < spring-web/scripts/hplus_user.sql
   mysql -h 127.0.0.1 -u user -P 3306 -p < spring-web/scripts/hplus_product.sql
   ```


