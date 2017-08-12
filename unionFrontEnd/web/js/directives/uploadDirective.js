'use strict';

app.directive('uploadFile', function() {
    return {
    	restrict:'AE',
    	scope:{
    		table:'@',
    		key:'@',
    		id:'@',
            type:'@'
    	},
        templateUrl: 'templates/uploadFile.html',
        controller:'uploadCtrl'
  };
});