# Raven bS++ Contributing Guide

Thank you for your interest in contributing to the Raven bS++ project! This document provides guidelines for contributing to the project.

## Contribution Process

1. Fork the project
2. Create a branch for your feature (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Versioning

We use [semantic versioning](https://semver.org/) with the MAJOR.MINOR.PATCH format:

- **MAJOR**: changes incompatible with previous versions
- **MINOR**: addition of backward-compatible features
- **PATCH**: backward-compatible bug fixes

The current version of the mod is **1.5.3**.

### Updating the Version

When updating the version, please modify the following files:

1. `gradle.properties`: Update the `version` property
2. `README.md`: Update the version mentioned in the Description section
3. `CONTRIBUTING.md`: Update the current version mentioned in this file

## Code Style

- Use descriptive variable and method names
- Comment your code when necessary
- Follow standard Java naming conventions
- Test your changes before submitting a PR

## Build Process

To compile the project locally:

```bash
./gradlew build --no-daemon
```

The compiled JAR files will be available in the `build/libs/` folder.

## Creating a Release

Releases are automatically created when a tag with the format `vX.Y.Z` is pushed to the repository. For example:

```bash
git tag v1.5.3
git push origin v1.5.3
```

This will trigger the GitHub Actions workflow that will compile the project and create a release with the JAR file.
