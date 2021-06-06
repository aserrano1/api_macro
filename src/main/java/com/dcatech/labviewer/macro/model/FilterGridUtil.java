/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcatech.labviewer.macro.model;


import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author dca24
 */
public class FilterGridUtil {
    private final static Logger logger = LoggerFactory.getLogger(FilterGridUtil.class);

    public static DataSourceRequest filter(JsonNode node) {

        DataSourceRequest req = new DataSourceRequest();

        try {

            if (node != null) {
                Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                String logic = null;
                if (node.get("logic") != null) {
                    logic = node.get("logic").asText();
                }
                if (fields != null) {
                    DataSourceRequest.FilterDescriptor filterDescriptor = new DataSourceRequest.FilterDescriptor();
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> next = fields.next();
                        //filterDescriptor = new DataSourceRequest.FilterDescriptor();
                        filterDescriptor.setLogic(logic);
                        if ("logic".equals(next.getKey())) {
                            filterDescriptor.setLogic(next.getValue().asText());
                        }

                        if (next.getValue().isArray()) {
                            Iterator<JsonNode> elements = next.getValue().elements();
                            List<DataSourceRequest.FilterDescriptor> filtersList = new ArrayList();
                            while (elements.hasNext()) {
                                JsonNode filter = elements.next();
                                DataSourceRequest.FilterDescriptor filtersDescriptor = new DataSourceRequest.FilterDescriptor();
                                filtersDescriptor.setField(filter.get("field").asText());
                                filtersDescriptor.setOperator(filter.get("operator").asText());
                                filtersDescriptor.setValue(filter.get("value").asText());
                                filtersDescriptor.setLogic(filterDescriptor.getLogic());
                                filtersList.add(filtersDescriptor);
                            }
                            filterDescriptor.setFilters(filtersList);
                        }
                    }
                    req.setFilter(filterDescriptor);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return req;
    }

    public static String addFilterToQuery(String fieldDB, String fieldGrid, String dataType, DataSourceRequest.FilterDescriptor filterDescriptor) {

        StringBuffer filtros = new StringBuffer();
        String filtrosFin = "";

        try {
            if (filterDescriptor != null) {

                List<DataSourceRequest.FilterDescriptor> filters = filterDescriptor.getFilters();
                if (dataType.equalsIgnoreCase("string")) {
                    fieldDB = "UPPER(" + fieldDB + ")";
                }
                for (DataSourceRequest.FilterDescriptor filter : filters) {

                    if (filter.getField().equalsIgnoreCase(fieldGrid)) {

                        switch (filter.getOperator()) {

                            case "eq":
                                filtros.append(filter.getLogic().equalsIgnoreCase("and") ? " and " : " or ");
                                filtros.append(fieldDB);
                                filtros.append(" = "); // Equals
                                filtros.append(operadorQuery(dataType, filter.getValue().toString().toUpperCase()));

                                break;
                            case "neq":

                                filtros.append(filter.getLogic().equalsIgnoreCase("and") ? " and " : " or ");
                                filtros.append(fieldDB);
                                filtros.append(" != "); //Not Equals
                                filtros.append(operadorQuery(dataType, filter.getValue().toString().toUpperCase()));

                                break;
                            case "gte":

                                filtros.append(filter.getLogic().equalsIgnoreCase("and") ? " and " : " or ");
                                filtros.append(fieldDB);
                                filtros.append(" >= "); //Is greater than or equal to 
                                filtros.append(operadorQuery(dataType, filter.getValue().toString()));

                                break;
                            case "gt":

                                filtros.append(filter.getLogic().equalsIgnoreCase("and") ? " and " : " or ");
                                filtros.append(fieldDB);
                                filtros.append(" > "); //Is greater than  
                                filtros.append(operadorQuery(dataType, filter.getValue().toString()));

                                break;
                            case "lte":

                                filtros.append(filter.getLogic().equalsIgnoreCase("and") ? " and " : " or ");
                                filtros.append(fieldDB);
                                filtros.append(" <= "); //Is less than or equal to 
                                filtros.append(operadorQuery(dataType, filter.getValue().toString()));

                                break;
                            case "lt":

                                filtros.append(filter.getLogic().equalsIgnoreCase("and") ? " and " : " or ");
                                filtros.append(fieldDB);
                                filtros.append(" < "); //Is less than  
                                filtros.append(operadorQuery(dataType, filter.getValue().toString()));

                                break;
                            case "contains":

                                filtros.append(filter.getLogic().equalsIgnoreCase("and") ? " and " : " or ");
                                filtros.append(fieldDB);
                                filtros.append(" LIKE "); //Not Equals
                                filtros.append(operadorQuery(dataType, "%" + filter.getValue().toString().toUpperCase() + "%"));

                                break;
                            case "startswith":

                                filtros.append(filter.getLogic().equalsIgnoreCase("and") ? " and " : " or ");
                                filtros.append(fieldDB);
                                filtros.append(" LIKE "); //Not Equals
                                filtros.append(operadorQuery(dataType, "%" + filter.getValue().toString().toUpperCase() + "%"));

                                break;
                        }

                    }
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        //return filtrosFin;
        return filtros.toString();
    }

    private static String operadorQuery(String dataType, String value) {

        StringBuffer operadorQuery = new StringBuffer();

        switch (dataType.toLowerCase()) {

            case "string":

                operadorQuery.append(" '"); //add comilla simple
                operadorQuery.append(value);
                operadorQuery.append("' "); //add comilla simple

                break;
            case "number":

                operadorQuery.append(" ");
                operadorQuery.append(value);
                operadorQuery.append(" ");

                break;

            case "boolean":

                operadorQuery.append(" ");
                operadorQuery.append(value.equalsIgnoreCase("false") ? "FALSE" : "TRUE");
                operadorQuery.append(" ");

                break;

            case "date":
                logger.info("pendiente filtro date");
                break;
        }

        return operadorQuery.toString();

    }

}
