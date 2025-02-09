
$(function(){
	loadMember()
});

function loadMember(){
	
	$.ajax({
		url : "/user/normal/userDetail",
		type : "GET",
		data : {idx : $("[name=idx]").val()},
		dataType : "JSON",
		success : function(data){
			console.log(data);
		}
		
	});
	
}