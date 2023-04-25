package com.app.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

/** I have included a field called quantity in this model and as such inventory may not be needed.
 */
@Entity
@Data
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private Double price;
    @Column
    private String category;
    @Column
    private String description;
    @Column
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "retailer_id", referencedColumnName = "id")
    private Retailer retailer;

}
