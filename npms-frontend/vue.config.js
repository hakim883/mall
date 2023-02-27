module.exports = {

    lintOnSave: false,

    publicPath: '/',

  

    outputDir: "dist", 

    lintOnSave: true, 

    runtimeCompiler: false, 

    transpileDependencies: [
        /* string or regex */
    ], 

    productionSourceMap: false, 

    chainWebpack: () => {},

    configureWebpack: () => {}, // CSS 

    css: {


        extract: true, // CSS source maps?

        sourceMap: false, // pass custom options to pre-processor loaders. e.g. to pass options to // sass-loader, use { sass: { ... } }

        loaderOptions: {}, // Enable CSS modules for all css / pre-processor files. // This option does not affect *.vue files.

        modules: false
    }, // use thread-loader for babel & TS in production build // enabled by default if the machine has more than 1 cores

    parallel: require("os").cpus().length > 1, 

    pwa: {}, // configure webpack-dev-server behavior

    devServer: {
        
         
        open: process.platform === "darwin",

        disableHostCheck: false,

        host: "127.0.0.1",

        port: 8088,

        https: false,

        hotOnly: false, // 
        
        // assetsSubDirectory: 'static',
        // assetsPublicPath: '/',
        // proxy: null // string | Object
        proxy: {
        //   '/Ajax_Json': {
            '/npmsBackEndServer': {
            target: 'http://127.0.0.1:8001',
            // ws: true,
            changeOrigin: true,
            pathRewrite: { '^/npmsBackEndServer': '' },
          },
        },
        // proxy: 'http://127.0.0.1:8080',
        // before: app => {}
    },

    pluginOptions: {
        // ...
        
    }
};