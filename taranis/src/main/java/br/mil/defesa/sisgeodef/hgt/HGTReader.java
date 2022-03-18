package br.mil.defesa.sisgeodef.hgt;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tum.gis.tiles3d.generator.PntcConfig;

public class HGTReader {
	private Logger logger = LoggerFactory.getLogger( HGTReader.class );
	private String path;
	private final String USER_AGENT = "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13";
	private final String WMSMapSource = "https://maps.geosolutionsgroup.com/geoserver/s2cloudless/wms";
	private PntcConfig config;
	private String jobId;


	// http://dwtkns.com/srtm30m/
	
	/*
		https://maps.geo-solutions.it/geoserver/osm/ows
		
		Layers:
		------------
		osm
		osm_marine_dark
		osm_simple_dark
		osm_simple_light
		builtup_area
		icesheet_outlines
		icesheet_polygons
		land_polygons
		landusages
		ne_10m_admin_0_boundary_lines_land
		ne_10m_bathymetry
		ne_10m_admin_0_sovereignty
		ne_10m_admin_1_states_provinces_lines
		ne_10m_coastline
		ne_10m_geography_marine_polys
		ne_10m_urban_areas
		ne_10m_populated_places
		osm_amenities
		osm_admin
		osm_boundary
		osm_buildings
		osm_housenumbers
		osm_places
		osm_transport_areas
		roads
		
	*/
	
	/*
		https://maps.geosolutionsgroup.com/geoserver/s2cloudless/wms
		
		Layers:
		------------
		s2cloudless
	*/
	
	/*
	 	https://ows.terrestris.de/osm/service
	 			
		Layers:
		------------
		TOPO-WMS
		OSM-WMS
		OSM-WMS-no-labels
		OSM-Overlay-WMS
		TOPO-OSM-WMS
		SRTM30-Hillshade
		SRTM30-Colored
		SRTM30-Colored-Hillshade
		SRTM30-Contour
	*/
	
	
	
	public HGTReader( String jobId, PntcConfig config, String path ) {
		this.path = path;
		this.config = config;
		this.jobId = jobId;
	}
	
	public List<Tile> importData() {
		File folder = new File( this.path );
		List<Tile> result = new ArrayList<Tile>();
		
	    for (final File fileEntry : folder.listFiles()) {
	        if ( (!fileEntry.isDirectory() ) && ( fileEntry.getName().endsWith("hgt") ) ) {
	        	
	        	try {
	        		Tile tile = new Tile( fileEntry.getName() );
	        		logger.info( fileEntry.getName() + " " + tile.getBbox() );
	        		String file = this.path + "/" + tile.getImageName();
	        		saveImage( file, Tile.HGT_ROW_LENGTH, "s2cloudless", tile.getBbox() );
	        		if( new File(file).exists() ) {
	        			result.add( tile );
	        		}
	        	} catch ( Exception e ) {
	        		logger.error( e.getMessage() );
	        	}
	        	
	        }
	    }
	    
	    try {
			for( Tile tile : result ) {
				logger.info("Processing tile " + tile.getName() );
				processHgt( tile );
			}	    
	    } catch ( Exception e ) {
	    	logger.error( e.getMessage() );
	    }
	    
	    return result;
	}
	
	private void processHgt( Tile tile ) throws Exception {
		String hgtName = this.path + "/" + tile.getTileName();
		String imageName = this.path + "/" + tile.getImageName();
		String imageReferenceName = this.path + "/" + tile.getName() + "_ref.png";
		
		ShortBuffer data = readHgtFile( hgtName );
		Connection sqlConnection  =  DriverManager.getConnection( this.config.getConnectionString(), this.config.getUserName(), this.config.getUserPassword() );
		
        BufferedImage image = ImageIO.read( new File( imageName ) );
        
        // Salva uma copia da imagem de referencia
    	BufferedImage bufferedImage = new BufferedImage( Tile.HGT_ROW_LENGTH, Tile.HGT_ROW_LENGTH, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setComposite(AlphaComposite.Clear);
		g2d.fillRect(0, 0, Tile.HGT_ROW_LENGTH, Tile.HGT_ROW_LENGTH);		
		g2d.setComposite(AlphaComposite.Src);		
		// ------------------------------------------------------------
		
		for( int col = 0; col < Tile.HGT_ROW_LENGTH; col++  ) {
			logger.info( tile.getName() + ": " + col + "/" + Tile.HGT_ROW_LENGTH );
			
			for( int row = 0; row < Tile.HGT_ROW_LENGTH; row++   ) {
    			
				
				int cell = ( Tile.HGT_ROW_LENGTH * (row)) + col;
    			short ele = data.get(cell);
    			
    	        int pixel = image.getRGB(col, row); // ( width, height )
    	        Color color = new Color(pixel, true);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();    	        
    	        
                // Salva uma copia da imagem de referencia
    	        g2d.setColor( new Color(red, green, blue) );
    	        g2d.drawLine(col,row,col,row);
    	        // ---------------------------------------
    	        
    			String coord = tile.getPixelCoordinates( col, row );
    			String[] latLon = coord.split(",");
    			
    			double lat = Double.valueOf( latLon[0] );
    			double lon = Double.valueOf( latLon[1] );
    			
				String insertSql = "insert into point_table(jobid,x,y,z,r,g,b,altitude) values(?,?,?,?,?,?,?,?);";
				PreparedStatement insertPs = sqlConnection.prepareStatement( insertSql );
				insertPs.setString(1, this.jobId);
				insertPs.setDouble(2, lon);
				insertPs.setDouble(3, lat);
				insertPs.setDouble(4, ele);
				insertPs.setDouble(5, red );
				insertPs.setDouble(6, green );
				insertPs.setDouble(7, blue );
				insertPs.setDouble(8, ele );
				insertPs.executeUpdate();    			
			}
		}
		logger.info("Done " + tile.getName() );
		sqlConnection.close();
    	g2d.dispose();
		ImageIO.write(bufferedImage, "png", new File( imageReferenceName ) );		
	}
	
    private  ShortBuffer readHgtFile( String hgtFile ) throws Exception {
        FileInputStream fis = new FileInputStream( hgtFile );
        FileChannel fc = fis.getChannel();
        ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size());
        while (bb.remaining() > 0) fc.read(bb);
        bb.flip();
        ShortBuffer sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        fc.close();
        fis.close();
        return sb;
    }		
	
    private void saveImage( String destinationFile, int resolution, String layerName, String bbox) throws IOException {
		String imageUrl = WMSMapSource + "/?service=WMS&srs=EPSG:4326&width="+resolution+"&height="+resolution+"&version=1.3&transparent=false&request=GetMap&layers="+layerName+"&format=image/png&bbox=" + bbox;
		
		// System.out.println(" > Downloading image " + layerName + " from " + WMSMapSource );
		System.out.println( imageUrl );
		
		File picutreFile = new File( destinationFile );
		if( picutreFile.exists() ) {
			logger.info( picutreFile + " already exists.");
			return;
		}
		
		imageUrl = imageUrl.replace(" ", "");
		
        URL url=new URL( imageUrl );
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setConnectTimeout( 240000 );
        conn.setRequestMethod("GET");
        conn.connect();
        java.nio.file.Files.copy( conn.getInputStream(), picutreFile.toPath(), StandardCopyOption.REPLACE_EXISTING);        
        
	}	    
    	
	
}
