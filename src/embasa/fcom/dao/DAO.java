    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package embasa.fcom.dao;

import embasa.fcom.entity.Entity;
import embasa.fcom.entity.Functionality;
import embasa.fcom.entity.TypeParamSQL;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author Bruno
 */
public class DAO {
    
    protected Functionality function;
    protected String sqlTemplate;
    protected String fileResult;
    protected static Properties props;
    
    static {
        props = new Properties();
        FileInputStream file;
        try {
            file = new FileInputStream("./config.properties");
            props.load(file);            
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public DAO(Functionality function) {
        this.function = function;
        sqlTemplate = getSqlTemplate();
    }
    
    private String getSqlTemplate() {
        //retorna direto o texto 
        String texto = function.getNameFileSQL();
        return texto;
        //try {
           // Scanner scanner;        
           // scanner = new Scanner(new File(function.getNameFileSQL()));
           // scanner.useDelimiter("\\Z");
           //return scanner.next();
        //} catch (FileNotFoundException ex) {
        //    throw new RuntimeException(ex);
       // }        
    }
    
    public void executeSQL(List<Entity> listEntity) {
//        Entity entity = new Entity("server", TypeParamSQL.NULL);
//        entity.getValue().add(props.getProperty("server"));
//        listEntity.add(entity);
//        
//        Calendar cal = Calendar.getInstance();
//        String nameFile = function.getName()    + "_" +
//            cal.get(Calendar.DATE)    + "-" +
//            cal.get(Calendar.MONTH)+1 + "-" +
//            cal.get(Calendar.YEAR)    + "_" +
//            cal.get(Calendar.HOUR)    + "h" +
//            cal.get(Calendar.MINUTE)  + "m" +
//            cal.get(Calendar.SECOND)  + "s";
//
//        entity = new Entity("file", TypeParamSQL.NULL);
//        entity.getValue().add(nameFile);
//        listEntity.add(entity);
        
        String sqlTemp = printSQL(listEntity);
        runSQL(sqlTemp);
    }

    protected String produceParam(List<Entity> listEntity) {
        //não precisa do toString
        String sqlTemp = sqlTemplate;
        //String sqlTemp = sqlTemplate.toString();
        for (Entity entity : listEntity) {
            sqlTemp = sqlTemp.replaceAll("@"+entity.getParam(), entity.toString());
        }
        return sqlTemp;
    }

    private Connection connection;
    
    protected Connection createConnection() {
        try {
            if (connection == null)
                connection = DriverManager.getConnection(
                    props.getProperty("url"),
                    props.getProperty("user"),
                    props.getProperty("password")
                );            
            return connection;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected void runSQL(String sql) {
        Connection conn = createConnection();
        try {
          Statement stat = conn.createStatement();
            System.out.println(sql);
            stat.execute(sql);
            conn.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
            
    }
    
        public String printSQL(List<Entity> listEntity) {
        Entity entity = new Entity("server", TypeParamSQL.NULL);
        entity.getValue().add(props.getProperty("server"));
        listEntity.add(entity);
        
        Calendar cal = Calendar.getInstance();
        String nameFile = function.getName()    + "_" +
            cal.get(Calendar.DATE)    + "-" +
            cal.get(Calendar.MONTH)+1 + "-" +
            cal.get(Calendar.YEAR)    + "_" +
            cal.get(Calendar.HOUR)    + "h" +
            cal.get(Calendar.MINUTE)  + "m" +
            cal.get(Calendar.SECOND)  + "s";

        entity = new Entity("file", TypeParamSQL.NULL);
        entity.getValue().add(nameFile);
        listEntity.add(entity);
        
        String sqlTemp = produceParam(listEntity);
        return sqlTemp;    
    }
    
    
}
