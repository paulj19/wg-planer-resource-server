#curl -v https://wg-planer-resource-server.onrender.com/register/new \
curl -v  http://localhost:8081/register/new \
	--data-raw '{"username": "Pauloykti", "password": "Password123!", "email": "wildromer999@gmail.com", "oid":null, "authServer":"HOME_BREW"}' \
	-H 'content-type: application/json' \
	    -H 'Accept-Language: en-US,en;q=0.9,de-DE;q=0.8,de;q=0.7' \
	      -H 'Authorization: Basic d2ctcGxhbmVyOnNlY3JldA==' \
	        -H 'Connection: keep-alive' \
		    -H 'Origin: http://127.0.0.1:19006' \
		      -H 'Referer: http://127.0.0.1:19006/' \
		        -H 'Sec-Fetch-Dest: empty' \
			  -H 'Sec-Fetch-Mode: cors' \
			    -H 'Sec-Fetch-Site: cross-site' \
			      -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36' \
			        -H 'sec-ch-ua: "Chromium";v="110", "Not A(Brand";v="24", "Google Chrome";v="110"' \
				  -H 'sec-ch-ua-mobile: ?0' \
				    -H 'sec-ch-ua-platform: "Linux"' 


#curl 'http://116.203.96.104:8080//register/new' \
#	  -X 'OPTIONS' \
#	    -H 'Accept: */*' \
#	      -H 'Accept-Language: en-US,en;q=0.9,de-DE;q=0.8,de;q=0.7' \
#	        -H 'Access-Control-Request-Headers: authorization,content-type' \
#		  -H 'Access-Control-Request-Method: POST' \
#		    -H 'Connection: keep-alive' \
#		      -H 'Origin: http://127.0.0.1:19006' \
#		        -H 'Referer: http://127.0.0.1:19006/' \
#			  -H 'Sec-Fetch-Mode: cors' \
#			    -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36' \
#			      --compressed \
#			        --insecure
