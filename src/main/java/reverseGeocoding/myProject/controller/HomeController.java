package reverseGeocoding.myProject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reverseGeocoding.myProject.domain.entity.Space;
import reverseGeocoding.myProject.repository.SpaceRepository;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final SpaceRepository spaceRepository;

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "37.5247809") String latitude,
                       @RequestParam(defaultValue = "126.9556553") String longitude, Model model) {
        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        List<Space> spaces = spaceRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("spaces", spaces);

        model.addAttribute("latitude", lat);
        model.addAttribute("longitude", lon);

        return "home";
    }
}
