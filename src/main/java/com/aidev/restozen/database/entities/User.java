package com.aidev.restozen.database.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column
	private String imageLocation;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<UserType> types;

}
