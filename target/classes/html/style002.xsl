<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
    <xsl:param name="imdbID" select="tt000000" />        
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
                    float: left;
                    }
                    .button {
                        font-size : 16px;
                        font-family: "Helvetica Neue","Helvetica","Arial","sans-serif";
                        text-decoration: none;
                        background-color: #ff0a16;
                        color: #ffffff;
                        padding: 2px 6px 2px 6px;
                    }
                    table {
                        float: left;
                        color : rgb(153, 153, 153);
                        font-size : 16px;
                        font-family: "Helvetica Neue","Helvetica","Arial","sans-serif";
                    }
                </style>
            </head>
            <body>

                <!-- display specific information for this IMDB ID -->
                <xsl:for-each select="movies/movie[imdbid = $imdbID]">
                    <!-- image -->                                  
                        <xsl:element name="img">
                            <xsl:attribute name="src">                                
                                <xsl:value-of select="poster"/>
                            </xsl:attribute>
                        </xsl:element>    
                        
                        <table>
                           <!-- title -->
                            <tr>
                                <td>Title</td>
                                <td>:</td>
                                <td><xsl:value-of select="title"/></td>                                
                            </tr>
                            
                            <!-- year -->
                            <tr>
                                <td>Year</td>
                                <td>:</td>
                                <td><xsl:value-of select="year"/></td>                                
                            </tr>
                            
                            <!-- IMDB rating -->
                            <tr>
                                <td>IMDB Rating</td>
                                <td>:</td>
                                <td><xsl:value-of select="imdbrating"/></td>                                
                            </tr>   
                            
                            <!-- IMDB votes -->
                            <tr>
                                <td>IMDB Votes</td>
                                <td>:</td>
                                <td><xsl:value-of select="imdbvotes"/></td>                                
                            </tr>                                                       

                            <!-- director(s) -->
                            <tr>
                                <td>Director</td>
                                <td>:</td>
                                <td>
                                    <ul>
                                    <xsl:for-each select="directors/director">
                                        <li><xsl:value-of select="."/></li>
                                    </xsl:for-each>   
                                    </ul>                                   
                                </td>
                            </tr>                          
                            
                            <!-- writer(s) -->
                            <tr>
                                <td>Writer</td>
                                <td>:</td>
                                <td>
                                    <ul>
                                    <xsl:for-each select="writers/writer">
                                        <li><xsl:value-of select="."/></li>
                                    </xsl:for-each>   
                                    </ul>                                   
                                </td>
                            </tr>                              
                            
                            <!-- actors -->
                            <tr>
                                <td>Actors</td>
                                <td>:</td>
                                <td>
                                    <ul>
                                    <xsl:for-each select="actors/actor">
                                        <li><xsl:value-of select="."/></li>
                                    </xsl:for-each>   
                                    </ul>                                   
                                </td>
                            </tr>                              
                            
                            <!-- plot -->
                            <tr>
                                <td>Plot</td>
                                <td>:</td>
                                <td><xsl:value-of select="plot"/></td>                                
                            </tr>           
                                   
                            <tr>
                                <td></td>
                                <td></td>
                                <td>
                                    <a class="button">
                                        <xsl:attribute name="href">/play/<xsl:value-of select="imdbid" /></xsl:attribute>
                                        PLAY
                                    </a>
                                </td>
                            </tr>
                        </table>                    
                         
                </xsl:for-each>
            

            </body>
        </html>
    </xsl:template>

</xsl:stylesheet> 
