package com.example.infra.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.infra.oauth.model.AppUser;

public interface IAppUserRepository extends JpaRepository<AppUser, String> {

}
