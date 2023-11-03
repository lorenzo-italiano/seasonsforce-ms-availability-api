mvn clean install

mv target/seasonsforce-ms-availability-api-1.0-SNAPSHOT.jar api-image/seasonsforce-ms-availability-api-1.0-SNAPSHOT.jar

cd api-image

docker build -t availability-api .

cd ../postgres-image

docker build -t availability-db .