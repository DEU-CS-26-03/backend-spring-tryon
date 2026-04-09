//JPA CRUD
package com.capstone.userimage.repository;

import com.capstone.userimage.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, String> {}