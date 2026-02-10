package net.dflmngr.model.entity;

import java.io.Serializable;
import jakarta.persistence.*;

import net.dflmngr.model.entity.keys.GlobalsPK;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="globals")
@IdClass(GlobalsPK.class)
public class Globals implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String code;

	@Id @Column(name = "group_code")
	private String groupCode;

	private String params;
	private String value;
}
