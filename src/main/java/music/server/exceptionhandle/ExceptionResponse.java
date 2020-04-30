package music.server.exceptionhandle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExceptionResponse<T> {

	int statusCode;
	T message;

	public ExceptionResponse(int statusCode, T message) {
		this.message = message;
		this.statusCode = statusCode;
	}

}