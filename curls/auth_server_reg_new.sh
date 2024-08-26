curl -v localhost:8080/register/new \
--data-raw '{"username": "paul", "password": "password"}' \
-H 'content-type: application/json' \
-H 'origin: http://127.0.0.1:19006' \
-H 'sec-fetch-mode: cors' \
-H 'sec-fetch-site: cross-site' \
-H 'Authorization: Basic d2ctcGxhbmVyOnNlY3JldA==' \
-H 'user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36' 

#curl -v  https://wg-planer-auth-server.onrender.com/register/new \
#--data-raw '{"username": "usernamexxxxx", "password": "password"}' \
#-H 'content-type: application/json' \
#-H 'origin: http://127.0.0.1:19006' \
#-H 'sec-fetch-mode: cors' \
#-H 'sec-fetch-site: cross-site' \
#-H 'Authorization: Basic d2ctcGxhbmVyOnNlY3JldA==' \
#-H 'user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36' 
