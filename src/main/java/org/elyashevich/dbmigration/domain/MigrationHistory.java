package org.elyashevich.dbmigration.domain;

import java.sql.Date;
import java.util.Objects;

// TODO
public class MigrationHistory {

    private Long id;

    private Integer version;

    private Boolean isLocked;

    private Date appliedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Date getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Date appliedAt) {
        this.appliedAt = appliedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MigrationHistory that = (MigrationHistory) o;
        return Objects.equals(id, that.id) && Objects.equals(version, that.version) && Objects.equals(isLocked, that.isLocked) && Objects.equals(appliedAt, that.appliedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, isLocked, appliedAt);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MigrationHistory{");
        sb.append("id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", isLocked=").append(isLocked);
        sb.append(", appliedAt=").append(appliedAt);
        sb.append('}');
        return sb.toString();
    }
}
