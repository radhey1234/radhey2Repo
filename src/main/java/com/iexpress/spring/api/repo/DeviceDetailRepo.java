package com.iexpress.spring.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.DeviceDetail;

@Repository
public interface DeviceDetailRepo extends JpaRepository<DeviceDetail, Integer>{

	@Modifying
	@Query("delete from DeviceDetail d where d.deviceToken = ?1")
	void deleteDeviceToken(String deviceToken);

	@Modifying
	@Query("delete from DeviceDetail d where d.user.id = ?1 and d.deviceToken = ?2")
	void deleteByUserIdAndDeviceToken(int userId, String deviceToken);

}
