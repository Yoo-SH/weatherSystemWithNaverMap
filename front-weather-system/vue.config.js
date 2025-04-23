const { defineConfig } = require('@vue/cli-service')
const webpack = require('webpack')

module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: false,
        __VUE_OPTIONS_API__: true,
        __VUE_PROD_DEVTOOLS__: false
      })
    ]
  },
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:3000', // 백엔드 서버 주소 (application.properties에서 확인한 포트)
        changeOrigin: true,
        logLevel: 'debug'
      }
    }
  }
})
