# Spring Boot and Vaadin course source code

This repository contains the source code for the [Building Modern Web Applications With Spring Boot and Vaadin](https://vaadin.com/docs/latest/flow/tutorials/in-depth-course).

*Live demo:* https://crm.demo.vaadin.com

## Running the Application
Strict CSP is only enabled in production mode: `mvn spring-boot:run -Pproduction`

## Branches

- This branch (24.5-strict-csp) is an example of how to enable strict CSP using a nonce with Vaadin 24.5
- The main branch contains the source code for the latest Vaadin release
- The `v14` branch contains the source code for Vaadin 14
