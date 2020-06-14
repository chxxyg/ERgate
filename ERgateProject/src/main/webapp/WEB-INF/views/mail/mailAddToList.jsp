<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>mailAddToList</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">	
<style>
@import url(http://fonts.googleapis.com/earlyaccess/nanumgothic.css);

html, body {
	margin: 0;
	height: 100%;
	overflow-x: hidden;
	overflow-y: auto;
	font-family: "Nanum Gothic", serif;
	overflow-x: hidden;
}

/* 현재팝업창 사이즈조절 */
.popup-outer {
	width: 1250px;
	height: 860px;
	margin: 0;
	box-sizing: border-box;
}

.popup-title {
	margin: 0;
	width: 100%;
	height: 50px;
	background-color: rgba(22, 160, 133, 0.68);
	font-size: 25px;
	padding: 10px;
	color: white;
	box-sizing: border-box;
}

.popup-content {
	padding: 20px;
}

/* 검색창 스타일 */
.searchBar {
	margin-left: 15px;
}

.searchBar>select {
	height: 36px;
	width: 70px;
	border: 1px solid lightgray;
	border-radius: 3px;
	font-family: inherit;
	display: inline-block;
}

.searchBar>input {
	padding: 5px;
	width: 220px;
	height: 25px;
	border: 1px solid lightgray;
	border-radius: 3px;
}

.searchBar>svg {
	fill: rgb(26, 188, 156);
	width: 37px;
	display: inline-block;
	vertical-align: bottom;
	height: 100%;
}

.searchBar>svg:hover {
	cursor: pointer;
	opacity: 70%;
}
/* 검색창 스타일 */
div {
	box-sizing: border-box;
}

/* 전체영역틀 */
#wrap {
	margin-top: 20px;
	margin-left: 15px; width : 1200px;
	height: 700px;
	width: 1200px;
}

#wrap>div {
	float: left;
	margin-right: 18px;
}

/* 좌측 조직도영역 */
#groupArea {
	height: 100%;
	width: 380px;
	border: 1px solid lightgray;
}

/* depList_area - 조직도 tree 스타일 */
.group_tree, .group_tree ul {
	list-style: none;
	padding-left: 17px;
}

.group_tree a {
	text-decoration: none;
	color: rgb(77, 77, 77);
}

.group_tree *::before {
	width: 17px;
	height: 15px;
	display: inline-block;
}

.group_tree a:before {
	content: '└';
	font-family: fontello;
}

.group_tree label {
	cursor: pointer;
}

.group_tree label:before {
	content: '\E802';
	font-family: fontello;
	color: gray;
}

.group_tree input[type="checkbox"] {
	display: none;
}

.group_tree input[type="checkbox"]:checked ~ul{
	display: none;
}

.group_tree input[type="checkbox"]:checked+label:before {
	content: '\E801';
}

.group_tree ul, .group_tree li {
	padding-top: 10px;
	height: 35px;
}

/* 중앙 리스트영역 */
#listArea {
	height: 100%;
	width: 380px;
	border: 1px solid lightgray;
}

/* 체크박스 스타일 */
.checkBox {
	zoom: 1.7;
}

/* 게시판 스타일 */
.boardTable {
	width: 100%;
	height: auto;
}

.boardTable, .boardTable th, .boardTable td {
	border-width: 1px 0;
	border-collapse: collapse;
	text-align: center;
	font-size: 15px;
}

.boardTable th {
	background-color: #999;
	color: #fff;
	height: 35px;
}

.boardTable td {
	border-color: lightgray;
	border-style: solid;
	height: 35px;
}

.boardTable tr:hover td {
	background-color: rgb(224, 224, 224);
	cursor: pointer;
}

/* 게시판 스타일 */

/* 우측 선택영역 */
#selectArea {
	height: 100%;
	width: 380px;	
}

#selectArea>div {
	width: 100%;
	height: 50%;	
}
#selectArea>div>div{border: 1px solid lightgray;}

/* 작은버튼 스타일 */
.smallBtn {
	width: 60px;
	height: 25px;
	border: 0px;
	border-radius: 5px;
	background: rgb(26, 188, 156); /* 회색 : rgb(190, 190, 190) */
	color: white;
	font-size: 12px;
}

/* 중간버튼 스타일 */
.middleBtn {
	width: 90px;
	height: 35px;
	border: 0px;
	border-radius: 5px;
	background: rgb(26, 188, 156); /* 회색 : rgb(190, 190, 190) */
	color: white;
	font-size: 15px;
}

.middleBtn:hover {
	cursor: pointer;
}
/* 중간버튼 스타일 */

/* 큰버튼 스타일 */
.bigBtn {
	width: 140px;
	height: 40px;
	border: 0px;
	border-radius: 5px;
	background: orange; /* 회색 : rgb(190, 190, 190) */
	color: white;
	font-size: 18px;
	display: inline-block;
}

.bigBtn:hover {
	cursor: pointer;
}
/* 큰버튼 스타일 */
#btnArea {
	margin-left: 910px;
}



.btnDel{
	color:rgb(190, 190, 190);
}
</style>
</head>
<body>


	<div class="popup-outer">
		<div class="popup-title">결재라인 설정</div>
		<div class="popup-content">
			<!-- 검색바 -->
			<div class="searchBar">
				<select id="condition" name="condition">
					<option value="empName">이름</option>
					<optio value="rankTitle">직급</option>
					<option value="jobTitle">직책</option>
					<option value="deptTitle">부서</option>
				</select> <input id="keyword" type="text" placeholder="이름/직급/직책/부서 검색">
				<svg  onclick="searchEmpProfile();" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
					fill="black" width="48px" height="48px">
									<path
						d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
									<path d="M0 0h24v24H0z" fill="none" /></svg>
			<button type="button" class="bigBtn" id="signerSubmit" style="margin-left: 680px;">등록</button>
			
			
			<script>
				$(document).on("click","#signerSubmit",function(){
					
					//opener.testConsole("zzzz");
					//window.close();
					
					var v = $(".signSel tbody").text();
					if(v==""){
						alert("결재자는 1명 이상 존재해야 합니다.");
						return false;
					}else{
						
						// 결재자 정보 부모화면에 출력
						var m1 = $("#signList tbody tr").eq(0).children().eq(2).text();
						opener.document.getElementById("m1").value=m1;
						var m2 = $("#signList tbody tr").eq(1).children().eq(2).text();
						opener.document.getElementById("m2").value=m2;
						var m3 = $("#signList tbody tr").eq(2).children().eq(2).text();
						opener.document.getElementById("m3").value=m3;
						var m4 = $("#signList tbody tr").eq(3).children().eq(2).text();
						opener.document.getElementById("m4").value=m4;
						var m5 = $("#signList tbody tr").eq(4).children().eq(2).text();
						opener.document.getElementById("m5").value=m5;
						var m6 = $("#signList tbody tr").eq(5).children().eq(2).text();
						opener.document.getElementById("m6").value=m6;
						
						var n1 = $("#signList tbody tr input[name=empId]").eq(0).val();
						if(n1){
							opener.document.getElementById("n1").value=n1 + "@ergate.com";
						}						
						
						var n2 = $("#signList tbody tr input[name=empId]").eq(1).val();
						if(n2){
							opener.document.getElementById("n2").value=n2 + "@ergate.com";
						}
						
						var n3 = $("#signList tbody tr input[name=empId]").eq(2).val();
						if(n3){
							opener.document.getElementById("n3").value=n3 + "@ergate.com";
						}
						var n4 = $("#signList tbody tr input[name=empId]").eq(3).val();
						if(n4){
							opener.document.getElementById("n4").value=n4 + "@ergate.com";
						}
						var n5 = $("#signList tbody tr input[name=empId]").eq(4).val();
						if(n5){
							opener.document.getElementById("n5").value=n5 + "@ergate.com";
						}
						
						var n6 = $("#signList tbody tr input[name=empId]").eq(5).val();
						if(n6){
							opener.document.getElementById("n6").value=n6 + "@ergate.com";

						}
						
						
						
						
						
						//결재자 아이디 배열에 담기
						var signerId=new Array();
						var cnt = $("#signList tbody input[name=empId]").length;
						for(var i=0;i<cnt;i++){
							var signerIdStr = $("#signList tbody input[name=empId]").eq(i).val() + "@ergate.com";
							signerId.push(signerIdStr);
						}
						//결재자 이름 배열에 담기
						var signerName=new Array();
						cnt = $("#signList tbody input[name=empName]").length;
						for(var i=0;i<cnt;i++){
							signerName.push($("#signList tbody input[name=empName]").eq(i).val());
						}

						
						opener.getSid(signerId);
						opener.getSname(signerName);
						
						
						
						
						// 참조자 쪽
						var i1 = $("#refList tbody tr").eq(0).children().eq(2).text();
						opener.document.getElementById("i1").value=i1;
						var i2 = $("#refList tbody tr").eq(1).children().eq(2).text();
						opener.document.getElementById("i2").value=i2;
						var i3 = $("#refList tbody tr").eq(2).children().eq(2).text();
						opener.document.getElementById("i3").value=i3;
						var i4 = $("#refList tbody tr").eq(3).children().eq(2).text();
						opener.document.getElementById("i4").value=i4;
						var i5 = $("#refList tbody tr").eq(4).children().eq(2).text();
						opener.document.getElementById("i5").value=i5;
						var i6 = $("#refList tbody tr").eq(5).children().eq(2).text();
						opener.document.getElementById("i6").value=i6;
						
						var l1 = $("#refList tbody tr input[name=empId]").eq(0).val();
						if(l1){
						opener.document.getElementById("l1").value=l1 + "@ergate.com";
						}
						var l2 = $("#refList tbody tr input[name=empId]").eq(1).val();
						if(l2){
						opener.document.getElementById("l2").value=l2 + "@ergate.com";
						}
						var l3 = $("#refList tbody tr input[name=empId]").eq(2).val();
						if(l3){
						opener.document.getElementById("l3").value=l3 + "@ergate.com";
						}
						var l4 = $("#refList tbody tr input[name=empId]").eq(3).val();
						if(l4){
						opener.document.getElementById("l4").value=l4 + "@ergate.com";
						}
						var l5 = $("#refList tbody tr input[name=empId]").eq(4).val();
						if(l5){
						opener.document.getElementById("l5").value=l5 + "@ergate.com";
						}
						var l6 = $("#refList tbody tr input[name=empId]").eq(5).val();
						if(l6){
						opener.document.getElementById("l6").value=l6 + "@ergate.com";
						}
						
						// 수신참조자이름 출력/변수에 담기
						var refName=new Array();
						cnt = $("#refList tbody input[name=empName]").length;
						for(var i=0;i<cnt;i++){
							refName.push($("#refList tbody input[name=empName]").eq(i).val());
						}
						
						
						// 수신참조자 아이디 변수에 담기
						var refId=new Array();
						cnt = $("#refList tbody input[name=empId]").length;
						for(var i=0;i<cnt;i++){
							var refStr = $("#refList tbody input[name=empId]").eq(i).val() + "@ergate.com";
							refId.push(refStr);
						}
						
						
						opener.getRefId(refId);
						opener.getRefName(refName);
						
						window.self.close();
					
					}
				});
			</script>
			 
			</div>
			<div id="wrap">
				<!-- 좌측 조직도영역 -->
				<div id="groupArea">
					<ul class="group_tree">
						<li><input type="checkbox" id="root"> 
						<label for="root" class="deptList" key="all"> ERgate</label>
							 <ul>
			                        <li class="deptList" key="D0">└ 임원</li>
			                        <li class="deptList" key="D1">└ 개발팀</li>
			                        <li class="deptList" key="D2">└ 회계팀</li>
			                        <li class="deptList" key="D3">└ 기술팀</li>
			                        <li class="deptList" key="D4">└ 총무팀</li>
			                        <li class="deptList" key="D5">└ 인사팀</li>
			                    </ul>
							</li>
					</ul>
				</div>

				<!-- 중앙 리스트영역 -->
				<div id="listArea">
					<div style="width:100%;height:100%;overflow:auto;">
						<table class="boardTable empList">
							<thead>
								<tr>
									<th width="40"><input class="checkBoxAll" type="checkbox" id="checkall" style="zoom: 1.7;"></th>
									<th>이름</th>
									<th>부서</th>
									<th width="120">직책/직급</th>
								</tr>
							</thead>
							<tbody>
							<!-- 조회해온 사원정보 들어갈자리 -->
							</tbody>
						</table>
					</div>	
				</div>

				<!-- 우측 선택영역 -->
				<div id="selectArea">
					<div id="signArea">
						<button class="middleBtn selSigner">선택추가</button>
						<h2 style="display:inline-block;margin:0;">받는사람</h2>
						<br><br>
						<div style="width:100%;height:280px;overflow:auto;">
								<table id="signList" class="boardTable signSel">
									<thead>
										<tr>
											<th width="100">이름</th>
											<th>부서</th>
											<th width="120">직책/직급</th>
											<th width="40"></th>
										</tr>
									</thead>
									<tbody></tbody>
								</table>
						</div>	
					</div>
					
					<div id="refArea">
						<button class="middleBtn selRef">선택추가</button>
						<h2 style="display:inline-block;margin:0;">참조</h2>
						<br><br>
						<div id="signSelDiv">
							<div style="width:100%;height:280px;overflow:auto;">
								<table id="refList" class="boardTable refSel">
								<thead>
									<tr>
										<th width="100">이름</th>
										<th>부서</th>
										<th width="120">직책/직급</th>
										<th width="40"></th>
									</tr>
								</thead>
								<tbody></tbody>
								</table>
							</div>	
						</div>
						
					</div>
				</div>
			</div>
		</div>
	</div>


	<script>
		//최상단 체크박스 클릭
		$("#checkall").click(function(){
		     //클릭되었으면
		     if($("#checkall").prop("checked")){
		            //input태그의 name이 chk인 태그들을 찾아서 checked옵션을 true로 정의
		            $("input[name=chk]").prop("checked",true);
		            //클릭이 안되있으면
		     }else{
		            //input태그의 name이 chk인 태그들을 찾아서 checked옵션을 false로 정의
		            $("input[name=chk]").prop("checked",false);
		     }
		 });
		
		//삭제버튼 클릭시 추가된 행 삭제
		$(document).on("click",".btnDel",function(){
			$(this).parent().parent().remove();
		});
		
		
		// 결재자 추가
		
		var value1 = "";
		
		$(document).on("click",".selSigner",function(){
			
			 $('.checkBox:checked').each(function() {
				var id = $(this).parent().parent().children("input[name=empId]").clone().wrapAll("<div/>").parent().html();
				var nameVal = $(this).parent().parent().children("input[name=empName]").clone().wrapAll("<div/>").parent().html();
				var name = $(this).parent().parent().children(".name").clone().wrapAll("<div/>").parent().html();
				var dept = $(this).parent().parent().children(".dept").clone().wrapAll("<div/>").parent().html();
				var jobRank = $(this).parent().parent().children(".jobRank").clone().wrapAll("<div/>").parent().html();
				
			
				var regExp = new RegExp($(this).parent().parent().children("input[name=empId]").val());
		       	
				//$(this).removeAttr("checked");
				
				if(regExp.test($(".signSel tbody").html())){
					console.log("중복");
					return false;
				}
				 
				value1 += '<tr>' +  id +nameVal+ name + dept + jobRank
		       			+ '<td><span class="material-icons btnDel">remove_circle</span></td>'
		       			+ '</tr>';
		       			
				$(".signSel tbody").append(value1);
				value1="";
				//var test1 = $(".signSel").html();
			//console.log("출력!!!"+test1);
		   });
			
		});
		
		
	</script>

	<script>
	
	// 수신참조자 추가
	var value2 = "";
	
	$(document).on("click",".selRef",function(){
		$('.checkBox:checked').each(function() {
			var id = $(this).parent().parent().children("input[name=empId]").clone().wrapAll("<div/>").parent().html();
			var nameVal = $(this).parent().parent().children("input[name=empName]").clone().wrapAll("<div/>").parent().html();
			var name = $(this).parent().parent().children(".name").clone().wrapAll("<div/>").parent().html();
			var dept = $(this).parent().parent().children(".dept").clone().wrapAll("<div/>").parent().html();
			var jobRank = $(this).parent().parent().children(".jobRank").clone().wrapAll("<div/>").parent().html();
			
		
			var regExp = new RegExp($(this).parent().parent().children("input[name=empId]").val());
	       	
			//$(this).removeAttr("checked");
			
			if(regExp.test($(".refSel tbody").html())){
				console.log("중복");
				return false;
			}
			 
			value2 += '<tr>' +  id +nameVal+ name + dept + jobRank
	       			+ '<td><span class="material-icons btnDel">remove_circle</span></td>'
	       			+ '</tr>';
	       			
			$(".refSel tbody").append(value2);
			value2="";
		});
		
	});	
	</script>
	
	
	<script>
	$(function(){
		//selectNoList();

		/* 조직도 부서별 사원 조회 */
		$(".deptList").click(function(){
			keyword =$(this).attr("key");
			
			$.ajax({
				url: "deptEmpList.gr",
				type: "post",
				data:{"keyword":keyword}, 
				async: false,
				success: function(eList){
				 	//console.log(eList);
					var value = "";
					
					if(eList.length == 0){ // 리스트가 비어있을 경우
		            	value = '<td colspan="4">조회된 사원이 없습니다. </td>';
					}else{ // 리스트가 비어있지 않을 경우
		            	//console.log(eList[0]);
						for(var i in eList){
							
							var empName = eList[i].empName;
							var empId = eList[i].empId;
							var empRank = eList[i].rankTitle;
							var empJob = eList[i].jobTitle;
							var empDept = eList[i].deptTitle;
							
							value += '<tr><input type="hidden" name="empId" value="'+empId+'">' +
									 '<input type="hidden" name="empName" value="'+empName+'">' +
									 '<td><input name="chk" class="checkBox" type="checkbox"></td>' +
									 '<td class="name">'+empName + '</td>' +
									 '<td class="dept">'+ empDept + '</td>' +
									 '<td class="jobRank" width="120">'+empJob+'/'+empRank+'</td></tr>';							 
							
						}
						$(".empList tbody").html(value);
					} 
					
				},
				error:function(){
					console.log("조직도 부서별 사원 리스트 조회 실패");
				}
			});
		});

	});
	
	
	function searchEmpProfile(){
		condition = $("#condition option:selected").val();
		keyword = $("#keyword").val();
		if(keyword==''){
			alert("키워드를 입력하세요");
			return;
		}
		$.ajax({
			url:"empListSearch.gr",
			type:"get",
			data:{condition:condition,
				 keyword:keyword},
			success: function(list){
			console.log(list);
				var value = "";
				
				if(list.length == 0){ // 리스트가 비어있을 경우
					value = '<td colspan="4">조회된 사원이 없습니다. </td>';
				}else{ // 리스트가 비어있지 않을 경우
	            
					for(var i in list){

						var empName = list[i].empName;
						var empId = list[i].empId;
						var empRank = list[i].rankTitle;
						var empJob = list[i].jobTitle;
						var empDept = list[i].deptTitle;
						
						value += '<tr><input type="hidden" name="empId" value="'+empId+'">' + 
						 '<td><input name="chk" class="checkBox" type="checkbox"></td>' +
						 '<td>'+empName + '</td>' +
						 '<td>'+ empDept + '</td>' +
						 '<td width="120">'+empJob+'/'+empRank+'</td></tr>';	
					}
				}
				$(".empList tbody").html(value);
			},
			error:function(){
				console.log("조직도 사원 리스트조회용 통신 실패");
			}
			
		})
		
	}
	</script>
	
	
	
	<!-- 팝업창 자동 사이즈맞춤용 스크립트 -->
	<script>
		$(document).ready(function() {
							// 팝업 창 크기를 HTML 크기에 맞추어 자동으로 크기를 조정하는 함수.
							var strWidth;
							var strHeight;

							//innerWidth / innerHeight / outerWidth / outerHeight 지원 브라우저
							if (window.innerWidth && window.innerHeight
									&& window.outerWidth && window.outerHeight) {
								strWidth = $(".popup-outer").outerWidth()
										+ (window.outerWidth - window.innerWidth);
								strHeight = $(".popup-outer").outerHeight()
										+ (window.outerHeight - window.innerHeight);
							} else {
								var strDocumentWidth = $(document).outerWidth();
								var strDocumentHeight = $(document)
										.outerHeight();

								window.resizeTo(strDocumentWidth,
										strDocumentHeight);

								var strMenuWidth = strDocumentWidth
										- $(window).width();
								var strMenuHeight = strDocumentHeight
										- $(window).height();

								strWidth = $(".popup-outer").outerWidth()
										+ strMenuWidth;
								strHeight = $(".popup-outer").outerHeight()
										+ strMenuHeight;
							}

							//resize
							window.resizeTo(strWidth, strHeight);
		});
	</script>

</body>
</html>