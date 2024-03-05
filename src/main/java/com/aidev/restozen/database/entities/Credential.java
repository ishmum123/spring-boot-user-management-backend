package com.aidev.restozen.database.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Credential {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@ManyToOne(optional = false, cascade = CascadeType.REMOVE)
	private UserType type;

}
