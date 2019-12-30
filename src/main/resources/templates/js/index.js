$(function(){

    $("#pull").click(function(){
        let kindleEmail = $("#kindleEmail").val();
        if(kindleEmail == undefined || kindleEmail==""){
            alert("kindleEmail不能为空");
            return;
        }
        let sendEmail = $("#sendEmail").val();
        if(sendEmail == undefined || sendEmail==""){
            alert("sendEmail不能为空");
            return;
        }
        let password = $("#password").val();
        if(password == undefined || password==""){
            alert("邮箱密码不能为空");
            return;
        }
        let files = $("#file")[0].files;
        if(files!= undefined && files.length == 0){
            alert("文件不能为空");
            return;
        }
        var formData = new FormData();

        for (var i = 0, len = files.length; i < len; i++) {
            formData.append('files', files[i]);
        }
        formData.append("kindleEmail", kindleEmail);
        formData.append("sendEmail", sendEmail);
        formData.append("password", password);
        $.ajax({
            url:"/pull",
            dataType:'json',
            type:'POST',
            async: false,
            data:formData,
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            success:function(data){
                console.log(data)
                alert(data);
            }
        });
    });
    window.setInterval(function() {
        $.ajax({
            url:"/status",
            dataType:'json',
            type:'POST',
            async: false,
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            success:function(data){
                console.log(data)
                if(data!=null){
                    $("#table").empty()
                    dataStr = JSON.stringify(data)
                    dataJson = JSON.parse(dataStr)
                    $("#table").prepend("<tr><th>书名</th><th>状态</th></tr>")
                    for(var key in dataJson){
                        var value = dataJson[key];
                        var status = value=="等待推送"?"active":(value=="推送中"?"success":(value=="推送失败"?"danger":"info"))
                        $("#table").append("<tr class=\""+status+"\"><td>"+key+"</td><td>"+dataJson[key]+"</td></tr>")
                    }
                }

            }
        });
    },10000)
});