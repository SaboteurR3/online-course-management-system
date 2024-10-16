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
