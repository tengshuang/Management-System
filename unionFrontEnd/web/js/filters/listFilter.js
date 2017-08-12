'user strict';

app.filter("listFilter", function () {
    return function (input, search) {
    	if(!search)
    		return input;
    	var ret = [];
        for(var i = 0; i < input.length; ++i) {
        	if(input[i].name && input[i].name.indexOf(search) >= 0)
        		ret.push(input[i]);
        }
        return ret;
    }
});
