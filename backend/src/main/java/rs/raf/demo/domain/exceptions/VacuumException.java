package rs.raf.demo.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class VacuumException extends RuntimeException{

    public VacuumException(String message){
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
