# Norris
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/) [![Maintainability](https://api.codeclimate.com/v1/badges/42704b7b56bbdba33b99/maintainability)](https://codeclimate.com/github/dotanuki-labs/norris/maintainability) 
![Main](https://github.com/dotanuki-labs/norris/workflows/Main/badge.svg)
[![License](https://img.shields.io/github/license/dotanuki-labs/gradle-profiler-pttest)](https://choosealicense.com/licenses/mit)

> An app that existed even before Android existed! 🔥

## About

Norris is a showcase for modern and well-crafted Android code. 

Here you will find a codebase powered by tests in all semantic levels (unit, integration, acceptance) as well an architectural design that promotes discipline over state, using Kotlin Coroutines + Kotlin Flows as foundation for a pragmatic unidirectional dataflow implementation.

## Project Overview

This project leverages on [api.chucknorris.io](https://api.chucknorris.io/) as remote data source to implement the following use cases

- User can search for Chuck Norris facts and share them
- Application will offer suggestions of searches
- Application also remembers terms searched by the user

![showcase-norris](.github/assets/showcase-norris.png)

The code is structured in a multi-module fashion, with semantics of high-level modules (under `features`) packaging high-level pieces of funcionality (including UI details) while low-level modules (or `platform` ones) provide required infrastructure for features, like networking, storage and so on.


## Building and Running

If you want a simple run emulating the PR pipeline, just use the companion script

```
./scripts/emulate-pr-build.sh
```

It will

- Run static analysers ([Ktlint](https://github.com/pinterest/ktlint) and [Detekt](https://arturbosch.github.io/detekt/))
- Run all unit tests
- Assemble the `release` APK
- Run Espresso tests

Please note that an online emulator is required in order to run this script sucessfully.

## Testing Strategy

This project is **heavily opinionated** on how testing should be done in modern Android projects. Particularly, I follow here the approach proposed by [Kent Dodds](https://kentcdodds.com/blog/write-tests) as much as possible:

> Write tests. Not too many. Mostly integration

In practice, we have

- No mocks are used, only fakes (when needed). This project does not use Mockito.
- Most of unit tests actually are on `platform` modules, since those libraries provide core functionally for features
- On features, tests are mandatory over data sources, although REST APIs should not be directly faked; instead, we test the whole networking stack by inject fake responses through it
- When a feature has some domain logic of interest - eg, some validation logic over data - such logic is unit tested
- Also on features, integrated tests run over over Activities by leveraging a pragmatic way to decoupling it from the underlying View and asserting data flows in the desired directions (either from data source to screen or from screen to data sources)
- This means that we don't unit test ViewModels
- Espresso tests ensure cross-screen user journeys and run against the release variant of the application     


## Credits

- [Mathias Schilling](https://github.com/matchilling), for the Chuck Norris facts [REST api](https://api.chucknorris.io/)
- [Material Design Icons](https://materialdesignicons.com/) for the error states images
- [Jetbrains](https://www.jetbrains.com/) for the amazing developer experience around Kotlin and Coroutines
- Someone in the Web for the Norris clipart ❤️ _(I'll be happy to put your name here, ping me!)_

## Author

Coded by Ubiratan Soares (follow me on [Twitter](https://twitter.com/ubiratanfsoares))

## License

```
The MIT License (MIT)

Copyright (c) 2019 Dotanuki Labs

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
