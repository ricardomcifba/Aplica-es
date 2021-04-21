/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package embasa.fcom.entity;

/**
 *
 * @author Bruno
 */
public enum Functionality {

    IQM("iqm", "copy (select * from view_iqm_geral where ano = @ano and mes = @mes @filtro) to '\\\\@server\\tmp\\iqm\\@file.txt' delimiter ';' CSV HEADER null '' ENCODING 'latin1'"),
    //IQM("iqm", "./sql/iqm.sql"),
    AVAL_SERV("avalServ", "./sql/avalServ.sql"),
    IMT("imt", "copy( select * from queries.view_amf_indicacao_matriculas_trabalhar where  localidade in (select localidades(@superintendencia )) and tipo_servico_recomendado in (@servico)) to '\\\\@server\\tmp\\imt\\@file.txt' delimiter ';' CSV HEADER null '' ENCODING 'utf8'");
    //IMT("imt", "./sql/imt.sql"),

    private String name;
    private String nameFileSQL;

    private Functionality(String name, String nameFileSQL) {
        this.name = name;
        this.nameFileSQL = nameFileSQL;
    }
    
    //para não modificar o contrutor que trabalha com chamada do arquivo, criei outra pra chamar a String
    private Functionality(String nameFileSQL) {
        this.nameFileSQL = nameFileSQL;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the nameFileSQL
     */
    public String getNameFileSQL() {
        return nameFileSQL;
    }

}
