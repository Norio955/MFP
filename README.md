java version "1.8.0_401";  
jetty-11.0.24;  
команда запуску - mvn jetty:run;  
URL DB: "jdbc:h2:file:./data/guest;AUTO_SERVER=TRUE";  
Шлях до файла: ./data/guest.mv.db;  
Список ендпоїнтів:  
GET / - головна сторінка гостьової книги (index.html).  
GET /comments - отримання списку відгуків у форматі JSON.  
POST /comments - додавання нового відгуку.  
