module.exports = {
    lang: 'en-US',
    title: 'Touhou Little Maid Dev Wiki',
    description: 'There is a wiki related to Touhou Little Maid mod development',
    base: '/TouhouLittleMaid/',
    themeConfig: {
        locales: {
            '/': {
                selectText: 'Languages',
                label: 'English',
                ariaLabel: 'Languages',
                lastUpdated: 'Last Updated',
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
                    title: 'Custom Model',
                    children: [
                        ['./custom_model_pack', 'Custom Model Pack'],
                        ['./maid_model_pack_details', 'Maid Model Pack Details'],
                        ['./chair_model_pack_details', 'Chair Model Pack Details'],
                        ['./custom_javascript_animation', 'Custom JavaScript Animation'],
                        ['./preset_animation', 'Preset Animation']
                    ]
                }, {
                    title: 'Modify Recipes',
                    children: [
                        ['./custom_altar_recipes', 'Custom Altar Recipes']
                    ]
                }]
            },
            '/zh_CN/': {
                selectText: '选择语言',
                label: '简体中文',
                lastUpdated: '上次更新',
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
                    title: '自定义模型',
                    children: [
                        ['./zh_CN/', '主页']
                    ]
                }]
            }
        }
    },
    locales: {
        '/': {
            lang: 'en-US',
            title: 'Touhou Little Maid Dev Wiki',
            description: 'There is a wiki related to Touhou Little Maid mod development'
        },
        '/zh_CN/': {
            lang: 'zh-CN',
            title: 'Touhou Little Maid 开发 Wiki',
            description: '有关于车万女仆的开发文档'
        }
    },
    markdown: {
        lineNumbers: true
    }
}
