<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <head>
                <title>NetFlix</title>             
                <style>
                    html {
                    font-family: sans-serif;
                    -ms-text-size-adjust: 100%;
                    -webkit-text-size-adjust: 100%
                    }                    
                    body {
                    background-color : rgb(3, 3, 3);
                    }
                    img {
                    padding : 5px;
                    height : 295px;
                    width : 165px;
                    }
                    h1 {                    
                    padding-left : 15px;
                    color : rgb(153, 153, 153);
                    font-size : 27px;
                    line-height : 38px;
                    font-family: "Helvetica Neue","Helvetica","Arial","sans-serif";
                    }
                </style>
            </head>
            <body>

                <!-- by genre -->
                <h1>Adventure</h1>
                <xsl:for-each select="movies/movie[genres/genre = 'Adventure']">
                    <xsl:sort select="imdbRating" data-type="number" order="descending"/>                        
                    <xsl:sort select="year" data-type="number" order="descending"/>
                    <!-- each movie gets its own container -->
                    <a> 
                        <xsl:attribute name="href">/movie/<xsl:value-of select="imdbid" /></xsl:attribute>                 
                        <xsl:element name="img">
                            <xsl:attribute name="src">                                
                                <xsl:value-of select="poster"/>
                            </xsl:attribute>
                        </xsl:element>    
                    </a>                 
                </xsl:for-each>
            
                <h1>Animation</h1>
                <xsl:for-each select="movies/movie[genres/genre = 'Animation']">
                    <xsl:sort select="imdbRating" data-type="number" order="descending"/>                        
                    <xsl:sort select="year" data-type="number" order="descending"/>
                    <!-- each movie gets its own container -->
                    <a> 
                        <xsl:attribute name="href">/movie/<xsl:value-of select="imdbid" /></xsl:attribute>                 
                        <xsl:element name="img">
                            <xsl:attribute name="src">                                
                                <xsl:value-of select="poster"/>
                            </xsl:attribute>
                        </xsl:element>    
                    </a>                 
                </xsl:for-each>            
            
                <h1>Comedy</h1>
                <xsl:for-each select="movies/movie[genres/genre = 'Comedy']">
                    <xsl:sort select="imdbRating" data-type="number" order="descending"/>                    
                    <xsl:sort select="year" data-type="number" order="descending"/>
                    <!-- each movie gets its own coZntainer -->
                    <a> 
                        <xsl:attribute name="href">/movie/<xsl:value-of select="imdbid" /></xsl:attribute>                 
                        <xsl:element name="img">
                            <xsl:attribute name="src">                                
                                <xsl:value-of select="poster"/>
                            </xsl:attribute>
                        </xsl:element>    
                    </a>      
                </xsl:for-each>

                <h1>Drama</h1>
                <xsl:for-each select="movies/movie[genres/genre = 'Drama']">
                    <xsl:sort select="imdbRating" data-type="number" order="descending"/>                        
                    <xsl:sort select="year" data-type="number" order="descending"/>
                    <!-- each movie gets its own container -->
                    <xsl:apply-templates select="movie"/>
                </xsl:for-each>

                <h1>Fantasy</h1>
                <xsl:for-each select="movies/movie[genres/genre = 'Fantasy']">
                    <xsl:sort select="imdbRating" data-type="number" order="descending"/>                        
                    <xsl:sort select="year" data-type="number" order="descending"/>
                    <!-- each movie gets its own container -->
                    <a> 
                        <xsl:attribute name="href">/movie/<xsl:value-of select="imdbid" /></xsl:attribute>                 
                        <xsl:element name="img">
                            <xsl:attribute name="src">                                
                                <xsl:value-of select="poster"/>
                            </xsl:attribute>
                        </xsl:element>    
                    </a>                                  
                </xsl:for-each>

            </body>
        </html>
    </xsl:template>

</xsl:stylesheet> 
