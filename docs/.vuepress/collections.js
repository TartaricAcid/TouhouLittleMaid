/**
 * @see https://theme-plume.vuejs.press/guide/collection/ 查看文档了解配置详情。
 *
 * Collections 配置文件，它在 `.vuepress/plume.config.js` 中被导入。
 *
 * 请注意，你应该先在这里配置好 Collections，然后再启动 vuepress，主题会在启动 vuepress 时，
 * 读取这里配置的 Collections，然后在与 Collection 相关的 Markdown 文件中，自动生成 permalink。
 *
 * collection 的  type 为 `post` 时，表示为 文档列表类型（即没有侧边导航栏，有文档列表页）
 * 可用于实现如 博客、专栏 等以文章列表聚合形式的文档集合 （内容相对碎片化的）
 *
 * collection 的 type 为 `doc` 时，表示为文档类型（即有侧边导航栏）
 * 可用于实现如 笔记、知识库、文档等以侧边导航栏形式的文档集合 （内容强关联、成体系的）
 * 如果发现 侧边栏没有显示，那么请检查你的配置是否正确，以及 Markdown 文件中的 permalink
 * 是否是以对应的 Collection 配置的 link 的前缀开头。 是否展示侧边栏是根据 页面链接 的前缀 与 `collection.link`
 * 的前缀是否匹配来决定。
 */

/**
 * 在受支持的 IDE 中会智能提示配置项。
 *
 * - `defineCollections` 是用于定义 collection 集合的帮助函数
 * - `defineCollection` 是用于定义单个 collection 配置的帮助函数
 *
 * 通过 `defineCollection` 定义的 collection 配置，应该填入 `defineCollections` 中
 */
import {defineCollection, defineCollections} from "vuepress-theme-plume";

/* =================== locale: zh-CN ======================= */

const zhDoc = defineCollection({
    // doc 类型，该类型带有侧边栏
    type: "doc",
    // 文档集合所在目录，相对于 `docs/`
    dir: "wiki",
    // `dir` 所指向的目录中的所有 markdown 文件，其 permalink 需要以 `linkPrefix` 配置作为前缀
    // 如果 前缀不一致，则无法生成侧边栏。
    // 所以请确保  markdown 文件的 permalink 都以 `/` + `linkPrefix` 开头
    linkPrefix: "/wiki/",
    // 文档标题，它将用于在页面的面包屑导航中显示
    title: "wiki",
    // 手动配置侧边栏结构
    sidebar: [
        {
            text: "新手入门",
            collapsed: false,
            icon: "carbon:idea",
            items: [
                "简介"
            ],
        },
        {
            text: "模型制作",
            collapsed: false,
            icon: "file-icons:3d-model",
            prefix: "model",
            items: [
                "如何开始"
            ],
        },
        {
            text: "附属开发",
            collapsed: false,
            prefix: "dev",
            icon: "ri:java-line",
            items: [
                "如何开始",
                "添加与女仆交互的工具",
                "添加女仆饰品",
                "添加自定义聊天气泡",
                "添加施法动画",
                "添加新的工作模式（上）",
                "添加新的工作模式（中）",
            ],
        },
        {
            text: "数据包制作",
            collapsed: false,
            icon: "si:json-fill",
            prefix: "datapack",
            items: [
                "如何开始",
                "修改合成表",
                "修改标签",
                "添加颜文字",
                "添加表情包",
                "添加残局"
            ],
        },
        {
            text: "整合包制作",
            collapsed: false,
            icon: "ri:javascript-line",
            prefix: "modpack",
            items: [
                "如何开始",
                "KubeJS 祭坛合成",
                "KubeJS 事件系统",
                "KubeJS 注册饰品与提示",
                "KubeJS 自定义工作模式",
            ],
        },
        {
            text: "更新日志",
            collapsed: true,
            icon: "ix:log",
            prefix: "log",
            items: [
                "1.5.1 更新日志",
                "1.5.0 更新日志",
                "1.4.6 更新日志",
                "1.4.5 更新日志",
                "1.4.4 更新日志",
                "1.4.3 更新日志",
                "1.4.2 更新日志",
                "1.4.0 更新日志",
                "1.3.8 更新日志",
                "1.3.7 更新日志",
                "1.3.6 更新日志",
                "1.3.5 更新日志",
                "1.3.3 更新日志",
                "1.3.2 更新日志",
                "1.3.1 更新日志",
                "1.3.0 更新日志",
            ],
        },
    ]
});

/**
 * 导出所有的 collections
 */
export const zhCollections = defineCollections([
    zhDoc,
]);

/* =================== locale: en-US ======================= */

const enDemoDoc = defineCollection({
    // doc 类型，该类型带有侧边栏
    type: "doc",
    // 文档集合所在目录，相对于 `docs/en/`
    dir: "wiki",
    // `dir` 所指向的目录中的所有 markdown 文件，其 permalink 需要以 `linkPrefix` 配置作为前缀
    // 如果 前缀不一致，则无法生成侧边栏。
    // 所以请确保  markdown 文件的 permalink 都以 `/en/` + `linkPrefix` 开头
    linkPrefix: "/wiki",
    // 文档标题，它将用于在页面的面包屑导航中显示
    title: "wiki",
    // 手动配置侧边栏结构
    sidebar: ["", "foo", "bar"]
});

/**
 * 导出所有的 collections
 */
export const enCollections = defineCollections([
    enDemoDoc,
]);

