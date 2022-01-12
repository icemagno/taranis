select pc_get( pc_explode(pa) ) from "sp-laz" 

select count(*) Sum(PC_NumPoints(pa)) from "sp-laz"

select PC_AsText( PC_Explode(pa) ) from "sp-laz" limit 10