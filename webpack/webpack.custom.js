const path = require('path');
const webpack = require('webpack');
const { merge } = require('webpack-merge');
const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer');
const WebpackNotifierPlugin = require('webpack-notifier');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');

const environment = require('./environment');
const proxyConfig = require('./proxy.conf');

module.exports = async (config, options, targetOptions) => {
  // 添加 ESLint 和通知插件
  if (config.mode === 'development') {
    config.plugins.push(
      new ESLintPlugin({
        configType: 'flat',
        extensions: ['ts', 'js', 'html'],
      }),
      new WebpackNotifierPlugin({
        title: 'app Group 40',
        contentImage: path.join(__dirname, 'Buy2Use2.png'),
      }),
    );
  }

  // ✅ 正确插入 CSP 头部
  if (!config.devServer) {
    config.devServer = {};
  }
  config.devServer.headers = {
    ...(config.devServer.headers || {}),
    'Content-Security-Policy': `
      default-src 'self';
      script-src 'self' 'unsafe-inline' 'unsafe-eval' https://maps.googleapis.com https://maps.gstatic.com https://storage.googleapis.com;
      style-src 'self' 'unsafe-inline' https://fonts.googleapis.com;
      font-src 'self' https://fonts.gstatic.com;
      img-src 'self' data: https://maps.gstatic.com https://maps.googleapis.com;
      frame-src 'self' https://storage.googleapis.com;
    `
      .replace(/\n/g, '')
      .trim(),
  };

  // 代理配置
  const tls = config.devServer?.server?.type === 'https';
  if (config.devServer) {
    config.devServer.proxy = proxyConfig({ tls });
  }

  // 浏览器同步
  if (targetOptions.target === 'serve' || config.watch) {
    config.plugins.push(
      new BrowserSyncPlugin(
        {
          host: 'localhost',
          port: 9000,
          https: tls,
          proxy: {
            target: `http${tls ? 's' : ''}://localhost:${targetOptions.target === 'serve' ? '4200' : '8080'}`,
            ws: true,
            proxyOptions: {
              changeOrigin: false,
            },
            proxyReq: [
              function (proxyReq) {
                proxyReq.setHeader('X-Forwarded-Host', 'localhost:9000');
                proxyReq.setHeader('X-Forwarded-Proto', 'https');
              },
            ],
          },
          socket: {
            clients: {
              heartbeatTimeout: 60000,
            },
          },
        },
        {
          reload: targetOptions.target === 'build',
        },
      ),
    );
  }

  // 生产模式分析器
  if (config.mode === 'production') {
    config.plugins.push(
      new BundleAnalyzerPlugin({
        analyzerMode: 'static',
        openAnalyzer: false,
        reportFilename: '../../stats.html',
      }),
    );
  }

  // 静态资源复制
  const patterns = [
    {
      context: require('swagger-ui-dist').getAbsoluteFSPath(),
      from: '*.{js,css,html,png}',
      to: 'swagger-ui/',
      globOptions: { ignore: ['**/index.html'] },
    },
    {
      from: path.join(path.dirname(require.resolve('axios/package.json')), 'dist/axios.min.js'),
      to: 'swagger-ui/',
    },
    { from: './src/main/webapp/swagger-ui/', to: 'swagger-ui/' },
  ];

  if (patterns.length > 0) {
    config.plugins.push(new CopyWebpackPlugin({ patterns }));
  }

  config.plugins.push(
    new webpack.DefinePlugin({
      __VERSION__: JSON.stringify(environment.__VERSION__),
      __DEBUG_INFO_ENABLED__: environment.__DEBUG_INFO_ENABLED__ || config.mode === 'development',
      SERVER_API_URL: JSON.stringify(environment.SERVER_API_URL),
    }),
  );

  config = merge(
    config,
    // jhipster-needle-add-webpack-config - JHipster will add custom config
  );

  return config;
};
