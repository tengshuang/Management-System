'user strict'

app.service('timeService', function() {
    this.pad = function (number) {
        if (number < 10) 
            return '0' + number;
        return number;
    }
    this.convert_time = function(aaa) {
        var p = new Date(aaa);
        var q = p.getFullYear() +
            '-' + this.pad(p.getMonth() + 1) +
            '-' + this.pad(p.getDate()) +
            ' ' + this.pad(p.getHours()) +
            ':' + this.pad(p.getMinutes());
        return q;
    }
});