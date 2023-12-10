# Bike diary

🚧 THIS PROJECT IS ON PAUSE IN FAVOUR OF MY [SCHOOL PROJECTS](https://github.com/ericafenyo/time-master)🚧

![GitHub workflow build](https://github.com/ericafenyo/bike-diary/actions/workflows/android.yml/badge.svg)
[![GitHub](https://img.shields.io/github/license/ericafenyo/bike-diary)](LICENSE)
[![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/ericafenyo/bike-diary?label=version)][version]

An Android app that allows users to record their cycling routes, save memorable photos, and track their personal improvement goals & statistics while discovering new places.

[version]: https://github.com/ericafenyo/bike-diary/releases

## Screenshots
> coming soon

## Documentation
### Functional specifications
* [Authentication portal](docs/functional-specifications/auth-portal.md) &mdash; Access the login and registration page
* [Login](docs/functional-specifications/login.md) &mdash; Log in to a user account

## Development setup

### Prerequisites

Once you have the keys, you can define them in your ./gradle.properties file.

1. **Server Url**: The application uses a dedicated API server to handle HTTP requests. Follow these [instructions](https://github.com/ericafenyo/bike-diary-api/blob/main/README.md) to configure and run the server locally on your system.

```yml
# ./gradle.properties

API_SERVER_URL=<insert>
```

2. **Mapbox download and public API keys**: You can find information on how to generate these keys via this link:

   https://docs.mapbox.com/android/maps/guides/install/

```yml
# ./gradle.properties

MAPBOX_DOWNLOAD_TOKEN=<insert>
MAPBOX_PUBLIC_TOKEN=<insert>
```

3. **Sentry DNS** (optional): If you use Sentry for app monitoring, you can add the DNS.

```yml
# ./gradle.properties

SENTRY_DNS=<insert>
```

> Note: replace `<insert>` with the correct values

### Building and Testing

This project uses the Gradle build system. To build or test this project, use the `gradlew build` or `gradlew test` command, respectively.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
