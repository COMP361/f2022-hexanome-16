# COMP 361 Project

## CLIENT CURRENTLY BROKEN DUE TO COPYRIGHT ISSUES

We cannot make assets publicly available due to Splendor copyright which we weren't constrained by while creating the project.

We've nuked the commit history of all copyrighted assets.

## Branch Nomenclature and Releases
Release versions and tags are updated every time a push is made to the ``master`` branch or a pull-request is made to 
either ``master`` or ``staging``. Every dev branch must start with a prefix following this nomenclature:

* ``perf/`` indicates a major update (e.g. a new extension)
* ``feat/`` indicates a minor update, i.e. a new feature
* ``fix/``, ``test/``, ``refac/`` or ``hotfix/``  indicates a new patch

The release tags/versions have the format ``v<major>.<minor>.<patch>`` and are updated according to the 
**base** branch of PRs. Staging releases are labeled as "Pre-release", while master
releases are labeled as "Release".

For example, suppose the latest release version is ``v1.2.3``. Then, if there is a PR :
* to merge ``staging`` into ``master``, the version is kept but the release becomes ``latest``.
* from any ``perf`` branch, then version is updated to ``v2.0.0``.
* from any ``feat`` branch, then version is updated to ``v1.3.0``.
* from any ``fix`` branch, or if there is a push to ``master`` directly, then version is updated to ``v1.2.4``.

## Notes for TAs/Teacher (as of Milestone 7)
 * We have multiple test suites for intelliJ.
   * Server Unit Tests
   * Integration Tests (these require a local Lobby Service to be running, either manually or via ```LS Only``` run config in IntelliJ)
   * All tests (runs both the Unit and Integration tests)
 * Code coverage tool runner in IntelliJ has a bug that includes Lombok-generated methods into testing, resulting in poorer coverage than it's supposed to report (https://youtrack.jetbrains.com/issue/IDEA-273961/IntelliJ-does-not-honor-lombok.addLombokGeneratedAnnotationtrue-and-reports-missing-coverage). This is workaroundable using a different code coverage runner (JaCoCo), which is set in all our run configs in IntelliJ, so please use "All tests" to verify code coverage.
 * If you want to try playing the game, use the Splendor Prod IntelliJ run config (equivalent to ```PROFILE_ID=prod mvn clean package javafx:run```), since that one is connected to the remote server.

## How to run the project (development)
### Prerequisites
  * JDK 17+
  * Maven 3.6+ (for native builds on Mac M1-based machines)
  * JavaFX 17+
  * Docker (20.10+ for Linux, 4.0+ for Desktop on Windows/Mac)
  * IntelliJ IDEA (preferably latest to automatically detect run configs)

### Option 1: LS in Docker, Server on host
  * Start the instance of Lobby Service in docker by running "LS Only" run config in IntelliJ.
    * Equivalent manual command: ```docker-compose up -d --build --force-recreate --remove-orphans```.
  * Compile and install the common library JAR by running "Common" run config in IntelliJ.
    * Equivalent manual command: ```cd common; mvn clean install```.
  * Start the server with Server Dev run config in IntelliJ.
    * Equivalent manual command: ```cd server; PROFILE_ID=dev mvn clean package spring-boot:run```.
  * Start the client with Splendor Dev run config in IntelliJ.
    * Equivalent manual command: ```cd client; PROFILE_ID=dev mvn clean package javafx:run```.

### Option 2: Both LS and Server in Docker
  * Start the server and Lobby Service by running "Server + LS" run config in IntelliJ.
    * If you'd like to run this manually, do ```docker-compose --profile with-server up -d --build --force-recreate --remove-orphans```.
  * Compile and install the common library JAR by running "Common" run config in IntelliJ.
      * Equivalent manual command: ```cd common; mvn clean install```.
  * Start the client with Splendor Dev run config in IntelliJ.
    * Equivalent manual command: ```cd client; PROFILE_ID=dev mvn clean package javafx:run```.

## How to deploy and run the project (production)
  * Prerequisites: you need a GitHub PAT (Personal Access Token) with the `write:packages` scope and an Ubuntu/Debian server.
    * If you don't want to hassle with PAT, you can use the one Costa created (already included in script), but deployment will be credited to him.
    * The Ubuntu/Debian server should not have a private IP to prevent inaccessible network configuration.
    * Commands below assume you are in the root of the project, and you're executing them in Bash (Git Bash for Windows).
  * Make sure the project is stable and ready to be deployed.
  * First, publish the images to GitHub Packages:  ```./publish-images.sh```.
  * Next, SSH into the server and run the setup script: ```ssh root@ip 'bash -s' < ./server-setup-script.sh``` (current IP is listed in ```server-prod.properties```).
  * Wait for the server to execute all commands.
  * Start the client with Splendor Prod run config in IntelliJ.

## Setup CheckStyle in IntelliJ
* Install [Intellij CheckStyle plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea).
* Go to `Settings > Tools > CheckStyle`, select latest CheckStyle version, set Scope to `Only Java sources (including tests)` and click `Apply`.
* In the same window, add our custom configuration (checkstyle.xml) by clicking the `+` button and selecting the file.
* Click Ok to save settings.
* In the bottom toolbar, click on CheckStyle and in the left panel click on double-folder button to scan the project.
* Fix all the errors and warnings that appear :)

## The Rules

 * Feel free to edit/replace this file.
 * Do not delete or rename the [reports](reports), [client](client), [server](server) or [docs](docs) directories.  
See [Static Content](#static-content)
 * Don't clutter your repo, update your [```.gitignore```](.gitignore) file, depending on your client language / technology.
    * Don't commit binaries. (jar files, class files, etc...)
    * Don't commit buffer files. (Vim buffer files, IDE meta files etc...)
 * Place your documentation in [```docs```](docs) on `master` branch.
 * Commit frequently, commit fine grained.
 * Use branches
 * **Don't push on master!**
    * Create a new branch for your feature.
    * Work until stable / tested.
    * Merge / rebase your temporary branch back to master.
    * Delete your temporary branch.

## Static content

 * [```.gitignore```](.gitignore): Preliminary git exclusion instructions. Visit [Toptal's generator](https://www.toptal.com/developers/gitignore) to update.
 * [```reports```](reports): Base directory for automatic report collection. Your weekly reports go here. Must be uploaded every monday noon **to master** and follow correct date string ```YYYY-MM-DD.md```. Use [template](reports/YYYY-MM-DD.md) for your own reports. Do not resubmit same report / copy and paste.
 * [```docs```](docs): source directory for your [enabled GitHub Pages](https://comp361.github.io/f2022-hexanome-00/). (Update number in link). Configure IDE to generate API docs into this directory or build your own webpage (optional).
 *  [```client```](client): Place your client sources into this directory. Do not use a separate repository for your work.
 * [```server```](server): Place your Spring Boot Game Server sources in this directory. Do not use a separate repository for your work.

## Useful Links

### Code Style and Tools

 * [Chrome MarkDown Plugin](https://chrome.google.com/webstore/detail/markdown-viewer/ckkdlimhmcjmikdlpkmbgfkaikojcbjk?hl=en).
    * Don't forget to enable ```file://``` in ```advanced settings```.
 * [IntelliJ Checkstyle Plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea).
    * Don't forget to enable [Google's Checkstyle Configuration](https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml).
 * [Git CheatSheet](git-cheatsheet.md).
 * [Advanced Rest Client (Rest Call Code Generator)](https://docs.advancedrestclient.com/installation).

### Requirements

 * [Lobby Service](https://github.com/kartoffelquadrat/LobbyService)
    * [Install Guide](https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/build-deploy.md)  
Recommended: Startup in ```dev``` profile (default).
    * [API Doc and ARC Configurations](https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/api.md)
    * [Game Developer Instructions](https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/game-dev.md)
 * [BGP sample deployment configuration](https://github.com/kartoffelquadrat/BoardGamePlatform) (This one is meant for testing / understanding the interaction between LS, UI and sample game)  
Board Game Platform (BGP) = Lobby Service + Lobby Service Web UI + Sample Game, all as docker containers.
    * Sample [Lobby Service Web UI](https://github.com/kartoffelquadrat/LobbyServiceWebInterface)
    * Sample Lobby Service compatible [Game (Tic Tac Toe, backend + frontend)](https://github.com/kartoffelquadrat/BgpXox)

 > Be careful not to confuse *Lobby Service* and *Board Game Platform*.

## Authors
 * [Peini Cheng](https://github.com/PeiniCheng)
 * [Constantin Buruiana](https://github.com/ConstBur)
 * [Kimi Zhao](https://github.com/kimikimizz)
 * [Imad Issafras](https://github.com/UnHappySquid)
 * [Tristan Leclair-Vani](https://github.com/TristanLeclair)
 * [Éléa Dufresne](https://github.com/eleadufresne)
