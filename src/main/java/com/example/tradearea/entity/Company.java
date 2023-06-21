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
                ", name=" + name +
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
}
