package Youtube_seo_Thumbnail.demo.Service;

import Youtube_seo_Thumbnail.demo.Entity.SearchVideo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    public final WebClient.Builder webclientBuilder;

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.base.url}")
    private String baseUrl;

    @Value("${youtube.api.max.related.videos}")
    private String maxRelated;

    public SearchVideo searchVideos(String videoTitle) {


    }
}
