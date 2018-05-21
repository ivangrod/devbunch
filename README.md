# devbunch-feed-collector

The feed collector project of DEVBunch is a tool to collect information of many posts from several technical blogs.

The project imports, normalizes and stores this information from different RSS declared in an OPML file: [engineering_blogs.opml](.src/main/resources/opml/engineering_blogs.opml)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- [Java8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Maven 3.5.0](https://maven.apache.org/download.cgi)

You have to install Docker and Docker Compose:

- [Docker](https://docs.docker.com/installation/#installation)
- [Docker Compose](https://docs.docker.com/compose/install)

### Installing

* Clone source code from git

```
$ git clone git@github.com:ivangrod/devbunch.git
```

* Build project with Maven

```
$ mvn clean install
```

* Run the application from the command line using:

```
$ ./mvnw spring-boot:run
```

## Built With

* [Spring Framework](https://spring.io/) - The framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds
* [Elasticsearch](https://www.elastic.co/products/elasticsearch) - Search engine based on [Lucene](https://lucene.apache.org/core/)

## Versioning

We use [SemVer](http://semver.org/) for versioning.

## Authors

* **Iván Gutiérrez** - *Initial work* - [ivangrod](https://github.com/ivangrod)

## Contributors

* **David Romero** - *Development and CI* - [david-romero](https://github.com/david-romero)

## License

This project is licensed under the GNU GENERAL PUBLIC LICENSE License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* [Engineering-blogs](https://github.com/kilimchoi/engineering-blogs)
