package com.simpleshop.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // orphanRemoval = if we remove item from list it gets deleted from db too
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,
               fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> items = new ArrayList<>();
}
