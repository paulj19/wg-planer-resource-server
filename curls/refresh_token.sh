curl -v 'https://wg-planer-auth-server.onrender.com/oauth2/token' \
	  -H 'Accept: application/json, text/plain, */*' \
	    -H 'Accept-Language: en-US,en;q=0.9,de-DE;q=0.8,de;q=0.7' \
	      -H 'Authorization: Basic d2ctcGxhbmVyOnNlY3JldA==' \
	        -H 'Connection: keep-alive' \
		  -H 'Content-Type: application/x-www-form-urlencoded' \
		    -H 'Origin: http://127.0.0.1:19006' \
		      -H 'Referer: http://127.0.0.1:19006/' \
		        -H 'Sec-Fetch-Dest: empty' \
			  -H 'Sec-Fetch-Mode: cors' \
			    -H 'Sec-Fetch-Site: cross-site' \
			      -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36' \
			        -H 'sec-ch-ua: "Chromium";v="110", "Not A(Brand";v="24", "Google Chrome";v="110"' \
				  -H 'sec-ch-ua-mobile: ?0' \
				    -H 'sec-ch-ua-platform: "Linux"' \
				      --data-raw 'refresh_token=UH0nvcnz6K08k0heGSXnhkzWt73HlaEyiiWTHtq05v5gePezPaSt24vdoX270vWCtdwwTEXuol7gshWJX048CVcZYDcb2ofgXa24t5_dM3SvBIhBfUlhN4sbnh8Xb8IH&grant_type=refresh_token' \
				        --compressed
