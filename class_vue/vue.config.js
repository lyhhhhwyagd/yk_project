let proxyObj = {}
proxyObj['/'] = {
  ws:false,
  target:'http://localhost:8080',
  changeOrigin:true,
  pathReWrite:{
    '^/': '/'
  }
}


module.exports = {
    devServer:{
      host:'localhost',
      port:8088,
      proxy: proxyObj
    }
  }