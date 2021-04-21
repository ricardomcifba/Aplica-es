/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package embasa.fcom.service;

import embasa.fcom.entity.Entity;
import embasa.fcom.entity.ParamSql;
import embasa.fcom.entity.TypeComponent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author t013384
 */
public class ServiceUtils {
    
    
    public static List<Entity> extractParam(Object object) {
        try {
            List<Entity> listEntity = new ArrayList<Entity>();
            Entity entity = null;
            //obtem todos os metodos da classe em que fez a chamada.
            for (Field field : object.getClass().getDeclaredFields()) {
                //verifica se o metodo tem alguma anotacao @ParamSql
                if (field.isAnnotationPresent(ParamSql.class)) {
                    //obtem a annotação
                    ParamSql paramSql = field.getAnnotation(ParamSql.class);

                    //obtem o objeto com o parametro desejado para extracao
                    field.setAccessible(true);
                    Object objParam = field.get(object);

                    //Verifica se o componente é um selected e se nao estiver marcado, vai para o próximo
                    if (paramSql.typeComponent().equals(TypeComponent.SELECTED)) {
                        Method method;
                        method = objParam.getClass().getMethod("isSelected", null);
                        boolean flag = (boolean) method.invoke(objParam, null);
                        if (!flag) {
                            continue;
                        }
                    }

                    for (Method method : objParam.getClass().getMethods()) {
                        if (method.getName().equalsIgnoreCase(paramSql.method()) && method.getParameterCount() == 0) {

                            //criar o entidade de envio de dados
                            entity = new Entity(paramSql.param(), paramSql.type());

                            int index = listEntity.lastIndexOf(entity);
                            if (index >= 0) {;
                                //paramentro é uma lista, possivel IN no sql
                                entity = listEntity.get(index);
                            } else {
                                listEntity.add(entity);
                            }

                            entity.setParam(paramSql.param());
                            entity.getValue().add(method.invoke(objParam, null));
                        }
                    }

                }
            }
//            System.out.println(listEntity);
            return listEntity;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
