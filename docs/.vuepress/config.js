const fs = require("fs")

function getLocales(local) {
    if (local) {
        let path = `./docs/${local}/locales.json`
        if (fs.existsSync(path)) {
            return JSON.parse(fs.readFileSync(path))
        }
    }
    return JSON.parse(fs.readFileSync("./docs/locales.json"))
}

module.exports = {
    lang: 'en-US',
    title: 'Touhou Little Maid Dev Wiki',
    description: 'There is a wiki related to Touhou Little Maid mod development',
    base: '/TouhouLittleMaid/',
    themeConfig: {
        locales: {
            '/': getLocales()["themeConfig"],
            '/de_DE/': getLocales("zh_CN")["themeConfig"],
            '/es_ES/': getLocales("es_ES")["themeConfig"],
            '/fr_FR/': getLocales("fr_FR")["themeConfig"],
            '/it_IT/': getLocales("it_IT")["themeConfig"],
            '/ja_JP/': getLocales("ja_JP")["themeConfig"],
            '/ko_KR/': getLocales("ko_KR")["themeConfig"],
            '/la_LA/': getLocales("la_LA")["themeConfig"],
            '/pt_BR/': getLocales("pt_BR")["themeConfig"],
            '/pt_PT/': getLocales("pt_PT")["themeConfig"],
            '/ru_RU/': getLocales("ru_RU")["themeConfig"],
            '/tr_TR/': getLocales("tr_TR")["themeConfig"],
            '/vi_VN/': getLocales("vi_VN")["themeConfig"],
            '/zh_CN/': getLocales("zh_CN")["themeConfig"],
        }
    },
    locales: {
        '/': getLocales()["title"],
        '/de_DE/': getLocales("zh_CN")["title"],
        '/es_ES/': getLocales("es_ES")["title"],
        '/fr_FR/': getLocales("fr_FR")["title"],
        '/it_IT/': getLocales("it_IT")["title"],
        '/ja_JP/': getLocales("ja_JP")["title"],
        '/ko_KR/': getLocales("ko_KR")["title"],
        '/la_LA/': getLocales("la_LA")["title"],
        '/pt_BR/': getLocales("pt_BR")["title"],
        '/pt_PT/': getLocales("pt_PT")["title"],
        '/ru_RU/': getLocales("ru_RU")["title"],
        '/tr_TR/': getLocales("tr_TR")["title"],
        '/vi_VN/': getLocales("vi_VN")["title"],
        '/zh_CN/': getLocales("zh_CN")["title"],
    },
    markdown: {
        lineNumbers: true
    }
}
