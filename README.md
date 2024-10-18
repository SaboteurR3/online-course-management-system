## API Documentation

The APIs for this project can be accessed through the Swagger UI at the following endpoint:
[http://localhost:8089/swagger/swagger-ui/index.html](http://localhost:8089/swagger/swagger-ui/index.html)
You can use this interface to explore the available APIs, their request and response formats, and test them directly from your browser.


## Run MinIO in Docker Desktop
### Step 1: Pull the MinIO Docker Image: docker pull minio/minio
### Step 2: To run MinIO in Docker Desktop, execute the following command in your terminal or PowerShell:
docker run -p 9000:9000 -p 9001:9001 \
-e "MINIO_ACCESS_KEY=your-access-key" \
-e "MINIO_SECRET_KEY=your-secret-key" \
minio/minio server /data --console-address ":9001"
