package org.example.service;

import org.example.entity.Addon;
import org.example.repository.AddonRepository;
import java.util.List;

public class AddonService {

    private final AddonRepository repo = new AddonRepository();

    public List<Addon> getAllAddons() {
        return repo.findAll();
    }
}
