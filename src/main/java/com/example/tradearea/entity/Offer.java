package com.example.tradearea.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    public Offer() { }

    public Offer(Long id, Company company, String description, String phone, Integer price, Integer amount, LocalDateTime updated) {
        this.id = id;
        this.company = company;
        this.description = description;
        this.phone = phone;
        this.price = price;
        this.amount = amount;
        this.updated = updated;
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

        final Offer offer = (Offer) o;

        if (!this.id.equals(offer.id)) {
            return false;
        }
        if (!this.phone.equals(offer.phone)) {
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
        return "Offer{"
                + "id=" + id
                + ", company=" + company
                + ", phone='" + phone + '\''
                + ", price='" + price + '\''
                + ", amount='" + amount + '\''
                + ", updated='" + updated + '\''
                + ", description='" + description + '\''
                + '}';
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

    public static class OfferBuilder {
        private Long id;
        private Company company;
        private String description;
        private String phone;
        private Integer price;
        private Integer amount;
        private LocalDateTime updated;


        OfferBuilder() {
        }


        public OfferBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OfferBuilder setCompany(Company company) {
            this.company = company;
            return this;
        }

        public OfferBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public OfferBuilder setPrice(Integer price) {
            this.price = price;
            return this;
        }

        public OfferBuilder setAmount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public OfferBuilder setUpdated(LocalDateTime updated) {
            this.updated = updated;
            return this;
        }

        public OfferBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Offer build() {
            return new Offer(id, company, description, phone, price, amount, updated);
        }
    }
    public static Offer.OfferBuilder builder() {
        return new Offer.OfferBuilder();
    }
}
