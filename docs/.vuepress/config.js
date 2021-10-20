module.exports = {
    lang: 'en-US',
    title: 'Touhou Little Maid Dev Wiki',
    description: 'There is a wiki related to Touhou Little Maid mod development',
    base: '/TouhouLittleMaid/',
    themeConfig: {
        locales: {
            '/en/': {
                selectText: 'Languages',
                label: 'English',
                ariaLabel: 'Languages',
                nav: [{
                    text: 'Home',
                    link: './'
                }, {
                    text: 'GitHub',
                    link: 'https://github.com/TartaricAcid/TouhouLittleMaid'
                }, {
                    text: 'CurseForge',
                    link: 'https://www.curseforge.com/minecraft/mc-mods/touhou-little-maid'
                }],
                sidebar: [{
                    title: 'Collapse',
                    children: [
                        ['./en/', 'Home']
                    ]
                }]
            },
            '/zh/': {
                selectText: '选择语言',
                label: '简体中文',
                nav: [{
                    text: '主页',
                    link: './'
                }, {
                    text: 'GitHub',
                    link: 'https://github.com/TartaricAcid/TouhouLittleMaid'
                }, {
                    text: 'CurseForge',
                    link: 'https://www.curseforge.com/minecraft/mc-mods/touhou-little-maid'
                }],
                sidebar: [{
                    title: '收起',
                    children: [
                        ['./zh/', '主页']
                    ]
                }]
            }
        }
    },
    locales: {
        '/en/': {
            lang: 'en-US',
            title: 'Touhou Little Maid Dev Wiki',
            description: 'There is a wiki related to Touhou Little Maid mod development'
        },
        '/zh/': {
            lang: 'zh-CN',
            title: 'Touhou Little Maid 开发 Wiki',
            description: '有关于车万女仆的开发文档'
        }
    }
}
