package hr.fer.zemris.vhdllab.service.extractor;

import hr.fer.zemris.vhdllab.dao.FileDao;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMetadataExtractor implements MetadataExtractor {

    @Autowired
    private FileDao fileDao;
    @Resource(name = "fileTypeBasedMetadataExtractor")
    private MetadataExtractor fileTypeBasedMetadataExtractor;

    public FileDao getFileDao() {
        return fileDao;
    }

    public MetadataExtractor getFileTypeBasedMetadataExtractor() {
        return fileTypeBasedMetadataExtractor;
    }

}
