'user strict';

app.controller('simpleChartCtrl',function($scope,dataX,titleX,subTitleX){
    console.log(dataX);
    $scope.myDataSource = {
    chart: {
        caption: titleX,
        subCaption: subTitleX,
        numberScaleValue: "60,60,24",
        numberScaleUnit: "分钟,小时,天",
        defaultNumberScale: "秒",
        theme: "fint",
    },
    data:dataX
    };

});