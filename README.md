# S3Uploader
This is a simple application that has basic CRUD like capabilities demonstrating how they are done with using S3 Java API

## Design
* `S3CrudServiceImpl` has all the methods that is capable of uploading an object to S3, uploading an object to S3 with metadata, downloading a given object, reading metadata from a given object, and deleting a given object in S3.

* `ClientBuilderManager` utility class build an S3 client to access S3 servie APIs.

* `PropertyManager` reads required properties from the configuration file makes them available across the application.

## Configuring AWS Infrastructure
* Create an `IAM Group`.

* Create an `IAM User`.

* Add created `IAM User` to `IAM Group`'s `Amazon S3` service.

* Go to `IAM Users`, select the created user and generate `key`/`value` pair in `Security credentials` tab.

* Create an `S3` bucket and have below `CORS configuration` in Permissions.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
<CORSRule>
    <AllowedOrigin>*</AllowedOrigin>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedHeader>*</AllowedHeader>
</CORSRule>
</CORSConfiguration>
```

## How to test
This is a simple `Java` `Maven` project.

Fill in the values in the `properties` file.

Run `App` class.
