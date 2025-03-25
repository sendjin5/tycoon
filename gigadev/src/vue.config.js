module.exports = {
    devServer: {
        proxy: {
            '/':{
                "target":'http://3.38.185.252:8080', // Spring boot의 주소 및 포트
                "pathRewrite":{'^/':''},
                "changeOrigin":true,
            }
        }
    }
}
