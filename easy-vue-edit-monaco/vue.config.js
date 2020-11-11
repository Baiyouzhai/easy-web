const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

module.exports = {
    devServer: {
        proxy: {
            '/edit/getProperties': {
                target: 'http://localhost:12580',
                ws: true,
                changeOrigin: true
            }
        }
    },
    configureWebpack: {
        plugins: [
            new MonacoWebpackPlugin()
        ]
    }
}