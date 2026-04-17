package sakilaApp.model.dao;

import java.time.LocalDateTime;

public record Actor(int id, String nom, String cognoms,  LocalDateTime lastUpdate) {}
