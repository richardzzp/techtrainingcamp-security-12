const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}

module.exports = {
    publicPath: './',
    assetsDir: 'static',
    productionSourceMap: false,
    chainWebpack: config => {
        config.resolve.alias
            .set("@", resolve("src"))
            .set("@components", resolve("src/components"))
            .set("@api", resolve("src/api"))
            .set("@data", resolve("public"))
            .set("@picture",resolve("src/assets/img"))
    },
    // devServer: {
    //         port: 8080,
    //         //跨域设置
    //     proxy: {
    //         '/api':{
    //             target:"tx.dwxh.xyz:8080",
    //             changeOrigin:true,
    //             pathRewrite:{
    //             }
    //         }
    //     }
    // }
}