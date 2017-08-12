'user strict';

app.controller('qrcodeProCtrl',function($scope,$mdDialog) {
	$scope.title='zengxiaopang';
	/*
	$scope.qrcode={
		v:4,
		e:'M',
		s:274,
		//bar:'https://github.com/monospaced/angular-qrcode',
		foo:'hahahahahahhahahaha'
	}*/
	$scope.closeDialog = function(){
    	$mdDialog.hide();
    }

});