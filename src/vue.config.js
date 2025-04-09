const path = require('path')
const webpack = require('webpack')

module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/smart-heating/' : '/',
  outputDir: 'dist',
  assetsDir: 'static',

  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://your-heating-api.com',
        changeOrigin: true,
        pathRewrite: { '^/api': '' }
      }
    },
    hot: true,
    historyApiFallback: {
      disableDotRule: true
    }
  },

  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
        process: 'process/browser' // 显式提供 process polyfill
      }),
      new webpack.DefinePlugin({
        'process.env': {
          NODE_ENV: JSON.stringify(process.env.NODE_ENV || 'development'),
          VUE_APP_ENV: JSON.stringify(process.env.VUE_APP_ENV || 'dev'),
          BASE_URL: JSON.stringify(process.env.BASE_URL || '/')
        }
      })
    ],
    resolve: {
      alias: {
        // 确保路径兼容性
        '@': path.resolve(__dirname, 'src')
      },
      fallback: {
        "path": require.resolve("path-browserify"), // 浏览器环境 path 兼容
        "process": require.resolve("process/browser") // 显式指定 process polyfill
      }
    }
  },

  chainWebpack: config => {
    config.plugins.delete('prefetch')

    // 更安全的环境变量注入方式
    config.plugin('define').tap(definitions => {
      Object.assign(definitions[0]['process.env'], {
        NODE_ENV: JSON.stringify(process.env.NODE_ENV || 'development'),
        VUE_APP_ENV: JSON.stringify(process.env.VUE_APP_ENV || 'dev')
      })
      return definitions
    })

    if (process.env.NODE_ENV === 'production') {
      config.plugin('compression').use('compression-webpack-plugin', [{
        algorithm: 'gzip',
        test: /\.(js|css|json|html)$/,
        threshold: 10240
      }])
    }
  },

  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'scss',
      patterns: [
        path.resolve(__dirname, 'src/styles/variables.scss')
      ]
    }
  }
}