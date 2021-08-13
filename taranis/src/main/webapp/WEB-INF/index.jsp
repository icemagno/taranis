<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html manifest="">

<jsp:include page="head.jsp" />



<style>

	.skin-black-light .main-header .navbar-brand {
		border-width: 0px !important;
	}

	.skin-black-light .main-header .navbar .navbar-custom-menu .navbar-nav>li>a, .skin-black-light .main-header .navbar .navbar-right>li>a{
		border-width: 0px !important;
	}
	
	
	.skin-black-light .main-header{
		border : 0px !important;
	}
	
	.skin-black-light .main-header .navbar {
		background-color: #3c8dbc !important;
	}
	
</style>



<body class="skin-black-light layout-top-nav">

	<div class="wrapper" style="height: 2000px">
		<!-- Main Header -->
		<header class="main-header">

			<nav class="navbar navbar-static-top" role="navigation">
				
				<div class="container-fluid">

					<div class="navbar-header">
						<img id="logoMd2" src="/resources/img/logo.png"	style="height: 45px; float: left; margin-top: 3px;"> 
						<a style="font-size: 22px; color:white;margin-right: 20px;" href="/" class="navbar-brand">Taranis</a>
					</div>

				</div>
			</nav>

		</header>

		<div class="content-wrapper" style="position: relative;">
			<section style="padding: 0px;" class="content container-fluid">

				<div class="row">
					<div class="fullWindow" id="cesiumContainer"></div>
				</div>
				
			</section>
		</div>

	    <jsp:include page="footer.jsp" />

		
	</div>
	
	<jsp:include page="requiredscripts.jsp" />

	<script src="/resources/Cesium/Cesium.js" type="text/javascript"></script>
	<script src="/resources/js/MagnoMetocRadarProvider.js" type="text/javascript"></script>
	<script src="/resources/js/scripts.js" type="text/javascript"></script>
	
</body>

</html>

