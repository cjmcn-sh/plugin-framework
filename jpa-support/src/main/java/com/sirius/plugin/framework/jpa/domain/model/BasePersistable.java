/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.domain.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BasePersistable implements Persistable<String> {

	private static final long serialVersionUID = -715456645278100196L;

	public BasePersistable() {
		this.id = UUID.randomUUID().toString();
	}

	public BasePersistable(String id) {
		this.id = id;
	}

	@Override
	public boolean isNew() {
		return getId() != null;
	}

	public Date getDateCreated() {
		return timeCreated != null ? new Date(timeCreated) : null;
	}

	public Date getDateModified() {
		return timeModified != null ? new Date(timeModified) : null;
	}

	@Id
	@Column(length = 36)
	protected String id;

	@CreatedBy
	@Column(nullable = false, updatable = false)
	protected String userCreated;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	protected Long timeCreated;

	@LastModifiedBy
	@Column(nullable = false)
	protected String userModified;

	@LastModifiedDate
	@Column(nullable = false)
	protected Long timeModified;

	@Basic
	protected Boolean deleted = false;

	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserCreated() {
		return userCreated;
	}

	public void setUserCreated(String userCreated) {
		this.userCreated = userCreated;
	}

	public Long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String getUserModified() {
		return userModified;
	}

	public void setUserModified(String userModified) {
		this.userModified = userModified;
	}

	public Long getTimeModified() {
		return timeModified;
	}

	public void setTimeModified(Long timeModified) {
		this.timeModified = timeModified;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof BasePersistable)) {
			return false;
		}

		BasePersistable that = (BasePersistable) o;

		if (!id.equals(that.id)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return String.format(
				"%s [id=%s, userCreated=%s, timeCreated=%s, userModified=%s, timeModified=%s, deleted=%s]",
				getClass(),
				id,
				userCreated,
				timeCreated,
				userModified,
				timeModified,
				deleted);
	}
}
