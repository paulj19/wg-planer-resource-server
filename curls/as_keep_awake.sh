while(true); do 
	curl https://wg-planer-auth-server.onrender.com/actuator/health;
	curl https://wg-planer-resource-server.onrender.com/actuator/health;
	sleep 300;
done
