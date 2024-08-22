# Kotlin Kata

This repo contains a basic skeleton for facilitating code katas.

For kata checkout https://codingdojo.org/kata/, but there are way more on the internet.

## Prerequisites

- macOS or Linux Computer, no support for windows
- basic unix shell skills
- java IDE (e.g. intellij)
- install [skdman](https://sdkman.io/)
    - enable auto env in `${HOME}/.sdkman/etc` set `sdkman_auto_env=true`
- install git `sudo apt install git` or `brew install git`
    - you created a GitHub account
    - you have an ssh key, else follow [this how to](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)
    - use `ssh-add` to avoid typing your password too many times
    - ssh keys is set up, see [docs](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/adding-a-new-ssh-key-to-your-github-account)
- please install [mob.sh](https://mob.sh/)

## Test Run

`./gradlew check` should build fine and show you that some assertions failed


## Ensemble Basic Commands

`git switch -c <new branch name here>` switch to another branch
`mob s` start a session
`mob n` hand over to the next person or use as a save point
`mob d` commit changes to your branch
