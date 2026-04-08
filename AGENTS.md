# AGENTS.md

## Language policy

- 默认使用简体中文回答。
- 除非我明确要求英文，否则不要切换英文叙述。
- 代码、命令、报错、API 名称保持原文，不要强行翻译。
- 提问澄清时也使用中文。

## Repo shape

- This repo is a single-package VuePress 2 docs site, not a monorepo.
- The real app wiring lives under `docs/.vuepress/`; the content lives under `docs/wiki/`.
- `docs/en/` is wired into config, but `docs/en/wiki/` is currently empty. Do not assume English docs are populated just because `/en/` exists in config and homepage links.
- `.ref/` is gitignored reference material, not source of truth for this repo's behavior.

## Canonical commands

- Install/CI-aligned dependency flow: `npm ci`
- Local dev: `npm run docs:dev`
- Dev with clean cache/temp: `npm run docs:dev-clean`
- Production build: `npm run docs:build`
- Preview built output: `npm run docs:preview`
- Theme maintenance helper: `npm run vp-update`

## Tooling reality

- `package.json` declares `packageManager: pnpm@10.14.0`, but the committed lockfile is `package-lock.json` and CI uses `npm ci`. Prefer npm commands when validating changes.
- Required Node version is `^20.6.0 || >=22.0.0`; GitHub Actions uses Node 20.
- In this Windows PowerShell environment, `npm` may be blocked by execution policy. Use `npm.cmd ...` if needed.
- `.npmrc` contains pnpm-oriented keys (`shamefully-hoist`, `shell-emulator`). npm warns about them but the repo still builds.

## Config split that matters

- `docs/.vuepress/config.js` contains VuePress/app-level config. Its header comments explicitly say edits here restart the dev server.
- `docs/.vuepress/plume.config.js` contains theme-level config. Its header comments explicitly say edits hot-update when supported.
- Do not duplicate the same setting in both files. `plume.config.js` overrides overlapping config from `config.js`.

## Navigation and content gotchas

- Navbar and sidebar are manually curated in `docs/.vuepress/navbar.js` and `docs/.vuepress/collections.js`; they are not inferred from the file tree.
- `collections.js` is high-risk: sidebar visibility depends on page permalink prefixes matching `collection.linkPrefix`.
- For Chinese docs, the collection is `dir: "wiki"` with `linkPrefix: "/wiki/"`. If you add or move pages under `docs/wiki/`, keep their permalinks aligned with that prefix or the sidebar can silently break.
- `collections.js` comments also state collections should be configured before starting VuePress because the theme reads them at startup to generate permalinks.
- The English collection is still scaffold-like: `docs/en/wiki/` is empty, while `docs/README.md` links to `/en/wiki/intro/` and `collections.js` still uses placeholder sidebar entries.

## Repo-specific behavior worth remembering

- Homepage entrypoint is `docs/README.md`.
- Client-side customization entrypoint is `docs/.vuepress/client.js`, which loads `docs/.vuepress/theme/styles/index.scss` and registers `RepoCard`.
- Search is local search (`search: { provider: "local" }`).
- Enabled markdown/theme features include annotation, plot, bilibili, mermaid, timeline, and enhanced tables.

## CI and deploy facts

- The only checked-in workflow is `.github/workflows/deploy.yml`.
- It triggers on pushes to the `wiki` branch and on manual dispatch.
- CI flow is: checkout with `fetch-depth: 0` -> setup Node 20 -> `npm ci` -> `npm run docs:build`.
- Built output is `docs/.vuepress/dist`.
- Deploy targets `gh_pages` on `TartaricAcid/TouhouLittleMaid`, not this repo itself.
- `fetch-depth: 0` is intentional for git-history-backed metadata such as contributors / last-updated style features.

## Validation

- There is no repo-local lint or test setup in the checked-in top-level config. The meaningful validation path is `npm ci` (when dependencies need syncing) then `npm run docs:build`.
- If previewing built output, run `npm run docs:preview` only after a successful build.
