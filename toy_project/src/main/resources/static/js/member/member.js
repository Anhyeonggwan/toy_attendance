/**
 * 
 */
 
function login(){
	
	if($("#user_id").val() == ""){
		alert("아이디를 입력해주세요.");
		return false;
	}
	
	if($("#password").val() == ""){
		alert("비밀번호를 입력해주세요.");
		return false;
	}
	
	$.ajax({
		url : "/user/login",
		type : "post",
		data : {
			user_id : $("#user_id").val(),
			password : $("#password").val()
		},
		dataType : "JSON",
		success : function(data){
			console.log(data);
			let code = data.code;
			if(code == "200"){
				
				setAccessToken("Bearer " + data.accessToken);
				
				/*$("#moveForm").attr("action", "/user/normal/pageDetail");
				$("#moveForm").attr("method", "GET");
				$("#moveForm #idx").val(data.idx);
				$("#moveForm").submit();*/
				
				window.location.href = "/user/normal/pageDetail?idx=" + data.idx;
			}else{
				alert(data.message);
			}
		}
	});
	
}

function goMemberDetail(){
	
}















