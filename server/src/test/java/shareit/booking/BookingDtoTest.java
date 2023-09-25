package shareit.booking;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.LastAndNextBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDtoResponse> json;
    @Autowired
    private JacksonTester<LastAndNextBookingDto> lastAndNextBookingJson;

    @Test
    void testBookingDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 12, 12, 9, 0);
        LocalDateTime end = LocalDateTime.of(2023, 12, 12, 14, 0);
        User user = User.builder()
                .id(1)
                .name("Name")
                .email("email@email.com")
                .build();

        Item item = Item.builder()
                .id(1)
                .name("ItemName")
                .description("Description")
                .available(true)
                .build();
        BookingDtoResponse bookingDto = BookingDtoResponse.builder()
                .id(1)
                .start(start)
                .end(end)
                .status(Status.APPROVED)
                .booker(user)
                .item(item)
                .build();

        JsonContent<BookingDtoResponse> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("Name");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("ItemName");


    }

    @Test
    void lastAndNextBooking() throws Exception {
        LastAndNextBookingDto lastAndNextBookingDto = LastAndNextBookingDto.builder()
                .id(1)
                .bookerId(1)
                .build();
        JsonContent<LastAndNextBookingDto> resultLastAndNext = lastAndNextBookingJson.write(lastAndNextBookingDto);
        assertThat(resultLastAndNext).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(resultLastAndNext).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }
}
