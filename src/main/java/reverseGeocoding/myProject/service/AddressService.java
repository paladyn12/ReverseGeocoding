package reverseGeocoding.myProject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reverseGeocoding.myProject.Point;

@Service
@Slf4j
public class AddressService {

    public void reverseGeocoding(Point point) {

        String coords = point.getLongitude() + "," + point.getLatitude();

        // 역지오코딩 API 요청 url
        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=" + coords + "&output=json&orders=addr,admcode,roadaddr";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", "o6w1qaagu0");
        headers.set("X-NCP-APIGW-API-KEY", "0lkbNtWJ69Whp88c2QGObSG9c1KSzh2Ul7hzwMdc");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate 객체 생성, API 호출 및 응답 수신
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity,String.class);
        String responseBody = response.getBody();

        log.info("responseBody={}",responseBody);

        //도로명주소 받아서 반환, controller에서 그 값을 template에 넘겨 alert

    }

    public void geocoding(String roadAddr) {

        // 지오코딩 API 요청 url
        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+roadAddr;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", "o6w1qaagu0");
        headers.set("X-NCP-APIGW-API-KEY", "0lkbNtWJ69Whp88c2QGObSG9c1KSzh2Ul7hzwMdc");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate 객체 생성, API 호출 및 응답 수신
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity,String.class);
        String responseBody = response.getBody();

        log.info("responseBody={}",responseBody);

        //위도, 경도받아서 반환, controller에서 그 값을 template에 넘겨 alert
    }
}
