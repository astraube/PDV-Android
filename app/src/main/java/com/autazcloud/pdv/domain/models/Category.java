package com.autazcloud.pdv.domain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Category extends RealmObject {

	@SerializedName("id")
	@Expose
	private long id;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("slug")
	@Expose
	private String slug;

	@SerializedName("description")
	@Expose
	private String description;

	/*@SerializedName("user_id")
	@Expose
	private long userId;

	@SerializedName("account_id")
	@Expose
	private long accountId;*/

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public Category() {
	}

	/**
	 *
	 * @param id
	 * @param description
	 * @param name
	 * @param slug
	 */
	public Category(long id, String name, String slug, String description) {
		super();
		this.id = id;
		this.name = name;
		this.slug = slug;
		this.description = description;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	/*@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}*/

	/*@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(name).append(slug).append(description).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Category) == false) {
			return false;
		}
		Category rhs = ((Category) other);
		return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(slug, rhs.slug).append(description, rhs.description).isEquals();
	}*/

}