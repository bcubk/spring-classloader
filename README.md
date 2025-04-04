# Spring ClassLoader

This repository demonstrates a bug creating CGLib proxies interactions with Spring Framework.

## Problem Description
When using customized ClassLoaders with Spring, particularly with features that rely on CGLIB proxies (like JobScope from Spring Batch), unexpected behavior can occur:

* CGLIB proxies may not be created correctly when using custom ClassLoaders
* Adding the JVM argument `--add-opens java.base/java.lang=ALL-UNNAMED` changes the behavior of proxy generation

## Demonstration
The test case `TestConfigurationClassEnhancer` demonstrates how:

1. A bean in the JobScope should be proxied with CGLIB
2The behavior changes when run with `--add-opens java.base/java.lang=ALL-UNNAMED`

## Usage
To see the issue:
```bash
mvn test
```