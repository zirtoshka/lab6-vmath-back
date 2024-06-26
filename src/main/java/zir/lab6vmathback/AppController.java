package zir.lab6vmathback;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.RequestInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import zir.lab6vmathback.utils.RequestODEInfo;
import zir.lab6vmathback.utils.SolvingODEHandler;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app-controller")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AppController {

    private final SolvingODEHandler solvingODEHandler = new SolvingODEHandler();

    @GetMapping
    public ResponseEntity<String> sayHello() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        System.out.println("it's method sayHello");
        return new ResponseEntity<>("{\"message\": \"Hello from secured endpoint\"}", httpHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> getInterpolation(@Valid @RequestBody RequestODEInfo requestODEInfo) {
        String response = "";
        final HttpHeaders httpHeaders = new HttpHeaders();

        int equation = requestODEInfo.getEquation();
        solvingODEHandler.setEquation(equation);

        BigDecimal yInLeftBorder = new BigDecimal(requestODEInfo.getYInLeftBorder());
        BigDecimal leftBorderX = new BigDecimal(requestODEInfo.getLeftBorderX());
        BigDecimal rightBorderX = new BigDecimal(requestODEInfo.getRightBorderX());

        if(leftBorderX.compareTo(rightBorderX)==0){
            return new ResponseEntity<>( httpHeaders, HttpStatus.BAD_REQUEST);

        }
        BigDecimal inaccuracy = new BigDecimal(requestODEInfo.getInaccuracy());
        BigDecimal step =new BigDecimal(requestODEInfo.getStep());

        for (int i = 0; i < 4; i++) {
            response+=solvingODEHandler.getSolvingByMethod(leftBorderX, yInLeftBorder,rightBorderX,step, inaccuracy,i);
        }
        response +="}";

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);


    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

}