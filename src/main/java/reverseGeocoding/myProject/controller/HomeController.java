package reverseGeocoding.myProject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "37.5247809") String latitude,
                       @RequestParam(defaultValue = "126.9556553") String longitude, Model model) {
        log.info("latitude={}",latitude);
        log.info("longitude={}",longitude);

        model.addAttribute("latitude", Double.valueOf(latitude));
        model.addAttribute("longitude", Double.valueOf(longitude));

        return "home";
    }
}
