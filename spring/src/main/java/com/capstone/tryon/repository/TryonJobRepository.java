package com.capstone.tryon.repository;

import com.capstone.tryon.entity.TryonJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TryonJobRepository extends JpaRepository<TryonJob, String> {

    // Python worker 작업 claim — 생성순 FIFO 보장
    Optional<TryonJob> findFirstByStatusInOrderByCreatedAtAsc(List<String> statuses);

    // 내 작업 목록 조회 (soft delete 제외, 최신순)
    List<TryonJob> findByUserIdAndDeletedFalseOrderByCreatedAtDesc(Long userId);
}