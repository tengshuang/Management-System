'user strict'

app.service('authenService', function(httpService,dialogService,globalUrl) {

    this.getKey = function(number){
        return httpService.get(globalUrl.getKey+"?user="+number);
    }

    this.encode = function(key,password){
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(key);
        return encrypt.encrypt(password);
        
    }

    this.loginCre=function(user){
        var data = {
                "no" : user.no,
                "password" : user.password
            };
        return httpService.post(globalUrl.LoginAccess, data);
    }

    this.signin=function(user){
        var data = {"user":user};
        return httpService.post(globalUrl.SigninAccess, data);
    }
});