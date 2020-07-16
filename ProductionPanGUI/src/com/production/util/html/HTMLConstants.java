package com.production.util.html;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public final class HTMLConstants {
    
    public static final String TR_HEAD_LIST_PLAN = "<tr>\n" +
"<th scope='col'>#</th>\n" +
"<th scope='col'>Part Number</th>\n" +
"<th scope='col'>WO</th>\n" +
"<th scope='col'>HRS</th>\n" +
"<th scope='col'>Setup</th>\n" +
"<th scope='col'>PZAS X HACER</th>\n" +
"<th scope='col'>Machine / Comentarios</th>\n" +
"</tr>";
    
    public static final String TR_HEAD_TURNS_PLAN = "<tr>\n" +
"<th scope='col'>Turn</th>\n" +
"<th scope='col'>Part Number</th>\n" +
"<th scope='col'>WO</th>\n" +
"<th scope='col'>HRS</th>\n" +
"<th scope='col'>Setup</th>\n" +
"<th scope='col'>PZAS X HACER</th>\n" +
"<th scope='col'>Machine / Comentarios</th>\n" +
"</tr>";
    
    public static final String TABLE_HEAD_MARK = "@TABLE.HEAD@";
    public static final String TABLE_ROWS_MARK = "@TABLE.ROWS@";
    public static final String TABLE_TITLE_MARK = "@TITLE@";
    public static final String TABLE_DAY_CONTENT = "<table class=\"table\">\n" +
"    <thead>\n" +
"      @TABLE.HEAD@\n" +
"    </thead>\n" +
"    <tbody>\n" +
"      @TABLE.ROWS@\n" +
"    </tbody>\n" +
"</table>";
    
    public static final String DAY_CONTENT = "@DAY@\n@TABLE.DAY.CONTENT@";
    public static final String DAY_CONTENT_MARK = "@TABLE.DAY.CONTENT@";
    public static final String DAY_MARK = "@DAY@";
    
    public static final String CONTENT_MARK = "@CONTENT@";
    
    private HTMLConstants() {}
    
}
