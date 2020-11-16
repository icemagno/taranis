select ST_GeomFromGeoHash(hash) from geohashes where lvl = 1 limit 100

select ST_Envelope( ST_Extent( the_geom ) ) from sipam_grade_sbbe


insert into geohashes ( lat, lon, hash, radar, centroide ) 
SELECT ST_Y(ST_Centroid(the_geom)), ST_X(ST_Centroid(the_geom)), 
ST_GEohash( ST_Centroid(the_geom),10 ), 'sbbe', ST_Centroid(the_geom) FROM public.sipam_grade_sbbe




http://geojson.tools/



drop function gethashes ( lvl integer, radar varchar );
create or replace function gethashes ( lvl integer, radar varchar ) 
	returns table (
		hash text
	) 
	language plpgsql
as $$
begin
	return query 
		select
			distinct( LEFT(gh.hash, $1) ) as hash
		from
			geohashes gh
		where
			gh.radar = $2;
end;$$




CREATE OR REPLACE FUNCTION public.detailgrid( lvl integer, radar varchar )
RETURNS json as
$func$   
select row_to_json(fc)
from (
    select
        'FeatureCollection' as "type",
        array_to_json(array_agg(f)) as "features"
    from (
        select
            'Feature' as "type",
            ST_AsGeoJSON(ST_SetSRID( ST_GeomFromGeoHash( gethashes($1, $2) )::geometry, 4326), 10, 1) :: json as "geometry",
			
    ) as f
) as fc;
$func$ LANGUAGE sql STABLE STRICT;




























CREATE OR REPLACE FUNCTION public.getproperties( grid_id integer )
RETURNS json as
$func$   
select row_to_json(fc)
from (
    select * from sipam_sbbe where id_grid = $1
) as fc;
$func$ LANGUAGE sql STABLE STRICT;

CREATE OR REPLACE FUNCTION public.getsipamradar( quantos integer, xmin double precision, ymin double precision, xmax double precision, ymax double precision)
RETURNS json as
$func$   
select row_to_json(fc)
from (
    select
        'FeatureCollection' as "type",
        array_to_json(array_agg(f)) as "features"
    from (
        select
            'Feature' as "type",
            ST_AsGeoJSON(ST_Transform( the_geom::geometry, 4326), 10) :: json as "geometry",
            (  select json_strip_nulls( row_to_json(t) ) from ( select getproperties(id)  ) t  ) as "properties"
        from sipam_grade_sbbe
        where public.sipam_grade_sbbe.the_geom::geometry && ST_MakeEnvelope($2, $3, $4, $5, 4326)
        limit $1
    ) as f
) as fc;
$func$ LANGUAGE sql STABLE STRICT;
