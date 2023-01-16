# COMP 361 Project

 > See also [my video instructions](https://www.cs.mcgill.ca/~mschie3/COMP361/Repository-Best-Practices.mp4) in the screencasts section on MyCourses.

## Notes for TAs/Teacher (as of January 3 2023)
 * We have multiple test suites for intellij.
   * Server Unit Tests (TODO: clear these of IT tests)
   * Integration Tests (these require the `LS Only` docker to be running inside of Intellij)
   * All tests (runs both the Unit and Integration tests)
 * Code coverage tool runner in IntelliJ has a bug that includes Lombok-generated methods into testing, resulting in poorer coverage than it's supposed to report (https://youtrack.jetbrains.com/issue/IDEA-273961/IntelliJ-does-not-honor-lombok.addLombokGeneratedAnnotationtrue-and-reports-missing-coverage). This is workaroundable using a different code coverage runner (JaCoCo), which is set in all our run configs in IntelliJ, so please use "All tests" to verify code coverage (should be 85%).
 * Some server tests require a local Lobby Service to be running.
 * If you want to try playing the game, use the Splendor Prod IntelliJ run config (equivalent to ```PROFILE_ID=prod mvn clean package javafx:run```), since that one is connected to the remote server.

## Setup CheckStyle in IntelliJ
 * Install [Intellij CheckStyle plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea).
 * Go to `Settings > Tools > CheckStyle`, select latest CheckStyle version, set Scope to `Only Java sources (including tests)` and click `Apply`.
 * In the same window, add our custom configuration (google_checks.xml) by clicking the `+` button and selecting the file.
 * Click Ok to save settings.
 * In the bottom toolbar, click on CheckStyle and in the left panel click on double-folder button to scan the project.
 * Fix all the errors and warnings that appear :)

## How to run the project (development)
  * Start the server and Lobby Service by running "Server + LS" run config in IntelliJ.
    * If you'd like to run this manually, do ```docker-compose --profile with-server up -d --build --force-recreate --remove-orphans```.
  * Start the client with Splendor Dev run config in IntelliJ.

## How to deploy and run the project (production)
  * Prerequisites: you need a GitHub PAT (Personal Access Token) with the `write:packages` scope and an Ubuntu/Debian server.
    * If you don't want to hassle with PAT, you can use the one Costa created (already included in script), but deployment will be credited to him.
    * The Ubuntu/Debian server should not have a private IP to prevent wrong Docker network configuration.
    * Commands below assume you are in the root of the project, and you're executing them in Bash (Git Bash for Windows).
  * Make sure the project is stable and ready to be deployed.
  * First, publish the images to GitHub Packages:  ```./publish-images.sh```.
  * Next, SSH into the server and run the setup script: ```ssh root@ip 'bash -s' < ./server-setup-script.sh``` (current ip is listed in ```server-prod.properties```).
  * Wait for the server to execute all commands.
  * Start the client with Splendor Prod run config in IntelliJ.

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
