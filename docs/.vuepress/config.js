const fs = require("fs")
const LOCAL = ["de_DE", "es_ES", "fr_FR", "it_IT", "ja_JP", "ko_KR", "la_LA", "pt_BR", "pt_PT", "ru_RU", "tr_TR", "vi_VN", "zh_CN"]

function getLocales(local) {
    if (local) {
        let path = `./docs/${local}/locales.json`
        if (fs.existsSync(path)) {
            let data = JSON.parse(fs.readFileSync(path));
            if (data["title"]["lang"]) {
                return data
            }
        }
    } else {
        return JSON.parse(fs.readFileSync("./docs/locales.json"))
    }
}

function getThemeConfigLocales() {
    let locales = {}
    locales["/"] = getLocales()["themeConfig"]
    for (let local of LOCAL) {
        let themeConfigLocales = getLocales(local)
        if (themeConfigLocales) {
            locales[`/${local}/`] = themeConfigLocales["themeConfig"]
        }
    }
    return locales
}

function getTitleLocales() {
    let locales = {}
    locales["/"] = getLocales()["title"]
    for (let local of LOCAL) {
        let titleLocales = getLocales(local)
        if (titleLocales) {
            locales[`/${local}/`] = titleLocales["title"]
        }
    }
    return locales
}

module.exports = {
    lang: 'en-US',
    title: 'Touhou Little Maid Dev Wiki',
    description: 'There is a wiki related to Touhou Little Maid mod development',
    base: '/TouhouLittleMaid/',
    themeConfig: {
        locales: getThemeConfigLocales()
    },
    locales: getTitleLocales(),
    markdown: {
        lineNumbers: true
    }
}
