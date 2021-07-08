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
- Run all unit/integration tests over JVM
- Assemble the `release` APK
- Run Espresso tests over Android/Instrumentation

Please note that an online emulator is required in order to run this script sucessfully.

## Implementation highlights

- **OkHttp** + **Retrofit** for networking
- **Kotlinx.Serialization** for Json handling
- **Kotlinx.Coroutines** for asynchrounous processing
- Unidirectional data-flow driven by **Kotlin Flows**
- Semi-manual dependencies/instances management at runtime driven by **Kodein**
- No annotations processors (therefore no `kapt`)
- No Fragments

## Testing Strategy

This project is **heavily opinionated** on how testing should be done in modern Android projects. Particularly, I follow here the approach proposed by [Kent Dodds](https://kentcdodds.com/blog/write-tests) as much as possible:

> Write tests. Not too many. Mostly integration

In practice, in this project:

- No mocks are used, only fakes when needed. That's correct, this project does not use Mockito or Mockk.
- Most of unit tests actually are on `platform` modules, since those libraries provide core functionally for features
- [Table-driven testing](https://dave.cheney.net/2019/05/07/prefer-table-driven-tests) is used whenever possible
- [Interaction-based tests are completely avoided](https://blog.ploeh.dk/2019/02/18/from-interaction-based-to-state-based-testing/); only state-based tests are used
- On `features`, tests are mandatory over data sources, although REST APIs are not directly faked; instead, we test the whole networking stack by inject fake responses on a Mock Server
- When a high level module owns some domain logic of interest - eg, some validation rule - such logic is unit tested
- Also on `features`, integrated tests run over Activities by leveraging a pragmatic way to decoupling them from their hosted Views. The inflated View is faked at testing time. 
- No unit test over ViewModels
- [Acceptance tests](https://www.davefarley.net/?p=186) are implemented with Espresso running over Android/Instrumentation     
- Espresso tests exercise the **release artefact**; the only difference when compared with a production-ready APK is the REST API URL passed-in at build time
- Acceptance tests run with a stress-first approach (5 runs per execution x 3 Jobs per run on CI)
- Acceptance tests exercise real user flows in a cross-screen / cross-feature fashion

Actual numbers:

Testing approach   | Execution Environment           | Amount   | Percentage |
-------------------| ------------------------------- | -------- | ---------- |
Unit tests         | JVM/plain                       | 11       | 39%        |
Integration tests  | JVM/plain + JVM/Instrumentation | 15       | 54%        |
Acceptance tests   | Android/Instrumentation         | 2        | 7%         |


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
