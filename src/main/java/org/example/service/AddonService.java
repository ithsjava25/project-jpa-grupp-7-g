package org.example.service;

// AddonService - Service class for Addon entity

import org.example.entity.Addon;
import org.example.repository.AddonRepository;
import java.util.List;

public class AddonService {

    private final AddonRepository repo = new AddonRepository();

    public List<Addon> getAllAddons() {
        return repo.findAll();
    }
}
