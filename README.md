## Run MinIO in Docker Desktop

To run MinIO in Docker Desktop, execute the following command in your terminal or PowerShell:

```powershell
docker run -d `
  --name minio `
  --network minio-network `
  -p 9000:9000 `
  -e "MINIO_ACCESS_KEY=minioadmin" `
  -e "MINIO_SECRET_KEY=minioadmin" `
  -v minio-data:/data `
  minio/minio server /data

## API Documentation

The APIs for this project can be accessed through the Swagger UI at the following endpoint:

[http://localhost:8089/swagger/swagger-ui/index.html](http://localhost:8089/swagger/swagger-ui/index.html)

You can use this interface to explore the available APIs, their request and response formats, and test them directly from your browser.
