# Bike diary

![GitHub workflow build](https://github.com/ericafenyo/bike-diary/actions/workflows/android.yml/badge.svg)
[![GitHub](https://img.shields.io/github/license/ericafenyo/bike-diary)](LICENSE)
[![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/ericafenyo/bike-diary?label=version)][version]

An Android cycling tracker app designed to help users reach fitness goals, explore new destinations, and capture unforgettable memories.

:construction: I am giving this project a major makeover, tweaking it based on new [user stories](docs/user-stories.md). While I had some features in there already, I decided to review everything to better manage the project. At first, I was adding features on the go, but I now have a complete set of [user stories](docs/user-stories.md) in place. :construction:


## Screenshots

> coming soon

## Documentation

- [Backlog](docs/backlog.md)
- [Users stories](docs/user-stories.md)
- Functional specifications
  - [Authentication portal](docs/functional-specifications/auth-portal.md) &mdash; Access the login and registration page
  - [Login](docs/functional-specifications/login.md) &mdash; Log in to a user account

## Development setup

### Prerequisites

Once you have the keys, you can define them in your ./gradle.properties file.

1. **Server Url**: The application uses a dedicated API server to handle HTTP requests. Follow these [instructions](https://github.com/ericafenyo/bike-diary-api/blob/main/README.md) to configure and run the server locally on your system.

```yml
# ./gradle.properties

API_SERVER_URL=<insert>
```

2. **Mapbox download and public API keys**: You can find information on how to generate these keys via this link:

   <https://docs.mapbox.com/android/maps/guides/install/>

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

[version]: https://github.com/ericafenyo/bike-diary/releases
