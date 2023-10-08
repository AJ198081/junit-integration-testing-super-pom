package dev.aj.springtdd.domain.model;

public record Post(Integer id, Integer userId, String title, String body, Integer version) { }
