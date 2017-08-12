'user strict';

app.controller('picCtrl',function($scope,$mdDialog){
	$scope.myDataSource = {
    chart: {
        caption: "Age profile of website visitors",
        subcaption: "Last Year",
        startingangle: "120",
        showlabels: "0",
        showlegend: "1",
        enablemultislicing: "0",
        slicingdistance: "15",
        showpercentvalues: "1",
        showpercentintooltip: "0",
        plottooltext: "数量: $value",
        //theme: "fint"
    },
    data: [
        {
            label: "已反馈",
            value: "1250400"
        },
        {
            label: "未反馈",
            value: "1463300"
        }
    ]
}
	$scope.events = {
    	dataplotclick: function (ev, props) {
        $scope.$apply(function () {
        	if(!isNaN(props.chartX)){;
            $mdDialog.show({
       			clickOutsideToClose: true,
         		templateUrl:"templates/simpleChart.html",
         		controller:"simpleChartCtrl"
        	});
        	}
        });
    }}

});