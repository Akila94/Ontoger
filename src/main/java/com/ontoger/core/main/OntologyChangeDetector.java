package com.ontoger.core.main;

import com.ontoger.core.constants.CommonConstants;
import com.ontoger.core.utils.OntologyReader;
import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OntologyChangeDetector {

    OntologyReader ontologyReader;

    public List<Chunk> getChangesFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.CHANGE);
    }

    public List<Chunk> getInsertsFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.INSERT);
    }

    public List<Chunk> getDeletesFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.DELETE);
    }

    private List<Delta> getDeltas() throws IOException {
        ontologyReader = new OntologyReader();
        final List<String> originalFileLines = ontologyReader.readFileContent(CommonConstants.ONTOLOGY_FILE_PATH,
                "Agriculture.owl");
        final List<String> editedFileLines = ontologyReader.readFileContent(CommonConstants.ONTOLOGY_FILE_PATH,
                "Agriculture_edited.owl");

        final Patch patch = DiffUtils.diff(originalFileLines, editedFileLines);

        return patch.getDeltas();
    }

    private List<Chunk> getChunksByType(Delta.TYPE type) throws IOException {
        final List<Chunk> listOfChanges = new ArrayList<Chunk>();
        final List<Delta> deltas = getDeltas();
        for (Delta delta : deltas) {
            if (delta.getType() == type) {
                listOfChanges.add(delta.getRevised());
            }
        }
        return listOfChanges;
    }
}
