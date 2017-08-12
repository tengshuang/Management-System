'user strict'

app.service('listService', function() {

    this.empty_target = function(list){
        for(var i = 0; i < list.length; i++){
            if(list[i] == 1) return 0;
        }
        return 1;
    };

    this.getAllAct=function(tmp,base_link){
        for(var i = 0; i < tmp.length; i++) {
            var obj = tmp[i];
            if(obj.event_id != null){
                obj.jump_link = base_link + obj.event_id;
                return tmp;
            }
            obj.jump_link=base_link+obj.id;
        }
        return tmp;
    };

    this.string_convert=function(str){
        if(str == null || str == ""){

            return "无";
        }
        return str;
    }
    
    this.getAllAct_activity=function(tmp,base_link){
        for(var i = 0; i < tmp.length; i++) {
            var obj = tmp[i];
            if(obj.activity_id != null){
                obj.jump_link = base_link + obj.activity_id;
            }
        }
        return tmp;
    };

    this.merge_EandA=function(event,tmp,base_link){
        for(var i = 0; i < tmp.length; i++) {
            var obj = tmp[i];
            if(obj.id != null){
                obj.jump_link = base_link + obj.id;
                obj.department = event[i].department;
                obj.attachment = event[i].attachment;
                obj.req_qrcode = event[i].req_qrcode;
            }
        }
        return tmp;
    }
    
    this.bool_feedback = function(item){
        //console.log("aaaa");
        if(item == null) return 0;
        //console.log("aaaa");
        if(item.txt == 1) return 1;
        //console.log("aaaa");
        if(item.file == 1) return 1;
        //console.log("aaaa");
        if(item.recieve == 1) return 1;
        //console.log("aaaa");
        return 0;
    };

    this.bool_children = function(item){
        //console.log("aaaa");
        if(item == null) return 0;
        //console.log("aaaa");
        if(item.req_qrcode == 1) return 1;
        //console.log("aaaa");
        if(item.req_photo == 1) return 1;
        //console.log("aaaa");
        if(item.req_report == 1) return 1;
        //console.log("aaaa");
        return 0;
    };

    this.getList = function(list){
        var length = list.length;
        var catelist = [];
        for(var i = 0; i < length; i++){
            catelist[i] = list[i].listname;
        }
        return catelist;
    }

    this.getPostargetID = function(listObject,getPostargetS){
        var length = getPostargetS.length;
        var catelist = [];
        for(var i = 0; i < length; i++){
            catelist[i] = listObject[getPostargetS[i]-1].id;
        }
        return catelist;
    }

    this.empty=function(str){
        if (str==null) return 1;
        if (str=="") return 1;
        return 0;
    }
});