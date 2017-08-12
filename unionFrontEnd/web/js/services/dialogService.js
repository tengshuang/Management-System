'user strict'

app.service('dialogService', function($mdDialog) {
    this.show = function(message){
        $mdDialog.show(
            $mdDialog.alert()
                .clickOutsideToClose(true)
                .title('消息')
                .textContent(message)
                .ok('确认')
        );
    }

    this.show_title = function(message,title){
        $mdDialog.show(
            $mdDialog.alert()
                .clickOutsideToClose(true)
                .title(title)
                .textContent(message)
                .ok('确认')
        );
    }

});