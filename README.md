```
https://www.baeldung.com/debezium-intro

INSERT INTO customer (id, fullname, email) VALUES (1, 'John Doe', 'jd@example.com')
```

```
mysql> select * from debezium_offsets;
+----------------------------------------------------------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| ke                                                                                           | va                                                                                                                                                                               |
+----------------------------------------------------------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| {"schema":null,"payload":["customer-mysql-connector",{"server":"customer-mysql-db-server"}]} | 0x7B2274735F736563223A313639343634303134372C2266696C65223A2262696E6C6F672E303030303032222C22706F73223A373332332C22726F77223A312C227365727665725F6964223A312C226576656E74223A327D |
+----------------------------------------------------------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
1 row in set (0.00 sec)
```