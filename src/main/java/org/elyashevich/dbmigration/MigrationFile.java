package org.elyashevich.dbmigration;

import java.util.Objects;

public class MigrationFile {
    private String filename;
    private String content;
    private Integer version;

    public MigrationFile(){
    }

    public MigrationFile(String filename, String content, Integer version) {
        this.filename = filename;
        this.content = content;
        this.version = version;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MigrationFile that = (MigrationFile) o;
        return Objects.equals(filename, that.filename) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, content);
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MigrationFile{");
        sb.append("filename='").append(filename).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", version=").append(version);
        sb.append('}');
        return sb.toString();
    }
}
