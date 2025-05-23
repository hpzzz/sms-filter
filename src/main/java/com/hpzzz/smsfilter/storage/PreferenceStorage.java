package com.hpzzz.smsfilter.storage;

import com.hpzzz.smsfilter.model.UserPreference;
import com.hpzzz.smsfilter.repository.UserPreferenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PreferenceStorage {

    private final UserPreferenceRepository repo;

    public PreferenceStorage(UserPreferenceRepository repo) {
        this.repo = repo;
    }

    public void startProtection(String phone) {
        repo.save(new UserPreference(phone, true));
    }

    public void stopProtection(String phone) {
        repo.save(new UserPreference(phone, false));
    }

    public boolean isProtected(String phone) {
        Optional<UserPreference> preference = repo.findById(phone);
        return preference.map(UserPreference::getProtectionEnabled).orElse(false);
    }
}
