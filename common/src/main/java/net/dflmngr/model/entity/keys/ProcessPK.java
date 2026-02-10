package net.dflmngr.model.entity.keys;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private String processId;
	private ZonedDateTime startTime;
}
