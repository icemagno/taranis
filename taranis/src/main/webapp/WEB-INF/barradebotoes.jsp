

<!-- 
		OS HANDLERS ESTAO NO ARQUIVO "MENUBAR.JS"
		QUE ESTAH HOSPEDADO NO MIDAS
 -->



<div class="btn-group">
	<!-- HOME  -->
	<button title="In�cio" id="toolHome" type="button"class="btn btn-success navbar-btn topmnubtn" >
		<img src="${midasLocation}/atlas/icons/home.png"  class="topmnuimg">
	</button>
</div>

<div class="btn-group">
	<!-- FULLSCREEN  -->
	<button title="Tela Cheia" id="toolFullScreen" type="button"class="btn btn-success navbar-btn topmnubtn" >
		<img src="${midasLocation}/atlas/icons/fullscreen2.png"  class="topmnuimg">
	</button>
</div>


<div class="btn-group">
	<!-- DESENHO  -->
    <a href="#" title="Desenho de Fei��es" class="btn btn-primary navbar-btn dropdown-toggle topmnubtn" data-toggle="dropdown">
    	<img src="${midasLocation}/atlas/icons/desenho.png" class="topmnuimg">
    </a>
    <ul style="background-color: transparent; border: 0px;" class="dropdown-menu dropdown-menu-lg">
    	<!-- 
        <li style="margin-top: 5px;">
			<button title="Desenhar Pol�gono 3D" id="toolAddPolygon" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/dr_3d.png" style="width: 35px; height: 35px;">
			</button>
        </li>
        -->
		<li style="margin-top: 5px;">
			<button title="Desenhar Pol�gono" id="toolAddPolygonSurface" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/dr_poly.png" style="width: 35px; height: 35px;">
			</button>
        </li>
		<li style="margin-top: 5px;">
			<button title="Desenhar Ponto" id="toolAddPoint" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/dr_point.png" style="width: 35px; height: 35px;">
			</button>
        </li>
		<li style="margin-top: 5px;">
			<button title="Desenhar Linha" id="toolAddLine" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/dr_line.png" style="width: 35px; height: 35px;">
			</button>
        </li>
		<li style="margin-top: 5px;">
			<button title="Desenhar Ret�ngulo" id="toolAddBox" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/dr_box.png" style="width: 35px; height: 35px;">
			</button>
        </li>
		<li style="margin-top: 5px;">
			<button title="Desenhar C�rculo" id="toolAddCircle" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/dr_circle.png" style="width: 35px; height: 35px;">
			</button>
        </li>
    </ul>
</div>

<div class="btn-group">
	<!-- ANALISE 2D  -->
    <a href="#" title="An�lise 2D" class="btn btn-primary navbar-btn dropdown-toggle topmnubtn" data-toggle="dropdown">
    	<img src="${midasLocation}/atlas/icons/analise2d.png" class="topmnuimg">
    </a>

    <ul style="background-color: transparent; border: 0px;" class="dropdown-menu dropdown-menu-lg">
    	 
        <li style="margin-top: 5px;">
			<button title="Buffer" id="toolBuffer" type="button" class="btn btn-warning btn-flat disabled" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/buffer.png" style="width: 35px; height: 35px;">
			</button>
        </li>
        <li style="margin-top: 5px;">
			<button title="Gr�fico de Voronoi" id="toolVoronoi" type="button" class="btn btn-warning btn-flat disabled" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/voronoi.png" style="width: 35px; height: 35px;">
			</button>
        </li>
         
        <li style="margin-top: 5px;">
			<button title="Perfil de Eleva��o" id="toolPrfilElevacao" type="button" class="btn btn-warning btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/perfil2.png" style="width: 35px; height: 35px;">
			</button>
        </li>
        <li style="margin-top: 5px;">
			<button title="C�lculo de Rota" id="toolRoutes" type="button" class="btn btn-warning btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/rota.png" style="width: 35px; height: 35px;">
			</button>
        </li>
    </ul>
    
</div>

<div class="btn-group">
	<!-- ANALISE 3D  -->
    <a href="#" id="analise3dMainBtn" title="An�lise 3D" class="btn btn-primary navbar-btn dropdown-toggle topmnubtn" data-toggle="dropdown">
    	<img src="${midasLocation}/atlas/icons/3d.png" class="topmnuimg">
    </a>
    
    <ul style="background-color: transparent; border: 0px;" class="dropdown-menu dropdown-menu-lg">
    <!--
        <li style="margin-top: 5px;">
			<button title="Vis�o Nadir" id="toolNadir" type="button" class="btn btn-success btn-flat disabled" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/vista_topo.png" style="width: 35px; height: 35px;">
			</button>
		</li>    
    -->
        <li style="margin-top: 5px;">
			<button title="Constru��es OpenStreetMap" id="toolOSM3D" type="button" class="btn btn-warning btn-flat disabled" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/osm3d.png" style="width: 35px; height: 35px;">
			</button>
        </li>
        <li style="margin-top: 5px;">
			<button title="Influ�ncia Visual (Viewshed)" id="toolViewShed" type="button" class="btn btn-warning btn-flat disabled" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/viewshed.png" style="width: 35px; height: 35px;">
			</button>
        </li>
    </ul>
</div>

<div class="btn-group">
	<!-- MEDIR  -->
	<button title="Medir Dist�ncias e �reas" id="showMeasureTools" type="button"class="btn btn-primary navbar-btn topmnubtn disabled" >
		<img src="${midasLocation}/atlas/icons/measure.png"  class="topmnuimg">
	</button>
</div>

<div class="btn-group">
	<!-- INTERROGAR  -->
	<button title="Interrogar" id="showToolQuery" type="button"class="btn btn-primary navbar-btn topmnubtn" >
		<img src="${midasLocation}/atlas/icons/query.png"  class="topmnuimg">
	</button>
</div>

<div class="btn-group">
	<!-- SOLUCIONADORES  -->
    <a href="#" title="Solucionadores" class="btn btn-primary navbar-btn dropdown-toggle topmnubtn" data-toggle="dropdown">
    	<img src="${midasLocation}/atlas/icons/solucionadores.png" class="topmnuimg">
    </a>
    <ul style="background-color: transparent; border: 0px;" class="dropdown-menu dropdown-menu-lg">
        <li style="margin-top: 5px;">
			<button title="Avisos R�dio" id="toolAvisoRadio" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/aviradio.png" style="width: 35px; height: 35px;">
			</button>
        </li>
		<li style="margin-top: 5px;">
			<button  title="�rea de Seguran�a de Plataformas" id="toolGtOpA" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/plataforma.png" style="width: 35px; height: 35px;">
			</button>
        </li>
        
        <!-- 
		<li style="margin-top: 5px;">
			<button title="Previs�o do Tempo" id="toolMetocMain" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/clima2.png" style="width: 35px; height: 35px;">
			</button>
        </li>
		<li style="margin-top: 5px;">
			<button title="Climatologia" id="toolMetoc" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/metoc.png" style="width: 35px; height: 35px;">
			</button>
        </li>
         -->
        
		<li style="margin-top: 5px;">
			<button title="Cor Meteorol�gica" id="toolCOR" type="button" class="btn btn-warning btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/cormet.png" style="width: 35px; height: 35px;">
			</button>
        </li>
		<li style="margin-top: 5px;">
			<button title="N�mero de Classifica��o de Pavimento (PCN)" id="toolPCN" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/pcn.png" style="width: 35px; height: 35px;">
			</button>
        </li>
    </ul>
</div>

<div class="btn-group">
	<!-- SALVAR TELA  -->
	<button title="Salvar Tela" id="toolScreenSnapShot" type="button"class="btn btn-primary navbar-btn topmnubtn" >
		<img id="saveScrBtn" src="${midasLocation}/atlas/icons/savescreen.png"  class="topmnuimg">
	</button>
</div>

<div class="btn-group">
	<!-- CATALOGOS  -->
    <a href="#" title="Cat�logos Diversos" class="btn btn-primary navbar-btn dropdown-toggle topmnubtn" data-toggle="dropdown">
    	<img src="${midasLocation}/atlas/icons/catalogs.png" class="topmnuimg">
    </a>
    <ul style="background-color: transparent; border: 0px;" class="dropdown-menu dropdown-menu-lg">
        <li style="margin-top: 5px;">
			<button title="Cat�logo EDGV-DEFESA" id="toolEdgvBook" type="button" class="btn btn-warning  btn-flat" style="padding: 0px;">
				<img src="${midasLocation}/atlas/icons/edgv.png" style="width: 35px; height: 35px;">
			</button>
        </li>
	</ul>	
</div>


<div class="btn-group">
	<!-- AJUDA  -->
	<button title="Ajuda" id="toolGuia"	type="button"class="btn btn-primary navbar-btn dropdown-toggle topmnubtn" >
		<img src="${midasLocation}/atlas/icons/help.png"  class="topmnuimg">
	</button>
</div>
