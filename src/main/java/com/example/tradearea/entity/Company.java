package com.example.tradearea.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;


@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 200, nullable = false, unique = true)
    private String name;

    @Column(name = "unp", length = 200, nullable = false, unique = true)
    private String unp;

    @Column(name = "email", length = 200,  nullable = false)
    private String email;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "description",  nullable = false)
    private String description;

    public Company() {

    }

    Company(Long id, String name, String unp, String email, LocalDateTime created, String description){
        this.id = id;
        this.name = name;
        this.unp = unp;
        this.email = email;
        this.created = created;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final Company company = (Company) o;

        if (!this.id.equals(company.id)) {
            return false;
        }
        if (!this.name.equals(company.name)) {
            return false;
        }
        if (!this.unp.equals(company.unp)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.name.hashCode();
        result = 31 * result + this.unp.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unp='" + unp + '\'' +
                ", email='" + email + '\'' +
                ", created='" + created + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getUnp() {
        return unp;
    }

    public void setUnp(String unp) {
        this.unp = unp;
    }

    public static class CompanyBuilder {
        private Long id;
        private String name;
        private String unp;
        private String email;
        private LocalDateTime created;
        private String description;

        CompanyBuilder() {
        }


        public CompanyBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CompanyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CompanyBuilder unp(String unp) {
            this.unp = unp;
            return this;
        }

        public CompanyBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CompanyBuilder created(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public CompanyBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Company build() {
            return new Company(id, name, unp, email, created, description);
        }
    }
    public static CompanyBuilder builder() {
        return new CompanyBuilder();
    }
}
