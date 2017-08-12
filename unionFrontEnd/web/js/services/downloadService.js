'user strict'

app.service('downloadService', function($mdDialog) {
    this.download = function(file_id){
		console.log(file_id);

		$mdDialog.show({
       		clickOutsideToClose: true,
          	locals:{
          		file_id :file_id
          	},
         	templateUrl:"templates/download.html",
         	controller:"downloadCtrl"
        });
	}
});