package com.spms.auth.repository;

import com.spms.auth.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByOtpToken(String otpToken);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Otp o WHERE o.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Otp o WHERE o.expiresAt < :cutoff")
    int deleteByExpiresAtBefore(@Param("cutoff") LocalDateTime cutoff);
}