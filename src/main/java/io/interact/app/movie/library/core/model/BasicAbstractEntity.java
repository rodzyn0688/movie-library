package io.interact.app.movie.library.core.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@MappedSuperclass
public abstract class BasicAbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    @CreationTimestamp
    @Column(name = "created", nullable = false, updatable = false)
    private Timestamp created;

    @UpdateTimestamp
    @Column(name = "last_modified", insertable = false)
    private Timestamp lastModified;

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicAbstractEntity)) return false;
        BasicAbstractEntity that = (BasicAbstractEntity) o;

        // Compare only when both are persist
        if (id != null && that.getId() != null && !id.equals(that.getId())) {
            return Objects.equals(id, that.id) &&
                    Objects.equals(created, that.created) &&
                    Objects.equals(lastModified, that.lastModified);
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, lastModified);
    }
}
