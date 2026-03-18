package com.bp.devtraceserver.repository;

import com.bp.devtraceserver.model.ApiRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiRequest extends JpaRepository<ApiRequestLog, Long> {
}
