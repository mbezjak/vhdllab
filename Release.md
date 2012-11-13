Release steps:

1. Add `Changelog.md` entry
2. Update `application.version` in `pom.xml`
3. Git work:

        $ git tag --annotate $version
        $ git push
        $ git push --tags
