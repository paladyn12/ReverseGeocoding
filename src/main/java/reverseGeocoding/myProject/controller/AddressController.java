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
import reverseGeocoding.myProject.domain.entity.Route;
import reverseGeocoding.myProject.domain.entity.Space;
import reverseGeocoding.myProject.repository.RouteRepository;
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
    private final RouteRepository routeRepository;


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
        Point geocodingedPoint = addressService.geocoding(roadAddr);
        addressService.saveSpace(geocodingedPoint, roadAddr);

        List<Space> spaces = spaceRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("spaces", spaces);

        model.addAttribute("message", "위도 : " + geocodingedPoint.getX() + ", 경도 : " + geocodingedPoint.getY());
        model.addAttribute("latitude", geocodingedPoint.getX());
        model.addAttribute("longitude", geocodingedPoint.getY());

        return "home";
    }

    @GetMapping("/d")
    public String distance(@RequestParam String stdRoadAddr,
                           @RequestParam String tgRoadAddr, Model model) {
        Point stdPoint = addressService.geocoding(stdRoadAddr);
        Point tgPoint = addressService.geocoding(tgRoadAddr);

        double distance = addressService.distance(stdPoint, tgPoint);

        addressService.saveRoute(stdPoint, stdRoadAddr, tgPoint, tgRoadAddr);



        List<Space> spaces = spaceRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Route> routes = routeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("spaces", spaces);
        model.addAttribute("routes", routes);

        model.addAttribute("message", "두 주소 사이의 거리는 " + (distance/1000) + "Km 입니다.");
        model.addAttribute("latitude", stdPoint.getX());
        model.addAttribute("longitude", stdPoint.getY());

        return "home";
    }

    @GetMapping("/dr")
    public String direction(@RequestParam String stdRoadAddr,
                           @RequestParam String tgRoadAddr, Model model) {
        Point stdPoint = addressService.geocoding(stdRoadAddr);
        Point tgPoint = addressService.geocoding(tgRoadAddr);

        String direction = addressService.direction(stdPoint, tgPoint);

        addressService.saveRoute(stdPoint, stdRoadAddr, tgPoint, tgRoadAddr);

        List<Space> spaces = spaceRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Route> routes = routeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("spaces", spaces);
        model.addAttribute("routes", routes);

        model.addAttribute("message", direction);
        model.addAttribute("latitude", stdPoint.getX());
        model.addAttribute("longitude", stdPoint.getY());

        return "home";
    }

}
