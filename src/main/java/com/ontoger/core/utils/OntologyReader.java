package com.ontoger.core.utils;

import com.ontoger.core.constants.CommonConstants;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OntologyReader {

    public List<String> readFileContent(String path, String fileName) throws IOException {

        File ontology = FileUtils.getFile(path + fileName);
        List<String> fileContent = FileUtils.readLines(ontology, "utf-8");

        return fileContent;
    }

}
