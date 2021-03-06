package br.ufsc.egc.rudger.expertprofiling.stopword;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Used for storing stop words in a HashSet. Can be used as resource object in UIMA. Terms in stop word files are
 * converted to lower case.
 */
public class StopWordSet implements SharedResourceObject {
    
    private HashSet<String> data;

    public StopWordSet() {
        super();
        this.data = new HashSet<String>();
    }

    public StopWordSet(final String[] fileNames) throws IOException {
        super();
        this.data = new HashSet<String>();
        for (String fileName : fileNames) {
            this.addStopWordListFile(fileName);
        }
    }

    /**
     * Loads a text file (UTF-8 encoding!) containing stop words. Only first word in each line will be taken into
     * account. Everything after "|" will be treated as comment.
     * 
     * @param fileName the file to read.
     * @throws IOException if the file could not be read.
     */
    public void addStopWordListFile(final String fileName) throws IOException {
        try (Reader reader = new FileReader(fileName)) {
            this.load(new FileReader(fileName));
        }
    }

    @Override
    public void load(final DataResource dataRes) throws ResourceInitializationException {
        try (InputStream is = dataRes.getInputStream()) {
            this.load(is);
        } catch (IOException e) {
            throw new ResourceInitializationException(e);
        }
    }

    public void load(final InputStream aIs) throws IOException {
        this.load(aIs, "UTF-8");
    }

    public void load(final InputStream aIs, final String aEncoding) throws IOException {
        this.load(new InputStreamReader(aIs, aEncoding));
    }

    /**
     * Load the stopwords from the given reader. If the set already contains data, new stopwords are added.
     *
     * @param aReader a reader.
     * @throws IOException if the data could not be read.
     */
    public void load(final Reader aReader) throws IOException {
        String line = null;
        BufferedReader br = new BufferedReader(aReader);
        while ((line = br.readLine()) != null) {
            String[] words = line.trim().split("\\s|\\|");
            if (words.length > 0 && words[0].trim().length() > 0) {
                this.data.add(words[0].toLowerCase());
            }
        }
    }

    public boolean contains(final String aWord) {
        return this.data.contains(aWord);
    }
    
    public int size(){
        return this.data.size();
    }
    
    public Set<String> getData(){
        return Collections.unmodifiableSet(this.data);
    }
}
