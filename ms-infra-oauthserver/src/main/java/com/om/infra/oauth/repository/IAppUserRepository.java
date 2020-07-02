package com.om.infra.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.om.infra.oauth.model.AppUser;

public interface IAppUserRepository extends JpaRepository<AppUser, String> {

}
