# Norris
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/) [![Maintainability](https://api.codeclimate.com/v1/badges/42704b7b56bbdba33b99/maintainability)](https://codeclimate.com/github/dotanuki-labs/norris/maintainability) 
![Main](https://github.com/dotanuki-labs/norris/workflows/Main/badge.svg)
[![License](https://img.shields.io/github/license/dotanuki-labs/gradle-profiler-pttest)](https://choosealicense.com/licenses/mit)

> An app that existed even before Android existed! 🔥

## About

Norris is a small and non-trivial Android project used as

- a showcase for some advanced techniques regarding infrastructure for large Android projects
- a sandbox for testing new tools in the open

This project leverages on [api.chucknorris.io](https://api.chucknorris.io/) as remote data source to implement 
the following use cases

- User can search for Chuck Norris facts and share them
- Application will offer suggestions of searches
- Application also remembers terms searched by the user

![showcase-norris](.github/assets/showcase-norris.png)

## Tooling

Some of the tools used in this project that maybe you are not aware about:

- [Dropshots](https://github.com/dropbox/dropshots) to drive screenshot tests
- [Maestro Clould](https://maestro.mobile.dev/) for E2E testing
- [Emulator.wtf](https://emulator.wtf) as a device farm
- [AppSweep](https://appsweep.guardsquare.com) to drive Security checks over a release build
- [Foresight](https://foresight.thundra.io/) to consolidade test reports from CI runs (JVM and Android/Instrumentation)
- [Renovate](https://docs.renovatebot.com/) to drive regular depencency updates
- [Mergify](https://mergify.com/) for Pull Request automation

## Blog posts

A few blog posts covering implementation details and motivations behind some decisions that shaped this project are coming!

Stay tuned! 🚀


## Credits

- [Mathias Schilling](https://github.com/matchilling), for the Chuck Norris facts [REST API](https://api.chucknorris.io/)
- [Material Design Icons](https://materialdesignicons.com/) for the error states images
- [Jetbrains](https://www.jetbrains.com/) for the amazing developer experience around Kotlin and Coroutines
- Someone in the Web for the Norris clipart ❤️ _(I'll be happy to put your name here, ping me!)_

## Author

Coded by Ubiratan Soares (follow me on [Mastodon](https://hachyderm.io/@ubiratansoares))

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
