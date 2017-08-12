'user strict';

app.filter("userFilter", function () {
    return function (input, search) {
    	if(!search)
    		return input;
    	var ret = [];
        for(var i = 0; i < input.length; ++i) {
        	if(input[i].realname && input[i].realname.indexOf(search) >= 0)
        		ret.push(input[i]);
        }
        return ret;
    }
});
