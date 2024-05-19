package reverseGeocoding.myProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reverseGeocoding.myProject.domain.SpaceDto;
import reverseGeocoding.myProject.domain.Space;
import reverseGeocoding.myProject.repository.SpaceRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService {

    private final SpaceRepository spaceRepository;

    // 위도와 경도를 받아 Point 반환
    public Point createPoint(double latitude, double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(latitude, longitude));
    }

    // Point와 도로명 주소로 Space 객체 저장, 이미 존재하는 Point이면 저장하지 않음
    public void saveSpace(Point point, String roadAddr) {
        if(spaceRepository.existsByPoint(point)) {
            log.info("이미 존재하는 위치정보");
        } else {
            SpaceDto spaceDto = new SpaceDto(point, roadAddr);
            spaceRepository.save(spaceDto.toEntity());
        }
    }

    // 위도, 경도를 받아 도로명 주소 반환
    public String reverseGeocoding(String latitude, String longitude) {

        String coords = longitude + "," + latitude;

        // 역지오코딩 API 요청 url
        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=" + coords + "&orders=roadaddr&output=json";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", "o6w1qaagu0");
        headers.set("X-NCP-APIGW-API-KEY", "0lkbNtWJ69Whp88c2QGObSG9c1KSzh2Ul7hzwMdc");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate 객체 생성, API 호출 및 응답 수신
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity,String.class);
        String responseBody = response.getBody();

        // API를 호출 하여 받은 데이터에서 도로명 주소를 뽑는 과정
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject status = jsonObject.getJSONObject("status");
        Integer code = (Integer) status.get("code");
        if(code == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject data = jsonArray.getJSONObject(0);

            // 도로명 주소의 상세 주소 얻기 (ex.서울특별시 종로구 홍지동 20의 [20])
            JSONObject land = data.getJSONObject("land");
            String detailAddr = (String) land.get("number1");
            log.info("detailAddr={}",detailAddr);

            // 도로명 주소 얻기 (ex.서울특별시 종로구 홍지동 20의 [서울특별시 종로구 홍지동])
            JSONObject region = data.getJSONObject("region");
            StringBuilder addr = new StringBuilder();
            for(int i = 1; i < 5; i++) {
                String area = "area" + i;
                String name = (String) region.getJSONObject(area).get("name");
                // 값이 없으면 그냥 공백이 두 번 더해지기 때문에 값이 없을 경우 공백을 추가하지 않음
                if(!name.isEmpty()) {
                    addr.append(name).append(" ");
                }
            }
            // 추출한 두 주소 합치기
            String roadAddr = addr + detailAddr;
            log.info("roadAddr={}", roadAddr);
            return roadAddr;
        } else {
            log.info("일치 하는 도로명 주소가 없습니다.");
            return null;
        }

        //도로명 주소 받아서 반환, controller에서 그 값을 template에 넘겨 alert
    }

    public Point geocoding(String roadAddr) {

        // 지오코딩 API 요청 url
        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + roadAddr;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", "o6w1qaagu0");
        headers.set("X-NCP-APIGW-API-KEY", "0lkbNtWJ69Whp88c2QGObSG9c1KSzh2Ul7hzwMdc");
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate 객체 생성, API 호출 및 응답 수신
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();

        // API를 호출 하여 받은 데이터에서 위도, 경도를 뽑는 과정
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray jsonArray = jsonObject.getJSONArray("addresses");
        JSONObject obj = jsonArray.getJSONObject(0);
        double longitude = Double.parseDouble(obj.getString("x"));
        double latitude = Double.parseDouble(obj.getString("y"));

        log.info("latitude={}",latitude);
        log.info("longitude={}",longitude);

        //위도, 경도를 받아서 반환, controller에서 그 값을 template에 넘겨 alert
        return createPoint(latitude, longitude);

        // 도로명 주소도 받아 Space 객체 만들어 반환 예정
    }
}
