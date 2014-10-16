Release steps:

1. Add `Changelog.md` entry
2. Update `application.version` in `pom.xml`
3. Git work:

        $ git tag --annotate $version
        $ git push
        $ git push --tags

Alternative steps:

For this, [poly-devel](https://github.com/mbezjak/poly-devel) needs to be
installed.

1. Add `Changelog.md` entry
2. Execute:

        $ release $version
