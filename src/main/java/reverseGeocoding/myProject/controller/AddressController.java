package reverseGeocoding.myProject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reverseGeocoding.myProject.domain.Space;
import reverseGeocoding.myProject.repository.SpaceRepository;
import reverseGeocoding.myProject.service.AddressService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;
    private final SpaceRepository spaceRepository;


    @GetMapping("/rg")
    public String reverseGeocoding(@RequestParam String latitude,
                                 @RequestParam String longitude, Model model) {

        String roadAddr = addressService.reverseGeocoding(latitude, longitude);

        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        // 위도경도로 Point 생성
        Point point = addressService.createPoint(lat, lon);
        addressService.saveSpace(point, roadAddr);
        if(roadAddr.isEmpty()) {
            model.addAttribute("message", "도로명 주소를 찾을 수 없는 좌표 입니다.");
        } else {
            model.addAttribute("message", "도로명 주소 : " + roadAddr);
        }

        List<Space> spaces = spaceRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("spaces", spaces);

        model.addAttribute("latitude", point.getX());
        model.addAttribute("longitude", point.getY());

        return "home";

    }

    @GetMapping("/g")
    public String geocoding(@RequestParam String roadAddr, Model model) {
        Point geocodedPoint = addressService.geocoding(roadAddr);
        addressService.saveSpace(geocodedPoint, roadAddr);

        List<Space> spaces = spaceRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("spaces", spaces);

        model.addAttribute("message", "위도 : " + geocodedPoint.getX() + ", 경도 : " + geocodedPoint.getY());
        model.addAttribute("latitude", geocodedPoint.getX());
        model.addAttribute("longitude", geocodedPoint.getY());

        return "home";
    }
}
