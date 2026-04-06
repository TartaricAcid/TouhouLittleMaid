/**
 * 查看以下文档了解主题配置
 * - @see https://theme-plume.vuejs.press/config/intro/ 配置说明
 * - @see https://theme-plume.vuejs.press/config/theme/ 主题配置项
 *
 * 请注意，对此文件的修改都会重启 vuepress 服务。
 * 部分配置项的更新没有必要重启 vuepress 服务，建议请在 `.vuepress/config.js` 文件中配置
 *
 * 特别的，请不要在两个配置文件中重复配置相同的项，当前文件的配置项会被覆盖
 */

import {viteBundler} from "@vuepress/bundler-vite";
import {defineUserConfig} from "vuepress";
import {plumeTheme} from "vuepress-theme-plume";

export default defineUserConfig({
    base: "/",
    lang: "zh-CN",
    locales: {
        "/": {
            title: "车万女仆文档",
            lang: "zh-CN",
            description: "主要以模组二次开发与模型制作相关内容为主的文档站点",
        },
        "/en/": {
            title: "Touhou Little Maid Wiki",
            lang: "en-US",
            description: "The Touhou Little Maid website primarily focuses on mod secondary development and model creation.",
        },
    },

    head: [
        // 配置站点图标
        ["link", {rel: "icon", type: "image/png", href: "https://theme-plume.vuejs.press/favicon-32x32.png"}],
        ["link", {
            rel: "stylesheet",
            href: "https://google-fonts.mirrors.sjtug.sjtu.edu.cn/css2?family=JetBrains+Mono&family=Noto+Sans+SC&family=Noto+Serif+SC:wght@900&display=swap"
        }]
    ],

    bundler: viteBundler(),
    shouldPrefetch: false, // 站点较大，页面数量较多时，不建议启用

    theme: plumeTheme({
        /* 添加您的部署域名, 有助于 SEO, 生成 sitemap */
        // hostname: 'https://your_site_url',

        /* 文档仓库配置，用于 editLink */
        docsRepo: "https://github.com/TartaricAcid/TouhouLittleMaid",
        docsDir: "docs",
        docsBranch: "wiki",

        /* 页内信息 */
        editLink: true,
        // lastUpdated: true,
        contributors: true,
        // changelog: false,

        /**
         * 编译缓存，加快编译速度
         * @see https://theme-plume.vuejs.press/config/basic/#cache
         */
        cache: "filesystem",

        /**
         * 为 markdown 文件自动添加 frontmatter 配置
         * @see https://theme-plume.vuejs.press/config/basic/#autofrontmatter
         */
        // autoFrontmatter: {
        //   permalink: true,  // 是否生成永久链接
        //   createTime: true, // 是否生成创建时间
        //   title: true,      // 是否生成标题
        // },

        /* 本地搜索, 默认启用 */
        search: {provider: "local"},

        /**
         * Algolia DocSearch
         * 启用此搜索需要将 本地搜索 search 设置为 false
         * @see https://theme-plume.vuejs.press/config/plugins/search/#algolia-docsearch
         */
        // search: {
        //   provider: 'algolia',
        //   appId: '',
        //   apiKey: '',
        //   indices: [''],
        // },

        /**
         * Shiki 代码高亮
         * @see https://theme-plume.vuejs.press/config/plugins/code-highlight/
         */
        codeHighlighter: {
            // twoslash: true, // 启用 twoslash
            // whitespace: true, // 启用 空格/Tab 高亮
            lineNumbers: true, // 启用行号
        },

        /* 文章字数统计、阅读时间，设置为 false 则禁用 */
        // readingTime: true,

        /**
         * markdown
         * @see https://theme-plume.vuejs.press/config/markdown/
         */
        markdown: {
            field: true,
            //   abbr: true,         // 启用 abbr 语法  *[label]: content
            annotation: true,   // 启用 annotation 语法  [+label]: content
            //   pdf: true,          // 启用 PDF 嵌入 @[pdf](/xxx.pdf)
            //   caniuse: true,      // 启用 caniuse 语法  @[caniuse](feature_name)
            plot: true,         // 启用隐秘文本语法 !!xxxx!!
            bilibili: true,     // 启用嵌入 bilibili视频 语法 @[bilibili](bid)
            //   youtube: true,      // 启用嵌入 youtube视频 语法 @[youtube](video_id)
            //   artPlayer: true,    // 启用嵌入 artPlayer 本地视频 语法 @[artPlayer](url)
            //   audioReader: true,  // 启用嵌入音频朗读功能 语法 @[audioReader](url)
            //   icon: { provider: 'iconify' },        // 启用内置图标语法  ::icon-name::
            //   table: true,        // 启用表格增强容器语法 ::: table
            //   codepen: true,      // 启用嵌入 codepen 语法 @[codepen](user/slash)
            //   replit: true,       // 启用嵌入 replit 语法 @[replit](user/repl-name)
            //   codeSandbox: true,  // 启用嵌入 codeSandbox 语法 @[codeSandbox](id)
            //   jsfiddle: true,     // 启用嵌入 jsfiddle 语法 @[jsfiddle](user/id)
            //   npmTo: true,        // 启用 npm-to 容器  ::: npm-to
            //   demo: true,         // 启用 demo 容器  ::: demo
            //   repl: {             // 启用 代码演示容器
            //     go: true,         // ::: go-repl
            //     rust: true,       // ::: rust-repl
            //     kotlin: true,     // ::: kotlin-repl
            //     python: true,     // ::: python-repl
            //   },
            //   math: {             // 启用数学公式
            //     type: 'katex',
            //   },
            //   chartjs: true,      // 启用 chart.js
            //   echarts: true,      // 启用 ECharts
            mermaid: true,      // 启用 mermaid
            //   flowchart: true,    // 启用 flowchart
            //   image: {
            //     figure: true,     // 启用 figure
            //     lazyload: true,   // 启用图片懒加载
            //     mark: true,       // 启用图片标记
            //     size: true,       // 启用图片大小
            //   },
            //   include: true,      // 在 Markdown 文件中导入其他 markdown 文件内容
            //   imageSize: 'local', // 启用 自动填充 图片宽高属性，避免页面抖动
            timeline: true,
            table: {
                // 表格默认对齐方式 'left' | 'center' | 'right'
                align: "center",
                // 表格宽度是否为最大内容宽度
                // 行内元素不再自动换行，超出容器宽度时表格显示滚动条
                maxContent: false,
                // 表格宽度默认占据整行
                fullWidth: true,
                /**
                 * 复制为 html/markdown
                 * true 相当于 `all`，相当于同时启用 html 和 markdown
                 */
                copy: false, // true | 'all' | 'html' | 'md'
            }
        },

        /**
         * 水印
         * @see https://theme-plume.vuejs.press/guide/features/watermark/
         */
        // watermark: true,

        /**
         * 评论 comments
         * @see https://theme-plume.vuejs.press/guide/features/comments/
         */
        // comment: {
        //   provider: '', // "Artalk" | "Giscus" | "Twikoo" | "Waline"
        //   comment: true,
        //   repo: '',
        //   repoId: '',
        //   category: '',
        //   categoryId: '',
        //   mapping: 'pathname',
        //   reactionsEnabled: true,
        //   inputPosition: 'top',
        // },

        /**
         * 资源链接替换
         * @see https://theme-plume.vuejs.press/guide/features/replace-assets/
         */
        // replaceAssets: 'https://cdn.example.com',

        /**
         * 加密功能
         * @see https://theme-plume.vuejs.press/guide/features/encryption/
         */
        // encrypt: {},
    }),
});
