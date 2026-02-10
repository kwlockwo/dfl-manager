package net.dflmngr.model.entity.keys;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DflPreseasonScoresPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private int playerId;
	private int round;
}
