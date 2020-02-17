# S3Uploader
This is a simple application that has basic CRUD like capabilities demonstrating how they are done with using S3 Java API

## Design
* `S3CrudServiceImpl` has all the methods that is capable of uploading an object to S3, uploading an object to S3 with metadata, downloading a given object, reading metadata from a given object, and deleting a given object in S3.

* `ClientBuilderManager` utility class build an S3 client to access S3 servie APIs.

* `PropertyManager` reads required properties from the configuration file makes them available across the application.


## How to test
This is a simple `Java` `Maven` project.

Run `App` class.
