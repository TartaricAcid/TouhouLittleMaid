# AGENTS.md

## Language policy

- 默认使用简体中文回答。
- 除非我明确要求英文，否则不要切换英文叙述。
- 代码、命令、报错、API 名称保持原文，不要强行翻译。
- 提问澄清时也使用中文。

## Operating rules

- Work from the repo root.
- Treat committed files and the verified Gradle command baseline as source of truth.
- Keep changes repo-specific. Don't paste generic Java, Forge, or Gradle advice.
- Don't invent tooling. This repo has no dedicated lint or formatter Gradle task.
- Don't treat local ignored files as policy. `src/test` is gitignored.

## Policy files

- No `.cursorrules`, no `.cursor/rules/**`, and no `.github/copilot-instructions.md` were found.

## Project overview

- This repo builds the Touhou Little Maid mod.
- `readme.md` describes it as a Minecraft Forge or NeoForge mod inspired by Little Maid Mob and Touhou Project.
- The active build here is the 1.20.1 ForgeGradle setup in `build.gradle`.
- Main code lives under `src/main/java` and resources live under `src/main/resources`.
- Generated resources are also loaded from `src/generated/resources`.

## Environment and toolchain

- Build tool: Gradle wrapper.
- Java toolchain: Java 17, set by `java.toolchain.languageVersion = JavaLanguageVersion.of(17)`.
- Test dependency: JUnit 4.13.2.
- Applied Gradle plugins include ForgeGradle, Parchment Librarian, MixinGradle, Java, IDEA, Eclipse, and Shadow.
- Common local run tasks come from the ForgeGradle `minecraft.runs` block.
- CI uses GitHub Actions on Ubuntu, sets up JDK 17, then runs `./gradlew build`.

## Canonical commands

### List tasks

Unix:

```bash
./gradlew tasks --all
```

Windows:

```bash
gradlew.bat tasks --all
```

Use this first if you need to confirm task names; verified output includes `build`, `check`, `test`, `runClient`,
`runClient2`, `runServer`, and `runData`.

### Full build

Unix:

```bash
./gradlew build
```

Windows:

```bash
gradlew.bat build
```

This matches CI and is the right final local validation when a full build is needed.

### Build without tests

Unix:

```bash
./gradlew build -x test
```

Windows:

```bash
gradlew.bat build -x test
```

This was verified locally; use it when test sources are irrelevant to the change or unavailable.

### Run game and data tasks

Unix:

```bash
./gradlew runClient
./gradlew runServer
./gradlew runData
```

Windows:

```bash
gradlew.bat runClient
gradlew.bat runServer
gradlew.bat runData
```

`runClient2` also exists if you need the second client profile from `build.gradle`.

### Run all checks

```bash
# Unix
./gradlew check
# Windows
gradlew.bat check
```

`check` runs the Gradle verification lifecycle and includes tests.

### Run all tests

Unix:

```bash
./gradlew test
```

Windows:

```bash
gradlew.bat test
```

### Run one test class

Unix:

```bash
./gradlew test --tests Test --dry-run
```

Windows:

```bash
gradlew.bat test --tests Test --dry-run
```

This exact form was verified locally, and Gradle docs also support `--tests Test` for real execution.

### Run one test method

Unix:

```bash
./gradlew test --tests Test.testEncodeDecodeRoundtrip --dry-run
```

Windows:

```bash
gradlew.bat test --tests Test.testEncodeDecodeRoundtrip --dry-run
```

This exact form was verified locally, and Gradle docs also support `--tests ClassName.methodName` syntax.

## Testing caveat

- `.gitignore` ignores `src/test`.
- `src/test/java/Test.java` may exist locally, but it is only a local example; treat the verified `--tests Test`
  examples as command-shape guidance, not proof that a committed shared test suite exists.

## Lint and formatting reality

- There is no verified `spotlessCheck` task; local verification showed `./gradlew spotlessCheck` fails with task not
  found.
- Don't add Spotless, Checkstyle, PMD, or formatter instructions unless the repo is changed to include them.
- For this repo, Gradle verification is centered on `test`, `check`, and `build`.

## Java style conventions seen in committed sources

- Use 4-space indentation and K&R braces.
- Imports are often grouped with blank lines between logical blocks.
- Wildcard imports also exist, so don't rewrite files just to force explicit imports everywhere.
- Package-level non-null defaults are common where `package-info.java` declares them.
- Nullability annotations are mixed. Both `javax.annotation` and JetBrains annotations appear.
- Explicit `@Nullable` and `@NotNull` are common, but not uniform across every package.
- Utility classes are often `final` with a private constructor, but not always.
- Constant names commonly use `UPPER_SNAKE_CASE`.
- Explicit local types are common. `var` is used selectively when the initializer makes the type obvious.
- Error handling now uses context-rich `LOGGER` exception logging. Keep following that pattern in new or updated code.

## Repo-specific constraints

- Keep generated-resource behavior intact. `src/generated/resources` is part of the main resources source set.
- Respect mixin and ForgeGradle setup in `build.gradle`.
- Don't treat uppercase statics as automatically immutable. The style baseline found uppercase non-final fields too.
- Prefer minimal, targeted edits over repo-wide cleanup passes.

## Validation checklist

- Confirm the change is grounded in committed files or locally verified command evidence.
- Run `./gradlew build` or `gradlew.bat build` when a full validation pass is appropriate.
- Use `./gradlew build -x test` or `gradlew.bat build -x test` only when that lighter check matches the task.
- For test targeting, prefer the verified Gradle forms shown above.
- If you need to inspect available tasks first, run `./gradlew tasks --all` or `gradlew.bat tasks --all`.
- Don't report lint results from nonexistent tasks.
- If you mention tests, note that `src/test` is gitignored and local examples are not committed-policy truth.
