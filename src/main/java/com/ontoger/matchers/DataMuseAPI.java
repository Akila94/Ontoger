package com.ontoger.matchers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.ontoger.core.constants.CommonConstants;
import com.ontoger.utils.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DataMuseAPI {

    /**
     * This method checks whether the first string is a synonym, hypernym, hyponym, meronym, comprises, or part-of
     * the other string provided according to the "parameter" provided.
     * <p>
     * Parameter Types
     * ================
     * Synonym: syn
     * Hypernym: spc
     * Hyponym: gen
     * Comprises: com
     * Part-of: par
     *
     * @param concept1  Name of the concept of Source Ontology
     * @param concept2  Name of the concept of Destination Ontology
     * @param parameter Type of the similarity need to check
     * @return Returns true if it is a match, return false is no match or null response
     * @throws IOException An Exception is thrown if connection errors happen
     */
    public boolean isRelated(String concept1, String concept2, String parameter) throws IOException {

        String shortName1 = Utils.getShortName(concept1);
        String shortName2 = Utils.getShortName(concept2);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(CommonConstants.DATAMUSE_ENDPOINT + parameter.toLowerCase() + "=" +
                shortName1);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            JsonParser parser = new JsonParser();
            JsonArray jsonObject = (JsonArray) parser.parse(content);
            int size = jsonObject.size();

            for (int i = 0; i < size; i++) {
                String s = jsonObject.get(i).getAsJsonObject().get("word").getAsString();
                if (shortName2.equalsIgnoreCase(s)) {
                    return true;
                } else {
                    if (i == size - 1) {
                        return false;
                    } else {
                        continue;
                    }
                }
            }
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return false;
    }
}
