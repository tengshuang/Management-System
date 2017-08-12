'use strict';

app.directive('uploadFile', function() {
    return {
    	restrict:'AE',
    	scope:{
    		table:'@',
    		key:'@',
    		id:'@'
    	},
        templateUrl: 'demo/templates/uploadFile.html',
        controller:'uploadCtrl'
  };
});