package com.example.tradeArea.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "description",  nullable = false)
    private String description;

    @Column(name = "phone", length = 30, nullable = false, unique = true)
    private String phone;

    @Column(name = "price", nullable = false, unique = true)
    private Integer price;

    @Column(name = "amount",  nullable = false)
    private Integer amount;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    public Offer(){}

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

        final Offer offer = (Offer) o;

        if (!this.id.equals(offer.id) ) {
            return false;
        }
        if (!this.phone.equals(offer.phone) ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.phone.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return "Audiobook{" +
                "id=" + id +
                ", company_id=" + company.getId() +
                ", phone='" + phone + '\'' +
                ", price='" + price + '\'' +
                ", amount='" + amount + '\'' +
                ", updated='" + updated + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }


    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
