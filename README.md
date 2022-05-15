# Ratpack AngularJS Template
-----------------------------
[![Build Status](https://www.travis-ci.com/bitsnaps/ratpack-angularjs-template.svg?branch=master)](https://www.travis-ci.com/bitsnaps/ratpack-angularjs-template)

[![Open in GitPod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/from-referrer/)

This project is has been created using a basic Groovy Ratpack application from lazybones.

It doesn't do much, it offers a CRUD operations on `User` model, but it can be useful to spin up a new Ratpack project.

This project demonstrates:

* Full REST API for CRUD operations on a single model.
* ORMLite integrated with Ratpack
* A full UI based on Bootstrap 3 & AngularJS 1.2.x legacy (for max browser support)
* JWT Authentication using Pac4j Ratpack module with stateless login/logout client side
* ShadowJar is ready in `build.gradle` to generate a full runnable jar file
* Non-blocking requests using java's `Promise`
* Unit testing with Spock framework
* A standard project structure:

```
    <proj>
      |
      +- src
          |
          +- ratpack
          |     |
          |     +- Ratpack.groovy
          |     +- ratpack.properties
          |     +- public // Static assets in here
          |          |
          |          +- images
          |          +- lib
          |             |
          |             +- controllers
          |             +- services
          |             +- app.js
          |          +- scripts
          |          +- styles
          |
          +- main
          |   |
          |   +- groovy
                   |
                   +- *.groovy // App classes in here!
          |
          +- test
              |
              +- groovy
                   |
                   +- UserSpec.groovy // Spock tests in here!
```

## Tested on:
- JDK 8 & executed in both OpenJRE 8 & 11
- Mac & Linux.

## How to run?

You can start the app with:

    ./gradlew run -t

You can run the test with:

    ./gradlew test

You can generate a runnable jar (aka. uberJar) with:

    ./gradlew ShadowJar

You can extend with more: security enhancements, more UI features, application configurations...

## Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/bitsnaps/ratpack-angularjs-template/issues).

## LICENSE

MIT License

Copyright (c) 2021 Ibrahim H.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
