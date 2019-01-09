package irina.rabbit.statistics.controller;

import com.google.common.collect.Lists;
import irina.rabbit.statistics.model.Booking;
import irina.rabbit.statistics.model.Statistic;
import irina.rabbit.statistics.model.User;
import irina.rabbit.statistics.repository.BookingRepository;
import irina.rabbit.statistics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/statistics")
@CrossOrigin(origins = "http://localhost:4200")
public class StatisticsController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Statistic>> getStatistics() {

        List<Statistic> all = Lists.newArrayList(bookingRepository.findAll())
                .stream()
                .map(this::toStatistic)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new ResponseEntity<>(all, HttpStatus.OK);
    }

        private Statistic toStatistic(Booking booking) {


        Statistic result = new Statistic();

        Optional<User> user = userRepository.findById(booking.getUserId());

        if(!user.isPresent()){
            return null;
        }

        result.setFirstName(user.get().getFirstName());
        result.setLastName(user.get().getLastName());
        result.setEmail(user.get().getEmail());
        result.setHotelName(booking.getHotelName());
        result.setRoomNumber(booking.getRoomNumber());
        result.setStartDate(booking.getStartDate().toString());
        result.setEndDate(booking.getEndDate().toString());

        return result;
    }
}
