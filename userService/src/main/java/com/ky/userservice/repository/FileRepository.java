package com.ky.userservice.repository;

import com.ky.userservice.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
