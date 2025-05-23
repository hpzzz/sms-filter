package com.hpzzz.smsfilter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserPreference {

    @Id
    private String phoneNumber;

    private Boolean protectionEnabled;
}
