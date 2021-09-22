package com.iexpress.spring.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.*;


/**
 * The persistent class for the device_detail database table.
 * 
 */
@Entity
@Table(name="device_detail")
@NamedQuery(name="DeviceDetail.findAll", query="SELECT d FROM DeviceDetail d")
public class DeviceDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="device_token", nullable=false, length=255)
	private String deviceToken;

	@Column(name="device_type", nullable=false, length=1)
	private String deviceType;

	@Column(name="is_deleted")
	private byte isDeleted;

	@Column(name="player_id", length=255)
	private String playerId;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;

	public DeviceDetail() {
	}

	public String getDeviceToken() {
		return this.deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public byte getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}


	public String getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static DeviceDetail of(String deviceToken, String deviceType) {
		DeviceDetail device = new DeviceDetail();
		device.setCreatedAt(Timestamp.from(Instant.now()));
		device.setModifiedAt(Timestamp.from(Instant.now()));
		device.setDeviceToken(deviceToken);
		device.setDeviceType(deviceType);
		return device;
	}
}