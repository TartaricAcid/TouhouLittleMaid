/**
 * 查看以下文档了解主题配置
 * - @see https://theme-plume.vuejs.press/config/intro/ 配置说明
 * - @see https://theme-plume.vuejs.press/config/theme/ 主题配置项
 *
 * 请注意，对此文件的修改不会重启 vuepress 服务，而是通过热更新的方式生效
 * 但同时部分配置项不支持热更新，请查看文档说明
 * 对于不支持热更新的配置项，请在 `.vuepress/config.js` 文件中配置
 *
 * 特别的，请不要在两个配置文件中重复配置相同的项，当前文件的配置项会覆盖 `.vuepress/config.js` 文件中的配置
 */

import {defineThemeConfig} from "vuepress-theme-plume";
import {enCollections, zhCollections} from "./collections";
import {enNavbar, zhNavbar} from "./navbar";

/**
 * @see https://theme-plume.vuejs.press/config/basic/
 */
export default defineThemeConfig({
    logo: "https://theme-plume.vuejs.press/plume.png",

    appearance: true,  // 配置 深色模式

    social: [
        {icon: "github", link: "https://github.com/TartaricAcid/TouhouLittleMaid"},
        {icon: "discord", link: "https://discord.gg/ysm-team"},
        {icon: "qq", link: "https://qm.qq.com/q/DxixtQH3Mc"},
        {
            icon: {
                name: "curseforge",
                svg: "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\"><path fill=\"currentColor\" d=\"M18.326 9.215s4.9-.773 5.674-3.027h-7.507V4.4H0l2.032 2.358v2.415s5.127-.266 7.11 1.237c2.714 2.516-3.053 5.917-3.053 5.917l-.99 3.273c1.547-1.473 4.494-3.377 9.899-3.286c-2.057.65-4.125 1.665-5.735 3.286h10.925l-1.029-3.273s-7.918-4.668-.833-7.112\"/></svg>"
            },
            link: "https://www.curseforge.com/minecraft/mc-mods/touhou-little-maid"
        },
        {
            icon: {
                name: "modrinth",
                svg: "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\"><path fill=\"currentColor\" d=\"M12.252.004a11.78 11.768 0 0 0-8.92 3.73a11 11 0 0 0-2.17 3.11a11.37 11.359 0 0 0-1.16 5.169c0 1.42.17 2.5.6 3.77c.24.759.77 1.899 1.17 2.529a12.3 12.298 0 0 0 8.85 5.639c.44.05 2.54.07 2.76.02c.2-.04.22.1-.26-1.7l-.36-1.37l-1.01-.06a8.5 8.489 0 0 1-5.18-1.8a5.3 5.3 0 0 1-1.3-1.26c0-.05.34-.28.74-.5a37.572 37.545 0 0 1 2.88-1.629c.03 0 .5.45 1.06.98l1 .97l2.07-.43l2.06-.43l1.47-1.47c.8-.8 1.48-1.5 1.48-1.52c0-.09-.42-1.63-.46-1.7c-.04-.06-.2-.03-1.02.18c-.53.13-1.2.3-1.45.4l-.48.15l-.53.53l-.53.53l-.93.1l-.93.07l-.52-.5a2.7 2.7 0 0 1-.96-1.7l-.13-.6l.43-.57c.68-.9.68-.9 1.46-1.1c.4-.1.65-.2.83-.33c.13-.099.65-.579 1.14-1.069l.9-.9l-.7-.7l-.7-.7l-1.95.54c-1.07.3-1.96.53-1.97.53c-.03 0-2.23 2.48-2.63 2.97l-.29.35l.28 1.03c.16.56.3 1.16.31 1.34l.03.3l-.34.23c-.37.23-2.22 1.3-2.84 1.63c-.36.2-.37.2-.44.1c-.08-.1-.23-.6-.32-1.03c-.18-.86-.17-2.75.02-3.73a8.84 8.84 0 0 1 7.9-6.93c.43-.03.77-.08.78-.1c.06-.17.5-2.999.47-3.039c-.01-.02-.1-.02-.2-.03Zm3.68.67c-.2 0-.3.1-.37.38c-.06.23-.46 2.42-.46 2.52c0 .04.1.11.22.16a8.51 8.499 0 0 1 2.99 2a8.38 8.379 0 0 1 2.16 3.449a6.9 6.9 0 0 1 .4 2.8c0 1.07 0 1.27-.1 1.73a9.4 9.4 0 0 1-1.76 3.769c-.32.4-.98 1.06-1.37 1.38c-.38.32-1.54 1.1-1.7 1.14c-.1.03-.1.06-.07.26c.03.18.64 2.56.7 2.78l.06.06a12.07 12.058 0 0 0 7.27-9.4c.13-.77.13-2.58 0-3.4a11.96 11.948 0 0 0-5.73-8.578c-.7-.42-2.05-1.06-2.25-1.06Z\"/></svg>"
            },
            link: "https://modrinth.com/mod/touhou-little-maid"
        }
    ],
    navbarSocialInclude: ["github", "discord", "qq", "modrinth", "curseforge"], // 允许显示在导航栏的 social 社交链接
    // aside: true, // 页内侧边栏， 默认显示在右侧
    // outline: [2, 3], // 页内大纲， 默认显示 h2, h3

    /**
     * 文章版权信息
     * @see https://theme-plume.vuejs.press/guide/features/copyright/
     */
    // copyright: true,

    // prevPage: true,   // 是否启用上一页链接
    // nextPage: true,   // 是否启用下一页链接
    // createTime: true, // 是否显示文章创建时间

    /* 站点页脚 */
    // footer: {
    //   message: 'Power by <a target="_blank" href="https://v2.vuepress.vuejs.org/">VuePress</a> & <a target="_blank" href="https://theme-plume.vuejs.press">vuepress-theme-plume</a>',
    //   copyright: '',
    // },

    /* 过渡动画 @see https://theme-plume.vuejs.press/config/basic/#transition */
    // transition: {
    //   page: true,        // 启用 页面间跳转过渡动画
    //   postList: true,    // 启用 博客文章列表过渡动画
    //   appearance: 'fade',  // 启用 深色模式切换过渡动画, 或配置过渡动画类型
    // },

    locales: {
        "/": {
            /**
             * @see https://theme-plume.vuejs.press/config/basic/#profile
             */
            profile: {
                avatar: "https://theme-plume.vuejs.press/plume.png",
                name: "车万女仆文档",
                description: "主要以模组二次开发与模型制作相关内容为主的文档站点",
                // circle: true,
                // location: '',
                // organization: '',
            },

            navbar: zhNavbar,
            collections: zhCollections,

            /**
             * 公告板
             * @see https://theme-plume.vuejs.press/guide/features/bulletin/
             */
            // bulletin: {
            //   layout: 'top-right',
            //   contentType: 'markdown',
            //   title: '',
            //   content: '',
            // },
        },
        "/en/": {
            /**
             * @see https://theme-plume.vuejs.press/config/basic/#profile
             */
            profile: {
                avatar: "https://theme-plume.vuejs.press/plume.png",
                name: "Touhou Little Maid Wiki",
                description: "The Touhou Little Maid website primarily focuses on mod secondary development and model creation.",
                // circle: true,
                // location: '',
                // organization: '',
            },

            navbar: enNavbar,
            collections: enCollections,

            /**
             * 公告板
             * @see https://theme-plume.vuejs.press/guide/features/bulletin/
             */
            // bulletin: {
            //   layout: 'top-right',
            //   contentType: 'markdown',
            //   title: '',
            //   content: '',
            // },
        },
    },
});
