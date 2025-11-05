/**
 * @see https://theme-plume.vuejs.press/config/navigation/ 查看文档了解配置详情
 *
 * Navbar 配置文件，它在 `.vuepress/plume.config.js` 中被导入。
 */

import {defineNavbarConfig} from "vuepress-theme-plume";

export const zhNavbar = defineNavbarConfig([
    {text: "首页", link: "/"}
]);

export const enNavbar = defineNavbarConfig([
    {text: "Home", link: "/en/"}
]);

