# demo
Demo SpringBoot Application with Spring Security

### Project Setup
The spotless formatter plugin has been enabled within this repo and will provide a mechanism to enforce common coding standards across the source files.

In order to provide this as an automated tool which is used in conjunction with `git commit` commands the following setup must be run, once, after cloning the repository locally.

These commands must be run form the command line in the project repository root:

```
chmod +x ./scripts/git/pre-commit.sh
ln -s ../../scripts/git/pre-commit.sh .git/hooks/pre-commit
```