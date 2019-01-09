package irina.rabbit.restGateway.controller;

import irina.rabbit.restGateway.dispatcher.CommandDispatcher;
import irina.rabbit.restGateway.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private CommandDispatcher messageDispatcher;

    @PostMapping
    public ResponseEntity createBooking(@RequestBody @Valid Booking booking) {

        if(booking.getEndDate().isBefore(booking.getStartDate())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        messageDispatcher.processBooking(booking);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
