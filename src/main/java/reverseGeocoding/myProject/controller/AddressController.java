package reverseGeocoding.myProject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reverseGeocoding.myProject.service.AddressService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/rg")
    public void reverseGeocoding(@RequestParam String latitude,
                                 @RequestParam String longitude) {
        addressService.reverseGeocoding(latitude, longitude);
    }

    @GetMapping("/g")
    public void geocoding(@RequestParam String roadAddr) {
        addressService.geocoding(roadAddr);
    }
}
